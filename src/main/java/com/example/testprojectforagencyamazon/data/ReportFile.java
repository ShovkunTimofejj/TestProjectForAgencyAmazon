package com.example.testprojectforagencyamazon.data;

import com.example.testprojectforagencyamazon.model.StatsByAsin;
import com.example.testprojectforagencyamazon.model.StatsByDate;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ReportFile {

    ReportSpecification reportSpecification;
    List<StatsByDate> salesAndTrafficByDate;
    List<StatsByAsin> salesAndTrafficByAsin;
}
