package com.server.cacheserver.service;


import java.util.concurrent.atomic.AtomicLong;

public class CacheStatistics {
    private final AtomicLong hits = new AtomicLong();
    private final AtomicLong misses = new AtomicLong();
    private final AtomicLong evictions = new AtomicLong();
    private final AtomicLong expiredRemovals = new AtomicLong();
    private final AtomicLong inserts = new AtomicLong();
    private final AtomicLong updates = new AtomicLong();
    private final AtomicLong deletes = new AtomicLong();

    public void incrementHits() {
        hits.incrementAndGet();
    }

    public void incrementMisses() {
        misses.incrementAndGet();
    }

    public void incrementEvictions() {
        evictions.incrementAndGet();
    }

    public void incrementExpiredRemovals() {
        expiredRemovals.incrementAndGet();
    }

    public void incrementInserts() {
        inserts.incrementAndGet();
    }

    public void incrementUpdates() {
        updates.incrementAndGet();
    }

    public void incrementDeletes() {
        deletes.incrementAndGet();
    }

    public long getHits() {
        return hits.get();
    }

    public long getMisses() {
        return misses.get();
    }

    public long getEvictions() {
        return evictions.get();
    }

    public long getExpiredRemovals() {
        return expiredRemovals.get();
    }

    public long getInserts() {
        return inserts.get();
    }

    public long getUpdates() {
        return updates.get();
    }

    public long getDeletes() {
        return deletes.get();
    }

    public double getHitRatio() {
        long h = hits.get();
        long m = misses.get();
        long total = h + m;
        if (total == 0) {
            return 0.0;
        }
        return (double) h / total;
    }
}
