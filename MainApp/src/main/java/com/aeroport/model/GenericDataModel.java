package com.aeroport.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Model de date generic utilizat pentru maparea flexibilă și dinamică a JSON-urilor
 * primite de la microservicii. Extrage și formatează automat informațiile esențiale.
 */
public class GenericDataModel {

    private String id;
    private List<String> info = new ArrayList<>();

    /**
     * Interceptează proprietatea "id" din JSON și o procesează recursiv
     * pentru a asigura obținerea unei valori de tip text.
     * * @param idRaw Valoarea brută a ID-ului din JSON.
     */
    @JsonProperty("id")
    public void unpackId(Object idRaw) {
        this.id = recursiveDiscover(idRaw);
    }

    /**
     * Interceptează dinamic orice proprietate necunoscută direct în clasă (mecanism Catch-All).
     * Descoperă recursiv conținutul și îl adaugă în lista de informații.
     * * @param key   Numele proprietății din JSON.
     * @param value Valoarea asociată proprietății.
     */
    @JsonAnySetter
    public void addAllFields(String key, Object value) {
        if (value != null) {
            info.add(recursiveDiscover(value));
        }
    }

    /**
     * Motorul de descoperire recursivă a informației.
     * Analizează structura obiectului și o transformă într-o reprezentare text elegantă.
     * * @param value Obiectul care trebuie analizat (Map, List, String, etc.).
     * @return Textul formatat extras din obiect.
     */
    private String recursiveDiscover(Object value) {
        // Cazul 1: Obiectul este o structură de tip Cheie-Valoare (Dicționar)
        if (value instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) value;

            // Strategia 1: Căutăm câmpuri specifice unui aeroport (Nume + Cod IATA)
            if (map.containsKey("nume")) {
                String result = map.get("nume").toString();
                if (map.containsKey("codIata")) {
                    result += " (" + map.get("codIata") + ")";
                } else if (map.containsKey("cod_iata")) {
                    result += " (" + map.get("cod_iata") + ")";
                }
                return result;
            }

            // Fallback: Dacă nu există nume, căutăm structura după Oraș
            if (map.containsKey("oras")) {
                String result = map.get("oras").toString();
                if (map.containsKey("codIata") || map.containsKey("cod_iata")) {
                    Object cod = map.containsKey("codIata") ? map.get("codIata") : map.get("cod_iata");
                    result += " (" + cod + ")";
                }
                return result;
            }

            // Strategia 2: Dacă structura este complet necunoscută, iterăm prin toate valorile din interior
            return map.values().stream()
                    .map(this::recursiveDiscover)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.joining(", "));
        }

        // Cazul 2: Obiectul este o listă de elemente
        if (value instanceof List) {
            return ((List<?>) value).stream()
                    .map(this::recursiveDiscover)
                    .collect(Collectors.joining("; "));
        }

        // Cazul 3: Valoare simplă / Primitivă
        if (value != null) {
            String valStr = value.toString();

            // LOGICA DE FORMATARE TIMP: Transformă formatul ISO (ex: 2026-05-18T14:30:00) în "YYYY-MM-DD HH:mm"
            if (valStr.contains("T") && valStr.length() >= 16) {
                return valStr.substring(0, 10) + " " + valStr.substring(11, 16);
            }
            return valStr;
        }

        return "";
    }

    // --- Getters și Setters ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getInfo() {
        return info;
    }

    public void setInfo(List<String> info) {
        this.info = info;
    }
}