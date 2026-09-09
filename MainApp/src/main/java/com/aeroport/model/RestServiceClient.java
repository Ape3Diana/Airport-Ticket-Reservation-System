package com.aeroport.model;

import com.aeroport.viewmodel.utils.LanguageManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

/**
 * Client de servicii REST responsabil pentru comunicarea cu microserviciile aplicației.
 * Intermediază toate operațiunile HTTP (GET, POST, PUT, DELETE) prin API Gateway.
 */
public class RestServiceClient {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    // URL-ul de bază al API Gateway-ului
    private static final String GATEWAY_URL = "http://localhost:9000";

    // Endpoints specifice rutate prin API Gateway
    private static final String URL_UTILIZATORI = GATEWAY_URL + "/api/utilizatori";
    private static final String URL_ZBORURI = GATEWAY_URL + "/api/zboruri";
    private static final String URL_BILETE = GATEWAY_URL + "/api/bilete";
    private static final String URL_NOTIFICARI = GATEWAY_URL + "/api/notificari";
    private static final String URL_EXPORT = GATEWAY_URL + "/api/export";
    private static final String URL_STATISTICI = GATEWAY_URL + "/api/statistici";

    /**
     * Metodă generică pentru a prelua o listă de date de la un anumit endpoint (Metoda GET).
     * * @param endpointUrl URL-ul complet al serviciului interogat.
     * @return O listă de obiecte GenericDataModel.
     * @throws Exception Dacă codul de răspuns nu este 200, returnează un mesaj localizat din LanguageManager.
     */
    public List<GenericDataModel> getListData(String endpointUrl) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            // Preluăm mesajul din dicționarul activ (RO, EN sau DE) în caz de eroare
            String mesajEroare = LanguageManager.getBundle().getString("error.fetch_data");
            throw new Exception(mesajEroare + response.body());
        }

        return mapper.readValue(response.body(), new TypeReference<List<GenericDataModel>>() {});
    }

    /**
     * Trimite o cerere de autentificare către microserviciul de utilizatori.
     * * @param email   Email-ul utilizatorului.
     * @param parola  Parola în clar.
     * @return Un obiect GenericDataModel reprezentând utilizatorul logat, sau null dacă autentificarea a eșuat.
     * @throws Exception În caz de probleme tehnice de rețea.
     */
    public GenericDataModel login(String email, String parola) throws Exception {
        Map<String, String> credentale = Map.of("email", email, "parola", parola);
        String body = mapper.writeValueAsString(credentale);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_UTILIZATORI + "/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), GenericDataModel.class);
        } else {
            // Logica de debugging cerută în consolă
            System.out.println("Eroare Login! Cod status: " + response.statusCode());
            System.out.println("Mesaj primit de la server: " + response.body());
            return null;
        }
    }

    /**
     * Trimite date noi către server (Metoda POST).
     * * @param url  Endpoint-ul țintă.
     * @param data Obiectul care va fi serializat în JSON și trimis.
     * @return Răspunsul brut primit de la server în format String.
     * @throws Exception Dacă statusul HTTP este >= 400 (Eroare client/server).
     */
    public String postData(String url, Object data) throws Exception {
        String json = mapper.writeValueAsString(data);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) {
            throw new Exception(response.body());
        }
        return response.body();
    }

    /**
     * Actualizează date existente pe server (Metoda PUT).
     * * @param url  Endpoint-ul țintă.
     * @param data Obiectul modificat ce trebuie salvat.
     * @return Răspunsul brut primit de la server.
     * @throws Exception Dacă statusul HTTP indică o eroare.
     */
    public String putData(String url, Object data) throws Exception {
        String json = mapper.writeValueAsString(data);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) {
            throw new Exception(response.body());
        }
        return response.body();
    }

    /**
     * Șterge o resursă de pe server pe baza URL-ului furnizat (Metoda DELETE).
     * * @param url Endpoint-ul exact al resursei ce se dorește ștearsă (conținând de regulă ID-ul).
     * @throws Exception Dacă ștergerea a eșuat pe server.
     */
    public void deleteData(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) {
            throw new Exception(response.body());
        }
    }

    /**
     * Preluarea biletelor asociate unui anumit zbor sub formă de structură generică (List of Maps).
     * Generat în mod special pentru pregătirea procesului de export.
     */
    public List<Map<String, Object>> getBileteZborAsMap(int idZbor) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL_BILETE + "/zbor/" + idZbor))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), new TypeReference<List<Map<String, Object>>>() {});
        }
        throw new Exception("Eroare la preluarea biletelor pentru export!");
    }

    /**
     * Trimite date către un microserviciu și descarcă răspunsul sub formă de flux binar (byte array).
     * Folositor pentru generarea și descărcarea fișierelor PDF, DOC sau Excel.
     */
    public byte[] downloadBinaryData(String url, Object data) throws Exception {
        String json = mapper.writeValueAsString(data);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

        if (response.statusCode() >= 400) {
            throw new Exception("Eroare la microserviciul de export!");
        }
        return response.body();
    }

    /**
     * Interoghează sistemul pentru a obține lista notificărilor în așteptare ale unui utilizator.
     * În caz de eșec returnează o listă goală pentru a nu bloca fluxul principal al aplicației.
     */
    public List<Map<String, Object>> getNotificariInAsteptare(String idUtilizator) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_NOTIFICARI + "/asteptare/" + idUtilizator))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return mapper.readValue(response.body(), new TypeReference<List<Map<String, Object>>>() {});
            }
        } catch (Exception e) {
            System.err.println("Eroare la preluarea notificărilor: " + e.getMessage());
        }
        return new java.util.ArrayList<>();
    }

    /**
     * Declanșează o alertă de securitate din panoul de administrare către un anumit utilizator.
     */
    public void trimiteAlertaSecuritate(int idUtilizator, String mesaj) {
        try {
            Map<String, Object> payload = Map.of(
                    "idUtilizator", idUtilizator,
                    "mesaj", mesaj
            );
            String json = mapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL_NOTIFICARI + "/trimite"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.err.println("Eroare la trimiterea alertei de securitate: " + e.getMessage());
        }
    }

    /**
     * Metodă generică pentru a prelua o listă de Map-uri (necesară pentru statistici).
     */
    public List<Map<String, Object>> getListMapData(String endpointUrl) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpointUrl))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), new TypeReference<List<Map<String, Object>>>() {});
        }
        throw new Exception("Eroare la preluarea datelor: HTTP " + response.statusCode());
    }

    // --- Getters pentru URL-urile Microserviciilor ---

    public String getUrlUtilizatori() { return URL_UTILIZATORI; }
    public String getUrlZboruri() { return URL_ZBORURI; }
    public String getUrlBilete() { return URL_BILETE; }
    public String getUrlNotificari() { return URL_NOTIFICARI; }
    public String getUrlExport() { return URL_EXPORT; }
    public String getUrlStatistici() { return URL_STATISTICI; }
}