package com.server.cacheserver.model;


public class CacheItem {
    private long key;
    private String value;
    private long expiryTimeMillis;
    private long sizeInBytes;

    public CacheItem() {
    }

    public CacheItem(long key, String value, long expiryTimeMillis) {
        this.key = key;
        this.value = value;
        this.expiryTimeMillis = expiryTimeMillis;
        this.sizeInBytes = estimateSize(key, value);
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
        this.sizeInBytes = estimateSize(this.key, this.value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        this.sizeInBytes = estimateSize(this.key, this.value);
    }

    public long getExpiryTimeMillis() {
        return expiryTimeMillis;
    }

    public void setExpiryTimeMillis(long expiryTimeMillis) {
        this.expiryTimeMillis = expiryTimeMillis;
    }

    public long getSizeInBytes() {
        return sizeInBytes;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiryTimeMillis;
    }

    private long estimateSize(long key, String value) {
        long keySize = Long.BYTES;
        long valueSize = value == null ? 0 : value.length() * 2L;
        long objectOverhead = 32L;
        return keySize + valueSize + objectOverhead;
    }

    @Override
    public String toString() {
        return "CacheItem{" +
                "key=" + key +
                ", value='" + value + '\'' +
                ", expiryTimeMillis=" + expiryTimeMillis +
                ", sizeInBytes=" + sizeInBytes +
                '}';
    }
}
