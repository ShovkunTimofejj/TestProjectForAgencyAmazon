package com.example.testprojectforagencyamazon.controller;

import com.example.testprojectforagencyamazon.data.response.ResponseContainer;
import com.example.testprojectforagencyamazon.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@EnableCaching
public class StatsController {

    private final StatsService statsService;

    @Cacheable(value = "statsByDateCache", key = "#date")
    @GetMapping(value = "/stats", params = "date")
    public ResponseEntity<ResponseContainer> findByDate(@RequestParam String date) throws InterruptedException {
        ResponseContainer response = statsService.getStatsByDate(date);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @Cacheable(value = "statsByDatesCache", key = "#startDate + #endDate")
    @GetMapping(value = "/stats", params = {"startDate", "endDate"})
    public ResponseEntity<ResponseContainer> findByDates(@RequestParam String startDate,
                                                         @RequestParam String endDate) throws InterruptedException {
        ResponseContainer response = statsService.getStatsByDates(startDate, endDate);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @Cacheable(value = "statsByAsinCache", key = "#asin")
    @GetMapping(value = "/stats", params = "asin")
    public ResponseEntity<ResponseContainer> findByAsin(@RequestParam String asin) throws InterruptedException {
        ResponseContainer response = statsService.getStatsByAsin(asin);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @Cacheable(value = "statsByAsinListCache", key = "#asinList.toString()")
    @GetMapping(value = "/stats")
    public ResponseEntity<ResponseContainer> findByAsinList(
            @RequestParam List<String> asinList) throws InterruptedException {
        ResponseContainer response = statsService.getStatsByAsinList(asinList);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @Cacheable(value = "sumStatsCache", key = "#type")
    @GetMapping(value = "/stats/sum", params = "type")
    public ResponseEntity<ResponseContainer> findSumByDate(@RequestParam String type) throws InterruptedException {
        ResponseContainer response = statsService.getSumStats(type);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}

