package com.aeroport.viewmodel.utils;

import java.util.Locale;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Manager responsabil cu gestionarea limbii curente a aplicației (Internaționalizare).
 * Utilizează proprietăți JavaFX pentru a permite actualizarea dinamică a interfeței.
 */
public class LanguageManager {

    // Româna este limba implicită a aplicației
    private static final ObjectProperty<Locale> locale = new SimpleObjectProperty<>(new Locale("ro"));

    /**
     * Oferă acces la proprietatea reactivă a obiectului Locale.
     * Utilă pentru componentele JavaFX care ascultă schimbările de limbă.
     */
    public static ObjectProperty<Locale> localeProperty() {
        return locale;
    }

    /**
     * Încarcă și returnează dicționarul de mesaje (ResourceBundle) corespunzător limbii active.
     * * @return ResourceBundle conținând traducerile din i18n/messages.
     */
    public static ResourceBundle getBundle() {
        return ResourceBundle.getBundle("i18n.messages", locale.get());
    }

    /**
     * Modifică limba curentă a aplicației.
     * * @param newLocale Noul obiect Locale (ex: Locale.ENGLISH, new Locale("de"), etc.).
     */
    public static void setLocale(Locale newLocale) {
        locale.set(newLocale);
    }
}