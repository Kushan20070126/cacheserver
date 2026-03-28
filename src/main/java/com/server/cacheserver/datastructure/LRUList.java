package com.server.cacheserver.datastructure;

import com.server.cacheserver.model.LRUNode;

public class LRUList {
    private LRUNode head;
    private LRUNode tail;

    public void addToFront(LRUNode node) {
        node.setPrev(null);
        node.setNext(head);

        if (head != null) {
            head.setPrev(node);
        }

        head = node;

        if (tail == null) {
            tail = node;
        }
    }

    public void moveToFront(LRUNode node) {
        if (node == null || node == head) {
            return;
        }

        remove(node);
        addToFront(node);
    }

    public void remove(LRUNode node) {
        if (node == null) {
            return;
        }

        if (node.getPrev() != null) {
            node.getPrev().setNext(node.getNext());
        } else {
            head = node.getNext();
        }

        if (node.getNext() != null) {
            node.getNext().setPrev(node.getPrev());
        } else {
            tail = node.getPrev();
        }

        node.setPrev(null);
        node.setNext(null);
    }

    public Long removeTail() {
        if (tail == null) {
            return null;
        }

        long key = tail.getKey();
        remove(tail);
        return key;
    }
}