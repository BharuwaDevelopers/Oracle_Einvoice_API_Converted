package bspl.einvoice.eiew.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import com.google.gson.Gson;

import java.sql.SQLException;

public class Taxilla_GenerateEinvoice {

    public static String GenerateIRNJsonFile(DataTable dt, String EINVEWB) {
        String msg = "";
        try {
            List<GenIrnRoot> customers = new ArrayList<>();

            if (dt.getRows().get(0).get("TRAN_CATG").toString().startsWith("EXP")) {
                GenIrnRoot TranDtls = new GenIrnRoot();
                TranDtls.setVersion("1.1");
                TranDtls.setTranDtls(GetTranGetails(dt));
                TranDtls.setDocDtls(GetDocumentDetails(dt));
                TranDtls.setSellerDtls(GetSellerDetails(dt));
                TranDtls.setBuyerDtls(GetBuyerDetails(dt));
                TranDtls.setItemList(getitem_list(dt));
                TranDtls.setValDtls(getvalue_details(dt));
                TranDtls.setPayDtls(getpayment_details(dt));
                TranDtls.setAddlDocDtls(get_additional_document(dt));
                TranDtls.setExpDtls(getexport_details(dt));
                customers.add(TranDtls);
            } else if (dt.getRows().get(0).get("BILLTO_GSTIN").toString().equals(dt.getRows().get(0).get("SHIPTO_GSTIN").toString())
                    && dt.getRows().get(0).get("BILLFROM_GSTIN").toString().equals(dt.getRows().get(0).get("SHIPFROM_GSTIN").toString())
                    && EINVEWB.equals("Y")) {
                if (dt.getRows().get(0).get("BILLTO_BNO").toString().equals(dt.getRows().get(0).get("SHIPTO_BNM").toString())) {
                    GenIrnRoot TranDtls = new GenIrnRoot();
                    TranDtls.setVersion("1.1");
                    TranDtls.setTranDtls(GetTranGetails(dt));
                    TranDtls.setDocDtls(GetDocumentDetails(dt));
                    TranDtls.setSellerDtls(GetSellerDetails(dt));
                    TranDtls.setBuyerDtls(GetBuyerDetails(dt));
                    TranDtls.setItemList(getitem_list(dt));
                    TranDtls.setValDtls(getvalue_details(dt));
                    TranDtls.setPayDtls(getpayment_details(dt));
                    TranDtls.setAddlDocDtls(get_additional_document(dt));
                    TranDtls.setExpDtls(getexport_details(dt));
                    TranDtls.setEwbDtls(getewaybill_details(dt));
                    customers.add(TranDtls);
                } else {
                    GenIrnRoot TranDtls = new GenIrnRoot();
                    TranDtls.setVersion("1.1");
                    TranDtls.setTranDtls(GetTranGetails(dt));
                    TranDtls.setDocDtls(GetDocumentDetails(dt));
                    TranDtls.setSellerDtls(GetSellerDetails(dt));
                    TranDtls.setBuyerDtls(GetBuyerDetails(dt));
                    TranDtls.setShipDtls(getship_details(dt));
                    TranDtls.setItemList(getitem_list(dt));
                    TranDtls.setValDtls(getvalue_details(dt));
                    TranDtls.setPayDtls(getpayment_details(dt));
                    TranDtls.setAddlDocDtls(get_additional_document(dt));
                    TranDtls.setExpDtls(getexport_details(dt));
                    TranDtls.setEwbDtls(getewaybill_details(dt));
                    customers.add(TranDtls);
                }
            } else if (!dt.getRows().get(0).get("BILLTO_GSTIN").toString().equals(dt.getRows().get(0).get("SHIPTO_GSTIN").toString())
                    && !dt.getRows().get(0).get("BILLFROM_GSTIN").toString().equals(dt.getRows().get(0).get("SHIPFROM_GSTIN").toString())
                    && EINVEWB.equals("Y")) {
                GenIrnRoot TranDtls = new GenIrnRoot();
                TranDtls.setVersion("1.1");
                TranDtls.setTranDtls(GetTranGetails(dt));
                TranDtls.setDocDtls(GetDocumentDetails(dt));
                TranDtls.setSellerDtls(GetSellerDetails(dt));
                TranDtls.setBuyerDtls(GetBuyerDetails(dt));
                TranDtls.setDispDtls(getdispatch_details(dt));
                TranDtls.setShipDtls(getship_details(dt));
                TranDtls.setItemList(getitem_list(dt));
                TranDtls.setValDtls(getvalue_details(dt));
                TranDtls.setPayDtls(getpayment_details(dt));
                TranDtls.setAddlDocDtls(get_additional_document(dt));
                TranDtls.setExpDtls(getexport_details(dt));
                TranDtls.setEwbDtls(getewaybill_details(dt));
                customers.add(TranDtls);
            } else if (dt.getRows().get(0).get("BILLTO_GSTIN").toString().equals(dt.getRows().get(0).get("SHIPTO_GSTIN").toString())
                    && !dt.getRows().get(0).get("BILLFROM_GSTIN").toString().equals(dt.getRows().get(0).get("SHIPFROM_GSTIN").toString())
                    && EINVEWB.equals("Y")) {
                if (dt.getRows().get(0).get("BILLTO_BNO").toString().equals(dt.getRows().get(0).get("SHIPTO_BNM").toString())) {
                    GenIrnRoot TranDtls = new GenIrnRoot();
                    TranDtls.setVersion("1.1");
                    TranDtls.setTranDtls(GetTranGetails(dt));
                    TranDtls.setDocDtls(GetDocumentDetails(dt));
                    TranDtls.setSellerDtls(GetSellerDetails(dt));
                    TranDtls.setBuyerDtls(GetBuyerDetails(dt));
                    TranDtls.setDispDtls(getdispatch_details(dt));
                    TranDtls.setItemList(getitem_list(dt));
                    TranDtls.setValDtls(getvalue_details(dt));
                    TranDtls.setPayDtls(getpayment_details(dt));
                    TranDtls.setAddlDocDtls(get_additional_document(dt));
                    TranDtls.setExpDtls(getexport_details(dt));
                    TranDtls.setEwbDtls(getewaybill_details(dt));
                    customers.add(TranDtls);
                } else {
                    GenIrnRoot TranDtls = new GenIrnRoot();
                    TranDtls.setVersion("1.1");
                    TranDtls.setTranDtls(GetTranGetails(dt));
                    TranDtls.setDocDtls(GetDocumentDetails(dt));
                    TranDtls.setSellerDtls(GetSellerDetails(dt));
                    TranDtls.setBuyerDtls(GetBuyerDetails(dt));
                    TranDtls.setDispDtls(getdispatch_details(dt));
                    TranDtls.setShipDtls(getship_details(dt));
                    TranDtls.setItemList(getitem_list(dt));
                    TranDtls.setValDtls(getvalue_details(dt));
                    TranDtls.setPayDtls(getpayment_details(dt));
                    TranDtls.setAddlDocDtls(get_additional_document(dt));
                    TranDtls.setExpDtls(getexport_details(dt));
                    TranDtls.setEwbDtls(getewaybill_details(dt));
                    customers.add(TranDtls);
                }
            } else if (!dt.getRows().get(0).get("BILLTO_GSTIN").toString().equals(dt.getRows().get(0).get("SHIPTO_GSTIN").toString())
                    && dt.getRows().get(0).get("BILLFROM_GSTIN").toString().equals(dt.getRows().get(0).get("SHIPFROM_GSTIN").toString())
                    && EINVEWB.equals("Y")) {
                GenIrnRoot TranDtls = new GenIrnRoot();
                TranDtls.setVersion("1.1");
                TranDtls.setTranDtls(GetTranGetails(dt));
                TranDtls.setDocDtls(GetDocumentDetails(dt));
                TranDtls.setSellerDtls(GetSellerDetails(dt));
                TranDtls.setBuyerDtls(GetBuyerDetails(dt));
                TranDtls.setShipDtls(getship_details(dt));
                TranDtls.setItemList(getitem_list(dt));
                TranDtls.setValDtls(getvalue_details(dt));
                TranDtls.setPayDtls(getpayment_details(dt));
                TranDtls.setAddlDocDtls(get_additional_document(dt));
                TranDtls.setExpDtls(getexport_details(dt));
                TranDtls.setEwbDtls(getewaybill_details(dt));
                customers.add(TranDtls);
            } else if (EINVEWB.equals("Y")) {
                GenIrnRoot TranDtls = new GenIrnRoot();
                TranDtls.setVersion("1.1");
                TranDtls.setTranDtls(GetTranGetails(dt));
                TranDtls.setDocDtls(GetDocumentDetails(dt));
                TranDtls.setSellerDtls(GetSellerDetails(dt));
                TranDtls.setBuyerDtls(GetBuyerDetails(dt));
                TranDtls.setDispDtls(getdispatch_details(dt));
                TranDtls.setShipDtls(getship_details(dt));
                TranDtls.setItemList(getitem_list(dt));
                TranDtls.setValDtls(getvalue_details(dt));
                TranDtls.setPayDtls(getpayment_details(dt));
                TranDtls.setAddlDocDtls(get_additional_document(dt));
                TranDtls.setExpDtls(getexport_details(dt));
                TranDtls.setEwbDtls(getewaybill_details(dt));
                customers.add(TranDtls);
            } else {
                GenIrnRoot TranDtls = new GenIrnRoot();
                TranDtls.setVersion("1.1");
                TranDtls.setTranDtls(GetTranGetails(dt));
                TranDtls.setDocDtls(GetDocumentDetails(dt));
                TranDtls.setSellerDtls(GetSellerDetails(dt));
                TranDtls.setBuyerDtls(GetBuyerDetails(dt));
                TranDtls.setItemList(getitem_list(dt));
                TranDtls.setValDtls(getvalue_details(dt));
                customers.add(TranDtls);
            }

            String json = new Gson().toJson(customers);
            String mystr = json.substring(1, json.length() - 1);

            ClsDynamic.JsonLog(mystr, dt.getRows().get(0).get("DOC_No").toString().replace("/", "_"));
            return msg = mystr;
        } catch (Exception ex) {
            ClsDynamic.WriteLog(ex.getMessage(), dt.getRows().get(0).get("DOC_No").toString().replace("/", "_"));
            return msg = "";
        }
    }

