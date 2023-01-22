package com.ulegalize.lawfirm.service.v2.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheService {

    @Autowired
    CacheManager cacheManager;

    @Scheduled(cron = "0 1 * * * ?") // every day at 1 am
    public void evictAllCaches() {
        cacheManager.getCacheNames().stream()
                .forEach(cacheName -> cacheManager.getCache(cacheName).clear());
    }

    public void evictCaches(String name) {
        if (cacheManager.getCache(name) != null) {
            cacheManager.getCache(name).clear();
        }
    }
}
