package com.aeroport.infrastructure;

import com.aeroport.domain.Bilet;
import com.aeroport.domain.dao.BiletProvider;
import com.aeroport.infrastructure.clients.BiletClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Adaptor care "privește" spre Domeniu și implementează DAOContract-ul.
 */
@Component
public class BiletClientAdapter implements BiletProvider {

    private final BiletClient biletClient;

    public BiletClientAdapter(BiletClient biletClient) {
        this.biletClient = biletClient;
    }

    @Override
    public List<Bilet> getBiletePentruZbor(int idZbor) {
        return biletClient.getBiletePentruZbor(idZbor);
    }
}