    public static TranDtls GetTranGetails(DataTable dt) {
        try {
            TranDtls TranDtls = new TranDtls();
            TranDtls.setTaxSch(dt.getRows().get(0).get("TAXSCH").toString().isEmpty() ? "GST" : dt.getRows().get(0).get("TAXSCH").toString());
            TranDtls.setSupTyp(dt.getRows().get(0).get("TRAN_CATG").toString());
            TranDtls.setRegRev(dt.getRows().get(0).get("TRAN_ECMTRN").toString());
            TranDtls.setEcmGstin(dt.getRows().get(0).get("ecommerce_gstin").toString().isEmpty() ? null : dt.getRows().get(0).get("ecommerce_gstin").toString());
            TranDtls.setIgstOnIntra(dt.getRows().get(0).get("IGST_INTRA").toString());
            return TranDtls;
        } catch (Exception ex) {
            ClsDynamic.WriteLog("Error from TranDtls " + ex.getMessage(), dt.getRows().get(0).get("DOC_No").toString().replace("/", "_"));
            ClsDynamic.UpdateErrorLog("Error from TranDtls " + ex.getMessage(), dt.getRows().get(0).get("DOC_No").toString());
            throw ex;
        }
    }

    public static DocDtls GetDocumentDetails(DataTable dt) {
        try {
            DocDtls DocDtls = new DocDtls();
            DocDtls.setTyp(dt.getRows().get(0).get("DOC_TYP").toString());
            DocDtls.setNo(dt.getRows().get(0).get("DOC_NO").toString());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            DocDtls.setDt(sdf.format(dt.getRows().get(0).get("DOC_DT")));
            return DocDtls;
        } catch (Exception ex) {
            ClsDynamic.WriteLog("Error from DocDtls " + ex.getMessage(), dt.getRows().get(0).get("DOC_No").toString().replace("/", "_"));
            ClsDynamic.UpdateErrorLog("Error from DocDtls " + ex.getMessage(), dt.getRows().get(0).get("DOC_No").toString());
            throw ex;
        }
    }

