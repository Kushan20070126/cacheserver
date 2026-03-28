package com.server.cacheserver.dto;

import com.server.cacheserver.model.CacheItem;

import java.util.List;

public class RangeResponse {
    private List<CacheItem> items;

    public RangeResponse(List<CacheItem> items) {
        this.items = items;
    }

    public List<CacheItem> getItems() {
        return items;
    }

    public void setItems(List<CacheItem> items) {
        this.items = items;
    }
}