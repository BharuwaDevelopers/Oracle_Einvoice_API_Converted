package bspl.einvoice.eiew.Models;


import java.util.List;
import java.util.Date;

import java.text.SimpleDateFormat;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.SimpleDateFormat;


import java.sql.SQLException;

import java.util.ArrayList;

public class Taxilla_GenerateEinvoice {

    public static String GenerateIRNJsonFile(ResultSet dt, String EINVEWB) {
        String msg = "";
        try {
            List<GenIrnRoot> customers = new ArrayList<>();

            if (dt.getString("TRAN_CATG").startsWith("EXP")) {
                //                   GenIrnRoot TranDtls = new GenIrnRoot();
                //                   TranDtls.setVersion("1.1");
                //                   TranDtls.setTranDtls(GetTranGetails(dt));
                //                   TranDtls.setDocDtls(GetDocumentDetails(dt));
                //                   TranDtls.setSellerDtls(GetSellerDetails(dt));
                //                   TranDtls.setBuyerDtls(GetBuyerDetails(dt));
                //                   TranDtls.setItemList(getitem_list(dt));
                //                   TranDtls.setValDtls(getvalue_details(dt));
                //                   TranDtls.setPayDtls(getpayment_details(dt));
                //                   TranDtls.setAddlDocDtls(get_additional_document(dt));
                //                   TranDtls.setExpDtls(getexport_details(dt));
                //                   customers.add(TranDtls);
            }

            else if (dt.getString("BILLTO_GSTIN").equals(dt.getString("SHIPTO_GSTIN")) &&
                     dt.getString("BILLFROM_GSTIN").equals(dt.getString("SHIPFROM_GSTIN")) && EINVEWB.equals("Y")) {
                //               } else if (dt.getRows().get(0).get("BILLTO_GSTIN").toString().equals(dt.getRows().get(0).get("SHIPTO_GSTIN").toString())
                //                    && dt.getRow().get(0).get("BILLFROM_GSTIN").toString().equals(dt.getRows().get(0).get("SHIPFROM_GSTIN").toString())
                //                    && EINVEWB.equals("Y")) {
                if (dt.getString("BILLTO_BNO").equals(dt.getString("SHIPTO_BNM"))) {
                    //                if (dt.getRows().get(0).get("BILLTO_BNO").toString().equals(dt.getRows().get(0).get("SHIPTO_BNM").toString())) {
                    //                    GenIrnRoot TranDtls = new GenIrnRoot();
                    //                    TranDtls.setVersion("1.1");
                    //                    TranDtls.setTranDtls(GetTranGetails(dt));
                    //                    TranDtls.setDocDtls(GetDocumentDetails(dt));
                    //                    TranDtls.setSellerDtls(GetSellerDetails(dt));
                    //                    TranDtls.setBuyerDtls(GetBuyerDetails(dt));
                    //                    TranDtls.setItemList(getitem_list(dt));
                    //                    TranDtls.setValDtls(getvalue_details(dt));
                    //                    TranDtls.setPayDtls(getpayment_details(dt));
                    //                    TranDtls.setAddlDocDtls(get_additional_document(dt));
                    //                    TranDtls.setExpDtls(getexport_details(dt));
                    //                    TranDtls.setEwbDtls(getewaybill_details(dt));
                    //                    customers.add(TranDtls);

                } else {
                    //                    GenIrnRoot TranDtls = new GenIrnRoot();
                    //                    TranDtls.setVersion("1.1");
                    //                    TranDtls.setTranDtls(GetTranGetails(dt));
                    //                    TranDtls.setDocDtls(GetDocumentDetails(dt));
                    //                    TranDtls.setSellerDtls(GetSellerDetails(dt));
                    //                    TranDtls.setBuyerDtls(GetBuyerDetails(dt));
                    //                    TranDtls.setShipDtls(getship_details(dt));
                    //                    TranDtls.setItemList(getitem_list(dt));
                    //                    TranDtls.setValDtls(getvalue_details(dt));
                    //                    TranDtls.setPayDtls(getpayment_details(dt));
                    //                    TranDtls.setAddlDocDtls(get_additional_document(dt));
                    //                    TranDtls.setExpDtls(getexport_details(dt));
                    //                    TranDtls.setEwbDtls(getewaybill_details(dt));
                    //                    customers.add(TranDtls);
                }
            } else if (dt.getString("BILLTO_GSTIN").equals(dt.getString("SHIPTO_GSTIN")) &&
                       dt.getString("BILLFROM_GSTIN").equals(dt.getString("SHIPFROM_GSTIN")) && EINVEWB.equals("Y")) {

                //               else if (!dt.getRows().get(0).get("BILLTO_GSTIN").toString().equals(dt.getRows().get(0).get("SHIPTO_GSTIN").toString())
                //                    && !dt.getRows().get(0).get("BILLFROM_GSTIN").toString().equals(dt.getRows().get(0).get("SHIPFROM_GSTIN").toString())
                //                    && EINVEWB.equals("Y")) {
                //                GenIrnRoot TranDtls = new GenIrnRoot();
                //                TranDtls.setVersion("1.1");
                //                TranDtls.setTranDtls(GetTranGetails(dt));
                //                TranDtls.setDocDtls(GetDocumentDetails(dt));
                //                TranDtls.setSellerDtls(GetSellerDetails(dt));
                //                TranDtls.setBuyerDtls(GetBuyerDetails(dt));
                //                TranDtls.setDispDtls(getdispatch_details(dt));
                //                TranDtls.setShipDtls(getship_details(dt));
                //                TranDtls.setItemList(getitem_list(dt));
                //                TranDtls.setValDtls(getvalue_details(dt));
                //                TranDtls.setPayDtls(getpayment_details(dt));
                //                TranDtls.setAddlDocDtls(get_additional_document(dt));
                //                TranDtls.setExpDtls(getexport_details(dt));
                //                TranDtls.setEwbDtls(getewaybill_details(dt));
                //                customers.add(TranDtls);
            } else if (dt.getString("BILLTO_GSTIN").equals(dt.getString("SHIPTO_GSTIN")) &&
                       dt.getString("BILLFROM_GSTIN").equals(dt.getString("SHIPFROM_GSTIN")) && EINVEWB.equals("Y")) {
                //               else if (dt.getRows().get(0).get("BILLTO_GSTIN").toString().equals(dt.getRows().get(0).get("SHIPTO_GSTIN").toString())
                //                    && !dt.getRows().get(0).get("BILLFROM_GSTIN").toString().equals(dt.getRows().get(0).get("SHIPFROM_GSTIN").toString())
                //                    && EINVEWB.equals("Y")) {
                if (dt.getString("BILLTO_BNO").equals(dt.getString("SHIPTO_BNM"))) {
                    //                if (dt.getRows().get(0).get("BILLTO_BNO").toString().equals(dt.getRows().get(0).get("SHIPTO_BNM").toString())) {
                    //                    GenIrnRoot TranDtls = new GenIrnRoot();
                    //                    TranDtls.setVersion("1.1");
                    //                    TranDtls.setTranDtls(GetTranGetails(dt));
                    //                    TranDtls.setDocDtls(GetDocumentDetails(dt));
                    //                    TranDtls.setSellerDtls(GetSellerDetails(dt));
                    //                    TranDtls.setBuyerDtls(GetBuyerDetails(dt));
                    //                    TranDtls.setDispDtls(getdispatch_details(dt));
                    //                    TranDtls.setItemList(getitem_list(dt));
                    //                    TranDtls.setValDtls(getvalue_details(dt));
                    //                    TranDtls.setPayDtls(getpayment_details(dt));
                    //                    TranDtls.setAddlDocDtls(get_additional_document(dt));
                    //                    TranDtls.setExpDtls(getexport_details(dt));
                    //                    TranDtls.setEwbDtls(getewaybill_details(dt));
                    //                    customers.add(TranDtls);

                } else {
                    //                    GenIrnRoot TranDtls = new GenIrnRoot();
                    //                    TranDtls.setVersion("1.1");
                    //                    TranDtls.setTranDtls(GetTranGetails(dt));
                    //                    TranDtls.setDocDtls(GetDocumentDetails(dt));
                    //                    TranDtls.setSellerDtls(GetSellerDetails(dt));
                    //                    TranDtls.setBuyerDtls(GetBuyerDetails(dt));
                    //                    TranDtls.setDispDtls(getdispatch_details(dt));
                    //                    TranDtls.setShipDtls(getship_details(dt));
                    //                    TranDtls.setItemList(getitem_list(dt));
                    //                    TranDtls.setValDtls(getvalue_details(dt));
                    //                    TranDtls.setPayDtls(getpayment_details(dt));
                    //                    TranDtls.setAddlDocDtls(get_additional_document(dt));
                    //                    TranDtls.setExpDtls(getexport_details(dt));
                    //                    TranDtls.setEwbDtls(getewaybill_details(dt));
                    //                    customers.add(TranDtls);
                }
            }

            //               else if (!dt.getRows().get(0).get("BILLTO_GSTIN").toString().equals(dt.getRows().get(0).get("SHIPTO_GSTIN").toString())
            //                    && dt.getRows().get(0).get("BILLFROM_GSTIN").toString().equals(dt.getRows().get(0).get("SHIPFROM_GSTIN").toString())
            //                    && EINVEWB.equals("Y")) {
            else if (dt.getString("BILLTO_GSTIN").equals(dt.getString("SHIPTO_GSTIN")) &&
                     dt.getString("BILLFROM_GSTIN").equals(dt.getString("SHIPFROM_GSTIN")) && EINVEWB.equals("Y")) {
//                GenIrnRoot TranDtls = new GenIrnRoot();
//                TranDtls.setVersion("1.1");
//                TranDtls.setTranDtls(GetTranGetails(dt));
//                TranDtls.setDocDtls(GetDocumentDetails(dt));
//                TranDtls.setSellerDtls(GetSellerDetails(dt));
//                TranDtls.setBuyerDtls(GetBuyerDetails(dt));
//                TranDtls.setShipDtls(getship_details(dt));
//                TranDtls.setItemList(getitem_list(dt));
//                TranDtls.setValDtls(getvalue_details(dt));
//                TranDtls.setPayDtls(getpayment_details(dt));
//                TranDtls.setAddlDocDtls(get_additional_document(dt));
//                TranDtls.setExpDtls(getexport_details(dt));
//                TranDtls.setEwbDtls(getewaybill_details(dt));
//                customers.add(TranDtls);
            } else if (EINVEWB.equals("Y")) {
//                GenIrnRoot TranDtls = new GenIrnRoot();
//                TranDtls.setVersion("1.1");
//                TranDtls.setTranDtls(GetTranGetails(dt));
//                TranDtls.setDocDtls(GetDocumentDetails(dt));
//                TranDtls.setSellerDtls(GetSellerDetails(dt));
//                TranDtls.setBuyerDtls(GetBuyerDetails(dt));
//                TranDtls.setDispDtls(getdispatch_details(dt));
//                TranDtls.setShipDtls(getship_details(dt));
//                TranDtls.setItemList(getitem_list(dt));
//                TranDtls.setValDtls(getvalue_details(dt));
//                TranDtls.setPayDtls(getpayment_details(dt));
//                TranDtls.setAddlDocDtls(get_additional_document(dt));
//                TranDtls.setExpDtls(getexport_details(dt));
//                TranDtls.setEwbDtls(getewaybill_details(dt));
//                customers.add(TranDtls);
            } else {
//                GenIrnRoot TranDtls = new GenIrnRoot();
//                TranDtls.setVersion("1.1");
//                TranDtls.setTranDtls(GetTranGetails(dt));
//                TranDtls.setDocDtls(GetDocumentDetails(dt));
//                TranDtls.setSellerDtls(GetSellerDetails(dt));
//                TranDtls.setBuyerDtls(GetBuyerDetails(dt));
//                TranDtls.setItemList(getitem_list(dt));
//                TranDtls.setValDtls(getvalue_details(dt));
//                customers.add(TranDtls);
            }

            String json = new Gson().toJson(customers);
            String mystr = json.substring(1, json.length() - 1);

            ClsDynamic.JsonLog(mystr, dt.getString("DOC_No").toString().replace("/", "_"));
            return msg = mystr;
        } catch (SQLException ex) {
            ClsDynamic.WriteLog(ex.getMessage(), dt.getString("DOC_No").toString().replace("/", "_"));
            return msg = "";
        }

    }


