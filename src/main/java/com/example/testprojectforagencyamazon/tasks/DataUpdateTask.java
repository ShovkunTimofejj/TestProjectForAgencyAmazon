package com.example.testprojectforagencyamazon.tasks;

import com.example.testprojectforagencyamazon.data.ReportFile;
import com.example.testprojectforagencyamazon.data.ReportSpecification;
import com.example.testprojectforagencyamazon.data.SummaryByAsinDto;
import com.example.testprojectforagencyamazon.data.SummaryByDateDto;
import com.example.testprojectforagencyamazon.data.response.ResponseContainer;
import com.example.testprojectforagencyamazon.model.StatsByAsin;
import com.example.testprojectforagencyamazon.model.StatsByDate;
import com.example.testprojectforagencyamazon.repository.ReportSpecificationRepository;
import com.example.testprojectforagencyamazon.repository.StatsByAsinRepository;
import com.example.testprojectforagencyamazon.repository.StatsByDateRepository;
import com.example.testprojectforagencyamazon.service.CacheService;
import com.example.testprojectforagencyamazon.service.JsonFileService;
import com.example.testprojectforagencyamazon.service.StatsService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataUpdateTask {

    private final CacheService cacheService;
    private final JsonFileService jsonFileService;
    private final StatsByDateRepository statsByDateRepository;
    private final StatsByAsinRepository statsByAsinRepository;
    private final ReportSpecificationRepository reportSpecificationRepository;
    private final StatsService statsService;

    @PostConstruct
    @Scheduled(cron = "0 0/5 * * * *")
    public void process() {
        try {
            ResponseContainer responseContainer = jsonFileService.updateFile();
            if (responseContainer.isError()) {
                logError("Database not updated");
                return;
            }

            ReportFile reportFile = (ReportFile) responseContainer.getResult();
            ReportSpecification reportSpecification = reportFile.getReportSpecification();
            List<StatsByDate> salesAndTrafficByDate = reportFile.getSalesAndTrafficByDate();
            List<StatsByAsin> salesAndTrafficByAsin = reportFile.getSalesAndTrafficByAsin();

            List<StatsByDate> savedStatsByDates = statsByDateRepository.saveAll(salesAndTrafficByDate);
            List<StatsByAsin> savedStatsByAsin = statsByAsinRepository.saveAll(salesAndTrafficByAsin);

            ReportSpecification existingReport = reportSpecificationRepository
                    .findReportSpecificationByDataStartTimeAndDataEndTime(
                            reportSpecification.getDataStartTime(), reportSpecification.getDataEndTime());
            ReportSpecification savedReport = Objects.isNull(existingReport)
                    ? reportSpecificationRepository.save(reportSpecification)
                    : existingReport;

            log.info("Database updated");

            cacheService.clearCache();

            ResponseContainer asinResponse = statsService.getSumStats("asin");
            if (asinResponse.isError()) {
                logError(asinResponse.getErrorMessage());
            } else {
                Object result = asinResponse.getResult();
                if (result instanceof ResponseEntity) {
                    result = ((ResponseEntity<?>) result).getBody();
                }
                SummaryByAsinDto asinSummary = (SummaryByAsinDto) result;
                cacheService.updateAsinSumCache(asinSummary);
            }

            ResponseContainer dateResponse = statsService.getSumStats("date");
            if (dateResponse.isError()) {
                logError(dateResponse.getErrorMessage());
            } else {
                Object result = dateResponse.getResult();
                if (result instanceof ResponseEntity) {
                    result = ((ResponseEntity<?>) result).getBody();
                }
                SummaryByDateDto dateSummary = (SummaryByDateDto) result;
                cacheService.updateDateSumCache(dateSummary);
            }

            cacheService.updateStatsByDateCache(savedStatsByDates);
            cacheService.updateStatsByAsinCache(savedStatsByAsin);
            cacheService.updateReportSpecification(savedReport);

            log.info("Cache updated successfully");

        } catch (Exception e) {
            logError(e.getMessage());
        }
    }

    private void logError(String errorMessage) {
        log.error(errorMessage);
    }
}


