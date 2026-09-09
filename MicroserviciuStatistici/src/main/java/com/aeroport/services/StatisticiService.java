package com.aeroport.services;

import com.aeroport.domain.Bilet;
import com.aeroport.domain.StatisticaGrafic;
import com.aeroport.domain.Zbor;
import com.aeroport.domain.dao.BiletProvider;
import com.aeroport.domain.dao.ZborProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Conține logica de business.
 * Comunică doar cu Pachetul Domain. Nu are nicio idee despre cum funcționează infrastructura sau controlerele.
 */
@Service
public class StatisticiService {

    private final ZborProvider zborProvider;
    private final BiletProvider biletProvider;

    public StatisticiService(ZborProvider zborProvider, BiletProvider biletProvider) {
        this.zborProvider = zborProvider;
        this.biletProvider = biletProvider;
    }

    public List<StatisticaGrafic> getVenituriZboruri() {
        List<StatisticaGrafic> statistici = new ArrayList<>();
        List<Zbor> zboruri = zborProvider.getAllZboruri();

        for (Zbor zbor : zboruri) {
            List<Bilet> bilete = biletProvider.getBiletePentruZbor(zbor.getId());
            double venitTotal = bilete.stream().mapToDouble(Bilet::getPretPlatit).sum();
            statistici.add(new StatisticaGrafic(zbor.getNumarZbor(), venitTotal));
        }
        return statistici;
    }

    public List<StatisticaGrafic> getGradOcupare() {
        List<StatisticaGrafic> statistici = new ArrayList<>();
        List<Zbor> zboruri = zborProvider.getAllZboruri();

        for (Zbor zbor : zboruri) {
            List<Bilet> bilete = biletProvider.getBiletePentruZbor(zbor.getId());
            int locuriVandute = bilete.size();
            int capacitateTotala = zbor.getLocuriDisponibile() + locuriVandute;

            double procentOcupare = 0.0;
            if (capacitateTotala > 0) {
                procentOcupare = ((double) locuriVandute / capacitateTotala) * 100;
            }
            statistici.add(new StatisticaGrafic(zbor.getNumarZbor(), Math.round(procentOcupare * 100.0) / 100.0));
        }
        return statistici;
    }

    public List<StatisticaGrafic> getBiletePeDestinatie() {
        Map<String, Integer> vanzariPerDestinatie = new HashMap<>();
        List<Zbor> zboruri = zborProvider.getAllZboruri();

        for (Zbor zbor : zboruri) {
            int bileteVandute = biletProvider.getBiletePentruZbor(zbor.getId()).size();
            String orasSosire = zbor.getAeroportSosire().get("oras");

            vanzariPerDestinatie.put(
                    orasSosire,
                    vanzariPerDestinatie.getOrDefault(orasSosire, 0) + bileteVandute
            );
        }

        return vanzariPerDestinatie.entrySet().stream()
                .map(entry -> new StatisticaGrafic(entry.getKey(), (double) entry.getValue()))
                .collect(Collectors.toList());
    }
}