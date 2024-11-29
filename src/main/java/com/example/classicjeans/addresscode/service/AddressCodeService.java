package com.example.classicjeans.addresscode.service;

import com.example.classicjeans.addresscode.CsvReader;
import com.example.classicjeans.addresscode.entity.AddressCode;
import com.example.classicjeans.addresscode.repository.AddressCodeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AddressCodeService {
    private final AddressCodeRepository repository;

    @Transactional(timeout = 3600)
    public void readCsvAndSave(String filePath) throws IOException {
        CSVParser csvParser = CsvReader.readCsvFile(filePath);

        for (CSVRecord record : csvParser) {
            AddressCode entity = new AddressCode(record.get("col1"), record.get("col2"));

            if(!repository.existsByCode(record.get("col1"))) {
                repository.save(entity);
            }
        }

        csvParser.close();
    }

    public String getAddress(String code) {
        AddressCode entity = repository.findByCode(code);

        if(entity == null) {
            return "";
        }

        return entity.getAddress();
    }
}
