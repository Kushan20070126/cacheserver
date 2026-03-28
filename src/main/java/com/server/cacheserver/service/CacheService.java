package com.server.cacheserver.service;

import com.server.cacheserver.datastructure.AVLTree;
import com.server.cacheserver.datastructure.LRUList;
import com.server.cacheserver.model.CacheItem;
import com.server.cacheserver.model.LRUNode;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheService {

    private final ConcurrentHashMap<Long, CacheItem> cacheMap = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, LRUNode> lruNodeMap = new ConcurrentHashMap<>();
    private final AVLTree avlTree = new AVLTree();
    private final LRUList lruList = new LRUList();
    private final CacheStatistics statistics = new CacheStatistics();

    private final long maxMemoryBytes = 1024 * 100;
    private long currentMemoryBytes = 0;

    private volatile boolean cleanupRunning = true;
    private Thread cleanupThread;

    @PostConstruct
    public void startCleanupThread() {
        cleanupThread = new Thread(() -> {
            while (cleanupRunning) {
                try {
                    cleanupExpiredKeys();
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        cleanupThread.setDaemon(true);
        cleanupThread.setName("cache-cleanup-thread");
        cleanupThread.start();
    }

    @PreDestroy
    public void stopCleanupThread() {
        cleanupRunning = false;
        if (cleanupThread != null) {
            cleanupThread.interrupt();
        }
    }

    public synchronized String set(long key, String value, long ttlSeconds) {
        if (ttlSeconds <= 0) {
            return "TTL must be greater than 0";
        }

        long expiryTimeMillis = System.currentTimeMillis() + (ttlSeconds * 1000);
        CacheItem newItem = new CacheItem(key, value, expiryTimeMillis);

        if (newItem.getSizeInBytes() > maxMemoryBytes) {
            return "Item too large for cache";
        }

        CacheItem existing = cacheMap.get(key);
        if (existing != null) {
            currentMemoryBytes -= existing.getSizeInBytes();

            cacheMap.put(key, newItem);
            avlTree.insert(key, newItem);
            currentMemoryBytes += newItem.getSizeInBytes();

            touchKey(key);
            statistics.incrementUpdates();

            evictIfNeeded();
            return "UPDATED: " + key;
        }

        while (currentMemoryBytes + newItem.getSizeInBytes() > maxMemoryBytes) {
            boolean evicted = evictLeastRecentlyUsed();
            if (!evicted) {
                return "Not enough memory";
            }
        }

        cacheMap.put(key, newItem);
        avlTree.insert(key, newItem);

        LRUNode node = new LRUNode(key);
        lruNodeMap.put(key, node);
        lruList.addToFront(node);

        currentMemoryBytes += newItem.getSizeInBytes();
        statistics.incrementInserts();

        return "INSERTED: " + key;
    }

    public synchronized String get(long key) {
        CacheItem item = cacheMap.get(key);

        if (item == null) {
            statistics.incrementMisses();
            return "CACHE MISS";
        }

        if (item.isExpired()) {
            removeKey(key, true);
            statistics.incrementMisses();
            return "EXPIRED";
        }

        touchKey(key);
        statistics.incrementHits();
        return item.getValue();
    }

    public synchronized String delete(long key) {
        if (!cacheMap.containsKey(key)) {
            return "KEY NOT FOUND";
        }

        removeKey(key, false);
        statistics.incrementDeletes();
        return "DELETED: " + key;
    }

    public synchronized List<CacheItem> range(long startKey, long endKey) {
        cleanupExpiredKeys();

        List<CacheItem> items = avlTree.rangeQuery(startKey, endKey);
        List<CacheItem> validItems = new ArrayList<>();

        for (CacheItem item : items) {
            if (!item.isExpired()) {
                validItems.add(item);
            }
        }

        return validItems;
    }

    public synchronized Map<String, Object> stats() {
        Map<String, Object> map = new HashMap<>();
        map.put("hits", statistics.getHits());
        map.put("misses", statistics.getMisses());
        map.put("evictions", statistics.getEvictions());
        map.put("expiredRemovals", statistics.getExpiredRemovals());
        map.put("inserts", statistics.getInserts());
        map.put("updates", statistics.getUpdates());
        map.put("deletes", statistics.getDeletes());
        map.put("hitRatio", statistics.getHitRatio());
        map.put("currentItems", cacheMap.size());
        map.put("currentMemoryBytes", currentMemoryBytes);
        map.put("maxMemoryBytes", maxMemoryBytes);
        return map;
    }

    private void touchKey(long key) {
        LRUNode node = lruNodeMap.get(key);
        if (node != null) {
            lruList.moveToFront(node);
        }
    }

    private boolean evictLeastRecentlyUsed() {
        Long lruKey = lruList.removeTail();
        if (lruKey == null) {
            return false;
        }

        CacheItem removed = cacheMap.remove(lruKey);
        avlTree.delete(lruKey);
        lruNodeMap.remove(lruKey);

        if (removed != null) {
            currentMemoryBytes -= removed.getSizeInBytes();
            statistics.incrementEvictions();
            return true;
        }
        return false;
    }

    private void evictIfNeeded() {
        while (currentMemoryBytes > maxMemoryBytes) {
            if (!evictLeastRecentlyUsed()) {
                break;
            }
        }
    }

    private void removeKey(long key, boolean expired) {
        CacheItem removed = cacheMap.remove(key);
        avlTree.delete(key);

        LRUNode node = lruNodeMap.remove(key);
        if (node != null) {
            lruList.remove(node);
        }

        if (removed != null) {
            currentMemoryBytes -= removed.getSizeInBytes();
        }

        if (expired) {
            statistics.incrementExpiredRemovals();
        }
    }

    private synchronized void cleanupExpiredKeys() {
        List<Long> expiredKeys = new ArrayList<>();

        for (Map.Entry<Long, CacheItem> entry : cacheMap.entrySet()) {
            if (entry.getValue().isExpired()) {
                expiredKeys.add(entry.getKey());
            }
        }

        for (Long key : expiredKeys) {
            removeKey(key, true);
        }
    }
}
