package com.example.testprojectforagencyamazon.data;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SalesByAsin<T> {
    int unitsOrdered;
    int unitsOrderedB2B;
    T orderedProductSales;
    T orderedProductSalesB2B;
    int totalOrderItems;
    int totalOrderItemsB2B;
}