    public static TranDtls GetTranGetails(ResultSet dt) {
        try {
            TranDtls TranDtls = new TranDtls();
            TranDtls.setTaxSch(dt.getString("TAXSCH").toString().isEmpty() ? "GST" :
                               dt.getString("TAXSCH").toString());
            TranDtls.setSupTyp(dt.getString("TRAN_CATG").toString());
            TranDtls.setRegRev(dt.getString("TRAN_ECMTRN").toString());
            TranDtls.setEcmGstin(dt.getString("ecommerce_gstin").toString().isEmpty() ? null :
                                 dt.getString("ecommerce_gstin").toString());
            TranDtls.setIgstOnIntra(dt.getString("IGST_INTRA").toString());
            return TranDtls;
        } catch (Exception ex) {
            ClsDynamic.WriteLog("Error from TranDtls " + ex.getMessage(),
                                dt.getString("DOC_No").toString().replace("/", "_"));
            ClsDynamic.UpdateErrorLog("Error from TranDtls " + ex.getMessage(),
                                      dt.getString("DOC_No").toString());
            throw ex;
        }
    }

    public static DocDtls GetDocumentDetails(ResultSet dt) {
        try {
            DocDtls DocDtls = new DocDtls();
            DocDtls.setTyp(dt.getString("DOC_TYP").toString());
            DocDtls.setNo(dt.getString("DOC_NO").toString());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            DocDtls.setDt(sdf.format(dt.getString("DOC_DT")));
            return DocDtls;
        } catch (Exception ex) {
            ClsDynamic.WriteLog("Error from DocDtls " + ex.getMessage(),
                                dt.getString("DOC_No").toString().replace("/", "_"));
            ClsDynamic.UpdateErrorLog("Error from DocDtls " + ex.getMessage(),
                                      dt.getString("DOC_No").toString());
            throw ex;
        }
    }

