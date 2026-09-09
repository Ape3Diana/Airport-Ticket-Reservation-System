package com.aeroport.infrastructure;

import com.aeroport.domain.IExportStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CsvExportStrategy implements IExportStrategy {

    @Override
    public byte[] exportData(List<Map<String, Object>> data) throws Exception {
        // Validare de siguranță: dacă nu avem date, returnăm un fișier gol
        if (data == null || data.isEmpty()) return new byte[0];

        // Folosim StringBuilder pentru că este mult mai eficient la concatenarea de text decât "+"
        StringBuilder csvBuilder = new StringBuilder();

        // 1. Extragem capul de tabel (numele coloanelor) din primul element
        Map<String, Object> firstRow = data.get(0);
        // Alipim cheile separate prin virgulă și trecem pe rândul următor (\n)
        csvBuilder.append(String.join(",", firstRow.keySet())).append("\n");

        // 2. Parcurgem fiecare rând de date
        for (Map<String, Object> row : data) {
            // Transformăm valorile în String-uri. Dacă o valoare e null, punem un string gol ""
            List<String> values = row.values().stream()
                    .map(v -> v != null ? v.toString() : "")
                    .toList();
            // Adăugăm valorile pe rând, separate prin virgulă
            csvBuilder.append(String.join(",", values)).append("\n");
        }

        // Convertim textul final într-un șir de octeți pentru a putea fi trimis prin HTTP
        return csvBuilder.toString().getBytes();
    }

    @Override
    public String getContentType() { return "text/csv"; }

    @Override
    public String getFileExtension() { return "csv"; }

    @Override
    public String getFormatName() { return "csv"; }
}