    public static SellerDtls GetSellerDetails(DataTable dt) {
        try {
            SellerDtls SellerDtls = new SellerDtls();
            SellerDtls.setGstin(dt.getRows().get(0).get("BILLFROM_GSTIN").toString());
            SellerDtls.setLglNm(dt.getRows().get(0).get("BILLFROM_TRDNM").toString());
            SellerDtls.setTrdNm(dt.getRows().get(0).get("BILLFROM_TRDNM").toString().isEmpty() ? null : dt.getRows().get(0).get("BILLFROM_TRDNM").toString());
            SellerDtls.setAddr1(dt.getRows().get(0).get("BILLFROM_BNO").toString().trim());
            SellerDtls.setAddr2(null);
            SellerDtls.setLoc(dt.getRows().get(0).get("BILLFROM_LOC").toString());
            SellerDtls.setPin(Integer.parseInt(dt.getRows().get(0).get("BILLFROM_PIN").toString().isEmpty() ? "201301" : dt.getRows().get(0).get("BILLFROM_PIN").toString()));
            SellerDtls.setStcd(dt.getRows().get(0).get("BILLFROM_STCD").toString().isEmpty() ? "0" : dt.getRows().get(0).get("BILLFROM_STCD").toString());
            SellerDtls.setPh(null);
            SellerDtls.setEm(null);
            return SellerDtls;
        } catch (Exception ex) {
            ClsDynamic.WriteLog("Error from SellerDtls " + ex.getMessage(), dt.getRows().get(0).get("DOC_No").toString().replace("/", "_"));
            ClsDynamic.UpdateErrorLog("Error from SellerDtls " + ex.getMessage(), dt.getRows().get(0).get("DOC_No").toString());
            throw ex;
        }
    }

    public static BuyerDtls GetBuyerDetails(DataTable dt) {
        try {
            BuyerDtls BuyerDtls = new BuyerDtls();
            BuyerDtls.setGstin(dt.getRows().get(0).get("BILLTO_GSTIN").toString());
            BuyerDtls.setLglNm(dt.getRows().get(0).get("BILLTO_TRDNM").toString());
            BuyerDtls.setTrdNm(dt.getRows().get(0).get("BILLTO_TRDNM").toString().isEmpty() ? null : dt.getRows().get(0).get("BILLTO_TRDNM").toString());
            BuyerDtls.setPos(dt.getRows().get(0).get("place_of_supply").toString().isEmpty() ? null : dt.getRows().get(0).get("place_of_supply").toString());
            BuyerDtls.setAddr1(dt.getRows().get(0).get("BILLTO_BNO").toString().trim());
            BuyerDtls.setAddr2(null);
            BuyerDtls.setLoc(dt.getRows().get(0).get("BILLTO_LOC").toString().isEmpty() ? "" : dt.getRows().get(0).get("BILLTO_LOC").toString());
            BuyerDtls.setPin(Integer.parseInt(dt.getRows().get(0).get("BILLTO_PIN").toString().isEmpty() ? "201301" : dt.getRows().get(0).get("BILLTO_PIN").toString()));
            BuyerDtls.setStcd(dt.getRows().get(0).get("BILLTO_STCD").toString().isEmpty() ? null : dt.getRows().get(0).get("BILLTO_STCD").toString());
            BuyerDtls.setPh(null);
            BuyerDtls.setEm(null);
            return BuyerDtls;
        } catch (Exception ex) {
            ClsDynamic.WriteLog("Error from BuyerDtls " + ex.getMessage(), dt.getRows().get(0).get("DOC_No").toString().replace("/", "_"));
            ClsDynamic.UpdateErrorLog("Error from BuyerDtls " + ex.getMessage(), dt.getRows().get(0).get("DOC_No").toString());
            throw ex;
        }
    }

