package com.aeroport.domain.dao;

import com.aeroport.domain.Zbor;
import java.util.List;

/**
 * Contract din interiorul Domeniului.
 * Cere o modalitate de a obține zborurile.
 */
public interface ZborProvider {
    List<Zbor> getAllZboruri();
}