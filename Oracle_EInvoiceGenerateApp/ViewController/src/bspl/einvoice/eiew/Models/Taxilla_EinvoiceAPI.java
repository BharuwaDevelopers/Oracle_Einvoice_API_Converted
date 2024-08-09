package bspl.einvoice.eiew.Models;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

import java.nio.charset.StandardCharsets;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.sql.ResultSet;

public class Taxilla_EinvoiceAPI {
   
    public static CompletableFuture<String> accessToken(ResultSet dt) {
           return CompletableFuture.supplyAsync(() -> {
               String invResult = "";
               try {
                   String url = Taxilla_API_Urls.AuthApi;
                   String jsonString = new Gson().toJson("");
                   OkHttpClient client = new OkHttpClient();

                   RequestBody body = RequestBody.create(jsonString, MediaType.parse("application/json; charset=utf-8"));
                   Request request = new Request.Builder()
                           .url(url)
                           .addHeader("gspappid", dt.getRows().get(0).get("EFUSERNAME").toString())
                           .addHeader("gspappsecret", dt.getRows().get(0).get("EFPASSWORD").toString())
                           .post(body)
                           .build();

                   try (Response response = client.newCall(request).execute()) {
                       if (response.isSuccessful()) {
                           invResult = response.body().string();
                           JsonObject json = JsonParser.parseString(invResult).getAsJsonObject();
                           String lrData2 = response.body().string();
                           AccessTokenResult value2 = new Gson().fromJson(lrData2, AccessTokenResult.class);
                           // Sekkey = value2.getData().getSek();
                           // Authtoken = value2.getData().getAuthToken();
                       } else {
                           invResult = response.toString();
                       }
                   }
                   return invResult;
               } catch (Exception err) {
                   String sqlStr = "update einvoice_generate_temp set ERRORMSG='" + invResult + "' where DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
                   DataLayer.executeNonQuery(OraDBConnection.OrclConnection, sqlStr);
                   return err.getMessage();
               }
           });
       }

