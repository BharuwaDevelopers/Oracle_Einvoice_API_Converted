package bspl.einvoice.eiew.ViewModels;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
//import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.google.gson.JsonObject;

import java.sql.ResultSet;

public class GetAccessToken {

    public static String Einvoice_API_Login(ResultSet dt) {
        String InvResult = "";
        try {
            String URL = All_API_Urls.EinvLoginUrl;
            String jsonstring = GetJsonAuthentication_API(dt);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(jsonstring, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                InvResult = response.body();
                JsonObject json = new JSONObject(InvResult);
                if (json.getInt("Status") == 0) {
                    String ErrorMsg = "Error";
                } else {
                    String LRData2 = response.body();
                    ObjectMapper mapper = new ObjectMapper();
                    Authentication_API_Response value2 = mapper.readValue(LRData2, Authentication_API_Response.class);
                    InvResult = value2.getAccess_token();
                }
            } else {
                InvResult = response.body();
                InvResult = "";
            }
            return InvResult;
        } catch (IOException | InterruptedException e) {
            return InvResult = e.getMessage();
        }
    }

    public static String GetJsonAuthentication_API(ResultSet dt) {
        Authentication_API Authentication_APIobj = new Authentication_API();
        Authentication_APIobj.setUsername(dt.getRows().get(0).get("EINVUSERNAME").toString());
        Authentication_APIobj.setPassword(dt.getRows().get(0).get("EINVPASSWORD").toString());
        Authentication_APIobj.setClient_id(dt.getRows().get(0).get("EFUSERNAME").toString());
        Authentication_APIobj.setClient_secret(dt.getRows().get(0).get("EFPASSWORD").toString());
        Authentication_APIobj.setGrant_type("password");

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(Authentication_APIobj);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class Authentication_API {
        private String username;
        private String password;
        private String client_id;
        private String client_secret;
        private String grant_type;

        // Getters and Setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getClient_id() {
            return client_id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }

        public String getClient_secret() {
            return client_secret;
        }

        public void setClient_secret(String client_secret) {
            this.client_secret = client_secret;
        }

        public String getGrant_type() {
            return grant_type;
        }

        public void setGrant_type(String grant_type) {
            this.grant_type = grant_type;
        }
    }

    public static class Authentication_API_Response {
        private String access_token;
        private String expires_in;
        private String token_type;

        // Getters and Setters
        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }

        public String getToken_type() {
            return token_type;
        }

        public void setToken_type(String token_type) {
            this.token_type = token_type;
        }
    }

    // Dummy DataTable class to simulate the DataTable in C#
    public static class ResultSet {
        private java.util.List<Map<String, Object>> rows = new java.util.ArrayList<>();

        public java.util.List<Map<String, Object>> getRows() {
            return rows;
        }

        public void addRow(Map<String, Object> row) {
            rows.add(row);
        }
    }

    // Dummy All_API_Urls class to simulate the All_API_Urls in C#
    public static class All_API_Urls {
        public static String EinvLoginUrl = "https://clientbasic.mastersindia.co/oauth/access_token";
    }
}