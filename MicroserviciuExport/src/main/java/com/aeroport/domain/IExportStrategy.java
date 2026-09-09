package com.aeroport.domain;

import java.util.List;
import java.util.Map;

/**
 * Interfața IExportStrategy reprezintă contractul pentru toate tipurile de export.
 * Folosim Design Pattern-ul "Strategy" pentru a decupla logica de export de Controller/Service.
 */
public interface IExportStrategy {

    /**
     * Metoda principală care conține logica de generare a fișierului.
     * @param data Lista de rânduri (fiecare rând este un Map cu Cheie=NumeColoană, Valoare=Date)
     * @return Un șir de octeți (byte[]) reprezentând fișierul fizic generat.
     */
    byte[] exportData(List<Map<String, Object>> data) throws Exception;

    /**
     * Returnează tipul MIME necesar browserului pentru a ști cum să interpreteze fișierul.
     * Ex: "application/json", "application/pdf".
     */
    String getContentType();

    /**
     * Returnează extensia fișierului pentru a seta corect numele la descărcare.
     * Ex: "csv", "pdf", "docx".
     */
    String getFileExtension();

    /**
     * Cheia unică prin care Serviciul va recunoaște această strategie.
     * Acest nume este cel primit de la client prin URL (ex: /api/export/pdf).
     */
    String getFormatName();
}