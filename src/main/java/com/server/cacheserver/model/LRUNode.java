package com.server.cacheserver.model;

public class LRUNode {
    private long key;
    private LRUNode prev;
    private LRUNode next;

    public LRUNode(long key) {
        this.key = key;
    }

    public long getKey() {
        return key;
    }

    public LRUNode getPrev() {
        return prev;
    }

    public void setPrev(LRUNode prev) {
        this.prev = prev;
    }

    public LRUNode getNext() {
        return next;
    }

    public void setNext(LRUNode next) {
        this.next = next;
    }
}