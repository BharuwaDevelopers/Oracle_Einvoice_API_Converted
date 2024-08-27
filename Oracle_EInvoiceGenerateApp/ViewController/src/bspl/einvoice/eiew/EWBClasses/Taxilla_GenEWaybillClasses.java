package bspl.einvoice.eiew.EWBClasses;

import com.google.gson.Gson;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Taxilla_GenEWaybillClasses {

    public String generateJsonFile(ResultSet dt, String access_token) throws SQLException {
        String msg = "";
        List<GenEWaybject> ewaobj = new ArrayList<>();
        GenEWaybject rootDtls = new GenEWaybject();

        rootDtls.supplyType = dt.getString("SUPPLYTYPE").isEmpty() ? "O" : dt.getString("SUPPLYTYPE");
        rootDtls.subSupplyType = dt.getString("SUBSUPPLYTYPE").isEmpty() ? "" : dt.getString("SUBSUPPLYTYPE");
        rootDtls.subSupplyDesc = dt.getString("SUBSUPPLYDESC").isEmpty() ? "" : dt.getString("SUBSUPPLYDESC");
        rootDtls.docType = dt.getString("DOCTYPE").isEmpty() ? "" : dt.getString("DOCTYPE");
        rootDtls.docNo = dt.getString("DOCNO").isEmpty() ? "" : dt.getString("DOCNO");
        rootDtls.docDate = dt.getString("DOCDATE").isEmpty() ? "" : dt.getString("DOCDATE");
        rootDtls.fromGstin = dt.getString("FROMGSTIN").isEmpty() ? "" : dt.getString("FROMGSTIN");
        rootDtls.fromTrdName = dt.getString("FROMTRDNAME").isEmpty() ? "" : dt.getString("FROMTRDNAME");
        rootDtls.fromAddr1 = dt.getString("FROMADDR1").isEmpty() ? "" : dt.getString("FROMADDR1");
        rootDtls.fromAddr2 = dt.getString("FROMADDR2").isEmpty() ? "" : dt.getString("FROMADDR2");
        rootDtls.fromPlace = dt.getString("FROMPLACE").isEmpty() ? "" : dt.getString("FROMPLACE");
        rootDtls.fromPincode = dt.getString("FROMPINCODE").isEmpty() ? 0 : Integer.parseInt(dt.getString("FROMPINCODE"));
        rootDtls.fromStateCode = dt.getString("FROMSTATECODE").isEmpty() ? 0 : Integer.parseInt(dt.getString("FROMSTATECODE"));
        rootDtls.actFromStateCode = dt.getString("ACTFROMSTATECODE").isEmpty() ? 0 : Integer.parseInt(dt.getString("ACTFROMSTATECODE"));
        rootDtls.toGstin = dt.getString("TOGSTIN").isEmpty() ? "" : dt.getString("TOGSTIN");
        rootDtls.toTrdname = dt.getString("TOTRDNAME").isEmpty() ? "" : dt.getString("TOTRDNAME");
        rootDtls.toAddr1 = dt.getString("TOADDR1").isEmpty() ? "" : dt.getString("TOADDR1");
        rootDtls.toAddr2 = dt.getString("TOADDR2").isEmpty() ? "" : dt.getString("TOADDR2");
        rootDtls.toPlace = dt.getString("TOPLACE").isEmpty() ? "" : dt.getString("TOPLACE");
        rootDtls.toPincode = dt.getString("TOPINCODE").isEmpty() ? 0 : Integer.parseInt(dt.getString("TOPINCODE"));
        rootDtls.toStateCode = dt.getString("TOSTATECODE").isEmpty() ? 0 : Integer.parseInt(dt.getString("TOSTATECODE"));
        rootDtls.actToStateCode = dt.getString("ACTTOSTATECODE").isEmpty() ? 0 : Integer.parseInt(dt.getString("ACTTOSTATECODE"));
        rootDtls.totalValue = dt.getString("TOTALVALUE").isEmpty() ? 0 : Integer.parseInt(dt.getString("TOTALVALUE"));
        rootDtls.cgstValue = dt.getString("CGSTVALUE").isEmpty() ? 0 : Double.parseDouble(dt.getString("CGSTVALUE"));
        rootDtls.sgstValue = dt.getString("SGSTVALUE").isEmpty() ? 0 : Double.parseDouble(dt.getString("SGSTVALUE"));
        rootDtls.igstValue = dt.getString("IGSTVALUE").isEmpty() ? 0 : Double.parseDouble(dt.getString("IGSTVALUE"));
        rootDtls.cessValue = dt.getString("CESSVALUE").isEmpty() ? 0 : Double.parseDouble(dt.getString("CESSVALUE"));
        rootDtls.transMode = dt.getString("TRANSMODE").isEmpty() ? "" : dt.getString("TRANSMODE");
        rootDtls.transDistance = dt.getString("TRANSDISTANCE").isEmpty() ? "" : dt.getString("TRANSDISTANCE");
        rootDtls.transporterName = dt.getString("TRANSPORTERNAME").isEmpty() ? "" : dt.getString("TRANSPORTERNAME");
        rootDtls.transporterId = dt.getString("TRANSPORTERID").isEmpty() ? "" : dt.getString("TRANSPORTERID");
        rootDtls.transDocNo = dt.getString("TRANSDOCNO").isEmpty() ? "" : dt.getString("TRANSDOCNO");
        rootDtls.transDocDate = dt.getString("TRANSDOCDATE").isEmpty() ? "" : dt.getString("TRANSDOCDATE");
        rootDtls.vehicleNo = dt.getString("VEHICLENO").isEmpty() ? "" : dt.getString("VEHICLENO");
        rootDtls.vehicleType = dt.getString("VEHICLETYPE").isEmpty() ? "" : dt.getString("VEHICLETYPE");
        rootDtls.totInvValue = dt.getString("TOTINVVALUE").isEmpty() ? 0 : Integer.parseInt(dt.getString("TOTINVVALUE"));
        rootDtls.transactionType = dt.getString("TRANSACTIONTYPE").isEmpty() ? 1 : Integer.parseInt(dt.getString("TRANSACTIONTYPE"));
        rootDtls.otherValue = dt.getString("OTHERVALUE").isEmpty() ? 0 : Integer.parseInt(dt.getString("OTHERVALUE"));
        rootDtls.cessNonAdvolValue = dt.getString("CESSNONADVOLVALUE").isEmpty() ? 0 : Integer.parseInt(dt.getString("CESSNONADVOLVALUE"));
        rootDtls.itemList = getItemList(dt);

        ewaobj.add(rootDtls);
        String json = new Gson().toJson(ewaobj);
        String mystr = json.substring(1, json.length() - 1);
        return msg = mystr;
    }

    public  String eWBJsonFile(ResultSet dt, String access_token) throws SQLException {
        String msg = "";
        try {
            List<GenEWaybject> ewaobj = new ArrayList<>();
            GenEWaybject rootDtls = new GenEWaybject();

            rootDtls.supplyType = "O";
            rootDtls.subSupplyType = dt.getString("SUBSUPPLYTYPE").isEmpty() ? "5" : dt.getString("SUBSUPPLYTYPE");
            rootDtls.subSupplyDesc = dt.getString("SUBSUPPLYDESC").isEmpty() ? "Others" : dt.getString("SUBSUPPLYDESC");
            rootDtls.docType = dt.getString("DOC_TYP").isEmpty() ? "CHL" : dt.getString("DOC_TYP");
            rootDtls.docNo = dt.getString("DOC_NO").isEmpty() ? "" : dt.getString("DOC_NO");
            rootDtls.docDate = dt.getDate("DOC_DT").toString().replace("-", "/");
            rootDtls.fromGstin = dt.getString("BILLFROM_GSTIN").isEmpty() ? "" : dt.getString("BILLFROM_GSTIN");
            rootDtls.fromTrdName = dt.getString("BILLFROM_TRDNM").isEmpty() ? "" : dt.getString("BILLFROM_TRDNM");
            rootDtls.fromAddr1 = dt.getString("BILLFROM_BNO").isEmpty() ? "" : dt.getString("BILLFROM_BNO");
            rootDtls.fromAddr2 = dt.getString("BILLFROM_BNO").isEmpty() ? "" : dt.getString("BILLFROM_BNO");
            rootDtls.fromPlace = dt.getString("BILLFROM_LOC").isEmpty() ? "" : dt.getString("BILLFROM_LOC");
            rootDtls.fromPincode = dt.getString("BILLFROM_PIN").isEmpty() ? 0 : Integer.parseInt(dt.getString("BILLFROM_PIN"));
            rootDtls.fromStateCode = dt.getString("BILLFROM_STCD").isEmpty() ? 0 : Integer.parseInt(dt.getString("BILLFROM_STCD"));
            rootDtls.actFromStateCode = dt.getString("BILLFROM_STCD").isEmpty() ? 0 : Integer.parseInt(dt.getString("BILLFROM_STCD"));
            rootDtls.toGstin = dt.getString("BILLTO_GSTIN").isEmpty() ? "" : dt.getString("BILLTO_GSTIN");
            rootDtls.toTrdname = dt.getString("BILLTO_TRDNM").isEmpty() ? "" : dt.getString("BILLTO_TRDNM");
            rootDtls.toAddr1 = dt.getString("BILLTO_BNO").isEmpty() ? "" : dt.getString("BILLTO_BNO");
            rootDtls.toAddr2 = dt.getString("BILLTO_BNO").isEmpty() ? "" : dt.getString("BILLTO_BNO");
            rootDtls.toPlace = dt.getString("BILLTO_LOC").isEmpty() ? "" : dt.getString("BILLTO_LOC");
            rootDtls.toPincode = dt.getString("BILLTO_PIN").isEmpty() ? 0 : Integer.parseInt(dt.getString("BILLTO_PIN"));
            rootDtls.toStateCode = dt.getString("BILLTO_STCD").isEmpty() ? 0 : Integer.parseInt(dt.getString("BILLTO_STCD"));
            rootDtls.actToStateCode = dt.getString("BILLTO_STCD").isEmpty() ? 0 : Integer.parseInt(dt.getString("BILLTO_STCD"));
            rootDtls.totalValue = dt.getString("VAL_ASSVAL").isEmpty() ? 0 : Integer.parseInt(dt.getString("VAL_ASSVAL"));
            rootDtls.cgstValue = dt.getString("VAL_CGSTVAL").isEmpty() ? 0 : Double.parseDouble(dt.getString("VAL_CGSTVAL"));
            rootDtls.sgstValue = dt.getString("VAL_SGSTVAL").isEmpty() ? 0 : Double.parseDouble(dt.getString("VAL_SGSTVAL"));
            rootDtls.igstValue = dt.getString("VAL_IGSTVAL").isEmpty() ? 0 : Double.parseDouble(dt.getString("VAL_IGSTVAL"));
            rootDtls.cessValue = dt.getString("VAL_CESVAL").isEmpty() ? 0 : Double.parseDouble(dt.getString("VAL_CESVAL"));
            rootDtls.otherValue = dt.getString("VAL_OTHCHRG").isEmpty() ? 0 : Integer.parseInt(dt.getString("VAL_OTHCHRG"));
            rootDtls.cessNonAdvolValue = dt.getString("VAL_CESNONADVAL").isEmpty() ? 0 : Integer.parseInt(dt.getString("VAL_CESNONADVAL"));
            rootDtls.transMode = dt.getString("EWAY_TRANSPORTAR_MODE").isEmpty() ? "" : dt.getString("EWAY_TRANSPORTAR_MODE");
            rootDtls.transDistance = dt.getString("EWAY_TRANSPORTAR_DISTANCE").isEmpty() ? "" : dt.getString("EWAY_TRANSPORTAR_DISTANCE");
            rootDtls.transporterName = dt.getString("EWAY_TRANSPORTAR_NAME").isEmpty() ? "" : dt.getString("EWAY_TRANSPORTAR_NAME");
            rootDtls.transporterId = dt.getString("EWAY_TRANSPORTAR_ID").isEmpty() ? "" : dt.getString("EWAY_TRANSPORTAR_ID");
            rootDtls.transDocNo = dt.getString("EWAY_TRANSPORTAR_DOCNO").isEmpty() ? "" : dt.getString("EWAY_TRANSPORTAR_DOCNO");
            rootDtls.transDocDate = dt.getString("EWAY_TRANSPORTAR_DOCDT").isEmpty() ? "" : dt.getString("EWAY_TRANSPORTAR_DOCDT");
            rootDtls.vehicleNo = dt.getString("EWAY_TRANSPORTAR_VEHINO").isEmpty() ? "" : dt.getString("EWAY_TRANSPORTAR_VEHINO");
            rootDtls.vehicleType = dt.getString("EWAY_TRANSPORTAR_VEHITYPE").isEmpty() ? "" : dt.getString("EWAY_TRANSPORTAR_VEHITYPE");
            rootDtls.totInvValue = dt.getString("VAL_TOTINVVAL").isEmpty() ? 0 : Integer.parseInt(dt.getString("VAL_TOTINVVAL"));
            rootDtls.transactionType = dt.getString("EWB_TRANSACTIONTYPE").isEmpty() ? 1 : Integer.parseInt(dt.getString("EWB_TRANSACTIONTYPE"));
            rootDtls.itemList = getItemList(dt);

            ewaobj.add(rootDtls);
            String json = new Gson().toJson(ewaobj);
            String mystr = json.substring(1, json.length() - 1);
            msg = mystr;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return msg;
    }

    public  List<ItemList> getItemList(ResultSet dt) throws SQLException {
        List<ItemList> itm = new ArrayList<>();
        while (dt.next()) {
            ItemList item = new ItemList();
            item.productName = dt.getString("ITEM_PRDNM").isEmpty() ? "" : dt.getString("ITEM_PRDNM");
            item.productDesc = dt.getString("ITEM_PRDDESC").isEmpty() ? "" : dt.getString("ITEM_PRDDESC");
            item.hsnCode = dt.getString("ITEM_HSNCD").isEmpty() ? "" : dt.getString("ITEM_HSNCD");
            item.quantity = dt.getString("ITEM_QTY").isEmpty() ? 0 : Double.parseDouble(dt.getString("ITEM_QTY"));
            item.qtyUnit = dt.getString("ITEM_UNIT").isEmpty() ? "" : dt.getString("ITEM_UNIT");
            item.cgstRate = dt.getString("ITEM_CGSTRT").isEmpty() ? 0 : Double.parseDouble(dt.getString("ITEM_CGSTRT"));
            item.sgstRate = dt.getString("ITEM_SGSTRT").isEmpty() ? 0 : Double.parseDouble(dt.getString("ITEM_SGSTRT"));
            item.igstRate = dt.getString("ITEM_IGSTRT").isEmpty() ? 0 : Double.parseDouble(dt.getString("ITEM_IGSTRT"));
            item.cessRate = dt.getString("ITEM_CESRT").isEmpty() ? 0 : Double.parseDouble(dt.getString("ITEM_CESRT"));
            item.cessNonAdvol = dt.getString("ITEM_CESNONADVAL").isEmpty() ? 0 : Double.parseDouble(dt.getString("ITEM_CESNONADVAL"));
            item.taxableAmount = dt.getString("ITEM_TOTITEMVAL").isEmpty() ? 0 : Double.parseDouble(dt.getString("ITEM_TOTITEMVAL"));
            itm.add(item);
        }
        return itm;
    }
//}

class ItemList {
    public String productName;
    public String productDesc;
    public String hsnCode;
    public double quantity;
    public String qtyUnit;
    public double taxableAmount;
    public double sgstRate;
    public double cgstRate;
    public double igstRate;
    public double cessRate;
    public double cessNonAdvol;
}

class GenEWaybject {
    public String supplyType;
    public String subSupplyType;
    public String subSupplyDesc;
    public String docType;
    public String docNo;
    public String docDate;
    public String fromGstin;
    public String fromTrdName;
    public String fromAddr1;
    public String fromAddr2;
    public String fromPlace;
    public int fromPincode;
    public int fromStateCode;
    public int actFromStateCode;
    public String toGstin;
    public String toTrdname;
    public String toAddr1;
    public String toAddr2;
    public String toPlace;
    public int toPincode;
    public int toStateCode;
    public int actToStateCode;
    public int totalValue;
    public double cgstValue;
    public double sgstValue;
    public double igstValue;
    public double cessValue;
    public String transMode;
    public String transDistance;
    public String transporterName;
    public String transporterId;
    public String transDocNo;
    public String transDocDate;
    public String vehicleNo;
    public String vehicleType;
    public int totInvValue;
    public int transactionType;
    public String dispatchFromGSTIN;
    public String dispatchFromTradeName;
    public String shipToGSTIN;
    public String shipToTradeName;
    public int otherValue;
    public int cessNonAdvolValue;
    public List<ItemList> itemList;
}

class EwaySuccessResult {
    public String ewayBillDate;
    public long ewayBillNo;
    public String alert;
    public String validUpto;
}

class EwaySuccessRoot {
    public boolean success;
    public EwaySuccessResult result;
    public String message;
}
}