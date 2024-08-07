package bspl.einvoice.eiew.Models;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.google.gson.Gson;


public class Taxilla_EwaybillByIrnClasses {

    public static String Generate_EwaybillIrn_Json(ResultSet rs, String sessionId, String access_token) {
        String msg = "";
        try {
            String calcdist = "0";

            List<EwayBillByIrnRoot> ewaybillIrn_obj = new ArrayList<>();

            if (rs.getString("TRAN_CATG").startsWith("EXP")) {
                EwayBillByIrnRoot rootDtls = new EwayBillByIrnRoot();
                rootDtls.setIrn(rs.getString("IRN").isEmpty() ? sessionId : rs.getString("IRN"));
                rootDtls.setTransId(rs.getString("EWAY_TRANSPORTAR_ID").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_ID"));
                rootDtls.setTransMode(rs.getString("EWAY_TRANSPORTAR_MODE").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_MODE"));
                rootDtls.setTransDocNo(rs.getString("EWAY_TRANSPORTAR_DOCNO").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_DOCNO"));
                rootDtls.setTransDocDt(rs.getString("EWAY_TRANSPORTAR_DOCDT").isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").format(new Date(rs.getTimestamp("EWAY_TRANSPORTAR_DOCDT").getTime())).replace("-", "/"));
                rootDtls.setVehNo(rs.getString("EWAY_TRANSPORTAR_VEHINO").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_VEHINO"));
                rootDtls.setDistance(Integer.parseInt((rs.getString("EWAY_TRANSPORTAR_DISTANCE").isEmpty() || rs.getString("EWAY_TRANSPORTAR_DISTANCE").equals("0")) ? calcdist : rs.getString("EWAY_TRANSPORTAR_DISTANCE")));
                rootDtls.setVehType(rs.getString("EWAY_TRANSPORTAR_VEHITYPE"));
                rootDtls.setTransName(rs.getString("EWAY_TRANSPORTAR_NAME").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_NAME"));
                rootDtls.setExpShipDtls(Expship_details(rs));
                // rootDtls.setDispDtls(getdispatch_details(rs));

                ewaybillIrn_obj.add(rootDtls);
            } else if (rs.getString("BILLTO_GSTIN").equals(rs.getString("SHIPTO_GSTIN"))) {
                if (rs.getString("EWAY_TRANSPORTAR_DISTANCE").isEmpty() || rs.getString("EWAY_TRANSPORTAR_DISTANCE").equals("0")) {
                    if (rs.getString("BILLFROM_PIN").equals(rs.getString("BILLTO_PIN"))) {
                        calcdist = "20";
                    } else {
                        String Rdistval = DistanceResponseClass.GoogleDistAPI(Integer.parseInt(Double.parseDouble(rs.getString("BILLFROM_PIN"))), Integer.parseInt(Double.parseDouble(rs.getString("BILLTO_PIN"))));
                        calcdist = Rdistval;
                        String commandText = "UPDATE einvoice_generate_temp SET EWAY_TRANSPORTAR_DISTANCE='" + calcdist + "' WHERE DOC_NO ='" + rs.getString("DOC_No") + "'";
                        int i = DataLayer.ExecuteNonQuery(OraDBConnection.OrclConnection, commandText);
                    }
                }

                EwayBillByIrnRoot rootDtls = new EwayBillByIrnRoot();
                rootDtls.setIrn(rs.getString("IRN").isEmpty() ? sessionId : rs.getString("IRN"));
                rootDtls.setTransId(rs.getString("EWAY_TRANSPORTAR_ID").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_ID"));
                rootDtls.setTransMode(rs.getString("EWAY_TRANSPORTAR_MODE").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_MODE"));
                rootDtls.setTransDocNo(rs.getString("EWAY_TRANSPORTAR_DOCNO").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_DOCNO"));
                rootDtls.setTransDocDt(rs.getString("EWAY_TRANSPORTAR_DOCDT").isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").format(new Date(rs.getTimestamp("EWAY_TRANSPORTAR_DOCDT").getTime())).replace("-", "/"));
                rootDtls.setVehNo(rs.getString("EWAY_TRANSPORTAR_VEHINO").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_VEHINO"));
                rootDtls.setDistance(Integer.parseInt((rs.getString("EWAY_TRANSPORTAR_DISTANCE").isEmpty() || rs.getString("EWAY_TRANSPORTAR_DISTANCE").equals("0")) ? calcdist : rs.getString("EWAY_TRANSPORTAR_DISTANCE")));
                rootDtls.setVehType(rs.getString("EWAY_TRANSPORTAR_VEHITYPE"));
                rootDtls.setTransName(rs.getString("EWAY_TRANSPORTAR_NAME").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_NAME"));
                // rootDtls.setExpShipDtls(Expship_details(rs));
                // rootDtls.setDispDtls(getdispatch_details(rs));

                ewaybillIrn_obj.add(rootDtls);
            } else if (rs.getString("BILLFROM_GSTIN").equals(rs.getString("SHIPFROM_GSTIN"))) {
                if (rs.getString("EWAY_TRANSPORTAR_DISTANCE").isEmpty() || rs.getString("EWAY_TRANSPORTAR_DISTANCE").equals("0")) {
                    if (rs.getString("BILLFROM_PIN").equals(rs.getString("BILLTO_PIN"))) {
                        calcdist = "20";
                    } else {
                        String Rdistval = DistanceResponseClass.GoogleDistAPI(Integer.parseInt(Double.parseDouble(rs.getString("BILLFROM_PIN"))), Integer.parseInt(Double.parseDouble(rs.getString("BILLTO_PIN"))));
                        calcdist = Rdistval;
                        String commandText = "UPDATE einvoice_generate_temp SET EWAY_TRANSPORTAR_DISTANCE='" + calcdist + "' WHERE DOC_NO ='" + rs.getString("DOC_No") + "'";
                        int i = DataLayer.ExecuteNonQuery(OraDBConnection.OrclConnection, commandText);
                    }
                }

                EwayBillByIrnRoot rootDtls = new EwayBillByIrnRoot();
                rootDtls.setIrn(rs.getString("IRN").isEmpty() ? sessionId : rs.getString("IRN"));
                rootDtls.setTransId(rs.getString("EWAY_TRANSPORTAR_ID").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_ID"));
                rootDtls.setTransMode(rs.getString("EWAY_TRANSPORTAR_MODE").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_MODE"));
                rootDtls.setTransDocNo(rs.getString("EWAY_TRANSPORTAR_DOCNO").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_DOCNO"));
                rootDtls.setTransDocDt(rs.getString("EWAY_TRANSPORTAR_DOCDT").isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").format(new Date(rs.getTimestamp("EWAY_TRANSPORTAR_DOCDT").getTime())).replace("-", "/"));
                rootDtls.setVehNo(rs.getString("EWAY_TRANSPORTAR_VEHINO").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_VEHINO"));
                rootDtls.setDistance(Integer.parseInt((rs.getString("EWAY_TRANSPORTAR_DISTANCE").isEmpty() || rs.getString("EWAY_TRANSPORTAR_DISTANCE").equals("0")) ? calcdist : rs.getString("EWAY_TRANSPORTAR_DISTANCE")));
                rootDtls.setVehType(rs.getString("EWAY_TRANSPORTAR_VEHITYPE"));
                rootDtls.setTransName(rs.getString("EWAY_TRANSPORTAR_NAME").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_NAME"));
                // rootDtls.setExpShipDtls(Expship_details(rs));
                // rootDtls.setDispDtls(getdispatch_details(rs));

                ewaybillIrn_obj.add(rootDtls);
            } else {
                if (rs.getString("EWAY_TRANSPORTAR_DISTANCE").isEmpty() || rs.getString("EWAY_TRANSPORTAR_DISTANCE").equals("0")) {
                    if (rs.getString("BILLFROM_PIN").equals(rs.getString("SHIPTO_PIN"))) {
                        calcdist = "20";
                    } else {
                        String Rdistval = DistanceResponseClass.GoogleDistAPI(Integer.parseInt(Double.parseDouble(rs.getString("BILLFROM_PIN"))), Integer.parseInt(Double.parseDouble(rs.getString("SHIPTO_PIN"))));
                        calcdist = Rdistval;
                        String commandText = "UPDATE einvoice_generate_temp SET EWAY_TRANSPORTAR_DISTANCE='" + calcdist + "' WHERE DOC_NO ='" + rs.getString("DOC_No") + "'";
                        int i = DataLayer.ExecuteNonQuery(OraDBConnection.OrclConnection, commandText);
                    }
                }

                EwayBillByIrnRoot rootDtls = new EwayBillByIrnRoot();
                rootDtls.setIrn(rs.getString("IRN").isEmpty() ? sessionId : rs.getString("IRN"));
                rootDtls.setTransId(rs.getString("EWAY_TRANSPORTAR_ID").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_ID"));
                rootDtls.setTransMode(rs.getString("EWAY_TRANSPORTAR_MODE").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_MODE"));
                rootDtls.setTransDocNo(rs.getString("EWAY_TRANSPORTAR_DOCNO").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_DOCNO"));
                rootDtls.setTransDocDt(rs.getString("EWAY_TRANSPORTAR_DOCDT").isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").format(new Date(rs.getTimestamp("EWAY_TRANSPORTAR_DOCDT").getTime())).replace("-", "/"));
                rootDtls.setVehNo(rs.getString("EWAY_TRANSPORTAR_VEHINO").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_VEHINO"));
                rootDtls.setDistance(Integer.parseInt((rs.getString("EWAY_TRANSPORTAR_DISTANCE").isEmpty() || rs.getString("EWAY_TRANSPORTAR_DISTANCE").equals("0")) ? calcdist : rs.getString("EWAY_TRANSPORTAR_DISTANCE")));
                rootDtls.setVehType(rs.getString("EWAY_TRANSPORTAR_VEHITYPE"));
                rootDtls.setTransName(rs.getString("EWAY_TRANSPORTAR_NAME").isEmpty() ? null : rs.getString("EWAY_TRANSPORTAR_NAME"));
                rootDtls.setExpShipDtls(Expship_details(rs));
                rootDtls.setDispDtls(getdispatch_details(rs));

                ewaybillIrn_obj.add(rootDtls);
            }

            String json = new Gson().toJson(ewaybillIrn_obj);
            String mystr = json.substring(1, json.length() - 2);
            return msg = mystr;
        } catch (Exception ex) {
            ClsDynamic.WriteLog(ex.getMessage());
            msg = "";
            return msg;
        }
    }

    public static DispDtls getdispatch_details(ResultSet rs) {
        DispDtls dispatch_details = new DispDtls();
        dispatch_details.setNm(rs.getString("SHIPFROM_TRDNM").isEmpty() ? "" : rs.getString("SHIPFROM_TRDNM"));
        dispatch_details.setAddr1(rs.getString("SHIPFROM_BNO") + " " + rs.getString("SHIPFROM_BNM") + " " + rs.getString("SHIPFROM_FLNO") + " " + rs.getString("SHIPFROM_DST"));
        dispatch_details.setAddr2(null);
        dispatch_details.setLoc(rs.getString("SHIPFROM_LOC").isEmpty() ? "" : rs.getString("SHIPFROM_LOC"));
        dispatch_details.setPin(Integer.parseInt(Double.parseDouble(rs.getString("SHIPFROM_PIN").isEmpty() ? "201301" : rs.getString("SHIPFROM_PIN"))));
        dispatch_details.setStcd(rs.getString("SHIPFROM_STCD").isEmpty() ? "" : rs.getString("SHIPFROM_STCD"));

        return dispatch_details;
    }

    public static ExpShipDtls Expship_details(ResultSet rs) {
        ExpShipDtls ship_details = new ExpShipDtls();
        ship_details.setAddr1(rs.getString("SHIPTO_BNO") + " " + rs.getString("SHIPTO_BNM") + " " + rs.getString("SHIPTO_FLNO") + " " + rs.getString("SHIPTO_DST"));
        ship_details.setAddr2(null);
        ship_details.setLoc(rs.getString("SHIPTO_LOC"));
        ship_details.setPin(Integer.parseInt(Double.parseDouble(rs.getString("SHIPTO_PIN").isEmpty() ? "201301" : rs.getString("SHIPTO_PIN"))));
        ship_details.setStcd(rs.getString("SHIPTO_STCD"));

        return ship_details;
    }

    public static String Cancel_Ewaybill_Json(ResultSet rs, String sessionId, String access_token) {
        String msg = "";
        List<CancelEwaybillIRNRoot> ewaybillIrn_obj = new ArrayList<>();
        CancelEwaybillIRNRoot rootDtls = new CancelEwaybillIRNRoot();
        rootDtls.setEwbNo(Long.parseLong(rs.getString("EWBNO").isEmpty() ? "111000609282" : rs.getString("EWBNO")));
        rootDtls.setCancelRsnCode(2);
        rootDtls.setCancelRmrk("Cancelled the order");
        ewaybillIrn_obj.add(rootDtls);
        String json = new Gson().toJson(ewaybillIrn_obj);
        String mystr = json.substring(1, json.length() - 2);
        return msg = mystr;
    }

    public static String ExtractQR_Code_Json(ResultSet rs, String sessionId, String access_token) {
        String msg = "";
        List<ExtractQRRoot> ewaybillIrn_obj = new ArrayList<>();
        ExtractQRRoot rootDtls = new ExtractQRRoot();
        rootDtls.setData(rs.getString("SIGNEDQRCODE"));
        ewaybillIrn_obj.add(rootDtls);
        String json = new Gson().toJson(ewaybillIrn_obj);
        String mystr = json.substring(1, json.length() - 2);
        return msg = mystr;
    }

    public static String Extract_SignedInvoice_Json(ResultSet rs, String sessionId, String access_token) {
        String msg = "";
        List<ExtractQRRoot> ewaybillIrn_obj = new ArrayList<>();
        ExtractQRRoot rootDtls = new ExtractQRRoot();
        rootDtls.setData(rs.getString("SIGNEDINVOICE"));
        ewaybillIrn_obj.add(rootDtls);
        String json = new Gson().toJson(ewaybillIrn_obj);
        String mystr = json.substring(1, json.length() - 2);
        return msg = mystr;
    }
}

class EwayBillByIrnRoot {
    private String Irn;
    private int Distance;
    private String TransMode;
    private String TransId;
    private String TransName;
    private String TransDocDt;
    private String TransDocNo;
    private String VehNo;
    private String VehType;
    private ExpShipDtls ExpShipDtls;
    private DispDtls DispDtls;

    // Getters and Setters
    public String getIrn() {
        return Irn;
    }

    public void setIrn(String irn) {
        Irn = irn;
    }

    public int getDistance() {
        return Distance;
    }

    public void setDistance(int distance) {
        Distance = distance;
    }

    public String getTransMode() {
        return TransMode;
    }

    public void setTransMode(String transMode) {
        TransMode = transMode;
    }

    public String getTransId() {
        return TransId;
    }

    public void setTransId(String transId) {
        TransId = transId;
    }

    public String getTransName() {
        return TransName;
    }

    public void setTransName(String transName) {
        TransName = transName;
    }

    public String getTransDocDt() {
        return TransDocDt;
    }

    public void setTransDocDt(String transDocDt) {
        TransDocDt = transDocDt;
    }

    public String getTransDocNo() {
        return TransDocNo;
    }

    public void setTransDocNo(String transDocNo) {
        TransDocNo = transDocNo;
    }

    public String getVehNo() {
        return VehNo;
    }

    public void setVehNo(String vehNo) {
        VehNo = vehNo;
    }

    public String getVehType() {
        return VehType;
    }

    public void setVehType(String vehType) {
        VehType = vehType;
    }

    public ExpShipDtls getExpShipDtls() {
        return ExpShipDtls;
    }

    public void setExpShipDtls(ExpShipDtls expShipDtls) {
        ExpShipDtls = expShipDtls;
    }

    public DispDtls getDispDtls() {
        return DispDtls;
    }

    public void setDispDtls(DispDtls dispDtls) {
        DispDtls = dispDtls;
    }
}

class ExpShipDtls {
    private String Addr1;
    private String Addr2;
    private String Loc;
    private int Pin;
    private String Stcd;

    // Getters and Setters
    public String getAddr1() {
        return Addr1;
    }

    public void setAddr1(String addr1) {
        Addr1 = addr1;
    }

    public String getAddr2() {
        return Addr2;
    }

    public void setAddr2(String addr2) {
        Addr2 = addr2;
    }

    public String getLoc() {
        return Loc;
    }

    public void setLoc(String loc) {
        Loc = loc;
    }

    public int getPin() {
        return Pin;
    }

    public void setPin(int pin) {
        Pin = pin;
    }

    public String getStcd() {
        return Stcd;
    }

    public void setStcd(String stcd) {
        Stcd = stcd;
    }
}

class DispDtls {
    private String Nm;
    private String Addr1;
    private String Addr2;
    private String Loc;
    private int Pin;
    private String Stcd;

    // Getters and Setters
    public String getNm() {
        return Nm;
    }

    public void setNm(String nm) {
        Nm = nm;
    }

    public String getAddr1() {
        return Addr1;
    }

    public void setAddr1(String addr1) {
        Addr1 = addr1;
    }

    public String getAddr2() {
        return Addr2;
    }

    public void setAddr2(String addr2) {
        Addr2 = addr2;
    }

    public String getLoc() {
        return Loc;
    }

    public void setLoc(String loc) {
        Loc = loc;
    }

    public int getPin() {
        return Pin;
    }

    public void setPin(int pin) {
        Pin = pin;
    }

    public String getStcd() {
        return Stcd;
    }

    public void setStcd(String stcd) {
        Stcd = stcd;
    }
}

class CancelEwaybillIRNRoot {
    private long ewbNo;
    private int cancelRsnCode;
    private String cancelRmrk;

    // Getters and Setters
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

class ExtractQRRoot {
    private String data;

    // Getters and Setters
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    
    public class SuccessResult_EwbByIrn {
        private long ewayBillNo;
        private String ewayBillDate;
        private String validUpto;

        public long getEwayBillNo() {
            return ewayBillNo;
        }

        public void setEwayBillNo(long ewayBillNo) {
            this.ewayBillNo = ewayBillNo;
        }

        public String getEwayBillDate() {
            return ewayBillDate;
        }

        public void setEwayBillDate(String ewayBillDate) {
            this.ewayBillDate = ewayBillDate;
        }

        public String getValidUpto() {
            return validUpto;
        }

        public void setValidUpto(String validUpto) {
            this.validUpto = validUpto;
        }
    }

    public class SuccessRoot_EwbByIrn {
        private boolean success;
        private String message;
        private SuccessResult_EwbByIrn result;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public SuccessResult_EwbByIrn getResult() {
            return result;
        }

        public void setResult(SuccessResult_EwbByIrn result) {
            this.result = result;
        }
    }

    public class SuccessResult_CEWBIRN {
        private long ewayBillNo;
        private String cancelDate;

        public long getEwayBillNo() {
            return ewayBillNo;
        }

        public void setEwayBillNo(long ewayBillNo) {
            this.ewayBillNo = ewayBillNo;
        }

        public String getCancelDate() {
            return cancelDate;
        }

        public void setCancelDate(String cancelDate) {
            this.cancelDate = cancelDate;
        }
    }

    public class SuccessRoot_CEWBIRN {
        private boolean success;
        private String message;
        private SuccessResult_CEWBIRN result;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public SuccessResult_CEWBIRN getResult() {
            return result;
        }

        public void setResult(SuccessResult_CEWBIRN result) {
            this.result = result;
        }
    }

    public class SuccessResult_EXTQR {
        private String irn;
        private String sellerGstin;
        private String docTyp;
        private double totInvVal;
        private String buyerGstin;
        private String docDt;
        private String docNo;
        private String mainHsnCode;
        private int itemCnt;

        public String getIrn() {
            return irn;
        }

        public void setIrn(String irn) {
            this.irn = irn;
        }

        public String getSellerGstin() {
            return sellerGstin;
        }

        public void setSellerGstin(String sellerGstin) {
            this.sellerGstin = sellerGstin;
        }

        public String getDocTyp() {
            return docTyp;
        }

        public void setDocTyp(String docTyp) {
            this.docTyp = docTyp;
        }

        public double getTotInvVal() {
            return totInvVal;
        }

        public void setTotInvVal(double totInvVal) {
            this.totInvVal = totInvVal;
        }

        public String getBuyerGstin() {
            return buyerGstin;
        }

        public void setBuyerGstin(String buyerGstin) {
            this.buyerGstin = buyerGstin;
        }

        public String getDocDt() {
            return docDt;
        }

        public void setDocDt(String docDt) {
            this.docDt = docDt;
        }

        public String getDocNo() {
            return docNo;
        }

        public void setDocNo(String docNo) {
            this.docNo = docNo;
        }

        public String getMainHsnCode() {
            return mainHsnCode;
        }

        public void setMainHsnCode(String mainHsnCode) {
            this.mainHsnCode = mainHsnCode;
        }

        public int getItemCnt() {
            return itemCnt;
        }

        public void setItemCnt(int itemCnt) {
            this.itemCnt = itemCnt;
        }
    }

    public class SuccessRoot_EXTQR {
        private boolean success;
        private String message;
        private SuccessResult_EXTQR result;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public SuccessResult_EXTQR getResult() {
            return result;
        }

        public void setResult(SuccessResult_EXTQR result) {
            this.result = result;
        }
    }

    public class IrnEwaybill_successResult {
        private String ewbNo;
        private String ewbDt;
        private String ewbValidTill;

        public String getEwbNo() {
            return ewbNo;
        }

        public void setEwbNo(String ewbNo) {
            this.ewbNo = ewbNo;
        }

        public String getEwbDt() {
            return ewbDt;
        }

        public void setEwbDt(String ewbDt) {
            this.ewbDt = ewbDt;
        }

        public String getEwbValidTill() {
            return ewbValidTill;
        }

        public void setEwbValidTill(String ewbValidTill) {
            this.ewbValidTill = ewbValidTill;
        }
    }

    public class IrnEwaybill_successRoot {
        private boolean success;
        private String message;
        private IrnEwaybill_successResult result;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public IrnEwaybill_successResult getResult() {
            return result;
        }

        public void setResult(IrnEwaybill_successResult result) {
            this.result = result;
        }
    }

    public class GetEwaybillResult {
        private long ewbNo;
        private String status;
        private String genGstin;
        private String ewbDt;
        private String ewbValidTill;

        public long getEwbNo() {
            return ewbNo;
        }

        public void setEwbNo(long ewbNo) {
            this.ewbNo = ewbNo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getGenGstin() {
            return genGstin;
        }

        public void setGenGstin(String genGstin) {
            this.genGstin = genGstin;
        }

        public String getEwbDt() {
            return ewbDt;
        }

        public void setEwbDt(String ewbDt) {
            this.ewbDt = ewbDt;
        }

        public String getEwbValidTill() {
            return ewbValidTill;
        }

        public void setEwbValidTill(String ewbValidTill) {
            this.ewbValidTill = ewbValidTill;
        }
    }

    public class GetEwaybillRoot {
        private boolean success;
        private String message;
        private GetEwaybillResult result;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public GetEwaybillResult getResult() {
            return result;
        }

        public void setResult(GetEwaybillResult result) {
            this.result = result;
        }
    }



}