       public static CompletableFuture<String> generateIrn(ResultSet dt, String jsonFile, String requestId, String sessionId, String auth, String einvewb) {
           return CompletableFuture.supplyAsync(() -> {
               String invResult = "";
               String respResult = "";
               try {
                   String url = Taxilla_API_Urls.GenIrnApi;
                   OkHttpClient client = new OkHttpClient();

                   RequestBody body = RequestBody.create(jsonFile, MediaType.parse("application/json; charset=utf-8"));
                   Request request = new Request.Builder()
                           .url(url)
                           .addHeader("user_name", dt.getRows().get(0).get("EINVUSERNAME").toString())
                           .addHeader("password", dt.getRows().get(0).get("EINVPASSWORD").toString())
                           .addHeader("gstin", dt.getRows().get(0).get("GSTIN").toString())
                           .addHeader("requestid", requestId)
                           .addHeader("Authorization", "Bearer " + auth)
                           .post(body)
                           .build();

                   try (Response response = client.newCall(request).execute()) {
                       if (response.isSuccessful()) {
                           invResult = response.body().string();
                           respResult = invResult;
                           ClsDynamic.writeLog(respResult, dt.getRows().get(0).get("DOC_NO").toString().replace("/", "_"));
                           String lrData2 = response.body().string();
                           try {
                               SuccessRoot value2 = new Gson().fromJson(lrData2, SuccessRoot.class);
                               SuccessRoot obj = value2;
                               String infodata = "";
                               if (value2.isSuccess()) {
                                   if (obj.getInfo() != null) {
                                       infodata = obj.getInfo().get(0).getDesc().toString();
                                   } else {
                                       infodata = "";
                                   }

                                   String commandText = "UPDATE einvoice_generate_temp SET IRN='" + obj.getResult().getIrn() + "',ACKNo='" + obj.getResult().getAckNo() + "',ACKDATE='" + obj.getResult().getAckDt() + "',SIGNEDQRCODE='" + obj.getResult().getSignedQRCode() + "',EWBNO = '" + obj.getResult().getEwbNo() + "' , EWBDT = '" + obj.getResult().getEwbDt() + "',EWBVALIDTILL = '" + obj.getResult().getEwbValidTill() + "' ,QRCODEURL ='' ,EINVOICEPDF ='',ERRORMSG='',ErrorCode='" + infodata + "'  WHERE ID='" + sessionId + "' and  DOC_NO ='" + dt.getRows().get(0).get("DOC_No").toString() + "'";
                                   ClsDynamic.writeLog(commandText, dt.getRows().get(0).get("DOC_NO").toString().replace("/", "_"));
                                   try (Connection connection = DriverManager.getConnection(OraDBConnection.OrclConnection)) {
                                       PreparedStatement statement = connection.prepareStatement(commandText);
                                       statement.executeUpdate();
                                   }

                                   respResult = "1";
                                   if (value2.getResult().getEwbNo() == null && einvewb.equals("Y") && dt.getRows().get(0).get("DOC_TYP").toString().equals("INV")) {
                                       String jfile = Taxilla_EwaybillByIrnClasses.generateEwaybillIrnJson(dt, value2.getResult().getIrn(), "");
                                       ClsDynamic.jsonLog(jfile, dt.getRows().get(0).get("DOC_NO").toString().replace("/", "_"));
                                       String rInvData = Taxilla_EinvoiceAPI.generateEwaybillIrn(dt, jfile, requestId, sessionId, auth).join();
                                       if (rInvData.equals("1")) {
                                           respResult = "2";
                                       } else {
                                           respResult = "3";
                                       }
                                   }
                               } else {
                                   String dQuote = "'";
                                   respResult = invResult;
                                   ClsDynamic.updateErrorLog(respResult, dt.getRows().get(0).get("DOC_NO").toString());
                               }
                           } catch (Exception ex) {
                               try {
                                   SuccessRoot2 value2 = new Gson().fromJson(lrData2, SuccessRoot2.class);
                                   SuccessRoot2 obj = value2;
                                   String infodata = "";
                                   if (value2.isSuccess()) {
                                       if (obj.getInfo() != null) {
                                           infodata = obj.getInfo().get(0).getDesc().get(0).getErrorMessage().toString();
                                       } else {
                                           infodata = "";
                                       }

                                       String commandText = "UPDATE einvoice_generate_temp SET IRN='" + obj.getResult().getIrn() + "',ACKNo='" + obj.getResult().getAckNo() + "',ACKDATE='" + obj.getResult().getAckDt() + "',SIGNEDQRCODE='" + obj.getResult().getSignedQRCode() + "',EWBNO = '" + obj.getResult().getEwbNo() + "' , EWBDT = '" + obj.getResult().getEwbDt() + "',EWBVALIDTILL = '" + obj.getResult().getEwbValidTill() + "' ,QRCODEURL ='' ,EINVOICEPDF ='',ERRORMSG='',ErrorCode='" + infodata + "'  WHERE id='" + sessionId + "' and DOC_NO ='" + dt.getRows().get(0).get("DOC_No").toString() + "'";
                                       ClsDynamic.writeLog(commandText, dt.getRows().get(0).get("DOC_NO").toString().replace("/", "_"));
                                       try (Connection connection = DriverManager.getConnection(OraDBConnection.OrclConnection)) {
                                           PreparedStatement statement = connection.prepareStatement(commandText);
                                           statement.executeUpdate();
                                       }

                                       respResult = "1";

                                       if (value2.getResult().getEwbNo() == null && einvewb.equals("Y") && dt.getRows().get(0).get("DOC_TYP").toString().equals("INV")) {
                                           String jfile = Taxilla_EwaybillByIrnClasses.generateEwaybillIrnJson(dt, value2.getResult().getIrn(), "");
                                           ClsDynamic.jsonLog(jfile, dt.getRows().get(0).get("DOC_NO").toString().replace("/", "_"));
                                           String rInvData = Taxilla_EinvoiceAPI.generateEwaybillIrn(dt, jfile, requestId, sessionId, auth).join();
                                           if (rInvData.equals("1")) {
                                               respResult = "2";
                                           } else {
                                               respResult = "3";
                                           }
                                       }
                                   } else {
                                       String dQuote = "'";
                                       respResult = invResult;
                                       ClsDynamic.updateErrorLog(respResult, dt.getRows().get(0).get("DOC_NO").toString());
                                   }
                               } catch (Exception ex1) {
                                   try {
                                       DublicateRoot dupValue2 = new Gson().fromJson(lrData2, DublicateRoot.class);
                                       if (dupValue2.getResult().get(0).getDesc().getIrn() != null) {
                                           String irnDtl = detailInvoiceByIrn(dt, dupValue2.getResult().get(0).getDesc().getIrn(), "", "", auth).join();
                                       } else {
                                           String dQuote = "'";
                                           respResult = invResult;
                                           ClsDynamic.updateErrorLog(respResult, dt.getRows().get(0).get("DOC_NO").toString());
                                       }
                                   } catch (Exception ex2) {
                                       String dQuote = "'";
                                       respResult = invResult;
                                       ClsDynamic.updateErrorLog(respResult, dt.getRows().get(0).get("DOC_NO").toString());
                                   }
                               }
                           }
                       } else {
                           ClsDynamic.writeLog(response.toString(), dt.getRows().get(0).get("DOC_NO").toString().replace("/", "_"));
                           String dQuote = "'";
                           respResult = invResult;
                           ClsDynamic.updateErrorLog(respResult, dt.getRows().get(0).get("DOC_NO").toString());
                       }
                   }
               } catch (Exception ex) {
                   String dQuote = "'";
                   respResult = invResult;
                   ClsDynamic.writeLog(ex.getMessage(), dt.getRows().get(0).get("DOC_NO").toString().replace("/", "_"));
                   ClsDynamic.updateErrorLog(respResult, dt.getRows().get(0).get("DOC_NO").toString());
               }
               return respResult;
           });
       }

