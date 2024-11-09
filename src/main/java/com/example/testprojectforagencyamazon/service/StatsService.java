package com.example.testprojectforagencyamazon.service;

import com.example.testprojectforagencyamazon.data.SummaryStatsByAsin;
import com.example.testprojectforagencyamazon.data.SummaryStatsByDate;
import com.example.testprojectforagencyamazon.data.response.ResponseContainer;
import com.example.testprojectforagencyamazon.mapper.SummaryMapper;
import com.example.testprojectforagencyamazon.repository.StatsByAsinRepository;
import com.example.testprojectforagencyamazon.repository.StatsByDateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableCaching
public class StatsService {
    private final StatsByDateRepository statsByDateRepository;
    private final StatsByAsinRepository statsByAsinRepository;
    private final SummaryMapper summaryMapper;
    private final CacheManager cacheManager;

    public ResponseContainer getStatsByDate(String date) {
        return getStatsFromCache("statsByDate", date, () -> statsByDateRepository.findByDate(date));
    }

    public ResponseContainer getStatsByDates(String startDate, String endDate) {
        LocalDate startLocalDate = LocalDate.parse(startDate);
        LocalDate endLocalDate = LocalDate.parse(endDate);
        List<LocalDate> dateRange = startLocalDate.datesUntil(endLocalDate.plusDays(1)).toList();

        return getStatsFromCache("statsByDate", dateRange, () -> statsByDateRepository.findByDateBetween(
                startLocalDate.minusDays(1).toString(), endLocalDate.plusDays(1).toString()));
    }

    public ResponseContainer getStatsByAsin(String asin) {
        return getStatsFromCache("statsByAsin", asin, () -> statsByAsinRepository.findByParentAsin(asin));
    }

    public ResponseContainer getStatsByAsinList(List<String> asinList) {
        return getStatsFromCache("statsByAsin", asinList, () -> statsByAsinRepository
                .findByParentAsinIn(asinList));
    }

    public ResponseContainer getSumStats(String type) {
        if (!isValidType(type)) {
            return buildErrorResponse("incorrect type", HttpStatus.BAD_REQUEST);
        }

        Cache cache = cacheManager.getCache("summary");
        if (cache != null && cache.get(type) != null) {
            return buildSuccessResponse(cache.get(type).get(), type);
        }

        return type.equals("asin") ? getSummaryByAsin() : getSummaryByDate();
    }

    private <T> ResponseContainer getStatsFromCache(String cacheName, Object key, Supplier<T> dbQuery) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null && cache.get(key) != null) {
            return buildSuccessResponse(cache.get(key).get(), cacheName);
        }

        try {
            T result = dbQuery.get();
            if (result != null) {
                cache.put(key, result);
                return buildSuccessResponse(result, cacheName);
            }
            return buildErrorResponse(cacheName + " not found", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logError(e);
            return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseContainer buildSuccessResponse(Object result, String cacheName) {
        ResponseContainer responseContainer = new ResponseContainer();
        return responseContainer.setSuccessResult(result);
    }

    private ResponseContainer buildErrorResponse(String message, HttpStatus status) {
        ResponseContainer responseContainer = new ResponseContainer();
        return responseContainer.setErrorMessageAndStatusCode(message, status.value());
    }

    private boolean isValidType(String type) {
        return "asin".equals(type) || "date".equals(type);
    }

    private ResponseContainer getSummaryByAsin() {
        try {
            SummaryStatsByAsin stats = statsByAsinRepository.sumByAsin();
            return buildSuccessResponse(summaryMapper.toAsinDto(stats), "asin");
        } catch (Exception e) {
            logError(e);
            return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseContainer getSummaryByDate() {
        try {
            SummaryStatsByDate stats = statsByDateRepository.sumByDate();
            return buildSuccessResponse(summaryMapper.toDateDto(stats), "date");
        } catch (Exception e) {
            logError(e);
            return buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void logError(Exception e) {
        log.error("Error: {}", e.getMessage(), e);
    }
}
