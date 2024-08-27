package bspl.einvoice.eiew.ViewModels;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.net.URI;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;



public class CanEinvoice {

    public String CancelJsonFile(ResultSet dt, String irn, String token) throws SQLException {
        String JsTest = "";
        try {
            CancelRootObject Calceljson = new CancelRootObject();
            Calceljson.access_token = token;
            Calceljson.user_gstin = dt.getString("GSTIN").toString();
            Calceljson.irn = dt.getString("IRN").toString();
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

    public CompletableFuture<String> CancelInvoice(String JsonFile, String sekdec, ResultSet dt) {
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
                        OracleDataInsert.InsertCancelDataOracle(obj, dt);
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


class CancelRootObject {
    public String access_token;
    public String user_gstin;

    void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    String getAccess_token() {
        return access_token;
    }

    void setUser_gstin(String user_gstin) {
        this.user_gstin = user_gstin;
    }

    String getUser_gstin() {
        return user_gstin;
    }

    void setIrn(String irn) {
        this.irn = irn;
    }

    String getIrn() {
        return irn;
    }

    void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    String getCancel_reason() {
        return cancel_reason;
    }

    void setCancel_remarks(String cancel_remarks) {
        this.cancel_remarks = cancel_remarks;
    }

    String getCancel_remarks() {
        return cancel_remarks;
    }
    public String irn;
    public String cancel_reason;
    public String cancel_remarks;
}

class CncelMessage {
    public String Irn;

    void setIrn(String Irn) {
        this.Irn = Irn;
    }

    String getIrn() {
        return Irn;
    }

    void setCancelDate(String CancelDate) {
        this.CancelDate = CancelDate;
    }

    String getCancelDate() {
        return CancelDate;
    }
    public String CancelDate;
}

class CancelResults {
    public CncelMessage message;
    public String errorMessage;
    public String InfoDtls;

    void setMessage(CncelMessage message) {
        this.message = message;
    }

    CncelMessage getMessage() {
        return message;
    }

    void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    String getErrorMessage() {
        return errorMessage;
    }

    void setInfoDtls(String InfoDtls) {
        this.InfoDtls = InfoDtls;
    }

    String getInfoDtls() {
        return InfoDtls;
    }

    void setStatus(String status) {
        this.status = status;
    }

    String getStatus() {
        return status;
    }

    void setCode(int code) {
        this.code = code;
    }

    int getCode() {
        return code;
    }
    public String status;
    public int code;
}

 class CancelRoot {
    public CancelResults results;

    public CancelResults getResults() {
        return results;
    }

    public void setResults(CancelResults results) {
        this.results = results;
    }
}
}