       public static CompletableFuture<String> cancelInvoice(ResultSet dt, String jsonFile, String requestId, String type, String auth) {
           return CompletableFuture.supplyAsync(() -> {
               String invRData = "";
               String invResult = "";
               try {
                   requestId = ClsDynamic.generateUniqueNumber();
                   String url = Taxilla_API_Urls.CanIrnApi;
                   OkHttpClient client = new OkHttpClient();

                   RequestBody body = RequestBody.create(jsonFile, MediaType.parse("application/json; charset=utf-8"));
                   Request request = new Request.Builder()
                           .url(url)
                           .addHeader("user_name", dt.getRows().get(0).get("EINVUSERNAME").toString())
                           .addHeader("password", dt.getRows().get(0).get("EINVPASSWORD").toString())
                           .addHeader("gstin", dt.getRows().get(0).get("GSTIN").toString())
                           .addHeader("requestid", requestId)
                           .addHeader("Authorization", "Bearer " + auth)
                           .post(body)
                           .build();

                   try (Response response = client.newCall(request).execute()) {
                       if (response.isSuccessful()) {
                           invResult = response.body().string();
                           JsonObject json = JsonParser.parseString(invResult).getAsJsonObject();
                           if (json.get("success").getAsBoolean()) {
                               String lrData2 = response.body().string();
                               EinvCanSuccessRoot value2 = new Gson().fromJson(lrData2, EinvCanSuccessRoot.class);
                               String sqlStr = "update einvoice_generate set Status='EINVCAN' where DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
                               DataLayer.executeNonQuery(OraDBConnection.OrclConnection, sqlStr);
                               invResult = "1";
                           } else {
                               String sqlStr = "update einvoice_generate set ERRORMSG='" + invResult + "' where DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
                               DataLayer.executeNonQuery(OraDBConnection.OrclConnection, sqlStr);
                           }
                       }
                   }
                   return invRData = invResult;
               } catch (Exception ex) {
                   String sqlStr = "update einvoice_generate set ERRORMSG='" + invResult + "' where DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
                   DataLayer.executeNonQuery(OraDBConnection.OrclConnection, sqlStr);
                   return invRData = ex.getMessage();
               }
           });
       }

