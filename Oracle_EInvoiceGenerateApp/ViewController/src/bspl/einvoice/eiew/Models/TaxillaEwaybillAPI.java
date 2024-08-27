package bspl.einvoice.eiew.Models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.net.URI;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class TaxillaEwaybillAPI {

    public static CompletableFuture<String> EwayAccessToken(ResultSet dt) {
        String InvResult = "";
        try {
            String URL = Taxilla_API_Urls.AuthApi;
            String jsonstring = new ObjectMapper().writeValueAsString("");
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(URL))
                    .header("Content-Type", "application/json")
                    .header("gspappid", dt.getString("EINVUSERNAME").toString())
                    .header("gspappsecret", dt.getString("EINVPASSWORD").toString())
                    .POST(HttpRequest.BodyPublishers.ofString(jsonstring, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                InvResult = response.body();
                JsonNode json = new ObjectMapper().readTree(InvResult);
                if (json.get("Status").asInt() == 0) {
                    String ErrorMsg = "Error";
                } else {
                    String LRData2 = response.body();
                    AccessTokenResult value2 = new ObjectMapper().readValue(LRData2, AccessTokenResult.class);
                }
            }
            return CompletableFuture.completedFuture(InvResult);
        } catch (Exception err) {
            System.out.println(err.toString());
            return CompletableFuture.completedFuture(err.getMessage());
        }
    }

    public static CompletableFuture<String> GenerateEwayBill(ResultSet dt, String jsonfile, String RequestId, String type, String auth) {
        String InvResult = "";
        try {
            String URL = Taxilla_API_Urls.GenEwaybillApi;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(URL))
                    .header("Content-Type", "application/json")
                    .header("username", dt.getString(0).get("EFUSERNAME").toString())
                    .header("password", dt.getString("EFPASSWORD").toString())
                    .header("gstin", dt.getString("GSTIN").toString())
                    .header("requestid", RequestId)
                    .header("Authorization", "Bearer " + auth)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonfile, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                InvResult = response.body();
                EwaySuccessRoot value2 = new ObjectMapper().readValue(response.body(), EwaySuccessRoot.class);
                if (value2.success) {
                    String commandText = "UPDATE EWB_GEN_STD SET EWAY_BILL_NO=?, EWAY_BILL_DATE=?, PDF_URL=?, ERRORMSG=? WHERE DOCNO=?";
                    try (Connection connection = DriverManager.getConnection(OraDBConnection.OrclConnection());
                         PreparedStatement command = connection.prepareStatement(commandText)) {
                        command.setString(1, value2.result.ewayBillNo);
                        command.setString(2, value2.result.ewayBillDate);
                        command.setString(3, value2.message);
                        command.setString(4, value2.result.alert);
                        command.setString(5, dt.getString("DOCNO").toString());
                        int rowsAffected = command.executeUpdate();
                        InvResult = "1";
                    }
                } else {
                    String sqlstr = "update EWB_GEN_STD set ERRORMSG='" + InvResult + "' where id='" + dt.getString("ID").toString() + "' and DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
                    int i = DataLayer.ExecuteNonQuery(OraDBConnection.OrclConnection(), CommandType.TEXT, sqlstr);
                }
            }
        } catch (Exception ex) {
            String sqlstr = "update EWB_GEN_STD set ERRORMSG='" + InvResult + "' where id='" + dt.getString("ID").toString() + "' and DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
            int i = DataLayer.ExecuteNonQuery(OraDBConnection.OrclConnection(), CommandType.TEXT, sqlstr);
            InvResult = ex.getMessage();
        }
        return CompletableFuture.completedFuture(InvResult);
    }

    public static CompletableFuture<String> GenEwayBill(ResultSet dt, String jsonfile, String RequestId, String type, String auth) {
        String InvResult = "";
        try {
            String URL = Taxilla_API_Urls.GenEwaybillApi;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(URL))
                    .header("Content-Type", "application/json")
                    .header("username", dt.getString("EINVUSERNAME").toString())
                    .header("password", dt.getString("EINVPASSWORD").toString())
                    .header("gstin", dt.getString("GSTIN").toString())
                    .header("requestid", RequestId)
                    .header("Authorization", "Bearer " + auth)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonfile, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                InvResult = response.body();
                ClsDynamic.WriteLog(InvResult, "EWB_" + dt.getString("DOC_NO").toString());
                EwaySuccessRoot value2 = new ObjectMapper().readValue(response.body(), EwaySuccessRoot.class);
                if (value2.success) {
                    String commandText = "UPDATE einvoice_generate_temp SET EWBNO=?, EWBDT=?, EWBVALIDTILL=?, ERRORMSG=? WHERE DOC_NO=?";
                    try (Connection connection = DriverManager.getConnection(OraDBConnection.OrclConnection());
                         PreparedStatement command = connection.prepareStatement(commandText)) {
                        command.setString(1, value2.result.ewayBillNo);
                        command.setString(2, value2.result.ewayBillDate);
                        command.setString(3, value2.result.validUpto);
                        command.setString(4, value2.result.alert);
                        command.setString(5, dt.getString("DOC_NO").toString());
                        int rowsAffected = command.executeUpdate();
                        InvResult = "1";
                    }
                } else {
                    String sqlstr = "update einvoice_generate_temp set ERRORMSG='" + InvResult + "' where id='" + dt.getRows().get(0).get("ID").toString() + "' and DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
                    int i = DataLayer.ExecuteNonQuery(OraDBConnection.OrclConnection(), CommandType.TEXT, sqlstr);
                }
            } else {
                InvResult = response.toString();
            }
        } catch (Exception ex) {
            String sqlstr = "update einvoice_generate_temp set ERRORMSG='" + InvResult + "' where id='" + dt.getRows().get(0).get("ID").toString() + "' and DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
            int i = DataLayer.ExecuteNonQuery(OraDBConnection.OrclConnection(), CommandType.TEXT, sqlstr);
            InvResult = ex.getMessage();
        }
        return CompletableFuture.completedFuture(InvResult);
    }

    public static CompletableFuture<String> CancelEwayBill(ResultSet dt, String jsonfile, String RequestId, String type, String auth) {
        String InvResult = "", InvResDecData = "";
        try {
            String URL = Taxilla_API_Urls.CANEWBApi;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(URL))
                    .header("Content-Type", "application/json")
                    .header("username", dt.getString("EINVUSERNAME").toString())
                    .header("password", dt.getString("EINVPASSWORD").toString())
                    .header("gstin", dt.getString("GSTIN").toString())
                    .header("requestid", RequestId)
                    .header("Authorization", "Bearer " + auth)
                    .POST(HttpRequest.BodyPublishers.ofString(jsonfile, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                InvResult = response.body();
                CanEwaybillSuccessRoot value2 = new ObjectMapper().readValue(response.body(), CanEwaybillSuccessRoot.class);
                if (value2.success) {
                    String sqlstr = "update einvoice_generate set EWBDT='" + value2.result.cancelDate + "', STATUS='EWBCAN' where DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
                    int i = DataLayer.ExecuteNonQuery(OraDBConnection.OrclConnection(), CommandType.TEXT, sqlstr);
                    InvResult = "1";
                }
            }
            return CompletableFuture.completedFuture(InvResult);
        } catch (Exception ex) {
            String sqlstr = "update einvoice_generate set ERRORMSG='" + InvResult + "' where DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
            int i = DataLayer.ExecuteNonQuery(OraDBConnection.OrclConnection(), CommandType.TEXT, sqlstr);
            return CompletableFuture.completedFuture(ex.getMessage());
        }
    }
}