    public static SellerDtls GetSellerDetails(ResultSet dt) {
        try {
            SellerDtls SellerDtls = new SellerDtls();
            SellerDtls.setGstin(dt.getString("BILLFROM_GSTIN").toString());
            SellerDtls.setLglNm(dt.getString("BILLFROM_TRDNM").toString());
            SellerDtls.setTrdNm(dt.getString("BILLFROM_TRDNM").toString().isEmpty() ? null :
                                dt.getString("BILLFROM_TRDNM").toString());
            SellerDtls.setAddr1(dt.getString("BILLFROM_BNO").toString().trim());
            SellerDtls.setAddr2(null);
            SellerDtls.setLoc(dt.getString("BILLFROM_LOC").toString());
            SellerDtls.setPin(Integer.parseInt(dt.getString("BILLFROM_PIN").toString().isEmpty() ? "201301" :
                                               dt.getString("BILLFROM_PIN").toString()));
            SellerDtls.setStcd(dt.getString("BILLFROM_STCD").toString().isEmpty() ? "0" :
                               dt.getString("BILLFROM_STCD").toString());
            SellerDtls.setPh(null);
            SellerDtls.setEm(null);
            return SellerDtls;
        } catch (Exception ex) {
            ClsDynamic.WriteLog("Error from SellerDtls " + ex.getMessage(),
                                dt.getString("DOC_No").toString().replace("/", "_"));
            ClsDynamic.UpdateErrorLog("Error from SellerDtls " + ex.getMessage(),
                                      dt.getString("DOC_No").toString());
            throw ex;
        }
    }

    public static BuyerDtls GetBuyerDetails(ResultSet dt) {
        try {
            BuyerDtls BuyerDtls = new BuyerDtls();
            BuyerDtls.setGstin(dt.getString("BILLTO_GSTIN").toString());
            BuyerDtls.setLglNm(dt.getString("BILLTO_TRDNM").toString());
            BuyerDtls.setTrdNm(dt.getString("BILLTO_TRDNM").toString().isEmpty() ? null :
                               dt.getString("BILLTO_TRDNM").toString());
            BuyerDtls.setPos(dt.getString("place_of_supply").toString().isEmpty() ? null :
                             dt.getString("place_of_supply").toString());
            BuyerDtls.setAddr1(dt.getString("BILLTO_BNO").toString().trim());
            BuyerDtls.setAddr2(null);
            BuyerDtls.setLoc(dt.getString("BILLTO_LOC").toString().isEmpty() ? "" :
                             dt.getString("BILLTO_LOC").toString());
            BuyerDtls.setPin(Integer.parseInt(dt.getString("BILLTO_PIN").toString().isEmpty() ? "201301" :
                                              dt.getString("BILLTO_PIN").toString()));
            BuyerDtls.setStcd(dt.getString("BILLTO_STCD").toString().isEmpty() ? null :
                              dt.getString("BILLTO_STCD").toString());
            BuyerDtls.setPh(null);
            BuyerDtls.setEm(null);
            return BuyerDtls;
        } catch (Exception ex) {
            ClsDynamic.WriteLog("Error from BuyerDtls " + ex.getMessage(),
                                dt.getString("DOC_No").toString().replace("/", "_"));
            ClsDynamic.UpdateErrorLog("Error from BuyerDtls " + ex.getMessage(),
                                      dt.getString("DOC_No").toString());
            throw ex;
        }
    }

