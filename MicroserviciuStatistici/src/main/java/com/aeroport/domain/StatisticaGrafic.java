package com.aeroport.domain;

/**
 * Clasa universală folosită pentru a trimite date către frontend sub formă de grafic.
 * Orice punct de pe grafic are nevoie de un Nume (eticheta) și o Valoare numerică.
 */
public class StatisticaGrafic {

    // Reprezintă numele elementului măsurat (ex: "Zborul RO301", "Destinația Londra")
    private String eticheta;

    // Reprezintă rezultatul calculului (ex: 15000 Euro venit, 85% ocupare)
    private Double valoare;

    // Constructor pentru a inițializa rapid obiectul când creăm listele de statistici
    public StatisticaGrafic(String eticheta, Double valoare) {
        this.eticheta = eticheta;
        this.valoare = valoare;
    }

    public String getEticheta() { return eticheta; }
    public void setEticheta(String eticheta) { this.eticheta = eticheta; }

    public Double getValoare() { return valoare; }
    public void setValoare(Double valoare) { this.valoare = valoare; }
}