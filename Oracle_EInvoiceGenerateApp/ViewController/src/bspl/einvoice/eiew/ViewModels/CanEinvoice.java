package bspl.einvoice.eiew.ViewModels;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.json.JSONObject;


public class CanEinvoice {

    public static String CancelJsonFile(DataTable dt, String irn, String token) {
        String JsTest = "";
        try {
            CancelRootObject Calceljson = new CancelRootObject();
            Calceljson.access_token = token;
            Calceljson.user_gstin = dt.getValueAt(0, "GSTIN").toString();
            Calceljson.irn = dt.getValueAt(0, "IRN").toString();
            Calceljson.cancel_reason = "1 ";
            Calceljson.cancel_remarks = "cancel Remarks";

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String objAddjson = mapper.writeValueAsString(Calceljson);
            return JsTest = objAddjson;
        } catch (JsonProcessingException ex) {
            // ErrorLog.WriteLog(ex);
        }
        return JsTest;
    }

    public static CompletableFuture<String> CancelInvoice(String JsonFile, String sekdec, DataTable dt) {
        String InvRData = "";
        String InvResult = "";
        String ErrorMsg = "";
        try {
            String URL = All_API_Urls.EinvcancelUrl;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(JsonFile, StandardCharsets.UTF_8))
                    .build();

            CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            return response.thenApply(res -> {
                if (res.statusCode() == 200) {
                    try {
                        InvResult = res.body();
                        ObjectMapper mapper = new ObjectMapper();
                        CancelRoot value2 = mapper.readValue(InvResult, CancelRoot.class);
                        CancelRoot obj = value2;

                        // SqlConnectionDB.CancelInesrtData(obj, dt);
                        OracelDataInsert.InsertCancelDataOracle(obj, dt);
                        if (!value2.results.errorMessage.isEmpty()) {
                            // error update
                        } else {
                            // Irn number and cancel date update
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return InvRData = InvResult;
            }).exceptionally(ex -> {
                String sqlstr = "update einvoice_generate_temp set ERRORMSG='" + InvResult + "' where id='" + dt.getValueAt(0, "ID").toString() + "' and DOC_NO='" + dt.getValueAt(0, "DOC_NO").toString() + "'";
                try (Connection conn = OraDBConnection.OrclConnection;
                     PreparedStatement pstmt = conn.prepareStatement(sqlstr)) {
                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                // ErrorLog.WriteLog(ex);
                return InvRData = ex.getMessage();
            }).get();
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }
        return CompletableFuture.completedFuture(InvRData);
    }
}

class CancelRootObject {
    public String access_token;
    public String user_gstin;
    public String irn;
    public String cancel_reason;
    public String cancel_remarks;
}

class CncelMessage {
    public String Irn;
    public String CancelDate;
}

class CancelResults {
    public CncelMessage message;
    public String errorMessage;
    public String InfoDtls;
    public String status;
    public int code;
}

class CancelRoot {
    public CancelResults results;
}