    public static DispDtls getdispatch_details(ResultSet dt) {
        try {
            DispDtls dispatch_details = new DispDtls();
            dispatch_details.setNm(dt.getString("SHIPFROM_TRDNM").toString().isEmpty() ? "" :
                                   dt.getString("SHIPFROM_TRDNM").toString());
            dispatch_details.setAddr1(dt.getString("SHIPFROM_BNO").toString() + " " +
                                      dt.getString("SHIPFROM_BNM").toString());
            dispatch_details.setAddr2(null);
            dispatch_details.setLoc(dt.getString("SHIPFROM_LOC").toString().isEmpty() ? "" :
                                    dt.getString("SHIPFROM_LOC").toString());
            dispatch_details.setPin(Integer.parseInt(dt.getString("SHIPFROM_PIN").toString().isEmpty() ?
                                                     "201301" : dt.getString("SHIPFROM_PIN").toString()));
            dispatch_details.setStcd(dt.getString("SHIPFROM_STCD").toString().isEmpty() ? "" :
                                     dt.getString("SHIPFROM_STCD").toString());
            return dispatch_details;
        } catch (Exception ex) {
            ClsDynamic.WriteLog("Error from dispatch_details " + ex.getMessage(),
                                dt.getRows().get(0).get("DOC_No").toString().replace("/", "_"));
            ClsDynamic.UpdateErrorLog("Error from dispatch_details " + ex.getMessage(),
                                      dt.getRows().get(0).get("DOC_No").toString());
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
            ship_details.setPin(rs.getString("SHIPTO_PIN").isEmpty() ? 201301 :
                                Integer.parseInt(rs.getString("SHIPTO_PIN"))); // Mandatory
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
                item.setIsServc(rs.getString("ITEM_IS_SERVICE").isEmpty() ? "" :
                                rs.getString("ITEM_IS_SERVICE")); // Mandatory
                item.setHsnCd(rs.getString("ITEM_HSNCD").isEmpty() ? "" : rs.getString("ITEM_HSNCD")); // Mandatory
                item.setBarcde(rs.getString("ITEM_BARCDE").isEmpty() ? null : rs.getString("ITEM_BARCDE"));
                item.setQty(Double.parseDouble(rs.getString("ITEM_QTY")));
                item.setFreeQty(Integer.parseInt(rs.getString("ITEM_FREEQTY")));
                item.setUnit(rs.getString("ITEM_UNIT").isEmpty() ? "" : rs.getString("ITEM_UNIT"));
                item.setUnitPrice(Double.parseDouble(rs.getString("ITEM_UNITPRICE"))); // Mandatory
                item.setTotAmt(Double.parseDouble(rs.getString("ITEM_TOTAMT").isEmpty() ? "0" :
                                                  rs.getString("ITEM_TOTAMT"))); // Mandatory
                item.setDiscount(Double.parseDouble(rs.getString("ITEM_DISCOUNT").isEmpty() ? "0.0" :
                                                    rs.getString("ITEM_DISCOUNT")));
                item.setPreTaxVal(Double.parseDouble(rs.getString("ITEM_PRETAX_VALUE").isEmpty() ? "0.0" :
                                                     rs.getString("ITEM_PRETAX_VALUE")));
                item.setAssAmt(Double.parseDouble(rs.getString("ITEM_ASSAMT").isEmpty() ? "0" :
                                                  rs.getString("ITEM_ASSAMT"))); // Mandatory
                item.setGstRt(Double.parseDouble(rs.getString("ITEM_SGSTRT").isEmpty() ? "0" :
                                                 rs.getString("ITEM_SGSTRT"))); // Mandatory
                item.setIgstAmt(Double.parseDouble(rs.getString("ITEM_IGSTAMT").isEmpty() ? "0" :
                                                   rs.getString("ITEM_IGSTAMT")));
                item.setCgstAmt(Double.parseDouble(rs.getString("ITEM_CGSTAMT").isEmpty() ? "0" :
                                                   rs.getString("ITEM_CGSTAMT")));
                item.setSgstAmt(Double.parseDouble(rs.getString("ITEM_SGSTAMT").isEmpty() ? "0" :
                                                   rs.getString("ITEM_SGSTAMT")));
                item.setCesRt(Double.parseDouble(rs.getString("ITEM_CESRT").isEmpty() ? "0.0" :
                                                 rs.getString("ITEM_CESRT")));
                item.setCesAmt(Double.parseDouble(rs.getString("ITEM_CESAMT").isEmpty() ? "0.0" :
                                                  rs.getString("ITEM_CESAMT")));
                item.setCesNonAdvlAmt(Double.parseDouble(rs.getString("ITEM_CESNONADVAL").isEmpty() ? "0" :
                                                         rs.getString("ITEM_CESNONADVAL")));
                item.setStateCesRt(Double.parseDouble(rs.getString("ITEM_STATECES").isEmpty() ? "0.0" :
                                                      rs.getString("ITEM_STATECES")));
                item.setStateCesAmt(Double.parseDouble(rs.getString("ITEM_STATECESAMT").isEmpty() ? "0.0" :
                                                       rs.getString("ITEM_STATECESAMT")));
                item.setStateCesNonAdvlAmt(Double.parseDouble(rs.getString("ITEM_STATECESNODAMT").isEmpty() ? "0.0" :
                                                              rs.getString("ITEM_STATECESNODAMT")));
                item.setOthChrg(Double.parseDouble(rs.getString("ITEM_OTHCHRG").isEmpty() ? "0" :
                                                   rs.getString("ITEM_OTHCHRG")));
                item.setTotItemVal(Double.parseDouble(rs.getString("ITEM_TOTITEMVAL").isEmpty() ? "0" :
                                                      rs.getString("ITEM_TOTITEMVAL"))); // Mandatory
                item.setOrgCntry("IN");
                item.setOrdLineRef("1");
                item.setPrdSlNo("101");
                item.setBchDtls(Get_Batch_details(rs.getString("ITEM_BATCH_NM"), rs.getString("ITEM_BATCH_EXPDT"),
                                                  rs.getString("ITEM_BATCH_WRDT")));
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
            batch_details.setExpdt(Expirydt.isEmpty() ? null :
                                   new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(Expirydt)));
            batch_details.setWrDt(WrDate.isEmpty() ? null :
                                  new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(WrDate)));
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
            lstattrdtl.add(new AttribDtl(rs.getString("ITEM_ATTRIBUTE_DETAILS").isEmpty() ? null :
                                         rs.getString("ITEM_ATTRIBUTE_DETAILS"),
                                         // Mandatory
                                         rs.getString("ITEM_ATTRIBUTE_VALUE").isEmpty() ? null :
                                         rs.getString("ITEM_ATTRIBUTE_VALUE")));
            return lstattrdtl;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static ValDtls getvalue_details(ResultSet rs) {
        try {
            ValDtls value_details = new ValDtls();
            rs.next();
            value_details.setAssVal(Double.parseDouble(rs.getString("VAL_ASSVAL").isEmpty() ? "0" :
                                                       rs.getString("VAL_ASSVAL"))); // Required
            value_details.setCgstVal(Double.parseDouble(rs.getString("VAL_CGSTVAL").isEmpty() ? "0" :
                                                        rs.getString("VAL_CGSTVAL")));
            value_details.setSgstVal(Double.parseDouble(rs.getString("VAL_SGSTVAL").isEmpty() ? "0" :
                                                        rs.getString("VAL_SGSTVAL")));
            value_details.setIgstVal(Double.parseDouble(rs.getString("VAL_IGSTVAL").isEmpty() ? "0" :
                                                        rs.getString("VAL_IGSTVAL")));
            value_details.setCesVal(Double.parseDouble(rs.getString("VAL_CESVAL").isEmpty() ? "0" :
                                                       rs.getString("VAL_CESVAL")));
            value_details.setStCesVal(Double.parseDouble(rs.getString("VAL_STCESVAL").isEmpty() ? "0" :
                                                         rs.getString("VAL_STCESVAL")));
            value_details.setDiscount(Double.parseDouble(rs.getString("VAL_TOTDISCOUNT").isEmpty() ? "0" :
                                                         rs.getString("VAL_TOTDISCOUNT")));
            value_details.setOthChrg(Double.parseDouble(rs.getString("VAL_OTHER_CHARGE").isEmpty() ? "0" :
                                                        rs.getString("VAL_OTHER_CHARGE")));
            value_details.setTotInvVal(Double.parseDouble(rs.getString("VAL_TOTINVVAL").isEmpty() ? "0" :
                                                          rs.getString("VAL_TOTINVVAL"))); // Required
            value_details.setRndOffAmt(Double.parseDouble(rs.getString("VAL_OTHCHRG").isEmpty() ? "0" :
                                                          rs.getString("VAL_OTHCHRG")));
            value_details.setTotInvValFc(Double.parseDouble(rs.getString("VAL_TOTINV_ADDCUR").isEmpty() ? "0" :
                                                            rs.getString("VAL_TOTINV_ADDCUR")));
            return value_details;
        } catch (SQLException ex) {
            ClsDynamic.WriteLog("Error from value_details " + ex.getMessage(),
                                rs.getString("DOC_No").replace("/", "_"));
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
            payment_details.setCrday(rs.getString("PAY_CRDAY").isEmpty() ? null :
                                     Integer.parseInt(rs.getString("PAY_CRDAY")));
            payment_details.setPaidamt(rs.getString("PAY_BALAMT").isEmpty() ? null :
                                       Double.parseDouble(rs.getString("PAY_BALAMT")));
            payment_details.setPaymtdue(rs.getString("PAY_OUTSTANDINGAMT").isEmpty() ? null :
                                        Double.parseDouble(rs.getString("PAY_OUTSTANDINGAMT")));
            return payment_details;
        } catch (SQLException ex) {
            ClsDynamic.WriteLog("Error from payment_details " + ex.getMessage(),
                                rs.getString("DOC_No").replace("/", "_"));
            ClsDynamic.UpdateErrorLog("Error from payment_details " + ex.getMessage(), rs.getString("DOC_No"));
            throw new RuntimeException(ex);
        }
    }

