package com.aeroport.infrastructure;

import com.aeroport.domain.IExportStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JsonExportStrategy implements IExportStrategy {

    // Instanțiem ObjectMapper (librăria Jackson).
    // Acesta "știe" să ia obiecte Java (precum Liste sau Map-uri) și să le convertească în text JSON valid.
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] exportData(List<Map<String, Object>> data) throws Exception {
        // writeValueAsBytes face automat magia: parcurge structura List<Map> și o transformă direct în byte[] JSON
        return objectMapper.writeValueAsBytes(data);
    }

    @Override
    public String getContentType() { return "application/json"; }

    @Override
    public String getFileExtension() { return "json"; }

    @Override
    public String getFormatName() { return "json"; }
}