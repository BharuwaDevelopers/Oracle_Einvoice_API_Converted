package bspl.einvoice.eiew.ViewModels;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GenerateEinvoice {
    
    public static String generateJsonFile(ResultSet dt, String access_token) {
           String msg = "";
           try {
               List<RootObject> customers = new ArrayList<>();

               if (dt.getString("EINV_WITH_EWB").equals("Y")) {
                   RootObject tranDtls = new RootObject();
                   tranDtls.setAccess_token(access_token);
                   tranDtls.setUser_gstin(dt.getString("GSTIN"));
                   tranDtls.setData_source("erp");
                   tranDtls.setTransaction_details(getTransactionDetails(dt));
                   tranDtls.setDocument_details(getDocumentDetails(dt));
                   tranDtls.setSeller_details(getSellerDetails(dt));
                   tranDtls.setBuyer_details(getBuyerDetails(dt));
                   tranDtls.setDispatch_details(getDispatchDetails(dt));
                   tranDtls.setShip_details(getShipDetails(dt));
                   tranDtls.setExport_details(getExportDetails(dt));
                   tranDtls.setPayment_details(getPaymentDetails(dt));
                   tranDtls.setReference_details(getReferenceDetails(dt));
                   tranDtls.setAdditional_document_details(getAdditionalDocumentDetails(dt));
                   tranDtls.setEwaybill_details(getEwaybillDetails(dt));
                   tranDtls.setValue_details(getValueDetails(dt));
                   tranDtls.setItem_list(getItemList(dt));

                   customers.add(tranDtls);
               } else {
                   RootObject tranDtls = new RootObject();
                   tranDtls.setAccess_token(access_token);
                   tranDtls.setUser_gstin(dt.getString("GSTIN"));
                   tranDtls.setData_source("erp");
                   tranDtls.setTransaction_details(getTransactionDetails(dt));
                   tranDtls.setDocument_details(getDocumentDetails(dt));
                   tranDtls.setSeller_details(getSellerDetails(dt));
                   tranDtls.setBuyer_details(getBuyerDetails(dt));
                   tranDtls.setValue_details(getValueDetails(dt));
                   tranDtls.setItem_list(getItemList(dt));

                   customers.add(tranDtls);
               }

               Gson gson = new Gson();
               String json = gson.toJson(customers);
               String mystr = json.substring(1, json.length() - 1);
               msg = mystr;
               return msg;
           } catch (Exception ex) {
               return msg = "0";
           }
       }

       public static CompletableFuture<Object> generateInvoice(String jsonFile, String sekdec, ResultSet dt) {
           CompletableFuture<Object> invResDecData = new CompletableFuture<>();
           HttpClient client = HttpClient.newHttpClient();
           String url = All_API_Urls.EinvGenerateUrl;

           HttpRequest request = HttpRequest.newBuilder()
                   .uri(URI.create(url))
                   .header("Content-Type", "application/json")
                   .POST(HttpRequest.BodyPublishers.ofString(jsonFile))
                   .build();

           client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                   .thenApply(response -> {
                       if (response.statusCode() == 200) {
                           String responseBody = response.body();
                           JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
                           INVRootObject value2 = new Gson().fromJson(jsonObject, INVRootObject.class);

                           if (value2.getResults().getCode() == 200) {
                        OracleDataInsert.updateDataOracle(value2, dt);
                               invResDecData.complete("1");
                           } else {
                        OracleDataInsert.updateDataOracle(value2, dt);
                               invResDecData.complete(response);
                           }
                       } else {
                           invResDecData.complete(response);
                       }
                       return invResDecData;
                   })
                   .exceptionally(ex -> {
                       String sqlstr = "update einvoice_generate_temp set ERRORMSG='" + ex.getMessage() + "' where id='" + dt.getString("ID") + "' and DOC_NO='" + dt.getString("DOC_NO") + "'";
                       DataLayer.executeNonQuery(OraDBConnection.OrclConnection, sqlstr);
                       invResDecData.complete(ex.getMessage());
                       return invResDecData;
                   });

           return invResDecData;
       }

       public static TransactionDetails getTransactionDetails(ResultSet dt) throws SQLException {
           TransactionDetails transactionDetails = new TransactionDetails();
           transactionDetails.setSupply_type(dt.getString("TRAN_CATG"));
           transactionDetails.setCharge_type(dt.getString("TRAN_ECMTRN"));
           transactionDetails.setIgst_on_intra(dt.getString("IGST_INTRA"));
           transactionDetails.setEcommerce_gstin(dt.getString("ecommerce_gstin"));
           return transactionDetails;
       }

       public static DocumentDetails getDocumentDetails(ResultSet dt) throws SQLException {
           DocumentDetails documentDetails = new DocumentDetails();
           documentDetails.setDocument_type(dt.getString("DOC_TYP"));
           documentDetails.setDocument_number(dt.getString("DOC_NO"));
           SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
           documentDetails.setDocument_date(sdf.format(dt.getDate("DOC_DT")));
           return documentDetails;
       }

       public static SellerDetails getSellerDetails(ResultSet dt) throws SQLException {
           SellerDetails sellerDetails = new SellerDetails();
           sellerDetails.setGstin(dt.getString("BILLFROM_GSTIN"));
           sellerDetails.setLegal_name(dt.getString("BILLFROM_TRDNM"));
           sellerDetails.setTrade_name(dt.getString("BILLFROM_TRDNM"));
           sellerDetails.setAddress1(dt.getString("BILLFROM_BNO") + " " + dt.getString("BILLFROM_BNM") + " " + dt.getString("BILLFROM_FLNO") + " " + dt.getString("BILLFROM_DST"));
           sellerDetails.setAddress2(dt.getString("BILLFROM_BNO") + " " + dt.getString("BILLFROM_BNM") + " " + dt.getString("BILLFROM_FLNO") + " " + dt.getString("BILLFROM_DST"));
           sellerDetails.setLocation(dt.getString("BILLFROM_LOC"));
           sellerDetails.setPincode(Integer.parseInt(dt.getString("BILLFROM_PIN")));
           sellerDetails.setState_code(dt.getString("BILLFROM_STCD"));
           sellerDetails.setPhone_number(dt.getString("BILLFROM_PH"));
           sellerDetails.setEmail(dt.getString("BILLFROM_EM"));
           return sellerDetails;
       }

       public static BuyerDetails getBuyerDetails(ResultSet dt) throws SQLException {
           BuyerDetails buyerDetails = new BuyerDetails();
           buyerDetails.setGstin(dt.getString("BILLTO_GSTIN"));
           buyerDetails.setLegal_name(dt.getString("BILLTO_TRDNM"));
           buyerDetails.setTrade_name(dt.getString("BILLTO_TRDNM"));
           buyerDetails.setAddress1(dt.getString("BILLTO_BNO") + " " + dt.getString("BILLTO_BNM") + " " + dt.getString("BILLTO_FLNO") + " " + dt.getString("BILLTO_DST"));
           buyerDetails.setAddress2(dt.getString("BILLTO_BNO") + " " + dt.getString("BILLTO_BNM") + " " + dt.getString("BILLTO_FLNO") + " " + dt.getString("BILLTO_DST"));
           buyerDetails.setLocation(dt.getString("BILLTO_LOC"));
           buyerDetails.setPincode(Integer.parseInt(dt.getString("BILLTO_PIN")));
           buyerDetails.setPlace_of_supply(dt.getString("place_of_supply"));
           buyerDetails.setState_code(dt.getString("BILLTO_STCD"));
           buyerDetails.setPhone_number(dt.getString("BILLTO_PH"));
           buyerDetails.setEmail(dt.getString("BILLTO_EM"));
           return buyerDetails;
       }

       public static DispatchDetails getDispatchDetails(ResultSet dt) throws SQLException {
           DispatchDetails dispatchDetails = new DispatchDetails();
           dispatchDetails.setCompany_name(dt.getString("SHIPFROM_TRDNM"));
           dispatchDetails.setAddress1(dt.getString("SHIPFROM_BNO") + " " + dt.getString("SHIPFROM_BNM") + " " + dt.getString("SHIPFROM_FLNO") + " " + dt.getString("SHIPFROM_DST"));
           dispatchDetails.setAddress2(dt.getString("SHIPFROM_BNO") + " " + dt.getString("SHIPFROM_BNM") + " " + dt.getString("SHIPFROM_FLNO") + " " + dt.getString("SHIPFROM_DST"));
           dispatchDetails.setLocation(dt.getString("SHIPFROM_LOC"));
           dispatchDetails.setPincode(Integer.parseInt(dt.getString("SHIPFROM_PIN")));
           dispatchDetails.setState_code(dt.getString("SHIPFROM_STCD"));
           return dispatchDetails;
       }

       public static ShipDetails getShipDetails(ResultSet dt) throws SQLException {
           ShipDetails shipDetails = new ShipDetails();
           shipDetails.setGstin(dt.getString("SHIPTO_GSTIN"));
           shipDetails.setLegal_name(dt.getString("SHIPTO_TRDNM"));
           shipDetails.setTrade_name(dt.getString("SHIPTO_TRDNM"));
           shipDetails.setAddress1(dt.getString("SHIPTO_BNO") + " " + dt.getString("SHIPTO_BNM") + " " + dt.getString("SHIPTO_FLNO") + " " + dt.getString("SHIPTO_DST"));
           shipDetails.setAddress2(dt.getString("SHIPTO_BNO") + " " + dt.getString("SHIPTO_BNM") + " " + dt.getString("SHIPTO_FLNO") + " " + dt.getString("SHIPTO_DST"));
           shipDetails.setLocation(dt.getString("SHIPTO_LOC"));
           shipDetails.setPincode(Integer.parseInt(dt.getString("SHIPTO_PIN")));
           shipDetails.setState_code(dt.getString("SHIPTO_STCD"));
           return shipDetails;
       }

       public static ExportDetails getExportDetails(ResultSet dt) throws SQLException {
           ExportDetails exportDetails = new ExportDetails();
           exportDetails.setShip_bill_number(dt.getString("EXP_SHIPBNO"));
           SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
           exportDetails.setShip_bill_date(sdf.format(dt.getDate("EXP_SHIPBDT")));
           exportDetails.setCountry_code(dt.getString("EXP_CNTCODE"));
           exportDetails.setForeign_currency(dt.getString("EXP_FORCUR"));
           exportDetails.setRefund_claim(dt.getString("REFUND_CLAIM"));
           exportDetails.setPort_code(dt.getString("EXP_PORT"));
           exportDetails.setExport_duty(dt.getString("EXP_DUTY"));
           return exportDetails;
       }

       public static PaymentDetails getPaymentDetails(ResultSet dt) throws SQLException {
           PaymentDetails paymentDetails = new PaymentDetails();
           paymentDetails.setBank_account_number(dt.getString("PAY_ACCTDET"));
           paymentDetails.setPaid_balance_amount(Double.parseDouble(dt.getString("PAY_BALAMT")));
           paymentDetails.setCredit_days(Double.parseDouble(dt.getString("PAY_CRDAY")));
           paymentDetails.setCredit_transfer(dt.getString("PAY_CRTRN"));
           paymentDetails.setDirect_debit(dt.getString("PAY_DIRDR"));
           paymentDetails.setBranch_or_ifsc(dt.getString("PAY_FININSBR"));
           paymentDetails.setPayment_mode(dt.getString("PAY_MODE"));
           paymentDetails.setPayee_name(dt.getString("PAY_NAM"));
           paymentDetails.setPayment_instruction(dt.getString("PAY_PAYINSTR"));
           paymentDetails.setPayment_term(dt.getString("PAY_PAYTERM"));
           paymentDetails.setOutstanding_amount(Double.parseDouble(dt.getString("PAY_OUTSTANDINGAMT")));
           return paymentDetails;
       }

       public static ReferenceDetails getReferenceDetails(ResultSet dt) throws SQLException {
           ReferenceDetails referenceDetails = new ReferenceDetails();
           referenceDetails.setInvoice_remarks(dt.getString("REF_INVRMK"));
           DocumentPeriodDetails documentPeriodDetails = new DocumentPeriodDetails();
           SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
           documentPeriodDetails.setInvoice_period_start_date(sdf.format(dt.getDate("REF_INVSTDT")));
           documentPeriodDetails.setInvoice_period_end_date(sdf.format(dt.getDate("REF_INVENDDT")));
           referenceDetails.setDocument_period_details(documentPeriodDetails);
           referenceDetails.setPreceding_document_details(getPrecedingDocumentDetails(dt));
           referenceDetails.setContract_details(getContractDetails(dt));
           return referenceDetails;
       }

       public static List<PrecedingDocumentDetail> getPrecedingDocumentDetails(ResultSet dt) throws SQLException {
           List<PrecedingDocumentDetail> lstpredocdtl = new ArrayList<>();
           while (dt.next()) {
               PrecedingDocumentDetail precedingDocumentDetail = new PrecedingDocumentDetail();
               precedingDocumentDetail.setReference_of_original_invoice(dt.getString("REF_INVNO"));
               SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
               precedingDocumentDetail.setPreceding_invoice_date(sdf.format(dt.getDate("REF_PRECINVDT")));
               precedingDocumentDetail.setOther_reference(dt.getString("REF_EXTREF"));
               lstpredocdtl.add(precedingDocumentDetail);
           }
           return lstpredocdtl;
       }
       
    public static List<ContractDetail> getContractDetail(DataTable dt) {
          List<ContractDetail> lstcontractdtl = new ArrayList<>();
          for (int i = 0; i < dt.getRows().size(); i++) {
              lstcontractdtl.add(new ContractDetail(
                  dt.getRows().get(i).get("REF_PRECINVNO").toString().isEmpty() ? "" : dt.getRows().get(i).get("REF_PRECINVNO").toString(),
                  dt.getRows().get(i).get("REF_PRECINVDT").toString().isEmpty() ? new SimpleDateFormat("dd/MM/yyyy").format(new Date()) : new SimpleDateFormat("dd/MM/yyyy").format(dt.getRows().get(i).get("REF_PRECINVDT")),
                  dt.getRows().get(i).get("REF_PROJREF").toString().isEmpty() ? "" : dt.getRows().get(i).get("REF_PROJREF").toString(),
                  dt.getRows().get(i).get("REF_CONTRREF").toString().isEmpty() ? "" : dt.getRows().get(i).get("REF_CONTRREF").toString(),
                  dt.getRows().get(i).get("REF_EXTREF").toString().isEmpty() ? "" : dt.getRows().get(i).get("REF_EXTREF").toString(),
                  dt.getRows().get(i).get("REF_PROJREF").toString().isEmpty() ? "" : dt.getRows().get(i).get("REF_PROJREF").toString(),
                  dt.getRows().get(i).get("REF_POREF").toString().isEmpty() ? "" : dt.getRows().get(i).get("REF_POREF").toString(),
                  dt.getRows().get(i).get("VENDOR_PO_REFDT").toString().isEmpty() ? new SimpleDateFormat("dd/MM/yyyy").format(new Date()) : new SimpleDateFormat("dd/MM/yyyy").format(dt.getRows().get(i).get("VENDOR_PO_REFDT"))
              ));
          }
          return lstcontractdtl;
      }

      public static ValueDetails getValueDetails(DataTable dt) {
          ValueDetails referenceDetails = new ValueDetails(
              Double.parseDouble(dt.getRows().get(0).get("VAL_ASSVAL").toString().isEmpty() ? "0" : dt.getRows().get(0).get("VAL_ASSVAL").toString()),
              Double.parseDouble(dt.getRows().get(0).get("VAL_CGSTVAL").toString().isEmpty() ? "0" : dt.getRows().get(0).get("VAL_CGSTVAL").toString()),
              Double.parseDouble(dt.getRows().get(0).get("VAL_SGSTVAL").toString().isEmpty() ? "0" : dt.getRows().get(0).get("VAL_SGSTVAL").toString()),
              Double.parseDouble(dt.getRows().get(0).get("VAL_IGSTVAL").toString().isEmpty() ? "0" : dt.getRows().get(0).get("VAL_IGSTVAL").toString()),
              Double.parseDouble(dt.getRows().get(0).get("VAL_CESVAL").toString().isEmpty() ? "0" : dt.getRows().get(0).get("VAL_CESVAL").toString()),
              Double.parseDouble(dt.getRows().get(0).get("VAL_STCESVAL").toString().isEmpty() ? "0" : dt.getRows().get(0).get("VAL_STCESVAL").toString()),
              Double.parseDouble(dt.getRows().get(0).get("VAL_TOTDISCOUNT").toString().isEmpty() ? "0" : dt.getRows().get(0).get("VAL_TOTDISCOUNT").toString()),
              Double.parseDouble(dt.getRows().get(0).get("VAL_OTHER_CHARGE").toString().isEmpty() ? "0" : dt.getRows().get(0).get("VAL_OTHER_CHARGE").toString()),
              Double.parseDouble(dt.getRows().get(0).get("VAL_TOTINVVAL").toString().isEmpty() ? "0" : dt.getRows().get(0).get("VAL_TOTINVVAL").toString()),
              Double.parseDouble(dt.getRows().get(0).get("VAL_ROUNDOFF_AMT").toString().isEmpty() ? "0" : dt.getRows().get(0).get("VAL_ROUNDOFF_AMT").toString()),
              Double.parseDouble(dt.getRows().get(0).get("VAL_TOTINV_ADDCUR").toString().isEmpty() ? "0" : dt.getRows().get(0).get("VAL_TOTINV_ADDCUR").toString())
          );
          return referenceDetails;
      }

      public static List<ItemList> getItemList(DataTable dt) {
          List<ItemList> itm = new ArrayList<>();
          for (int i = 0; i < dt.getRows().size(); i++) {
              itm.add(new ItemList(
                  String.valueOf(i + 1),
                  dt.getRows().get(i).get("ITEM_PRDDESC").toString(),
                  dt.getRows().get(i).get("ITEM_IS_SERVICE").toString().isEmpty() ? "" : dt.getRows().get(i).get("ITEM_IS_SERVICE").toString(),
                  dt.getRows().get(i).get("ITEM_HSNCD").toString().isEmpty() ? "" : dt.getRows().get(i).get("ITEM_HSNCD").toString(),
                  dt.getRows().get(i).get("ITEM_BARCDE").toString().isEmpty() ? "" : dt.getRows().get(i).get("ITEM_BARCDE").toString(),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_QTY").toString()),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_FREEQTY").toString()),
                  dt.getRows().get(i).get("ITEM_UNIT").toString().isEmpty() ? "" : dt.getRows().get(i).get("ITEM_UNIT").toString(),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_UNITPRICE").toString()),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_TOTAMT").toString().isEmpty() ? "0" : dt.getRows().get(i).get("ITEM_TOTAMT").toString()),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_PRETAX_VALUE").toString().isEmpty() ? "0" : dt.getRows().get(i).get("ITEM_PRETAX_VALUE").toString()),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_DISCOUNT").toString().isEmpty() ? "0" : dt.getRows().get(i).get("ITEM_DISCOUNT").toString()),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_OTHCHRG").toString().isEmpty() ? "0" : dt.getRows().get(i).get("ITEM_OTHCHRG").toString()),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_ASSAMT").toString().isEmpty() ? "0" : dt.getRows().get(i).get("ITEM_ASSAMT").toString()),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_SGSTRT").toString().isEmpty() ? "0" : dt.getRows().get(i).get("ITEM_SGSTRT").toString()),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_IGSTAMT").toString().isEmpty() ? "0" : dt.getRows().get(i).get("ITEM_IGSTAMT").toString()),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_CGSTAMT").toString().isEmpty() ? "0" : dt.getRows().get(i).get("ITEM_CGSTAMT").toString()),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_SGSTAMT").toString().isEmpty() ? "0" : dt.getRows().get(i).get("ITEM_SGSTAMT").toString()),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_CESRT").toString().isEmpty() ? "0.0" : dt.getRows().get(i).get("ITEM_CESRT").toString()),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_CESAMT").toString().isEmpty() ? "0.0" : dt.getRows().get(i).get("ITEM_CESAMT").toString()),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_CESNONADVAL").toString().isEmpty() ? "0" : dt.getRows().get(i).get("ITEM_CESNONADVAL").toString()),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_STATECES").toString().isEmpty() ? "0.0" : dt.getRows().get(i).get("ITEM_STATECES").toString()),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_STATECESAMT").toString().isEmpty() ? "0.0" : dt.getRows().get(i).get("ITEM_STATECESAMT").toString()),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_STATECESNODAMT").toString().isEmpty() ? "0.0" : dt.getRows().get(i).get("ITEM_STATECESNODAMT").toString()),
                  Double.parseDouble(dt.getRows().get(i).get("ITEM_TOTITEMVAL").toString().isEmpty() ? "0" : dt.getRows().get(i).get("ITEM_TOTITEMVAL").toString()),
                  "IN",
                  "1",
                  "101",
                  new BatchDetails(
                      dt.getRows().get(i).get("ITEM_BATCH_NM").toString().isEmpty() ? "Testing" : dt.getRows().get(i).get("ITEM_BATCH_NM").toString(),
                      dt.getRows().get(0).get("ITEM_BATCH_EXPDT").toString().isEmpty() ? new SimpleDateFormat("dd/MM/yyyy").format(new Date()) : new SimpleDateFormat("dd/MM/yyyy").format(dt.getRows().get(0).get("ITEM_BATCH_EXPDT")),
                      dt.getRows().get(0).get("ITEM_BATCH_WRDT").toString().isEmpty() ? new SimpleDateFormat("dd/MM/yyyy").format(new Date()) : new SimpleDateFormat("dd/MM/yyyy").format(dt.getRows().get(0).get("ITEM_BATCH_WRDT"))
                  ),
                  getAttributeDetails(dt)
              ));
          }
          return itm;
      }

      public static List<AttributeDetail> getAttributeDetails(DataTable dt) {
          List<AttributeDetail> lstattrdtl = new ArrayList<>();
          lstattrdtl.add(new AttributeDetail(
              dt.getRows().get(0).get("ITEM_ATTRIBUTE_DETAILS").toString().isEmpty() ? "Testing" : dt.getRows().get(0).get("ITEM_ATTRIBUTE_DETAILS").toString(),
              dt.getRows().get(0).get("ITEM_ATTRIBUTE_VALUE").toString().isEmpty() ? "1001" : dt.getRows().get(0).get("ITEM_ATTRIBUTE_VALUE").toString()
          ));
          return lstattrdtl;
      }

      public static List<AdditionalDocumentDetail> getAdditionalDocument(DataTable dt) {
          List<AdditionalDocumentDetail> lstAddDocDtl = new ArrayList<>();
          for (int i = 0; i < dt.getRows().size(); i++) {
              lstAddDocDtl.add(new AdditionalDocumentDetail(
                  dt.getRows().get(i).get("ADD_DOC_URL").toString(),
                  dt.getRows().get(i).get("ADD_SUPPORTING_DOC").toString(),
                  dt.getRows().get(i).get("ADD_ADDITIONAL_INFO").toString()
              ));
          }
          return lstAddDocDtl;
      }

      public static EwaybillDetails getEwaybillDetails(DataTable dt) {
          EwaybillDetails ewaybillDetails = new EwaybillDetails(
              dt.getRows().get(0).get("EWAY_TRANSPORTAR_ID").toString(),
              dt.getRows().get(0).get("EWAY_TRANSPORTAR_NAME").toString(),
              dt.getRows().get(0).get("EWAY_TRANSPORTAR_MODE").toString(),
              dt.getRows().get(0).get("EWAY_TRANSPORTAR_DISTANCE").toString(),
              dt.getRows().get(0).get("EWAY_TRANSPORTAR_DOCNO").toString(),
              dt.getRows().get(0).get("EWAY_TRANSPORTAR_DOCDT").toString(),
              dt.getRows().get(0).get("EWAY_TRANSPORTAR_VEHINO").toString(),
              dt.getRows().get(0).get("EWAY_TRANSPORTAR_VEHITYPE").toString()
          );
          return ewaybillDetails;
      }
      
    public class TransactionDetails {
        public String supply_type;
        public String charge_type;
        public String igst_on_intra;
        public String ecommerce_gstin;
    }

    public class DocumentDetails {
        public String document_type;
        public String document_number;
        public String document_date;
    }

    public class SellerDetails {
        public String gstin;
        public String legal_name;
        public String trade_name;
        public String address1;
        public String address2;
        public String location;
        public int pincode;
        public String state_code;
        public String phone_number;
        public String email;
    }

    public class BuyerDetails {
        public String gstin;
        public String legal_name;
        public String trade_name;
        public String address1;
        public String address2;
        public String location;
        public int pincode;
        public String place_of_supply;
        public String state_code;
        public String phone_number;
        public String email;
    }

    public class DispatchDetails {
        public String company_name;
        public String address1;
        public String address2;
        public String location;
        public int pincode;
        public String state_code;
    }

    public class ShipDetails {
        public String gstin;
        public String legal_name;
        public String trade_name;
        public String address1;
        public String address2;
        public String location;
        public int pincode;
        public String state_code;
    }

    public class ExportDetails {
        public String ship_bill_number;
        public String ship_bill_date;
        public String country_code;
        public String foreign_currency;
        public String refund_claim;
        public String port_code;
        public String export_duty;
    }

    public class PaymentDetails {
        public String bank_account_number;
        public double paid_balance_amount;
        public double credit_days;
        public String credit_transfer;
        public String direct_debit;
        public String branch_or_ifsc;
        public String payment_mode;
        public String payee_name;
        public String payment_instruction;
        public String payment_term;
        public double outstanding_amount;
    }

    public class DocumentPeriodDetails {
        public String invoice_period_start_date;
        public String invoice_period_end_date;
    }

    public class PrecedingDocumentDetail {
        public String reference_of_original_invoice;
        public String preceding_invoice_date;
        public String other_reference;
    }

    public class ContractDetail {
        public String receipt_advice_number;
        public String receipt_advice_date;
        public String batch_reference_number;
        public String contract_reference_number;
        public String other_reference;
        public String project_reference_number;
        public String vendor_po_reference_number;
        public String vendor_po_reference_date;
    }

    public class ReferenceDetails {
        public String invoice_remarks;
        public DocumentPeriodDetails document_period_details;
        public List<PrecedingDocumentDetail> preceding_document_details;
        public List<ContractDetail> contract_details;
    }

    public class AdditionalDocumentDetail {
        public String supporting_document_url;
        public String supporting_document;
        public String additional_information;
    }

    public class ValueDetails {
        public double total_assessable_value;
        public double total_cgst_value;
        public double total_sgst_value;
        public double total_igst_value;
        public double total_cess_value;
        public double total_cess_value_of_state;
        public double total_discount;
        public double total_other_charge;
        public double total_invoice_value;
        public double round_off_amount;
        public double total_invoice_value_additional_currency;
    }

    public class EwaybillDetails {
        public String transporter_id;
        public String transporter_name;
        public String transportation_mode;
        public String transportation_distance;
        public String transporter_document_number;
        public String transporter_document_date;
        public String vehicle_number;
        public String vehicle_type;
    }

    public class BatchDetails {
        public String name;
        public String expiry_date;
        public String warranty_date;
    }

    public class AttributeDetail {
        public String item_attribute_details;
        public String item_attribute_value;
    }

    public class ItemList {
        public String item_serial_number;
        public String product_description;
        public String is_service;
        public String hsn_code;
        public String bar_code;
        public double quantity;
        public double free_quantity;
        public String unit;
        public double unit_price;
        public double total_amount;
        public double pre_tax_value;
        public double discount;
        public double other_charge;
        public double assessable_value;
        public double gst_rate;
        public double igst_amount;
        public double cgst_amount;
        public double sgst_amount;
        public double cess_rate;
        public double cess_amount;
        public double cess_nonadvol_amount;
        public double state_cess_rate;
        public double state_cess_amount;
        public double state_cess_nonadvol_amount;
        public double total_item_value;
        public String country_origin;
        public String order_line_reference;
        public String product_serial_number;
        public BatchDetails batch_details;
        public List<AttributeDetail> attribute_details;
    }

    public class RootObject {
        public String access_token;
        public String user_gstin;
        public String data_source;
        public TransactionDetails transaction_details;
        public DocumentDetails document_details;
        public SellerDetails seller_details;
        public BuyerDetails buyer_details;
        public DispatchDetails dispatch_details;
        public ShipDetails ship_details;
        public ExportDetails export_details;
        public PaymentDetails payment_details;
        public ReferenceDetails reference_details;
        public List<AdditionalDocumentDetail> additional_document_details;
        public EwaybillDetails ewaybill_details;
        public ValueDetails value_details;
        public List<ItemList> item_list;
    }

    public class Message {
        public long AckNo;
        public String AckDt;
        public String Irn;
        public String SignedInvoice;
        public String SignedQRCode;
        public String EwbNo;
        public String EwbDt;
        public String EwbValidTill;
        public String QRCodeUrl;
        public String EinvoicePdf;
        public String Status;
        public String alert;
        public boolean error;
    }

    public class Results {
        public Message message;
        public String errorMessage;
        public String InfoDtls;
        public String status;
        public int code;
        public String requestId;
    }

    public class INVRootObject {
        public Results results;
    }
    
       
       
}
