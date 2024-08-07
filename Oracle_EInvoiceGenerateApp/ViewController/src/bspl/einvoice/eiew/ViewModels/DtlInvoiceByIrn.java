package bspl.einvoice.eiew.ViewModels;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.http.HttpHeaders;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.Map;

public class DtlInvoiceByIrn {
    public static CompletableFuture<String> DetailInvoiceByIrn(String IRN_No, String access_token, Map<String, String> dt) {
        String InvRData = "";
        String InvResult = "";
        try {
            String URL = All_API_Urls.EinvGetUrl;
            HttpClient client = HttpClient.newHttpClient();
            String param = "access_token=" + access_token + "&gstin=" + dt.get("GSTIN") + "&irn=" + IRN_No;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(URL + param))
                    .build();

            CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            response.thenApply(HttpResponse::body).thenAccept(body -> {
                try {
                    InvResult = body;
                    ObjectMapper mapper = new ObjectMapper();
                    INVDtlRootObject value2 = mapper.readValue(body, INVDtlRootObject.class);
                    INVDtlRootObject obj = value2;

                    // OracelDataInsert.InsertCancelDataOracle(obj, dt);
                    // SqlConnectionDB.CancelInesrtData(obj, dt);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).join();

            return CompletableFuture.completedFuture(InvRData = InvResult);
        } catch (Exception ex) {
            String sqlstr = "update einvoice_generate_temp set ERRORMSG='" + InvResult + "' where id='" + dt.get("ID") + "' and DOC_NO='" + dt.get("DOC_NO") + "'";
            try (Connection conn = DriverManager.getConnection(OraDBConnection.OrclConnection);
                 Statement stmt = conn.createStatement()) {
                int i = stmt.executeUpdate(sqlstr);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // ErrorLog.WriteLog(ex);
            return CompletableFuture.completedFuture(InvRData = ex.getMessage());
        }
    }
}

class INVDtlMessage {
    private String AckNo;
    private String AckDt;
    private String Irn;
    private String Status;
    private String SignedInvoice;
    private String SignedQRCode;
    private int EwbNo;
    private String EwbDt;
    private String EwbValidTill;

    // Getters and Setters
}

class INVDtlResults {
    private INVDtlMessage message;
    private String errorMessage;
    private String InfoDtls;
    private String status;
    private int code;

    // Getters and Setters
}

class INVDtlRootObject {
    private INVDtlResults results;

    // Getters and Setters
}