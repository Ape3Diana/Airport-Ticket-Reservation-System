package com.aeroport.infrastructure;

import com.aeroport.domain.IExportStrategy;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

@Component
public class PdfExportStrategy implements IExportStrategy {

    @Override
    public byte[] exportData(List<Map<String, Object>> data) throws Exception {
        if (data == null || data.isEmpty()) return new byte[0];

        // 1. Inițializăm documentul PDF cu dimensiunea A4
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // 2. Conectăm documentul la fluxul nostru de memorie
        PdfWriter.getInstance(document, out);
        document.open(); // Trebuie deschis înainte de a scrie elemente în el

        // 3. Încărcăm un font din sistem (Arial) care suportă diacritice românești (IDENTITY_H)
        // EMBEDDED asigură că fontul este inclus în PDF, ca să se vadă corect pe orice PC.
        BaseFont arial = BaseFont.createFont("C:/Windows/Fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        // 4. Setăm fontul pentru Titlu și îl adăugăm
        Font titleFont = new Font(arial, 18, Font.BOLD, BaseColor.BLACK);
        Paragraph title = new Paragraph("Raport Export Date Aeroport", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f);
        document.add(title);

        // 5. Creăm structura tabelului PDF
        Map<String, Object> firstRow = data.get(0);
        PdfPTable table = new PdfPTable(firstRow.size());
        table.setWidthPercentage(100); // Se întinde pe toată lățimea disponibilă a paginii

        // 6. Formatăm Capul de Tabel (Antet) cu fundal gri închis și text alb
        Font headerFont = new Font(arial, 11, Font.BOLD, BaseColor.WHITE);
        for (String key : firstRow.keySet()) {
            PdfPCell headerCell = new PdfPCell(new Phrase(key.toUpperCase(), headerFont));
            headerCell.setBackgroundColor(BaseColor.DARK_GRAY);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerCell.setPadding(6f);
            table.addCell(headerCell);
        }

        // 7. Introducem datele rând cu rând
        Font dataFont = new Font(arial, 10, Font.NORMAL, BaseColor.BLACK);
        for (Map<String, Object> row : data) {
            for (Object value : row.values()) {
                String text = value != null ? value.toString() : "";
                PdfPCell cell = new PdfPCell(new Phrase(text, dataFont));
                cell.setPadding(5f);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
            }
        }

        document.add(table);

        // Finalizăm documentul. Abia la close() datele sunt efectiv scrise în flux (out)
        document.close();

        return out.toByteArray();
    }

    @Override
    public String getContentType() { return "application/pdf"; }

    @Override
    public String getFileExtension() { return "pdf"; }

    @Override
    public String getFormatName() { return "pdf"; }
}