       public static CompletableFuture<String> detailInvoiceByIrn(ResultSet dt, String irnNo, String requestId, String type, String auth) {
           return CompletableFuture.supplyAsync(() -> {
               String invRData = "";
               String invResult = "";
               try {
                   String url = Taxilla_API_Urls.GetInvbyIrn;
                   OkHttpClient client = new OkHttpClient();

                   Request request = new Request.Builder()
                           .url(url + "?irn=" + irnNo)
                           .addHeader("user_name", dt.getRows().get(0).get("EINVUSERNAME").toString())
                           .addHeader("password", dt.getRows().get(0).get("EINVPASSWORD").toString())
                           .addHeader("gstin", dt.getRows().get(0).get("GSTIN").toString())
                           .addHeader("requestid", requestId)
                           .addHeader("Authorization", "Bearer " + auth)
                           .get()
                           .build();

                   try (Response response = client.newCall(request).execute()) {
                       if (response.isSuccessful()) {
                           invResult = response.body().string();
                           String lrData2 = response.body().string();
                           // INVDtlRootObject value2 = new Gson().fromJson(lrData2, INVDtlRootObject.class);
                           // INVDtlRootObject obj = value2;

                           // OracelDataInsert.insertCancelDataOracle(obj, dt);
                           // SqlConnectionDB.cancelInesrtData(obj, dt);
                       }
                   }
                   return invRData = invResult;
               } catch (Exception ex) {
                   String sqlStr = "update einvoice_generate_temp set ERRORMSG='" + invResult + "' where DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
                   DataLayer.executeNonQuery(OraDBConnection.OrclConnection, sqlStr);
                   return invRData = ex.getMessage();
               }
           });
       }
       
