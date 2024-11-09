package com.example.testprojectforagencyamazon.service;

import com.example.testprojectforagencyamazon.data.ReportSpecification;
import com.example.testprojectforagencyamazon.data.SummaryByAsinDto;
import com.example.testprojectforagencyamazon.data.SummaryByDateDto;
import com.example.testprojectforagencyamazon.model.StatsByAsin;
import com.example.testprojectforagencyamazon.model.StatsByDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableCaching
public class CacheService {

    private final CacheManager cacheManager;

    private void updateCache(String cacheName, Object key, Object value) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            log.error("Cache '{}' is null", cacheName);
        } else {
            cache.put(key, value);
            log.info("Cache '{}' updated with key '{}'", cacheName, key);
        }
    }

    public void updateStatsByDateCache(List<StatsByDate> list) {
        for (StatsByDate item : list) {
            updateCache("statsByDate", item.getDate(), item);
        }
    }

    public void updateStatsByAsinCache(List<StatsByAsin> list) {
        for (StatsByAsin item : list) {
            updateCache("statsByAsin", item.getParentAsin(), item);
        }
    }

    public void updateReportSpecification(ReportSpecification reportSpecification) {
        SimpleKey key = new SimpleKey(reportSpecification.getDataStartTime(), reportSpecification.getDataEndTime());
        updateCache("report", key, reportSpecification);
    }

    @Caching(evict = {@CacheEvict("summary"), @CacheEvict("statsByDate"), @CacheEvict("statsByAsin"),
            @CacheEvict("report")})
    public void clearCache() {
        log.info("Clearing all caches: summary, statsByDate, statsByAsin, report");
    }

    public void updateAsinSumCache(SummaryByAsinDto summary) {
        updateCache("summary", "asin", summary);
    }

    public void updateDateSumCache(SummaryByDateDto summary) {
        updateCache("summary", "date", summary);
    }
}

