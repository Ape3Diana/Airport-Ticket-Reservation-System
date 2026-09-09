package com.aeroport.domain;

/**
 * Clasa Bilet reprezintă modelul de date pentru un bilet achiziționat.
 * Conține doar informațiile strict necesare pentru calculul statisticilor noastre.
 */
public class Bilet {

    // Atribut care stochează suma de bani plătită pentru acest bilet.
    // Este folosit pentru a calcula venitul total generat de un zbor.
    private double pretPlatit;

    // Getter: returnează prețul biletului
    public double getPretPlatit() {
        return pretPlatit;
    }

    // Setter: setează prețul biletului (folosit la deserializarea din JSON)
    public void setPretPlatit(double pretPlatit) {
        this.pretPlatit = pretPlatit;
    }
}