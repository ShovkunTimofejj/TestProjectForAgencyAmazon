package com.example.testprojectforagencyamazon.data;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummaryByAsinDto {
    SalesByAsin<Double> salesByAsin;
    TrafficByAsin trafficByAsin;
}
