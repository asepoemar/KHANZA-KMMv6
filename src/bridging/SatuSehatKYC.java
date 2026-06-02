package bridging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fungsi.koneksiDB;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 * Modul Verifikasi Profil KYC (Know Your Customer) SATUSEHAT
 * Ref: https://satusehat.kemkes.go.id/platform/docs/id/kyc/
 *
 * Semua request KYC didelegasikan ke kyc_api.php di web server.
 * URL web server disimpan di setting/database.xml key URLKYCPHP.
 */
public class SatuSehatKYC {

    private final ApiSatuSehat api = new ApiSatuSehat();
    private final ObjectMapper mapper = new ObjectMapper();

    public String challengeCode = "";
    public String ihsNumber     = "";
    public String generateUrl   = "";
    public String errorMessage  = "";

    /**
     * Mendapatkan kode akses 6 digit yang tampil di SATUSEHAT Mobile pasien.
     */
    public boolean generateChallengeCode(String nik, String nama) {
        challengeCode = "";
        ihsNumber     = "";
        errorMessage  = "";
        try {
            ObjectNode body = mapper.createObjectNode();
            body.put("action", "challenge_code");
            body.put("nik",    nik);
            body.put("nama",   nama);

            String json = callKycApi(body);
            System.out.println("KYC Challenge Code Response: " + json);

            JsonNode root = mapper.readTree(json);
            if (root.path("success").asBoolean()) {
                challengeCode = root.path("challenge_code").asText();
                ihsNumber     = root.path("ihs_number").asText();
                return true;
            } else {
                String err = root.path("error").asText();
                errorMessage = err.isEmpty() ? "Gagal mendapatkan kode akses" : err;
                return false;
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
            System.out.println("SatuSehatKYC generateChallengeCode: " + e);
            return false;
        }
    }

    /**
     * Membuat URL verifikasi profil pasien via kyc_api.php di web server.
     */
    public boolean generateValidationUrl(String agentName, String agentNik) {
        generateUrl  = "";
        errorMessage = "";
        try {
            ObjectNode body = mapper.createObjectNode();
            body.put("action",     "generate_url");
            body.put("agent_name", agentName);
            body.put("agent_nik",  agentNik);

            String json = callKycApi(body);
            System.out.println("KYC Generate URL Response: " + json);

            JsonNode root = mapper.readTree(json);
            if (root.path("success").asBoolean()) {
                generateUrl = root.path("url").asText();
                return true;
            } else {
                String err = root.path("error").asText();
                errorMessage = err.isEmpty() ? "Gagal membuat URL verifikasi" : err;
                return false;
            }
        } catch (Exception e) {
            errorMessage = e.getMessage();
            System.out.println("SatuSehatKYC generateValidationUrl: " + e);
            return false;
        }
    }

    /**
     * POST JSON ke kyc_api.php, kembalikan response body sebagai String.
     * URL diambil dari database.xml key URLKYCPHP.
     */
    private String callKycApi(ObjectNode body) throws Exception {
        String url = koneksiDB.URLKYCPHP();
        System.out.println("KYC API URL: " + url);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(body), headers);
        String response = api.getRest()
                .exchange(url, HttpMethod.POST, request, String.class)
                .getBody();
        System.out.println("KYC API raw response: " + response);
        return response;
    }
}
