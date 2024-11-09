package com.example.testprojectforagencyamazon.repository;

import com.example.testprojectforagencyamazon.data.SummaryStatsByDate;
import com.example.testprojectforagencyamazon.model.StatsByDate;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface StatsByDateRepository extends MongoRepository<StatsByDate, String> {

    List<StatsByDate> findByDateBetween(String startDate, String endDate);
    Optional<StatsByDate> findByDate(String date);

    String SALES_FIELD_PREFIX = "$salesByDate";
    String TRAFFIC_FIELD_PREFIX = "$trafficByDate";

    String SUM_BY_DATE_AGGREGATION = "{" +
            "\"$group\": {" +
            "\"_id\": \"" + SALES_FIELD_PREFIX + ".id\"," +

            // Summarizing sales figures
            "\"orderedProductSales\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".orderedProductSales.amount\"}," +
            "\"orderedProductSalesB2B\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".orderedProductSalesB2B.amount\"}," +
            "\"unitsOrdered\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".unitsOrdered\"}," +
            "\"unitsOrderedB2B\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".unitsOrderedB2B\"}," +
            "\"totalOrderItems\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".totalOrderItems\"}," +
            "\"totalOrderItemsB2B\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".totalOrderItemsB2B\"}," +
            "\"averageSalesPerOrderItem\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".averageSalesPerOrderItem.amount\"}," +
            "\"averageSalesPerOrderItemB2B\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".averageSalesPerOrderItemB2B.amount\"}," +
            "\"averageUnitsPerOrderItem\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".averageUnitsPerOrderItem\"}," +
            "\"averageUnitsPerOrderItemB2B\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".averageUnitsPerOrderItemB2B\"}," +
            "\"averageSellingPrice\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".averageSellingPrice.amount\"}," +
            "\"averageSellingPriceB2B\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".averageSellingPriceB2B.amount\"}," +
            "\"unitsRefunded\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".unitsRefunded\"}," +
            "\"refundRate\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".refundRate\"}," +
            "\"claimsGranted\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".claimsGranted\"}," +
            "\"claimsAmount\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".claimsAmount.amount\"}," +
            "\"shippedProductSales\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".shippedProductSales.amount\"}," +
            "\"unitsShipped\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".unitsShipped\"}," +
            "\"ordersShipped\": {\"$sum\": \"" + SALES_FIELD_PREFIX + ".ordersShipped\"}," +

            // Summarizing traffic indicators
            "\"browserPageViews\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".browserPageVievs\"}," +
            "\"browserPageViewsB2B\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".browserPageVievsB2B\"}," +
            "\"mobileAppPageViews\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".MobileAppPageViews\"}," +
            "\"mobileAppPageViewsB2B\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".MobileAppPageViewsB2B\"}," +
            "\"pageViews\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".pageViews\"}," +
            "\"pageViewsB2B\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".pageViewsB2B\"}," +
            "\"browserSessions\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".browserSessions\"}," +
            "\"browserSessionsB2B\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".browserSessionsB2B\"}," +
            "\"mobileAppSessions\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".mobileAppSessions\"}," +
            "\"mobileAppSessionsB2B\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".mobileAppSessionsB2B\"}," +
            "\"sessions\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".sessions\"}," +
            "\"sessionsB2B\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".sessionsB2B\"}," +
            "\"buyBoxPercentage\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".buyBoxPercentage\"}," +
            "\"buyBoxPercentageB2B\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".buyBoxPercentageB2B\"}," +
            "\"orderItemSessionPercentage\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".orderItemSessionPercentage\"}," +
            "\"orderItemSessionPercentageB2B\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".orderItemSessionPercentageB2B\"}," +
            "\"unitSessionPercentage\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".unitSessionPercentage\"}," +
            "\"unitSessionPercentageB2B\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".unitSessionPercentageB2B\"}," +
            "\"averageOfferCount\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".averageOfferCount\"}," +
            "\"averageParentItems\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".averageParentItems\"}," +
            "\"feedbackReceived\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".feedbackReceived\"}," +
            "\"negativeFeedbackReceived\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".negativeFeedbackReceived\"}," +
            "\"receivedNegativeFeedbackRate\": {\"$sum\": \"" + TRAFFIC_FIELD_PREFIX + ".receivedNegativeFeedbackRate\"}" +
            "}}";

    @Aggregation(pipeline = {SUM_BY_DATE_AGGREGATION})
    SummaryStatsByDate sumByDate();
}