    public static DispDtls getdispatch_details(DataTable dt) {
        try {
            DispDtls dispatch_details = new DispDtls();
            dispatch_details.setNm(dt.getRows().get(0).get("SHIPFROM_TRDNM").toString().isEmpty() ? "" : dt.getRows().get(0).get("SHIPFROM_TRDNM").toString());
            dispatch_details.setAddr1(dt.getRows().get(0).get("SHIPFROM_BNO").toString() + " " + dt.getRows().get(0).get("SHIPFROM_BNM").toString());
            dispatch_details.setAddr2(null);
            dispatch_details.setLoc(dt.getRows().get(0).get("SHIPFROM_LOC").toString().isEmpty() ? "" : dt.getRows().get(0).get("SHIPFROM_LOC").toString());
            dispatch_details.setPin(Integer.parseInt(dt.getRows().get(0).get("SHIPFROM_PIN").toString().isEmpty() ? "201301" : dt.getRows().get(0).get("SHIPFROM_PIN").toString()));
            dispatch_details.setStcd(dt.getRows().get(0).get("SHIPFROM_STCD").toString().isEmpty() ? "" : dt.getRows().get(0).get("SHIPFROM_STCD").toString());
            return dispatch_details;
        } catch (Exception ex) {
            ClsDynamic.WriteLog("Error from dispatch_details " + ex.getMessage(), dt.getRows().get(0).get("DOC_No").toString().replace("/", "_"));
            ClsDynamic.UpdateErrorLog("Error from dispatch_details " + ex.getMessage(), dt.getRows().get(0).get("DOC_No").toString());
            throw ex;
        }
    }
    
    public static ShipDtls getship_details(ResultSet rs) {
            try {
                ShipDtls ship_details = new ShipDtls();
                rs.next();
                ship_details.setGstin(rs.getString("SHIPTO_GSTIN").isEmpty() ? null : rs.getString("SHIPTO_GSTIN"));
                ship_details.setLglNm(rs.getString("SHIPTO_TRDNM")); // Mandatory
                ship_details.setTrdNm(rs.getString("SHIPTO_TRDNM").isEmpty() ? null : rs.getString("SHIPTO_TRDNM"));
                ship_details.setAddr1(rs.getString("SHIPTO_BNO").trim() + rs.getString("SHIPTO_BNM")); // Mandatory
                ship_details.setAddr2(null);
                ship_details.setLoc(rs.getString("SHIPTO_LOC")); // Mandatory
                ship_details.setPin(rs.getString("SHIPTO_PIN").isEmpty() ? 201301 : Integer.parseInt(rs.getString("SHIPTO_PIN"))); // Mandatory
                ship_details.setStcd(rs.getString("SHIPTO_STCD")); // Mandatory

                return ship_details;
            } catch (SQLException ex) {
                ClsDynamic.WriteLog("Error from ship_details " + ex.getMessage(), rs.getString("DOC_No").replace("/", "_"));
                ClsDynamic.UpdateErrorLog("Error from ship_details " + ex.getMessage(), rs.getString("DOC_No"));
                throw new RuntimeException(ex);
            }
        }

