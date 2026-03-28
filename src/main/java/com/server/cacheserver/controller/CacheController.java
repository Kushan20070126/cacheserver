package com.server.cacheserver.controller;

import com.server.cacheserver.dto.RangeResponse;
import com.server.cacheserver.dto.SetRequest;
import com.server.cacheserver.model.CacheItem;
import com.server.cacheserver.service.CacheService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cache")
public class CacheController {

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @PostMapping
    public String set(@RequestBody SetRequest request) {
        return cacheService.set(request.getKey(), request.getValue(), request.getTtlSeconds());
    }

    @GetMapping("/{key}")
    public String get(@PathVariable long key) {
        return cacheService.get(key);
    }

    @DeleteMapping("/{key}")
    public String delete(@PathVariable long key) {
        return cacheService.delete(key);
    }

    @GetMapping("/range")
    public List<CacheItem> range(@RequestParam long start, @RequestParam long end) {
        return cacheService.range(start, end);
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return cacheService.stats();
    }
}