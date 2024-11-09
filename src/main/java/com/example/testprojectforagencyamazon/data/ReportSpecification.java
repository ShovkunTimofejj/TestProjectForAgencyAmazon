package com.example.testprojectforagencyamazon.data;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReportSpecification {
    String reportType;
    ReportOptions reportOptions;
    String dataStartTime;
    String dataEndTime;
    List<String> marketplaceIds;
}