        public static List<ItemList> getitem_list(ResultSet rs) {
            try {
                List<ItemList> itm = new ArrayList<>();
                while (rs.next()) {
                    ItemList item = new ItemList();
                    item.setSlNo(String.valueOf(rs.getRow())); // Mandatory
                    item.setPrdDesc(rs.getString("ITEM_PRDDESC"));
                    item.setIsServc(rs.getString("ITEM_IS_SERVICE").isEmpty() ? "" : rs.getString("ITEM_IS_SERVICE")); // Mandatory
                    item.setHsnCd(rs.getString("ITEM_HSNCD").isEmpty() ? "" : rs.getString("ITEM_HSNCD")); // Mandatory
                    item.setBarcde(rs.getString("ITEM_BARCDE").isEmpty() ? null : rs.getString("ITEM_BARCDE"));
                    item.setQty(Double.parseDouble(rs.getString("ITEM_QTY")));
                    item.setFreeQty(Integer.parseInt(rs.getString("ITEM_FREEQTY")));
                    item.setUnit(rs.getString("ITEM_UNIT").isEmpty() ? "" : rs.getString("ITEM_UNIT"));
                    item.setUnitPrice(Double.parseDouble(rs.getString("ITEM_UNITPRICE"))); // Mandatory
                    item.setTotAmt(Double.parseDouble(rs.getString("ITEM_TOTAMT").isEmpty() ? "0" : rs.getString("ITEM_TOTAMT"))); // Mandatory
                    item.setDiscount(Double.parseDouble(rs.getString("ITEM_DISCOUNT").isEmpty() ? "0.0" : rs.getString("ITEM_DISCOUNT")));
                    item.setPreTaxVal(Double.parseDouble(rs.getString("ITEM_PRETAX_VALUE").isEmpty() ? "0.0" : rs.getString("ITEM_PRETAX_VALUE")));
                    item.setAssAmt(Double.parseDouble(rs.getString("ITEM_ASSAMT").isEmpty() ? "0" : rs.getString("ITEM_ASSAMT"))); // Mandatory
                    item.setGstRt(Double.parseDouble(rs.getString("ITEM_SGSTRT").isEmpty() ? "0" : rs.getString("ITEM_SGSTRT"))); // Mandatory
                    item.setIgstAmt(Double.parseDouble(rs.getString("ITEM_IGSTAMT").isEmpty() ? "0" : rs.getString("ITEM_IGSTAMT")));
                    item.setCgstAmt(Double.parseDouble(rs.getString("ITEM_CGSTAMT").isEmpty() ? "0" : rs.getString("ITEM_CGSTAMT")));
                    item.setSgstAmt(Double.parseDouble(rs.getString("ITEM_SGSTAMT").isEmpty() ? "0" : rs.getString("ITEM_SGSTAMT")));
                    item.setCesRt(Double.parseDouble(rs.getString("ITEM_CESRT").isEmpty() ? "0.0" : rs.getString("ITEM_CESRT")));
                    item.setCesAmt(Double.parseDouble(rs.getString("ITEM_CESAMT").isEmpty() ? "0.0" : rs.getString("ITEM_CESAMT")));
                    item.setCesNonAdvlAmt(Double.parseDouble(rs.getString("ITEM_CESNONADVAL").isEmpty() ? "0" : rs.getString("ITEM_CESNONADVAL")));
                    item.setStateCesRt(Double.parseDouble(rs.getString("ITEM_STATECES").isEmpty() ? "0.0" : rs.getString("ITEM_STATECES")));
                    item.setStateCesAmt(Double.parseDouble(rs.getString("ITEM_STATECESAMT").isEmpty() ? "0.0" : rs.getString("ITEM_STATECESAMT")));
                    item.setStateCesNonAdvlAmt(Double.parseDouble(rs.getString("ITEM_STATECESNODAMT").isEmpty() ? "0.0" : rs.getString("ITEM_STATECESNODAMT")));
                    item.setOthChrg(Double.parseDouble(rs.getString("ITEM_OTHCHRG").isEmpty() ? "0" : rs.getString("ITEM_OTHCHRG")));
                    item.setTotItemVal(Double.parseDouble(rs.getString("ITEM_TOTITEMVAL").isEmpty() ? "0" : rs.getString("ITEM_TOTITEMVAL"))); // Mandatory
                    item.setOrgCntry("IN");
                    item.setOrdLineRef("1");
                    item.setPrdSlNo("101");
                    item.setBchDtls(Get_Batch_details(rs.getString("ITEM_BATCH_NM"), rs.getString("ITEM_BATCH_EXPDT"), rs.getString("ITEM_BATCH_WRDT")));
                    item.setAttribDtls(Get_attribute_details(rs));
                    itm.add(item);
                }
                return itm;
            } catch (SQLException ex) {
                ClsDynamic.WriteLog("Error from Item Details " + ex.getMessage(), rs.getString("DOC_No").replace("/", "_"));
                ClsDynamic.UpdateErrorLog("Error from Item Details " + ex.getMessage(), rs.getString("DOC_No"));
                throw new RuntimeException(ex);
            }
        }

