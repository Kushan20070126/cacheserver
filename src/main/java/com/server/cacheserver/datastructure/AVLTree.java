package com.server.cacheserver.datastructure;

import com.server.cacheserver.model.CacheItem;

import java.util.ArrayList;
import java.util.List;

public class AVLTree {

    private static class AVLNode {
        long key;
        CacheItem value;
        AVLNode left;
        AVLNode right;
        int height;

        AVLNode(long key, CacheItem value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private AVLNode root;

    public void insert(long key, CacheItem value) {
        root = insert(root, key, value);
    }

    public CacheItem search(long key) {
        AVLNode node = searchNode(root, key);
        return node == null ? null : node.value;
    }

    public void delete(long key) {
        root = delete(root, key);
    }

    public List<CacheItem> rangeQuery(long startKey, long endKey) {
        List<CacheItem> result = new ArrayList<>();
        rangeQuery(root, startKey, endKey, result);
        return result;
    }

    private AVLNode insert(AVLNode node, long key, CacheItem value) {
        if (node == null) {
            return new AVLNode(key, value);
        }

        if (key < node.key) {
            node.left = insert(node.left, key, value);
        } else if (key > node.key) {
            node.right = insert(node.right, key, value);
        } else {
            node.value = value;
            return node;
        }

        updateHeight(node);
        return balance(node);
    }

    private AVLNode delete(AVLNode node, long key) {
        if (node == null) {
            return null;
        }

        if (key < node.key) {
            node.left = delete(node.left, key);
        } else if (key > node.key) {
            node.right = delete(node.right, key);
        } else {
            if (node.left == null && node.right == null) {
                return null;
            }
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }

            AVLNode successor = getMin(node.right);
            node.key = successor.key;
            node.value = successor.value;
            node.right = delete(node.right, successor.key);
        }

        updateHeight(node);
        return balance(node);
    }

    private AVLNode searchNode(AVLNode node, long key) {
        if (node == null) {
            return null;
        }

        if (key == node.key) {
            return node;
        }
        if (key < node.key) {
            return searchNode(node.left, key);
        }
        return searchNode(node.right, key);
    }

    private void rangeQuery(AVLNode node, long startKey, long endKey, List<CacheItem> result) {
        if (node == null) {
            return;
        }

        if (startKey < node.key) {
            rangeQuery(node.left, startKey, endKey, result);
        }

        if (startKey <= node.key && node.key <= endKey) {
            result.add(node.value);
        }

        if (endKey > node.key) {
            rangeQuery(node.right, startKey, endKey, result);
        }
    }

    private AVLNode getMin(AVLNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private int height(AVLNode node) {
        return node == null ? 0 : node.height;
    }

    private void updateHeight(AVLNode node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private int getBalance(AVLNode node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private AVLNode balance(AVLNode node) {
        int balanceFactor = getBalance(node);

        if (balanceFactor > 1) {
            if (getBalance(node.left) < 0) {
                node.left = rotateLeft(node.left);
            }
            return rotateRight(node);
        }

        if (balanceFactor < -1) {
            if (getBalance(node.right) > 0) {
                node.right = rotateRight(node.right);
            }
            return rotateLeft(node);
        }

        return node;
    }

    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode t2 = x.right;

        x.right = y;
        y.left = t2;

        updateHeight(y);
        updateHeight(x);

        return x;
    }

    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode t2 = y.left;

        y.left = x;
        x.right = t2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }
}