    public static CompletableFuture<String> generateEwaybillIrn(ResultSet dt, String jsonfile, String requestId, String sessionId, String auth) {
           String invRData = "";
           String invResult = "";

           try {
               requestId = ClsDynamic.generateUniqueNumber();
               String url = TaxillaAPIUrls.GenEwbbyIrn;
               HttpClient client = HttpClient.newHttpClient();

               HttpRequest request = HttpRequest.newBuilder()
                       .uri(URI.create(url))
                       .header("Content-Type", "application/json")
                       .header("user_name", dt.getRows().get(0).get("EINVUSERNAME").toString())
                       .header("password", dt.getRows().get(0).get("EINVPASSWORD").toString())
                       .header("gstin", dt.getRows().get(0).get("GSTIN").toString())
                       .header("requestid", requestId)
                       .header("Authorization", "Bearer " + auth)
                       .POST(HttpRequest.BodyPublishers.ofString(jsonfile, StandardCharsets.UTF_8))
                       .build();

               CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
               response.thenAccept(res -> {
                   if (res.statusCode() == 200) {
                       try {
                           invResult = res.body();
                           ClsDynamic.writeLog(invResult, dt.getRows().get(0).get("DOC_NO").toString().replace("/", "_"));
                           ObjectMapper mapper = new ObjectMapper();
                           JsonNode json = mapper.readTree(invResult);
                           if (json.get("success").asBoolean()) {
                               IrnEwaybillSuccessRoot value2 = mapper.readValue(invResult, IrnEwaybillSuccessRoot.class);
                               String sqlstr = "update einvoice_generate_temp set EWBNO='" + value2.getResult().getEwbNo() + "',EWBDT='" + value2.getResult().getEwbDt() + "',EWBVALIDTILL='" + value2.getResult().getEwbValidTill() + "' where id='" + sessionId + "' and DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
                               ClsDynamic.writeLog(sqlstr, dt.getRows().get(0).get("DOC_NO").toString().replace("/", "_"));
                               executeNonQuery(sqlstr);
                               invResult = "1";
                           } else {
                               String rval = getEwaybillByIrn(dt, dt.getRows().get(0).get("IRN").toString(), "", "", auth).get();
                               if (!"1".equals(rval)) {
                                   String sqlstr = "update einvoice_generate_temp set ERRORMSG='" + json.get("message").asText() + "' where id='" + sessionId + "' and DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
                                   executeNonQuery(sqlstr);
                               }
                           }
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
               });

               return CompletableFuture.completedFuture(invResult);
           } catch (Exception ex) {
               ClsDynamic.writeLog(ex.toString(), dt.getRows().get(0).get("DOC_NO").toString().replace("/", "_"));
               String dQuote = "\"";
               String errmsg = invResult;
               String sqlstr = "update einvoice_generate_temp set ERRORMSG='" + errmsg.replace(dQuote, "") + "' where id='" + dt.getRows().get(0).get("ID").toString() + "' and DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
               executeNonQuery(sqlstr);
               return CompletableFuture.completedFuture(invResult);
           }
       }

       public static CompletableFuture<String> getEwaybillByIrn(ResultSet dt, String irnNo, String requestId, String type, String auth) {
           String invRData = "";
           String invResult = "";

           try {
               requestId = ClsDynamic.generateUniqueNumber();
               String url = TaxillaAPIUrls.GetEwbbyIrn;
               HttpClient client = HttpClient.newHttpClient();

               HttpRequest request = HttpRequest.newBuilder()
                       .uri(URI.create(url + "?irn=" + irnNo))
                       .header("Content-Type", "application/json")
                       .header("user_name", dt.getRows().get(0).get("EINVUSERNAME").toString())
                       .header("password", dt.getRows().get(0).get("EINVPASSWORD").toString())
                       .header("gstin", dt.getRows().get(0).get("GSTIN").toString())
                       .header("requestid", requestId)
                       .header("Authorization", "Bearer " + auth)
                       .GET()
                       .build();

               CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
               response.thenAccept(res -> {
                   if (res.statusCode() == 200) {
                       try {
                           invResult = res.body();
                           ClsDynamic.writeLog(invResult, dt.getRows().get(0).get("DOC_NO").toString().replace("/", "_"));
                           ObjectMapper mapper = new ObjectMapper();
                           GetEwaybillRoot ewbresult = mapper.readValue(invResult, GetEwaybillRoot.class);
                           if (ewbresult.isSuccess()) {
                               String sqlstr = "update einvoice_generate_temp set EWBNO='" + ewbresult.getResult().getEwbNo() + "',EWBDT='" + ewbresult.getResult().getEwbDt() + "',EWBVALIDTILL='" + ewbresult.getResult().getEwbValidTill() + "' where DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
                               ClsDynamic.writeLog(sqlstr, dt.getRows().get(0).get("DOC_NO").toString().replace("/", "_"));
                               executeNonQuery(sqlstr);
                               invResult = "1";
                           }
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
               });

               return CompletableFuture.completedFuture(invResult);
           } catch (Exception ex) {
               String sqlstr = "update einvoice_generate_temp set ERRORMSG='" + invResult + "' where id='" + dt.getRows().get(0).get("ID").toString() + "' and DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
               executeNonQuery(sqlstr);
               return CompletableFuture.completedFuture(ex.getMessage());
           }
       }

       public static CompletableFuture<String> cancelEwaybill(ResultSet dt, String jsonfile, String requestId, String type, String auth) {
           String invRData = "";
           String invResult = "";

           try {
               String url = TaxillaAPIUrls.CanEwb;
               HttpClient client = HttpClient.newHttpClient();

               HttpRequest request = HttpRequest.newBuilder()
                       .uri(URI.create(url))
                       .header("Content-Type", "application/json")
                       .header("user_name", dt.getRows().get(0).get("EINVUSERNAME").toString())
                       .header("password", dt.getRows().get(0).get("EINVPASSWORD").toString())
                       .header("gstin", dt.getRows().get(0).get("GSTIN").toString())
                       .header("requestid", requestId)
                       .header("Authorization", "Bearer " + auth)
                       .POST(HttpRequest.BodyPublishers.ofString(jsonfile, StandardCharsets.UTF_8))
                       .build();

               CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
               response.thenAccept(res -> {
                   if (res.statusCode() == 200) {
                       try {
                           invResult = res.body();
                           ObjectMapper mapper = new ObjectMapper();
                           JsonNode json = mapper.readTree(invResult);
                           if (json.get("success").asBoolean()) {
                               IrnEwaybillSuccessRoot value2 = mapper.readValue(invResult, IrnEwaybillSuccessRoot.class);
                               String sqlstr = "update einvoice_generate set status='CAN' where DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
                               executeNonQuery(sqlstr);
                               invResult = "1";
                           } else {
                               String sqlstr = "update einvoice_generate set ERRORMSG='" + json.get("message").asText() + "' where DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
                               executeNonQuery(sqlstr);
                           }
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                   }
               });

               return CompletableFuture.completedFuture(invResult);
           } catch (Exception ex) {
               String sqlstr = "update einvoice_generate set ERRORMSG='" + invResult + "' where DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
               executeNonQuery(sqlstr);
               return CompletableFuture.completedFuture(ex.getMessage());
           }
       }

       public static CompletableFuture<String> extractQRCode(ResultSet dt, String jsonfile, String requestId, String type, String auth) {
           String invRData = "";
           String invResult = "";

           try {
               String url = TaxillaAPIUrls.ExtQrCode;
               HttpClient client = HttpClient.newHttpClient();

               HttpRequest request = HttpRequest.newBuilder()
                       .uri(URI.create(url))
                       .header("Content-Type", "application/json")
                       .header("user_name", dt.getRows().get(0).get("EINVUSERNAME").toString())
                       .header("password", dt.getRows().get(0).get("EINVPASSWORD").toString())
                       .header("gstin", dt.getRows().get(0).get("GSTIN").toString())
                       .header("requestid", requestId)
                       .header("Authorization", "Bearer " + auth)
                       .POST(HttpRequest.BodyPublishers.ofString(jsonfile, StandardCharsets.UTF_8))
                       .build();

               CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
               response.thenAccept(res -> {
                   if (res.statusCode() == 200) {
                       invResult = res.body();
                   }
               });

               return CompletableFuture.completedFuture(invResult);
           } catch (Exception ex) {
               String sqlstr = "update einvoice_generate_temp set ERRORMSG='" + invResult + "' where DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
               executeNonQuery(sqlstr);
               return CompletableFuture.completedFuture(ex.getMessage());
           }
       }

       public static CompletableFuture<String> extractSignedInvoice(ResultSet dt, String jsonfile, String requestId, String type, String auth) {
           String invRData = "";
           String invResult = "";

           try {
               String url = TaxillaAPIUrls.ExtSignInv;
               HttpClient client = HttpClient.newHttpClient();

               HttpRequest request = HttpRequest.newBuilder()
                       .uri(URI.create(url))
                       .header("Content-Type", "application/json")
                       .header("user_name", dt.getRows().get(0).get("EINVUSERNAME").toString())
                       .header("password", dt.getRows().get(0).get("EINVPASSWORD").toString())
                       .header("gstin", dt.getRows().get(0).get("GSTIN").toString())
                       .header("requestid", requestId)
                       .header("Authorization", "Bearer " + auth)
                       .POST(HttpRequest.BodyPublishers.ofString(jsonfile, StandardCharsets.UTF_8))
                       .build();

               CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
               response.thenAccept(res -> {
                   if (res.statusCode() == 200) {
                       invResult = res.body();
                   }
               });

               return CompletableFuture.completedFuture(invResult);
           } catch (Exception ex) {
               String sqlstr = "update einvoice_generate_temp set ERRORMSG='" + invResult + "' where DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
               executeNonQuery(sqlstr);
               return CompletableFuture.completedFuture(ex.getMessage());
           }
       }

       public static CompletableFuture<String> generateQRImage(ResultSet dt, String jsonfile, String requestId, String type, String auth) {
           String invRData = "";
           String invResult = "";

           try {
               String url = TaxillaAPIUrls.GenQRImg;
               HttpClient client = HttpClient.newHttpClient();

               HttpRequest request = HttpRequest.newBuilder()
                       .uri(URI.create(url))
                       .header("Content-Type", "application/json")
                       .header("user_name", dt.getRows().get(0).get("EINVUSERNAME").toString())
                       .header("password", dt.getRows().get(0).get("EINVPASSWORD").toString())
                       .header("gstin", dt.getRows().get(0).get("GSTIN").toString())
                       .header("requestid", requestId)
                       .header("Authorization", "Bearer " + auth)
                       .POST(HttpRequest.BodyPublishers.ofString(jsonfile, StandardCharsets.UTF_8))
                       .build();

               CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
               response.thenAccept(res -> {
                   if (res.statusCode() == 200) {
                       invResult = res.body();
                   }
               });

               return CompletableFuture.completedFuture(invResult);
           } catch (Exception ex) {
               String sqlstr = "update einvoice_generate_temp set ERRORMSG='" + invResult + "' where DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
               executeNonQuery(sqlstr);
               return CompletableFuture.completedFuture(ex.getMessage());
           }
       }

       private static void executeNonQuery(String sql) {
           try (Connection conn = OraDBConnection.getOrclConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
               stmt.executeUpdate();
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
       
       
    public static CompletableFuture<String> getQRImage(ResultSet dt, String jsonfile, String requestId, String type, String auth) {
           String invRData = "";
           String invResult = "";
           try {
               String url = Taxilla_API_Urls.GetQRImg;
               HttpClient client = HttpClient.newHttpClient();

               HttpRequest request = HttpRequest.newBuilder()
                       .uri(URI.create(url))
                       .header("Accept", "application/json")
                       .header("user_name", dt.getRow(0).get("EINVUSERNAME").toString())
                       .header("password", dt.getRow(0).get("EINVPASSWORD").toString())
                       .header("gstin", dt.getRow(0).get("GSTIN").toString())
                       .header("requestid", requestId)
                       .header("width", "250")
                       .header("height", "250")
                       .header("imgtype", "jpg")
                       .header("Authorization", "Bearer " + auth)
                       .header("irn", dt.getRow(0).get("IRN").toString())
                       .POST(HttpRequest.BodyPublishers.ofString(jsonfile))
                       .build();

               CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

               response.thenApply(HttpResponse::body).thenAccept(body -> {
                   invResult = body;
                   // Process the response body if needed
               }).join();

               invRData = invResult;
           } catch (Exception ex) {
               String sqlstr = "update einvoice_generate_temp set ERRORMSG='" + invResult + "' where DOC_NO='" + dt.getRow(0).get("DOC_NO").toString() + "'";
               int i = DataLayer.executeNonQuery(OraDBConnection.getOrclConnection(), sqlstr);
               // ErrorLog.writeLog(ex);
               invRData = ex.getMessage();
           }

           return CompletableFuture.completedFuture(invRData);
       }
}
