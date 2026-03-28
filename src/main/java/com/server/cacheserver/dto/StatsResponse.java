package com.server.cacheserver.dto;

public class StatsResponse {
    private long hits;
    private long misses;
    private long evictions;
    private long expiredRemovals;
    private int currentItems;
    private int maxItems;

    public StatsResponse() {
    }

    public StatsResponse(long hits, long misses, long evictions, long expiredRemovals, int currentItems, int maxItems) {
        this.hits = hits;
        this.misses = misses;
        this.evictions = evictions;
        this.expiredRemovals = expiredRemovals;
        this.currentItems = currentItems;
        this.maxItems = maxItems;
    }

    public long getHits() {
        return hits;
    }

    public void setHits(long hits) {
        this.hits = hits;
    }

    public long getMisses() {
        return misses;
    }

    public void setMisses(long misses) {
        this.misses = misses;
    }

    public long getEvictions() {
        return evictions;
    }

    public void setEvictions(long evictions) {
        this.evictions = evictions;
    }

    public long getExpiredRemovals() {
        return expiredRemovals;
    }

    public void setExpiredRemovals(long expiredRemovals) {
        this.expiredRemovals = expiredRemovals;
    }

    public int getCurrentItems() {
        return currentItems;
    }

    public void setCurrentItems(int currentItems) {
        this.currentItems = currentItems;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public void setMaxItems(int maxItems) {
        this.maxItems = maxItems;
    }
}