    public static RefDtls getreference_details(ResultSet rs) {
        try {
            RefDtls reference_details = new RefDtls();
            rs.next();
            reference_details.setInvRm(rs.getString("REF_INVRMK").isEmpty() ? null : rs.getString("REF_INVRMK"));
            reference_details.setDocPerdDtls(new DocPerdDtls(rs.getString("REF_INVSTDT").isEmpty() ?
                                                             new SimpleDateFormat("dd/MM/yyyy").format(new Date()) :
                                                             new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("REF_INVSTDT"))),
                                                             // Mandatory
                                                             rs.getString("REF_INVENDDT").isEmpty() ?
                                                             new SimpleDateFormat("dd/MM/yyyy").format(new Date()) : new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("REF_INVENDDT"))) // Mandatory
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
            lstpredocdtl.add(new PrecDocDtl(rs.getString("REF_INVNO"), // Mandatory
                    rs.getString("REF_PRECINVDT").isEmpty() ? new SimpleDateFormat("dd/MM/yyyy").format(new Date()) :
                    new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("REF_PRECINVDT"))), // Mandatory
                    rs.getString("REF_EXTREF").isEmpty() ? null : rs.getString("REF_EXTREF")));
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return lstpredocdtl;
    }

    public static List<ContrDtl> Get_ContractDetail(ResultSet rs) {
        List<ContrDtl> lstcontractdtl = new ArrayList<>();
        try {
            rs.next();
            lstcontractdtl.add(new ContrDtl(rs.getString("REF_PRECINVNO").isEmpty() ? null :
                                            rs.getString("REF_PRECINVNO"),
                                            rs.getString("REF_PRECINVDT").isEmpty() ? null :
                                            new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("REF_PRECINVDT"))),
                                            rs.getString("REF_PROJREF").isEmpty() ? null : rs.getString("REF_PROJREF"),
                                            rs.getString("REF_CONTRREF").isEmpty() ? null :
                                            rs.getString("REF_CONTRREF"),
                                            rs.getString("REF_EXTREF").isEmpty() ? null : rs.getString("REF_EXTREF"),
                                            rs.getString("REF_PROJREF").isEmpty() ? null : rs.getString("REF_PROJREF"),
                                            rs.getString("REF_POREF").isEmpty() ? null : rs.getString("REF_POREF"),
                                            rs.getString("VENDOR_PO_REFDT").isEmpty() ? null :
                                            new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("VENDOR_PO_REFDT")))));
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return lstcontractdtl;
    }

    public static List<AddlDocDtl> get_additional_document(ResultSet rs) {
        List<AddlDocDtl> lstAddDocDtl = new ArrayList<>();
        try {
            rs.next();
            lstAddDocDtl.add(new AddlDocDtl(rs.getString("ADD_DOC_URL").isEmpty() ? null : rs.getString("ADD_DOC_URL"),
                                            rs.getString("ADD_SUPPORTING_DOC").isEmpty() ? null :
                                            rs.getString("ADD_SUPPORTING_DOC"),
                                            rs.getString("ADD_ADDITIONAL_INFO").isEmpty() ? null :
                                            rs.getString("ADD_ADDITIONAL_INFO")));
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
            export_details.setShipBDt(rs.getString("EXP_SHIPBDT").isEmpty() ? null :
                                      new SimpleDateFormat("dd/MM/yyyy").format(new SimpleDateFormat("yyyy-MM-dd").parse(rs.getString("EXP_SHIPBDT"))));
            export_details.setCntCode(rs.getString("EXP_CNTCODE").isEmpty() ? null : rs.getString("EXP_CNTCODE"));
            export_details.setForCur(rs.getString("EXP_FORCUR").isEmpty() ? null : rs.getString("EXP_FORCUR"));
            export_details.setRefClm(rs.getString("REFUND_CLAIM").isEmpty() ? null : rs.getString("REFUND_CLAIM"));
            export_details.setPort(rs.getString("EXP_PORT").isEmpty() ? null : rs.getString("EXP_PORT"));
            export_details.setExpDuty(rs.getString("EXP_DUTY").isEmpty() ? null : rs.getString("EXP_DUTY"));
            return export_details;
        } catch (SQLException ex) {
            ClsDynamic.WriteLog("Error from export_details " + ex.getMessage(),
                                rs.getString("DOC_No").replace("/", "_"));
            throw new RuntimeException(ex);
        }
    }

    public class TranDtls {
        private String taxSch;
        private String supTyp;
        private String regRev;

        public void setTaxSch(String taxSch) {
            this.taxSch = taxSch;
        }

        public String getTaxSch() {
            return taxSch;
        }

        public void setSupTyp(String supTyp) {
            this.supTyp = supTyp;
        }

        public String getSupTyp() {
            return supTyp;
        }

        public void setRegRev(String regRev) {
            this.regRev = regRev;
        }

        public String getRegRev() {
            return regRev;
        }

        public void setEcmGstin(Object ecmGstin) {
            this.ecmGstin = ecmGstin;
        }

        public Object getEcmGstin() {
            return ecmGstin;
        }

        public void setIgstOnIntra(String igstOnIntra) {
            this.igstOnIntra = igstOnIntra;
        }

        public String getIgstOnIntra() {
            return igstOnIntra;
        }
        private Object ecmGstin;
        private String igstOnIntra;

        // Getters and Setters
    }

    public class DocDtls {
        private String typ;

        public void setTyp(String typ) {
            this.typ = typ;
        }

        public String getTyp() {
            return typ;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getNo() {
            return no;
        }

        public void setDt(String dt) {
            this.dt = dt;
        }

        public String getDt() {
            return dt;
        }
        private String no;
        private String dt;

        // Getters and Setters
    }

    public class SellerDtls {
        private String gstin;
        private String lglNm;
        private String trdNm;
        private String addr1;

        public void setGstin(String gstin) {
            this.gstin = gstin;
        }

        public String getGstin() {
            return gstin;
        }

        public void setLglNm(String lglNm) {
            this.lglNm = lglNm;
        }

        public String getLglNm() {
            return lglNm;
        }

        public void setTrdNm(String trdNm) {
            this.trdNm = trdNm;
        }

        public String getTrdNm() {
            return trdNm;
        }

        public void setAddr1(String addr1) {
            this.addr1 = addr1;
        }

        public String getAddr1() {
            return addr1;
        }

        public void setAddr2(String addr2) {
            this.addr2 = addr2;
        }

        public String getAddr2() {
            return addr2;
        }

        public void setLoc(String loc) {
            this.loc = loc;
        }

        public String getLoc() {
            return loc;
        }

        public void setPin(int pin) {
            this.pin = pin;
        }

        public int getPin() {
            return pin;
        }

        public void setStcd(String stcd) {
            this.stcd = stcd;
        }

        public String getStcd() {
            return stcd;
        }

        public void setPh(String ph) {
            this.ph = ph;
        }

        public String getPh() {
            return ph;
        }

        public void setEm(String em) {
            this.em = em;
        }

        public String getEm() {
            return em;
        }
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

        public void setGstin(String gstin) {
            this.gstin = gstin;
        }

        public String getGstin() {
            return gstin;
        }

        public void setLglNm(String lglNm) {
            this.lglNm = lglNm;
        }

        public String getLglNm() {
            return lglNm;
        }

        public void setTrdNm(String trdNm) {
            this.trdNm = trdNm;
        }

        public String getTrdNm() {
            return trdNm;
        }

        public void setPos(String pos) {
            this.pos = pos;
        }

        public String getPos() {
            return pos;
        }

        public void setAddr1(String addr1) {
            this.addr1 = addr1;
        }

        public String getAddr1() {
            return addr1;
        }

        public void setAddr2(String addr2) {
            this.addr2 = addr2;
        }

        public String getAddr2() {
            return addr2;
        }

        public void setLoc(String loc) {
            this.loc = loc;
        }

        public String getLoc() {
            return loc;
        }

        public void setPin(int pin) {
            this.pin = pin;
        }

        public int getPin() {
            return pin;
        }

        public void setStcd(String stcd) {
            this.stcd = stcd;
        }

        public String getStcd() {
            return stcd;
        }

        public void setPh(String ph) {
            this.ph = ph;
        }

        public String getPh() {
            return ph;
        }

        public void setEm(String em) {
            this.em = em;
        }

        public String getEm() {
            return em;
        }
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

        public void setNm(String nm) {
            this.nm = nm;
        }

        public String getNm() {
            return nm;
        }

        public void setAddr1(String addr1) {
            this.addr1 = addr1;
        }

        public String getAddr1() {
            return addr1;
        }

        public void setAddr2(String addr2) {
            this.addr2 = addr2;
        }

        public String getAddr2() {
            return addr2;
        }

        public void setLoc(String loc) {
            this.loc = loc;
        }

        public String getLoc() {
            return loc;
        }

        public void setPin(int pin) {
            this.pin = pin;
        }

        public int getPin() {
            return pin;
        }

        public void setStcd(String stcd) {
            this.stcd = stcd;
        }

        public String getStcd() {
            return stcd;
        }
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

        public void setGstin(String gstin) {
            this.gstin = gstin;
        }

        public String getGstin() {
            return gstin;
        }

        public void setLglNm(String lglNm) {
            this.lglNm = lglNm;
        }

        public String getLglNm() {
            return lglNm;
        }

        public void setTrdNm(String trdNm) {
            this.trdNm = trdNm;
        }

        public String getTrdNm() {
            return trdNm;
        }

        public void setAddr1(String addr1) {
            this.addr1 = addr1;
        }

        public String getAddr1() {
            return addr1;
        }

        public void setAddr2(String addr2) {
            this.addr2 = addr2;
        }

        public String getAddr2() {
            return addr2;
        }

        public void setLoc(String loc) {
            this.loc = loc;
        }

        public String getLoc() {
            return loc;
        }

        public void setPin(int pin) {
            this.pin = pin;
        }

        public int getPin() {
            return pin;
        }

        public void setStcd(String stcd) {
            this.stcd = stcd;
        }

        public String getStcd() {
            return stcd;
        }
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

        public void setNm(String nm) {
            this.nm = nm;
        }

        public String getNm() {
            return nm;
        }

        public void setExpdt(String expdt) {
            this.expdt = expdt;
        }

        public String getExpdt() {
            return expdt;
        }

        public void setWrDt(String wrDt) {
            this.wrDt = wrDt;
        }

        public String getWrDt() {
            return wrDt;
        }
        private String wrDt;

        // Getters and Setters
    }

    public class AttribDtl {
        private String nm;
        private String val;

        public void setNm(String nm) {
            this.nm = nm;
        }

        public String getNm() {
            return nm;
        }

        public void setVal(String val) {
            this.val = val;
        }

        public String getVal() {
            return val;
        }

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

        public void setAssVal(double assVal) {
            this.assVal = assVal;
        }

        public double getAssVal() {
            return assVal;
        }

        public void setCgstVal(double cgstVal) {
            this.cgstVal = cgstVal;
        }

        public double getCgstVal() {
            return cgstVal;
        }

        public void setSgstVal(double sgstVal) {
            this.sgstVal = sgstVal;
        }

        public double getSgstVal() {
            return sgstVal;
        }

        public void setIgstVal(double igstVal) {
            this.igstVal = igstVal;
        }

        public double getIgstVal() {
            return igstVal;
        }

        public void setCesVal(double cesVal) {
            this.cesVal = cesVal;
        }

        public double getCesVal() {
            return cesVal;
        }

        public void setStCesVal(double stCesVal) {
            this.stCesVal = stCesVal;
        }

        public double getStCesVal() {
            return stCesVal;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public double getDiscount() {
            return discount;
        }

        public void setOthChrg(double othChrg) {
            this.othChrg = othChrg;
        }

        public double getOthChrg() {
            return othChrg;
        }

        public void setRndOffAmt(double rndOffAmt) {
            this.rndOffAmt = rndOffAmt;
        }

        public double getRndOffAmt() {
            return rndOffAmt;
        }

        public void setTotInvVal(double totInvVal) {
            this.totInvVal = totInvVal;
        }

        public double getTotInvVal() {
            return totInvVal;
        }

        public void setTotInvValFc(double totInvValFc) {
            this.totInvValFc = totInvValFc;
        }

        public double getTotInvValFc() {
            return totInvValFc;
        }
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

        public void setNm(String nm) {
            this.nm = nm;
        }

        public String getNm() {
            return nm;
        }

        public void setAccdet(String accdet) {
            this.accdet = accdet;
        }

        public String getAccdet() {
            return accdet;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getMode() {
            return mode;
        }

        public void setFininsbr(String fininsbr) {
            this.fininsbr = fininsbr;
        }

        public String getFininsbr() {
            return fininsbr;
        }

        public void setPayterm(String payterm) {
            this.payterm = payterm;
        }

        public String getPayterm() {
            return payterm;
        }

        public void setPayinstr(String payinstr) {
            this.payinstr = payinstr;
        }

        public String getPayinstr() {
            return payinstr;
        }

        public void setCrtrn(String crtrn) {
            this.crtrn = crtrn;
        }

        public String getCrtrn() {
            return crtrn;
        }

        public void setDirdr(String dirdr) {
            this.dirdr = dirdr;
        }

        public String getDirdr() {
            return dirdr;
        }

        public void setCrday(int crday) {
            this.crday = crday;
        }

        public int getCrday() {
            return crday;
        }

        public void setPaidamt(double paidamt) {
            this.paidamt = paidamt;
        }

        public double getPaidamt() {
            return paidamt;
        }

        public void setPaymtdue(double paymtdue) {
            this.paymtdue = paymtdue;
        }

        public double getPaymtdue() {
            return paymtdue;
        }
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
        public void setInvRm(String invRm) {
            this.invRm = invRm;
        }

        public String getInvRm() {
            return invRm;
        }

        public void setDocPerdDtls(Taxilla_GenerateEinvoice.DocPerdDtls docPerdDtls) {
            this.docPerdDtls = docPerdDtls;
        }

        public Taxilla_GenerateEinvoice.DocPerdDtls getDocPerdDtls() {
            return docPerdDtls;
        }

        public void setPrecDocDtls(List<Taxilla_GenerateEinvoice.PrecDocDtl> precDocDtls) {
            this.precDocDtls = precDocDtls;
        }

        public List<Taxilla_GenerateEinvoice.PrecDocDtl> getPrecDocDtls() {
            return precDocDtls;
        }

        public void setContrDtls(List<Taxilla_GenerateEinvoice.ContrDtl> contrDtls) {
            this.contrDtls = contrDtls;
        }

        public List<Taxilla_GenerateEinvoice.ContrDtl> getContrDtls() {
            return contrDtls;
        }
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
        public void setTendrefr(String tendrefr) {
            this.tendrefr = tendrefr;
        }

        public String getTendrefr() {
            return tendrefr;
        }
    }

    public class DocPerdDtls {
        private String invStDt;
        private String invEndDt;

        public void setInvStDt(String invStDt) {
            this.invStDt = invStDt;
        }

        public String getInvStDt() {
            return invStDt;
        }

        public void setInvEndDt(String invEndDt) {
            this.invEndDt = invEndDt;
        }

        public String getInvEndDt() {
            return invEndDt;
        }
        // Getters and Setters
    }

    public class PrecDocDtl {
        private String invNo;
        private String invDt;
        private String othRefNo;

        public void setInvNo(String invNo) {
            this.invNo = invNo;
        }

        public String getInvNo() {
            return invNo;
        }

        public void setInvDt(String invDt) {
            this.invDt = invDt;
        }

        public String getInvDt() {
            return invDt;
        }

        public void setOthRefNo(String othRefNo) {
            this.othRefNo = othRefNo;
        }

        public String getOthRefNo() {
            return othRefNo;
        }

        // Getters and Setters
    }

    public class AddlDocDtl {
        private String url;
        private String docs;
        private String info;

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setDocs(String docs) {
            this.docs = docs;
        }

        public String getDocs() {
            return docs;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }

        // Getters and Setters
    }

    public class ExpDtls {
        private String shipBNo;
        private String shipBDt;
        private String port;

        public void setShipBNo(String shipBNo) {
            this.shipBNo = shipBNo;
        }

        public String getShipBNo() {
            return shipBNo;
        }

        public void setShipBDt(String shipBDt) {
            this.shipBDt = shipBDt;
        }

        public String getShipBDt() {
            return shipBDt;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getPort() {
            return port;
        }

        public void setRefClm(String refClm) {
            this.refClm = refClm;
        }

        public String getRefClm() {
            return refClm;
        }

        public void setForCur(String forCur) {
            this.forCur = forCur;
        }

        public String getForCur() {
            return forCur;
        }

        public void setCntCode(String cntCode) {
            this.cntCode = cntCode;
        }

        public String getCntCode() {
            return cntCode;
        }

        public void setExpDuty(Object expDuty) {
            this.expDuty = expDuty;
        }

        public Object getExpDuty() {
            return expDuty;
        }
        private String refClm;
        private String forCur;
        private String cntCode;
        private Object expDuty;

        // Getters and Setters
    }

    public class EwbDtls {
        private String transid;
        private String transname;

        public void setTransid(String transid) {
            this.transid = transid;
        }

        public String getTransid() {
            return transid;
        }

        public void setTransname(String transname) {
            this.transname = transname;
        }

        public String getTransname() {
            return transname;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

        public int getDistance() {
            return distance;
        }

        public void setTransdocno(String transdocno) {
            this.transdocno = transdocno;
        }

        public String getTransdocno() {
            return transdocno;
        }

        public void setTransdocDt(String transdocDt) {
            this.transdocDt = transdocDt;
        }

        public String getTransdocDt() {
            return transdocDt;
        }

        public void setVehno(String vehno) {
            this.vehno = vehno;
        }

        public String getVehno() {
            return vehno;
        }

        public void setVehtype(String vehtype) {
            this.vehtype = vehtype;
        }

        public String getVehtype() {
            return vehtype;
        }

        public void setTransMode(String transMode) {
            this.transMode = transMode;
        }

        public String getTransMode() {
            return transMode;
        }
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

        public void setSlNo(String slNo) {
            this.slNo = slNo;
        }

        public String getSlNo() {
            return slNo;
        }

        public void setPrdDesc(String prdDesc) {
            this.prdDesc = prdDesc;
        }

        public String getPrdDesc() {
            return prdDesc;
        }

        public void setIsServc(String isServc) {
            this.isServc = isServc;
        }

        public String getIsServc() {
            return isServc;
        }

        public void setHsnCd(String hsnCd) {
            this.hsnCd = hsnCd;
        }

        public String getHsnCd() {
            return hsnCd;
        }

        public void setBarcde(String barcde) {
            this.barcde = barcde;
        }

        public String getBarcde() {
            return barcde;
        }

        public void setQty(double qty) {
            this.qty = qty;
        }

        public double getQty() {
            return qty;
        }

        public void setFreeQty(int freeQty) {
            this.freeQty = freeQty;
        }

        public int getFreeQty() {
            return freeQty;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setTotAmt(double totAmt) {
            this.totAmt = totAmt;
        }

        public double getTotAmt() {
            return totAmt;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public double getDiscount() {
            return discount;
        }

        public void setPreTaxVal(double preTaxVal) {
            this.preTaxVal = preTaxVal;
        }

        public double getPreTaxVal() {
            return preTaxVal;
        }

        public void setAssAmt(double assAmt) {
            this.assAmt = assAmt;
        }

        public double getAssAmt() {
            return assAmt;
        }

        public void setGstRt(double gstRt) {
            this.gstRt = gstRt;
        }

        public double getGstRt() {
            return gstRt;
        }

        public void setIgstAmt(double igstAmt) {
            this.igstAmt = igstAmt;
        }

        public double getIgstAmt() {
            return igstAmt;
        }

        public void setCgstAmt(double cgstAmt) {
            this.cgstAmt = cgstAmt;
        }

        public double getCgstAmt() {
            return cgstAmt;
        }

        public void setSgstAmt(double sgstAmt) {
            this.sgstAmt = sgstAmt;
        }

        public double getSgstAmt() {
            return sgstAmt;
        }

        public void setCesRt(double cesRt) {
            this.cesRt = cesRt;
        }

        public double getCesRt() {
            return cesRt;
        }

        public void setCesAmt(double cesAmt) {
            this.cesAmt = cesAmt;
        }

        public double getCesAmt() {
            return cesAmt;
        }

        public void setCesNonAdvlAmt(double cesNonAdvlAmt) {
            this.cesNonAdvlAmt = cesNonAdvlAmt;
        }

        public double getCesNonAdvlAmt() {
            return cesNonAdvlAmt;
        }

        public void setStateCesRt(double stateCesRt) {
            this.stateCesRt = stateCesRt;
        }

        public double getStateCesRt() {
            return stateCesRt;
        }

        public void setStateCesAmt(double stateCesAmt) {
            this.stateCesAmt = stateCesAmt;
        }

        public double getStateCesAmt() {
            return stateCesAmt;
        }

        public void setStateCesNonAdvlAmt(double stateCesNonAdvlAmt) {
            this.stateCesNonAdvlAmt = stateCesNonAdvlAmt;
        }

        public double getStateCesNonAdvlAmt() {
            return stateCesNonAdvlAmt;
        }

        public void setOthChrg(double othChrg) {
            this.othChrg = othChrg;
        }

        public double getOthChrg() {
            return othChrg;
        }

        public void setTotItemVal(double totItemVal) {
            this.totItemVal = totItemVal;
        }

        public double getTotItemVal() {
            return totItemVal;
        }

        public void setOrdLineRef(String ordLineRef) {
            this.ordLineRef = ordLineRef;
        }

        public String getOrdLineRef() {
            return ordLineRef;
        }

        public void setOrgCntry(String orgCntry) {
            this.orgCntry = orgCntry;
        }

        public String getOrgCntry() {
            return orgCntry;
        }

        public void setPrdSlNo(String prdSlNo) {
            this.prdSlNo = prdSlNo;
        }

        public String getPrdSlNo() {
            return prdSlNo;
        }

        public void setBchDtls(Taxilla_GenerateEinvoice.BchDtls bchDtls) {
            this.bchDtls = bchDtls;
        }

        public Taxilla_GenerateEinvoice.BchDtls getBchDtls() {
            return bchDtls;
        }

        public void setAttribDtls(List<Taxilla_GenerateEinvoice.AttribDtl> attribDtls) {
            this.attribDtls = attribDtls;
        }

        public List<Taxilla_GenerateEinvoice.AttribDtl> getAttribDtls() {
            return attribDtls;
        }
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

        public void setVersion(String version) {
            this.version = version;
        }

        public String getVersion() {
            return version;
        }

        public void setTranDtls(Taxilla_GenerateEinvoice.TranDtls tranDtls) {
            this.tranDtls = tranDtls;
        }

        public Taxilla_GenerateEinvoice.TranDtls getTranDtls() {
            return tranDtls;
        }

        public void setDocDtls(Taxilla_GenerateEinvoice.DocDtls docDtls) {
            this.docDtls = docDtls;
        }

        public Taxilla_GenerateEinvoice.DocDtls getDocDtls() {
            return docDtls;
        }

        public void setSellerDtls(Taxilla_GenerateEinvoice.SellerDtls sellerDtls) {
            this.sellerDtls = sellerDtls;
        }

        public Taxilla_GenerateEinvoice.SellerDtls getSellerDtls() {
            return sellerDtls;
        }

        public void setBuyerDtls(Taxilla_GenerateEinvoice.BuyerDtls buyerDtls) {
            this.buyerDtls = buyerDtls;
        }

        public Taxilla_GenerateEinvoice.BuyerDtls getBuyerDtls() {
            return buyerDtls;
        }

        public void setDispDtls(Taxilla_GenerateEinvoice.DispDtls dispDtls) {
            this.dispDtls = dispDtls;
        }

        public Taxilla_GenerateEinvoice.DispDtls getDispDtls() {
            return dispDtls;
        }

        public void setShipDtls(Taxilla_GenerateEinvoice.ShipDtls shipDtls) {
            this.shipDtls = shipDtls;
        }

        public Taxilla_GenerateEinvoice.ShipDtls getShipDtls() {
            return shipDtls;
        }

        public void setItemList(List<Taxilla_GenerateEinvoice.ItemList> itemList) {
            this.itemList = itemList;
        }

        public List<Taxilla_GenerateEinvoice.ItemList> getItemList() {
            return itemList;
        }

        public void setValDtls(Taxilla_GenerateEinvoice.ValDtls valDtls) {
            this.valDtls = valDtls;
        }

        public Taxilla_GenerateEinvoice.ValDtls getValDtls() {
            return valDtls;
        }

        public void setPayDtls(Taxilla_GenerateEinvoice.PayDtls payDtls) {
            this.payDtls = payDtls;
        }

        public Taxilla_GenerateEinvoice.PayDtls getPayDtls() {
            return payDtls;
        }

        public void setRefDtls(Taxilla_GenerateEinvoice.RefDtls refDtls) {
            this.refDtls = refDtls;
        }

        public Taxilla_GenerateEinvoice.RefDtls getRefDtls() {
            return refDtls;
        }

        public void setAddlDocDtls(List<Taxilla_GenerateEinvoice.AddlDocDtl> addlDocDtls) {
            this.addlDocDtls = addlDocDtls;
        }

        public List<Taxilla_GenerateEinvoice.AddlDocDtl> getAddlDocDtls() {
            return addlDocDtls;
        }

        public void setExpDtls(Taxilla_GenerateEinvoice.ExpDtls expDtls) {
            this.expDtls = expDtls;
        }

        public Taxilla_GenerateEinvoice.ExpDtls getExpDtls() {
            return expDtls;
        }

        public void setEwbDtls(Taxilla_GenerateEinvoice.EwbDtls ewbDtls) {
            this.ewbDtls = ewbDtls;
        }

        public Taxilla_GenerateEinvoice.EwbDtls getEwbDtls() {
            return ewbDtls;
        }
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

        public void setAckNo(long ackNo) {
            this.ackNo = ackNo;
        }

        public long getAckNo() {
            return ackNo;
        }

        public void setAckDt(String ackDt) {
            this.ackDt = ackDt;
        }

        public String getAckDt() {
            return ackDt;
        }

        public void setIrn(String irn) {
            this.irn = irn;
        }

        public String getIrn() {
            return irn;
        }

        public void setSignedInvoice(String signedInvoice) {
            this.signedInvoice = signedInvoice;
        }

        public String getSignedInvoice() {
            return signedInvoice;
        }

        public void setSignedQRCode(String signedQRCode) {
            this.signedQRCode = signedQRCode;
        }

        public String getSignedQRCode() {
            return signedQRCode;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setEwbNo(String ewbNo) {
            this.ewbNo = ewbNo;
        }

        public String getEwbNo() {
            return ewbNo;
        }

        public void setEwbDt(String ewbDt) {
            this.ewbDt = ewbDt;
        }

        public String getEwbDt() {
            return ewbDt;
        }

        public void setEwbValidTill(String ewbValidTill) {
            this.ewbValidTill = ewbValidTill;
        }

        public String getEwbValidTill() {
            return ewbValidTill;
        }

        public void setRemarks(Object remarks) {
            this.remarks = remarks;
        }

        public Object getRemarks() {
            return remarks;
        }
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
        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

    public class SuccessInfo {
        private String infCd;

        public void setInfCd(String infCd) {
            this.infCd = infCd;
        }

        public String getInfCd() {
            return infCd;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }
        private String desc;

        // Getters and Setters
    }

    public class SuccessInfo2 {
        private String infCd;
        private List<Desc> desc;

        // Getters and Setters
        public void setInfCd(String infCd) {
            this.infCd = infCd;
        }

        public String getInfCd() {
            return infCd;
        }

        public void setDesc(List<Taxilla_GenerateEinvoice.Desc> desc) {
            this.desc = desc;
        }

        public List<Taxilla_GenerateEinvoice.Desc> getDesc() {
            return desc;
        }
    }

    public class SuccessRoot {
        private boolean success;
        private String message;
        private SuccessResult result;
        private List<SuccessInfo> info;

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

        public SuccessResult getResult() {
            return result;
        }

        public void setResult(SuccessResult result) {
            this.result = result;
        }

        public List<SuccessInfo> getInfo() {
            return info;
        }

        public void setInfo(List<SuccessInfo> info) {
            this.info = info;
        }
    }

    public class SuccessRoot2 {
        private boolean success;
        private String message;
        private SuccessResult result;

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setResult(Taxilla_GenerateEinvoice.SuccessResult result) {
            this.result = result;
        }

        public Taxilla_GenerateEinvoice.SuccessResult getResult() {
            return result;
        }

        public void setInfo(List<Taxilla_GenerateEinvoice.SuccessInfo2> info) {
            this.info = info;
        }

        public List<Taxilla_GenerateEinvoice.SuccessInfo2> getInfo() {
            return info;
        }
        private List<SuccessInfo2> info;

        // Getters and Setters
    }

    public class DublicateDesc {
        private long ackNo;
        private String ackDt;
        private String irn;

        public void setAckNo(long ackNo) {
            this.ackNo = ackNo;
        }

        public long getAckNo() {
            return ackNo;
        }

        public void setAckDt(String ackDt) {
            this.ackDt = ackDt;
        }

        public String getAckDt() {
            return ackDt;
        }

        public void setIrn(String irn) {
            this.irn = irn;
        }

        public String getIrn() {
            return irn;
        }

        // Getters and Setters
    }

    public class DublicateResult {
        private String infCd;

        public void setInfCd(String infCd) {
            this.infCd = infCd;
        }

        public String getInfCd() {
            return infCd;
        }

        public void setDesc(Taxilla_GenerateEinvoice.DublicateDesc desc) {
            this.desc = desc;
        }

        public Taxilla_GenerateEinvoice.DublicateDesc getDesc() {
            return desc;
        }
        private DublicateDesc desc;

        // Getters and Setters
    }

    public class DublicateRoot {
        private boolean success;

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setResult(List<Taxilla_GenerateEinvoice.DublicateResult> result) {
            this.result = result;
        }

        public List<Taxilla_GenerateEinvoice.DublicateResult> getResult() {
            return result;
        }
        private String message;
        private List<DublicateResult> result;

        // Getters and Setters
    }


}
