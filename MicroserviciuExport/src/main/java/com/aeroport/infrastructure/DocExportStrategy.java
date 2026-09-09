package com.aeroport.infrastructure;

import com.aeroport.domain.IExportStrategy;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@Component
public class DocExportStrategy implements IExportStrategy {

    @Override
    public byte[] exportData(List<Map<String, Object>> data) throws Exception {
        if (data == null || data.isEmpty()) return new byte[0];

        // Folosim try-with-resources pentru a închide automat documentul și fluxul de memorie
        try (XWPFDocument document = new XWPFDocument();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // 1. Construim Titlul Documentului
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("Raport Export Date Aeroport");
            titleRun.setBold(true);
            titleRun.setFontSize(16);

            // 2. Creăm tabelul. Număr rânduri = dimensiunea listei + 1 (pentru antet)
            int rows = data.size();
            int cols = data.get(0).size();
            XWPFTable table = document.createTable(rows + 1, cols);

            // 3. Populăm primul rând (Antetul) cu numele coloanelor (cheile din Map)
            Map<String, Object> firstRow = data.get(0);
            int colIdx = 0;
            for (String key : firstRow.keySet()) {
                table.getRow(0).getCell(colIdx).setText(key.toUpperCase());
                colIdx++;
            }

            // 4. Populăm restul tabelului cu datele efective
            int rowIdx = 1; // Începem de la rândul 1 (rândul 0 e antetul)
            for (Map<String, Object> row : data) {
                colIdx = 0;
                for (Object value : row.values()) {
                    table.getRow(rowIdx).getCell(colIdx).setText(value != null ? value.toString() : "");
                    colIdx++;
                }
                rowIdx++;
            }

            // Scriem conținutul generat în ByteArrayOutputStream
            document.write(out);
            // Returnăm documentul sub formă de octeți
            return out.toByteArray();
        }
    }

    // MIME type oficial pentru fișiere Word moderne (.docx)
    @Override
    public String getContentType() {
        return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    }

    @Override
    public String getFileExtension() { return "docx"; }

    @Override
    public String getFormatName() { return "doc"; }
}