package bspl.einvoice.eiew.EWBClasses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Taxilla_CanEwaybillClasses {
   
   
    public class Taxilla_CanEwaybillClasses {
        
        public static String generateCancelJson(List<Map<String, Object>> dt, String accessToken) throws JsonProcessingException {
            String msg = "";
            List<CanEwayObject> ewaobj = new ArrayList<>();
            CanEwayObject rootDtls = new CanEwayObject();
            rootDtls.setEwbNo(Long.parseLong(dt.get(0).get("EWBNO").toString()));
            rootDtls.setCancelRsnCode(1);
            rootDtls.setCancelRmrk("Others");
            ewaobj.add(rootDtls);
            
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(ewaobj);
            String mystr = json.substring(1, json.length() - 2);
            return msg = mystr;
        }
    }

    class CanEwayObject {
        private long ewbNo;
        private int cancelRsnCode;
        private String cancelRmrk;

        public long getEwbNo() {
            return ewbNo;
        }

        public void setEwbNo(long ewbNo) {
            this.ewbNo = ewbNo;
        }

        public int getCancelRsnCode() {
            return cancelRsnCode;
        }

        public void setCancelRsnCode(int cancelRsnCode) {
            this.cancelRsnCode = cancelRsnCode;
        }

        public String getCancelRmrk() {
            return cancelRmrk;
        }

        public void setCancelRmrk(String cancelRmrk) {
            this.cancelRmrk = cancelRmrk;
        }
    }

    class CanEwaySuccessResult {
        private String cancelDate;
        private String ewayBillNo;

        public String getCancelDate() {
            return cancelDate;
        }

        public void setCancelDate(String cancelDate) {
            this.cancelDate = cancelDate;
        }

        public String getEwayBillNo() {
            return ewayBillNo;
        }

        public void setEwayBillNo(String ewayBillNo) {
            this.ewayBillNo = ewayBillNo;
        }
    }

    class CanEwaybillSuccessRoot {
        private boolean success;
        private CanEwaySuccessResult result;
        private String message;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public CanEwaySuccessResult getResult() {
            return result;
        }

        public void setResult(CanEwaySuccessResult result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
