package com.aeroport.services;

import com.aeroport.domain.IExportStrategy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExportService {

    // Un dicționar (Map) intern care asociază formatul cu clasa potrivită.
    // Ex: "csv" -> instanța de CsvExportStrategy
    private final Map<String, IExportStrategy> strategies = new HashMap<>();

    /**
     * Constructor Injection: Spring găsește automat absolut toate clasele din proiect care implementează
     * interfața IExportStrategy (deoarece au adnotarea @Component) și le trimite aici sub formă de listă.
     */
    public ExportService(List<IExportStrategy> strategyList) {
        for (IExportStrategy strategy : strategyList) {
            // Populăm dicționarul. Folosim toLowerCase() ca măsură de siguranță (dacă clientul trimite "PDF")
            strategies.put(strategy.getFormatName().toLowerCase(), strategy);
        }
    }

    /**
     * Metoda principală apelată de Controller.
     * Caută strategia corectă și declanșează procesul de export.
     */
    public byte[] generateExport(String format, List<Map<String, Object>> data) throws Exception {
        IExportStrategy strategy = getStrategy(format);
        if (strategy == null) {
            // Dacă nu avem o strategie pentru formatul cerut, aruncăm o eroare prinsă ulterior de Controller
            throw new IllegalArgumentException("Format nesuportat: " + format);
        }
        return strategy.exportData(data);
    }

    /**
     * Returnează obiectul de tip strategie pe baza numelui.
     * Este folosit și de Controller pentru a afla metadatele (ContentType, Extensie).
     */
    public IExportStrategy getStrategy(String format) {
        return strategies.get(format.toLowerCase());
    }
}