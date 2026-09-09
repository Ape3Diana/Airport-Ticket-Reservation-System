package com.aeroport.infrastructure;

import com.aeroport.domain.Zbor;
import com.aeroport.domain.dao.ZborProvider;
import com.aeroport.infrastructure.clients.ZborClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Adaptor care "privește" spre Domeniu și implementează DAOContract-ul.
 */
@Component
public class ZborClientAdapter implements ZborProvider {

    private final ZborClient zborClient;

    public ZborClientAdapter(ZborClient zborClient) {
        this.zborClient = zborClient;
    }

    @Override
    public List<Zbor> getAllZboruri() {
        return zborClient.getAllZboruri();
    }
}