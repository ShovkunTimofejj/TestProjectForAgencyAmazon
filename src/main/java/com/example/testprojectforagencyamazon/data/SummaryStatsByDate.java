package com.example.testprojectforagencyamazon.data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SummaryStatsByDate {
    Double orderedProductSales;
    Double orderedProductSalesB2B;
    int unitsOrdered;
    int unitsOrderedB2B;
    int totalOrderItems;
    int totalOrderItemsB2B;
    Double averageSalesPerOrderItem;
    Double averageSalesPerOrderItemB2B;
    Double averageUnitsPerOrderItem;
    Double averageUnitsPerOrderItemB2B;
    Double averageSellingPrice;
    Double averageSellingPriceB2B;
    int unitsRefunded;
    Double refundRate;
    int claimsGranted;
    Double claimsAmount;
    Double shippedProductSales;
    int unitsShipped;
    int ordersShipped;
    int browserPageViews;
    int browserPageViewsB2B;
    int mobileAppPageViews;
    int mobileAppPageViewsB2B;
    int pageViews;
    int pageViewsB2B;
    int browserSessions;
    int browserSessionsB2B;
    int mobileAppSessions;
    int mobileAppSessionsB2B;
    int sessions;
    int sessionsB2B;
    Double buyBoxPercentage;
    Double buyBoxPercentageB2B;
    Double orderItemSessionPercentage;
    Double orderItemSessionPercentageB2B;
    Double unitSessionPercentage;
    Double unitSessionPercentageB2B;
    int averageOfferCount;
    int averageParentItems;
    int feedbackReceived;
    int negativeFeedbackReceived;
    int receivedNegativeFeedbackRate;


}