        public static BchDtls Get_Batch_details(String Name, String Expirydt, String WrDate) {
            try {
                BchDtls batch_details = new BchDtls();
                batch_details.setNm(Name.isEmpty() ? "Testing" : Name); // Mandatory
                batch_details.setExpdt(Expirydt.isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(Expirydt)));
                batch_details.setWrDt(WrDate.isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(WrDate)));
                return batch_details;
            } catch (Exception ex) {
                ClsDynamic.WriteLog("Error from batch_details " + ex.getMessage());
                throw new RuntimeException(ex);
            }
        }

        public static List<AttribDtl> Get_attribute_details(ResultSet rs) {
            try {
                List<AttribDtl> lstattrdtl = new ArrayList<>();
                rs.next();
                lstattrdtl.add(new AttribDtl(
                    rs.getString("ITEM_ATTRIBUTE_DETAILS").isEmpty() ? null : rs.getString("ITEM_ATTRIBUTE_DETAILS"), // Mandatory
                    rs.getString("ITEM_ATTRIBUTE_VALUE").isEmpty() ? null : rs.getString("ITEM_ATTRIBUTE_VALUE")
                ));
                return lstattrdtl;
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }

        public static ValDtls getvalue_details(ResultSet rs) {
            try {
                ValDtls value_details = new ValDtls();
                rs.next();
                value_details.setAssVal(Double.parseDouble(rs.getString("VAL_ASSVAL").isEmpty() ? "0" : rs.getString("VAL_ASSVAL"))); // Required
                value_details.setCgstVal(Double.parseDouble(rs.getString("VAL_CGSTVAL").isEmpty() ? "0" : rs.getString("VAL_CGSTVAL")));
                value_details.setSgstVal(Double.parseDouble(rs.getString("VAL_SGSTVAL").isEmpty() ? "0" : rs.getString("VAL_SGSTVAL")));
                value_details.setIgstVal(Double.parseDouble(rs.getString("VAL_IGSTVAL").isEmpty() ? "0" : rs.getString("VAL_IGSTVAL")));
                value_details.setCesVal(Double.parseDouble(rs.getString("VAL_CESVAL").isEmpty() ? "0" : rs.getString("VAL_CESVAL")));
                value_details.setStCesVal(Double.parseDouble(rs.getString("VAL_STCESVAL").isEmpty() ? "0" : rs.getString("VAL_STCESVAL")));
                value_details.setDiscount(Double.parseDouble(rs.getString("VAL_TOTDISCOUNT").isEmpty() ? "0" : rs.getString("VAL_TOTDISCOUNT")));
                value_details.setOthChrg(Double.parseDouble(rs.getString("VAL_OTHER_CHARGE").isEmpty() ? "0" : rs.getString("VAL_OTHER_CHARGE")));
                value_details.setTotInvVal(Double.parseDouble(rs.getString("VAL_TOTINVVAL").isEmpty() ? "0" : rs.getString("VAL_TOTINVVAL"))); // Required
                value_details.setRndOffAmt(Double.parseDouble(rs.getString("VAL_OTHCHRG").isEmpty() ? "0" : rs.getString("VAL_OTHCHRG")));
                value_details.setTotInvValFc(Double.parseDouble(rs.getString("VAL_TOTINV_ADDCUR").isEmpty() ? "0" : rs.getString("VAL_TOTINV_ADDCUR")));
                return value_details;
            } catch (SQLException ex) {
                ClsDynamic.WriteLog("Error from value_details " + ex.getMessage(), rs.getString("DOC_No").replace("/", "_"));
                ClsDynamic.UpdateErrorLog("Error from value_details " + ex.getMessage(), rs.getString("DOC_No"));
                throw new RuntimeException(ex);
            }
        }

        public static PayDtls getpayment_details(ResultSet rs) {
            try {
                PayDtls payment_details = new PayDtls();
                rs.next();
                payment_details.setNm(rs.getString("PAY_NAM").isEmpty() ? null : rs.getString("PAY_NAM"));
                payment_details.setAccdet(rs.getString("PAY_ACCTDET").isEmpty() ? null : rs.getString("PAY_ACCTDET"));
                payment_details.setMode(rs.getString("PAY_MODE").isEmpty() ? null : rs.getString("PAY_MODE"));
                payment_details.setFininsbr(rs.getString("PAY_FININSBR").isEmpty() ? null : rs.getString("PAY_FININSBR"));
                payment_details.setPayterm(rs.getString("PAY_PAYTERM").isEmpty() ? null : rs.getString("PAY_PAYTERM"));
                payment_details.setPayinstr(rs.getString("PAY_PAYINSTR").isEmpty() ? null : rs.getString("PAY_PAYINSTR"));
                payment_details.setCrtrn(rs.getString("PAY_CRTRN").isEmpty() ? null : rs.getString("PAY_CRTRN"));
                payment_details.setDirdr(rs.getString("PAY_DIRDR").isEmpty() ? null : rs.getString("PAY_DIRDR"));
                payment_details.setCrday(rs.getString("PAY_CRDAY").isEmpty() ? null : Integer.parseInt(rs.getString("PAY_CRDAY")));
                payment_details.setPaidamt(rs.getString("PAY_BALAMT").isEmpty() ? null : Double.parseDouble(rs.getString("PAY_BALAMT")));
                payment_details.setPaymtdue(rs.getString("PAY_OUTSTANDINGAMT").isEmpty() ? null : Double.parseDouble(rs.getString("PAY_OUTSTANDINGAMT")));
                return payment_details;
            } catch (SQLException ex) {
                ClsDynamic.WriteLog("Error from payment_details " + ex.getMessage(), rs.getString("DOC_No").replace("/", "_"));
                ClsDynamic.UpdateErrorLog("Error from payment_details " + ex.getMessage(), rs.getString("DOC_No"));
                throw new RuntimeException(ex);
            }
        }

        public static RefDtls getreference_details(ResultSet rs) {
            try {
                RefDtls reference_details = new RefDtls();
                rs.next();
                reference_details.setInvRm(rs.getString("REF_INVRMK").isEmpty() ? null : rs.getString("REF_INVRMK"));
                reference_details.setDocPerdDtls(new DocPerdDtls(
                    rs.getString("REF_INVSTDT").isEmpty() ? new SimpleDateFormat("dd/MM/yyyy").format(new Date()) : new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("REF_INVSTDT"))), // Mandatory
                    rs.getString("REF_INVENDDT").isEmpty() ? new SimpleDateFormat("dd/MM/yyyy").format(new Date()) : new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("REF_INVENDDT"))) // Mandatory
                ));
                reference_details.setPrecDocDtls(Get_preceding_document_details(rs));
                reference_details.setContrDtls(Get_ContractDetail(rs));
                return reference_details;
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        public static List<PrecDocDtl> Get_preceding_document_details(ResultSet rs) {
            List<PrecDocDtl> lstpredocdtl = new ArrayList<>();
            try {
                rs.next();
                lstpredocdtl.add(new PrecDocDtl(
                    rs.getString("REF_INVNO"), // Mandatory
                    rs.getString("REF_PRECINVDT").isEmpty() ? new SimpleDateFormat("dd/MM/yyyy").format(new Date()) : new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("REF_PRECINVDT"))), // Mandatory
                    rs.getString("REF_EXTREF").isEmpty() ? null : rs.getString("REF_EXTREF")
                ));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return lstpredocdtl;
        }

        public static List<ContrDtl> Get_ContractDetail(ResultSet rs) {
            List<ContrDtl> lstcontractdtl = new ArrayList<>();
            try {
                rs.next();
                lstcontractdtl.add(new ContrDtl(
                    rs.getString("REF_PRECINVNO").isEmpty() ? null : rs.getString("REF_PRECINVNO"),
                    rs.getString("REF_PRECINVDT").isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("REF_PRECINVDT"))),
                    rs.getString("REF_PROJREF").isEmpty() ? null : rs.getString("REF_PROJREF"),
                    rs.getString("REF_CONTRREF").isEmpty() ? null : rs.getString("REF_CONTRREF"),
                    rs.getString("REF_EXTREF").isEmpty() ? null : rs.getString("REF_EXTREF"),
                    rs.getString("REF_PROJREF").isEmpty() ? null : rs.getString("REF_PROJREF"),
                    rs.getString("REF_POREF").isEmpty() ? null : rs.getString("REF_POREF"),
                    rs.getString("VENDOR_PO_REFDT").isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("VENDOR_PO_REFDT")))
                ));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return lstcontractdtl;
        }

        public static List<AddlDocDtl> get_additional_document(ResultSet rs) {
            List<AddlDocDtl> lstAddDocDtl = new ArrayList<>();
            try {
                rs.next();
                lstAddDocDtl.add(new AddlDocDtl(
                    rs.getString("ADD_DOC_URL").isEmpty() ? null : rs.getString("ADD_DOC_URL"),
                    rs.getString("ADD_SUPPORTING_DOC").isEmpty() ? null : rs.getString("ADD_SUPPORTING_DOC"),
                    rs.getString("ADD_ADDITIONAL_INFO").isEmpty() ? null : rs.getString("ADD_ADDITIONAL_INFO")
                ));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            return lstAddDocDtl;
        }

        public static ExpDtls getexport_details(ResultSet rs) {
            try {
                ExpDtls export_details = new ExpDtls();
                rs.next();
                export_details.setShipBNo(rs.getString("EXP_SHIPBNO").isEmpty() ? null : rs.getString("EXP_SHIPBNO"));
                export_details.setShipBDt(rs.getString("EXP_SHIPBDT").isEmpty() ? null : new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("EXP_SHIPBDT"))));
                export_details.setCntCode(rs.getString("EXP_CNTCODE").isEmpty() ? null : rs.getString("EXP_CNTCODE"));
                export_details.setForCur(rs.getString("EXP_FORCUR").isEmpty() ? null : rs.getString("EXP_FORCUR"));
                export_details.setRefClm(rs.getString("REFUND_CLAIM").isEmpty() ? null : rs.getString("REFUND_CLAIM"));
                export_details.setPort(rs.getString("EXP_PORT").isEmpty() ? null : rs.getString("EXP_PORT"));
                export_details.setExpDuty(rs.getString("EXP_DUTY").isEmpty() ? null : rs.getString("EXP_DUTY"));
                return export_details;
            } catch (SQLException ex) {
                ClsDynamic.WriteLog("Error from export_details " + ex.getMessage(), rs.getString("DOC_No").replace("/", "_"));
                throw new RuntimeException(ex);
            }
        }
        
    public class TranDtls {
        private String taxSch;
        private String supTyp;
        private String regRev;
        private Object ecmGstin;
        private String igstOnIntra;

        // Getters and Setters
    }

    public class DocDtls {
        private String typ;
        private String no;
        private String dt;

        // Getters and Setters
    }

    public class SellerDtls {
        private String gstin;
        private String lglNm;
        private String trdNm;
        private String addr1;
        private String addr2;
        private String loc;
        private int pin;
        private String stcd;
        private String ph;
        private String em;

        // Getters and Setters
    }

    public class BuyerDtls {
        private String gstin;
        private String lglNm;
        private String trdNm;
        private String pos;
        private String addr1;
        private String addr2;
        private String loc;
        private int pin;
        private String stcd;
        private String ph;
        private String em;

        // Getters and Setters
    }

    public class DispDtls {
        private String nm;
        private String addr1;
        private String addr2;
        private String loc;
        private int pin;
        private String stcd;

        // Getters and Setters
    }

    public class ShipDtls {
        private String gstin;
        private String lglNm;
        private String trdNm;
        private String addr1;
        private String addr2;
        private String loc;
        private int pin;
        private String stcd;

        // Getters and Setters
    }

    public class BchDtls {
        private String nm;
        private String expdt;
        private String wrDt;

        // Getters and Setters
    }

    public class AttribDtl {
        private String nm;
        private String val;

        // Getters and Setters
    }

    public class ValDtls {
        private double assVal;
        private double cgstVal;
        private double sgstVal;
        private double igstVal;
        private double cesVal;
        private double stCesVal;
        private double discount;
        private double othChrg;
        private double rndOffAmt;
        private double totInvVal;
        private double totInvValFc;

        // Getters and Setters
    }

    public class PayDtls {
        private String nm;
        private String accdet;
        private String mode;
        private String fininsbr;
        private String payterm;
        private String payinstr;
        private String crtrn;
        private String dirdr;
        private int crday;
        private double paidamt;
        private double paymtdue;

        // Getters and Setters
    }

    public class RefDtls {
        private String invRm;
        private DocPerdDtls docPerdDtls;
        private List<PrecDocDtl> precDocDtls;
        private List<ContrDtl> contrDtls;

        // Getters and Setters
    }

    public class ContrDtl {
        private String recAdvRefr;
        private String recAdvDt;
        private String tendrefr;
        private String contrrefr;
        private String extrefr;
        private String projrefr;
        private String porefr;
        private String poRefDt;

        // Getters and Setters
    }

    public class DocPerdDtls {
        private String invStDt;
        private String invEndDt;

        // Getters and Setters
    }

    public class PrecDocDtl {
        private String invNo;
        private String invDt;
        private String othRefNo;

        // Getters and Setters
    }

    public class AddlDocDtl {
        private String url;
        private String docs;
        private String info;

        // Getters and Setters
    }

    public class ExpDtls {
        private String shipBNo;
        private String shipBDt;
        private String port;
        private String refClm;
        private String forCur;
        private String cntCode;
        private Object expDuty;

        // Getters and Setters
    }

    public class EwbDtls {
        private String transid;
        private String transname;
        private int distance;
        private String transdocno;
        private String transdocDt;
        private String vehno;
        private String vehtype;
        private String transMode;

        // Getters and Setters
    }

    public class ItemList {
        private String slNo;
        private String prdDesc;
        private String isServc;
        private String hsnCd;
        private String barcde;
        private double qty;
        private int freeQty;
        private String unit;
        private double unitPrice;
        private double totAmt;
        private double discount;
        private double preTaxVal;
        private double assAmt;
        private double gstRt;
        private double igstAmt;
        private double cgstAmt;
        private double sgstAmt;
        private double cesRt;
        private double cesAmt;
        private double cesNonAdvlAmt;
        private double stateCesRt;
        private double stateCesAmt;
        private double stateCesNonAdvlAmt;
        private double othChrg;
        private double totItemVal;
        private String ordLineRef;
        private String orgCntry;
        private String prdSlNo;
        private BchDtls bchDtls;
        private List<AttribDtl> attribDtls;

        // Getters and Setters
    }

    public class GenIrnRoot {
        private String version;
        private TranDtls tranDtls;
        private DocDtls docDtls;
        private SellerDtls sellerDtls;
        private BuyerDtls buyerDtls;
        private DispDtls dispDtls;
        private ShipDtls shipDtls;
        private List<ItemList> itemList;
        private ValDtls valDtls;
        private PayDtls payDtls;
        private RefDtls refDtls;
        private List<AddlDocDtl> addlDocDtls;
        private ExpDtls expDtls;
        private EwbDtls ewbDtls;

        // Getters and Setters
    }

    public class SuccessResult {
        private long ackNo;
        private String ackDt;
        private String irn;
        private String signedInvoice;
        private String signedQRCode;
        private String status;
        private String ewbNo;
        private String ewbDt;
        private String ewbValidTill;
        private Object remarks;

        // Getters and Setters
    }

    public class Desc {
        private String errorCode;
        private String errorMessage;

        // Getters and Setters
    }

    public class SuccessInfo {
        private String infCd;
        private String desc;

        // Getters and Setters
    }

    public class SuccessInfo2 {
        private String infCd;
        private List<Desc> desc;

        // Getters and Setters
    }

    public class SuccessRoot {
        private boolean success;
        private String message;
        private SuccessResult result;
        private List<SuccessInfo> info;

        // Getters and Setters
    }

    public class SuccessRoot2 {
        private boolean success;
        private String message;
        private SuccessResult result;
        private List<SuccessInfo2> info;

        // Getters and Setters
    }

    public class DublicateDesc {
        private long ackNo;
        private String ackDt;
        private String irn;

        // Getters and Setters
    }

    public class DublicateResult {
        private String infCd;
        private DublicateDesc desc;

        // Getters and Setters
    }

    public class DublicateRoot {
        private boolean success;
        private String message;
        private List<DublicateResult> result;

        // Getters and Setters
    } 
    
    
}