package bspl.einvoice.eiew.ViewModels;

import java.util.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.sql.ResultSet;

public class EwaybillByIrn {
    public String Generate_EwaybillIrn_Json(ResultSet dt, String sessionId, String access_token) {
        String msg = "";
        try {
            List<e_waybillIrnBO> ewaybillIrn_obj = new ArrayList<>();
            e_waybillIrnBO rootDtls = new e_waybillIrnBO();
            rootDtls.access_token = access_token;
            rootDtls.user_gstin = dt.getString("GSTIN").toString();
            rootDtls.irn = dt.getString("IRN").toString();
            rootDtls.transporter_id = dt.getString("EWAY_TRANSPORTAR_ID").toString();
            rootDtls.transportation_mode = dt.getString("EWAY_TRANSPORTAR_MODE").toString();
            rootDtls.transporter_document_number = dt.getString("EWAY_TRANSPORTAR_DOCNO").toString();
            rootDtls.transporter_document_date = new SimpleDateFormat("dd/MM/yyyy").format(dt.getRows().get(0).get("EWAY_TRANSPORTAR_DOCDT"));
            rootDtls.vehicle_number = dt.getString("EWAY_TRANSPORTAR_VEHINO").toString();
            rootDtls.distance = Integer.parseInt(dt.getString("EWAY_TRANSPORTAR_DISTANCE").toString());
            rootDtls.vehicle_type = dt.getString("EWAY_TRANSPORTAR_VEHITYPE").toString();
            rootDtls.transporter_name = dt.getString("EWAY_TRANSPORTAR_NAME").toString();
            rootDtls.data_source = "erp";

            ewaybillIrn_obj.add(rootDtls);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(ewaybillIrn_obj);
            String mystr = json.substring(1, json.length() - 2);
            return msg = mystr;
        } catch (Exception ex) {
            msg = "0";
            return msg;
        }
    }

    public CompletableFuture<String> Generate_EwaybillIrn(String JsonFile, String sekdec, ResultSet dt, String Type) {
        CompletableFuture<String> future = new CompletableFuture<>();
        String InvResult = "", InvRData = "";
        HttpClient client = HttpClient.newHttpClient();
        try {
            String URL = All_API_Urls.EwbByIrnUrl;
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(URL))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(JsonFile))
                    .build();

            client.sendAsync(request, BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenAccept(response -> {
                        try {
                            InvResult = response;
                            ObjectMapper objectMapper = new ObjectMapper();
                            SuccessEwbByIrn value2 = objectMapper.readValue(response, SuccessEwbByIrn.class);
                        OracleDataInsert.UpdateEWBByIRNNO(value2, dt);
                        } catch (Exception ex) {
                            try {
                                ObjectMapper objectMapper = new ObjectMapper();
                                ErrorEwbByIrn value2 = objectMapper.readValue(response, ErrorEwbByIrn.class);
                                String sqlstr = "update einvoice_generate_temp set ERRORMSG='" + InvResult + "' where id='" + dt.getRows().get(0).get("ID").toString() + "' and DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
                                int i = DataLayer.ExecuteNonQuery(OraDBConnection.OrclConnection(), sqlstr);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        future.complete(InvResult);
                    })
                    .exceptionally(ex -> {
                        String sqlstr = "update einvoice_generate_temp set ERRORMSG='" + InvResult + "' where id='" + dt.getRows().get(0).get("ID").toString() + "' and DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
                        int i = DataLayer.ExecuteNonQuery(OraDBConnection.OrclConnection(), sqlstr);
                        future.completeExceptionally(ex);
                        return null;
                    });
        } catch (Exception ex) {
            String sqlstr = "update einvoice_generate_temp set ERRORMSG='" + InvResult + "' where id='" + dt.getRows().get(0).get("ID").toString() + "' and DOC_NO='" + dt.getRows().get(0).get("DOC_NO").toString() + "'";
            int i = DataLayer.ExecuteNonQuery(OraDBConnection.OrclConnection(), sqlstr);
            future.completeExceptionally(ex);
        }
        return future;
    }
//}

class e_waybillIrnBO {
    public String access_token;
    public String user_gstin;
    public String irn;
    public String transporter_id;
    public String transportation_mode;
    public String transporter_document_number;
    public String transporter_document_date;
    public String vehicle_number;
    public int distance;
    public String vehicle_type;
    public String transporter_name;
    public String data_source;
}

class SuccessMessage {
    public long EwbNo;

        void setEwbNo(long EwbNo) {
            this.EwbNo = EwbNo;
        }

        long getEwbNo() {
            return EwbNo;
        }

        void setEwbDt(String EwbDt) {
            this.EwbDt = EwbDt;
        }

        String getEwbDt() {
            return EwbDt;
        }

        void setEwbValidTill(String EwbValidTill) {
            this.EwbValidTill = EwbValidTill;
        }

        String getEwbValidTill() {
            return EwbValidTill;
        }
        public String EwbDt;
    public String EwbValidTill;
}

class SuccessResults {
    public SuccessMessage message;
    public String errorMessage;
    public String InfoDtls;
    public String status;

        void setMessage(EwaybillByIrn.SuccessMessage message) {
            this.message = message;
        }

        EwaybillByIrn.SuccessMessage getMessage() {
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
        public int code;
}

class SuccessEwbByIrn {
    public SuccessResults results;

    void setResults(SuccessResults results) {
        this.results = results;
    }

    SuccessResults getResults() {
        return results;
    }
}

class ErrorResults {
    public String message;
    public String errorMessage;
    public String InfoDtls;
    public String status;
    public int code;
}

class ErrorEwbByIrn {
    public ErrorResults results;
}

}
