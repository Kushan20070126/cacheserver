package com.server.cacheserver.cli;

import com.server.cacheserver.model.CacheItem;
import com.server.cacheserver.service.CacheService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class CommandLineCacheRunner implements CommandLineRunner {

    private final CacheService cacheService;

    public CommandLineCacheRunner(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public void run(String... args) {
        new Thread(this::startCli).start();
    }



    private void startCli() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Cache CLI started");
        System.out.println("SET <key> <value> <ttlSeconds>");
        System.out.println("GET <key>");
        System.out.println("DELETE <key>");
        System.out.println("RANGE <startKey> <endKey>");
        System.out.println("STATS");
        System.out.println("EXIT");

        while (true) {
            try {
                System.out.print("> ");
                String line = scanner.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\s+");
                String command = parts[0].toUpperCase();

                switch (command) {
                    case "SET" -> {
                        if (parts.length < 4) {
                            System.out.println("Usage: SET <key> <value> <ttlSeconds>");
                            continue;
                        }
                        long key = Long.parseLong(parts[1]);
                        String value = parts[2];
                        long ttl = Long.parseLong(parts[3]);
                        System.out.println(cacheService.set(key, value, ttl));
                    }
                    case "GET" -> {
                        if (parts.length < 2) {
                            System.out.println("Usage: GET <key>");
                            continue;
                        }
                        long key = Long.parseLong(parts[1]);
                        System.out.println(cacheService.get(key));
                    }
                    case "DELETE" -> {
                        if (parts.length < 2) {
                            System.out.println("Usage: DELETE <key>");
                            continue;
                        }
                        long key = Long.parseLong(parts[1]);
                        System.out.println(cacheService.delete(key));
                    }
                    case "RANGE" -> {
                        if (parts.length < 3) {
                            System.out.println("Usage: RANGE <startKey> <endKey>");
                            continue;
                        }
                        long start = Long.parseLong(parts[1]);
                        long end = Long.parseLong(parts[2]);
                        List<CacheItem> items = cacheService.range(start, end);

                        if (items.isEmpty()) {
                            System.out.println("No items found");
                        } else {
                            for (CacheItem item : items) {
                                System.out.println(item);
                            }
                        }
                    }
                    case "STATS" -> System.out.println(cacheService.stats());
                    case "EXIT" -> {
                        System.out.println("Exiting CLI");
                        return;
                    }
                    default -> System.out.println("Unknown command");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}