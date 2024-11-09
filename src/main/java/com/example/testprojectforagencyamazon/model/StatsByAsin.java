package com.example.testprojectforagencyamazon.model;

import com.example.testprojectforagencyamazon.data.Sale;
import com.example.testprojectforagencyamazon.data.SalesByAsin;
import com.example.testprojectforagencyamazon.data.TrafficByAsin;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "salesAndTrafficByAsin")
@NoArgsConstructor
@Data
public class StatsByAsin {
    @Id
    String parentAsin;
    SalesByAsin<Sale> salesByAsin;
    TrafficByAsin trafficByAsin;
}
