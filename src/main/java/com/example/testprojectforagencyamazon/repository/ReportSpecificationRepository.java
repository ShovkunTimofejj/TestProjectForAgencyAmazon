package com.example.testprojectforagencyamazon.repository;

import com.example.testprojectforagencyamazon.data.ReportSpecification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReportSpecificationRepository extends MongoRepository<ReportSpecification, String> {
    ReportSpecification findReportSpecificationByDataStartTimeAndDataEndTime(String dataStartTime, String dataEndTime);

}
