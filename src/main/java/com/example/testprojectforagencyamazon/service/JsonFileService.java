package com.example.testprojectforagencyamazon.service;

import com.example.testprojectforagencyamazon.data.ReportFile;
import com.example.testprojectforagencyamazon.data.response.ResponseContainer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableCaching
public class JsonFileService {

    private final ObjectMapper objectMapper;

    private static final Path FILE_PATH = Paths.get("src", "test_report.json");

    public ResponseContainer updateFile() {
        ResponseContainer responseContainer = new ResponseContainer();
        ReportFile reportFile;

        if (!Files.exists(FILE_PATH)) {
            log.error("File not found at path: {}", FILE_PATH);
            return responseContainer.setErrorMessageAndStatusCode("File not found",
                    HttpStatus.NOT_FOUND.value());
        }

        try (BufferedReader reader = Files.newBufferedReader(FILE_PATH, StandardCharsets.UTF_8)) {
            reportFile = objectMapper.readValue(reader, ReportFile.class);
        } catch (IOException e) {
            log.error("Error reading file {}: {}", FILE_PATH, e.getMessage());
            return responseContainer.setErrorMessageAndStatusCode("Error reading file",
                    HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        log.info("File successfully updated: {}", FILE_PATH);
        return responseContainer.setSuccessResult(reportFile);
    }
}


