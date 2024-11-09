package com.example.testprojectforagencyamazon.data;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesByDate<T> {
    T orderedProductSales;
    T orderedProductSalesB2B;
    int unitsOrdered;
    int unitsOrderedB2B;
    int totalOrderItems;
    int totalOrderItemsB2B;
    T averageSalesPerOrderItem;
    T averageSalesPerOrderItemB2B;
    double averageUnitsPerOrderItem;
    double averageUnitsPerOrderItemB2B;
    T averageSellingPrice;
    T averageSellingPriceB2B;
    int unitsRefunded;
    double refundRate;
    int claimsGranted;
    T claimsAmount;
    T shippedProductSales;
    int unitsShipped;
    int ordersShipped;
}
