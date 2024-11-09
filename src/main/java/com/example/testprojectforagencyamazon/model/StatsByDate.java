package com.example.testprojectforagencyamazon.model;

import com.example.testprojectforagencyamazon.data.Sale;
import com.example.testprojectforagencyamazon.data.SalesByDate;
import com.example.testprojectforagencyamazon.data.TrafficByDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "salesAndTrafficByDate")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class StatsByDate {
    @Id
    String date;
    SalesByDate<Sale> salesByDate;
    TrafficByDate trafficByDate;
}
