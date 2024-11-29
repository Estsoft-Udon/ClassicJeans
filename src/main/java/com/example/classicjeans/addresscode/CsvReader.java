package com.example.classicjeans.addresscode;

import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.*;
import java.nio.charset.Charset;

@RequiredArgsConstructor
public class CsvReader {
    public static CSVParser readCsvFile(String filePath) throws IOException {
        Reader reader = new InputStreamReader(new FileInputStream(filePath), Charset.forName("EUC-KR"));

        CSVParser csvParser = new CSVParser(reader,
                CSVFormat.DEFAULT
                        .withHeader()              // 헤더를 사용
                        .withIgnoreHeaderCase());    // 헤더 대소문자 무시
        return csvParser;
    }
}