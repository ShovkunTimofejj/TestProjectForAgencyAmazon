package com.example.testprojectforagencyamazon.data;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummaryByDateDto {
    SalesByDate<Double> salesByDate;
    TrafficByDate trafficByDate;

}
