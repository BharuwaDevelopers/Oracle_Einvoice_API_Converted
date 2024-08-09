package bspl.einvoice.eiew.Models;

import java.sql.ResultSet;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class Taxilla_CancelIrnClasses {
    
  
    public static String CancelJsonFile(ResultSet dt, String irn, String token, String CanCode) {
        String JsTest = "";
        String msg = "", CanRemarks = "";
        if (CanCode.equals("1"))
            CanRemarks = "Duplicate Entry";
        else if (CanCode.equals("2"))
            CanRemarks = "Data entry mistake";
        else if (CanCode.equals("3"))
            CanRemarks = "Order Cancelled";
        else if (CanCode.equals("4"))
            CanRemarks = "Others";

        CancelRootObject Calceljson = new CancelRootObject();
        Calceljson.irn = dt.getValueAt(0, "IRN").toString();
        Calceljson.cnlrsn = CanCode;
        Calceljson.cnlrem = CanRemarks;

        JSONObject objAddjson = new JSONObject(Calceljson);
        return JsTest = objAddjson.toString();
    }
}

class CancelRootObject {
    public String irn;
    public String cnlrsn;
    public String cnlrem;
}

class SuccessResult {
    public String Irn;
    public String CancelDate;
}

class EinvCanSuccessRoot {
    public boolean success;
    public String message;
    public SuccessResult result;
}
