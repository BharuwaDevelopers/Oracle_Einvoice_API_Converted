package bspl.einvoice.eiew.ViewModels;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URI;
//import java.net.http.HttpClient;
//import java.net.http.HttpRequest;
//import java.net.http.HttpResponse;
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
    
    public String generateJsonFile(ResultSet dt, String access_token) {
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
                       DataLayer.executeNonQuery(OraDBConnection.OrclConnection(), sqlstr);
                       invResDecData.complete(ex.getMessage());
                       return invResDecData;
                   });

           return invResDecData;
       }

       public TransactionDetails getTransactionDetails(ResultSet dt) throws SQLException {
           TransactionDetails transactionDetails = new TransactionDetails();
           transactionDetails.setSupply_type(dt.getString("TRAN_CATG"));
           transactionDetails.setCharge_type(dt.getString("TRAN_ECMTRN"));
           transactionDetails.setIgst_on_intra(dt.getString("IGST_INTRA"));
           transactionDetails.setEcommerce_gstin(dt.getString("ecommerce_gstin"));
           return transactionDetails;
       }

       public DocumentDetails getDocumentDetails(ResultSet dt) throws SQLException {
           DocumentDetails documentDetails = new DocumentDetails();
           documentDetails.setDocument_type(dt.getString("DOC_TYP"));
           documentDetails.setDocument_number(dt.getString("DOC_NO"));
           SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
           documentDetails.setDocument_date(sdf.format(dt.getDate("DOC_DT")));
           return documentDetails;
       }

       public SellerDetails getSellerDetails(ResultSet dt) throws SQLException {
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

       public BuyerDetails getBuyerDetails(ResultSet dt) throws SQLException {
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

       public DispatchDetails getDispatchDetails(ResultSet dt) throws SQLException {
           DispatchDetails dispatchDetails = new DispatchDetails();
           dispatchDetails.setCompany_name(dt.getString("SHIPFROM_TRDNM"));
           dispatchDetails.setAddress1(dt.getString("SHIPFROM_BNO") + " " + dt.getString("SHIPFROM_BNM") + " " + dt.getString("SHIPFROM_FLNO") + " " + dt.getString("SHIPFROM_DST"));
           dispatchDetails.setAddress2(dt.getString("SHIPFROM_BNO") + " " + dt.getString("SHIPFROM_BNM") + " " + dt.getString("SHIPFROM_FLNO") + " " + dt.getString("SHIPFROM_DST"));
           dispatchDetails.setLocation(dt.getString("SHIPFROM_LOC"));
           dispatchDetails.setPincode(Integer.parseInt(dt.getString("SHIPFROM_PIN")));
           dispatchDetails.setState_code(dt.getString("SHIPFROM_STCD"));
           return dispatchDetails;
       }

       public ShipDetails getShipDetails(ResultSet dt) throws SQLException {
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

       public ExportDetails getExportDetails(ResultSet dt) throws SQLException {
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

       public PaymentDetails getPaymentDetails(ResultSet dt) throws SQLException {
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

       public ReferenceDetails getReferenceDetails(ResultSet dt) throws SQLException {
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

       public List<PrecedingDocumentDetail> getPrecedingDocumentDetails(ResultSet dt) throws SQLException {
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
       
    public static List<ContractDetail> getContractDetail(ResultSet dt) {
          List<ContractDetail> lstcontractdtl = new ArrayList<>();
          try{
          for (int i = 0; i < dt.getRow(); i++) {
              lstcontractdtl.add(new ContractDetail(
                  dt.getString("REF_PRECINVNO").toString().isEmpty() ? "" : dt.getString("REF_PRECINVNO").toString(),
                  dt.getString("REF_PRECINVDT").toString().isEmpty() ? new SimpleDateFormat("dd/MM/yyyy").format(new Date()) : new SimpleDateFormat("dd/MM/yyyy").format(dt.getRows().get(i).get("REF_PRECINVDT")),
                  dt.getString("REF_PROJREF").toString().isEmpty() ? "" : dt.getString("REF_PROJREF").toString(),
                  dt.getString("REF_CONTRREF").toString().isEmpty() ? "" : dt.getString("REF_CONTRREF").toString(),
                  dt.getString("REF_EXTREF").toString().isEmpty() ? "" : dt.getString("REF_EXTREF").toString(),
                  dt.getString("REF_PROJREF").toString().isEmpty() ? "" : dt.getString("REF_PROJREF").toString(),
                  dt.getString("REF_POREF").toString().isEmpty() ? "" : dt.getString("REF_POREF").toString(),
                  dt.getString("VENDOR_PO_REFDT").toString().isEmpty() ? new SimpleDateFormat("dd/MM/yyyy").format(new Date()) : new SimpleDateFormat("dd/MM/yyyy").format(dt.getRows().get(i).get("VENDOR_PO_REFDT"))
              ));
          }
          }
          catch(Exception ex)
          {}
          return lstcontractdtl;
      }

      public static ValueDetails getValueDetails(ResultSet dt) {
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

      public static List<ItemList> getItemList(ResultSet dt) throws SQLException {
          List<ItemList> itm = new ArrayList<>();
          for (int i = 0; i < dt.getRow(); i++) {
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

      public static List<AttributeDetail> getAttributeDetails(ResultSet dt) {
          List<AttributeDetail> lstattrdtl = new ArrayList<>();
          lstattrdtl.add(new AttributeDetail(
              dt.getRows().get(0).get("ITEM_ATTRIBUTE_DETAILS").toString().isEmpty() ? "Testing" : dt.getRows().get(0).get("ITEM_ATTRIBUTE_DETAILS").toString(),
              dt.getRows().get(0).get("ITEM_ATTRIBUTE_VALUE").toString().isEmpty() ? "1001" : dt.getRows().get(0).get("ITEM_ATTRIBUTE_VALUE").toString()
          ));
          return lstattrdtl;
      }

      public static List<AdditionalDocumentDetail> getAdditionalDocument(ResultSet dt) {
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

      public static EwaybillDetails getEwaybillDetails(ResultSet dt) {
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

        public void setSupply_type(String supply_type) {
            this.supply_type = supply_type;
        }

        public String getSupply_type() {
            return supply_type;
        }

        public void setCharge_type(String charge_type) {
            this.charge_type = charge_type;
        }

        public String getCharge_type() {
            return charge_type;
        }

        public void setIgst_on_intra(String igst_on_intra) {
            this.igst_on_intra = igst_on_intra;
        }

        public String getIgst_on_intra() {
            return igst_on_intra;
        }

        public void setEcommerce_gstin(String ecommerce_gstin) {
            this.ecommerce_gstin = ecommerce_gstin;
        }

        public String getEcommerce_gstin() {
            return ecommerce_gstin;
        }
    }

    public class DocumentDetails {
        public String document_type;
        public String document_number;

        public void setDocument_type(String document_type) {
            this.document_type = document_type;
        }

        public String getDocument_type() {
            return document_type;
        }

        public void setDocument_number(String document_number) {
            this.document_number = document_number;
        }

        public String getDocument_number() {
            return document_number;
        }

        public void setDocument_date(String document_date) {
            this.document_date = document_date;
        }

        public String getDocument_date() {
            return document_date;
        }
        public String document_date;
    }

    public class SellerDetails {
        public String gstin;
        public String legal_name;
        public String trade_name;
        public String address1;

        public void setGstin(String gstin) {
            this.gstin = gstin;
        }

        public String getGstin() {
            return gstin;
        }

        public void setLegal_name(String legal_name) {
            this.legal_name = legal_name;
        }

        public String getLegal_name() {
            return legal_name;
        }

        public void setTrade_name(String trade_name) {
            this.trade_name = trade_name;
        }

        public String getTrade_name() {
            return trade_name;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getAddress2() {
            return address2;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLocation() {
            return location;
        }

        public void setPincode(int pincode) {
            this.pincode = pincode;
        }

        public int getPincode() {
            return pincode;
        }

        public void setState_code(String state_code) {
            this.state_code = state_code;
        }

        public String getState_code() {
            return state_code;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
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

        public void setGstin(String gstin) {
            this.gstin = gstin;
        }

        public String getGstin() {
            return gstin;
        }

        public void setLegal_name(String legal_name) {
            this.legal_name = legal_name;
        }

        public String getLegal_name() {
            return legal_name;
        }

        public void setTrade_name(String trade_name) {
            this.trade_name = trade_name;
        }

        public String getTrade_name() {
            return trade_name;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getAddress2() {
            return address2;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLocation() {
            return location;
        }

        public void setPincode(int pincode) {
            this.pincode = pincode;
        }

        public int getPincode() {
            return pincode;
        }

        public void setPlace_of_supply(String place_of_supply) {
            this.place_of_supply = place_of_supply;
        }

        public String getPlace_of_supply() {
            return place_of_supply;
        }

        public void setState_code(String state_code) {
            this.state_code = state_code;
        }

        public String getState_code() {
            return state_code;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
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

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getAddress2() {
            return address2;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLocation() {
            return location;
        }

        public void setPincode(int pincode) {
            this.pincode = pincode;
        }

        public int getPincode() {
            return pincode;
        }

        public void setState_code(String state_code) {
            this.state_code = state_code;
        }

        public String getState_code() {
            return state_code;
        }
        public String location;
        public int pincode;
        public String state_code;
    }

    public class ShipDetails {
        public String gstin;
        public String legal_name;
        public String trade_name;
        public String address1;

        public void setGstin(String gstin) {
            this.gstin = gstin;
        }

        public String getGstin() {
            return gstin;
        }

        public void setLegal_name(String legal_name) {
            this.legal_name = legal_name;
        }

        public String getLegal_name() {
            return legal_name;
        }

        public void setTrade_name(String trade_name) {
            this.trade_name = trade_name;
        }

        public String getTrade_name() {
            return trade_name;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getAddress2() {
            return address2;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLocation() {
            return location;
        }

        public void setPincode(int pincode) {
            this.pincode = pincode;
        }

        public int getPincode() {
            return pincode;
        }

        public void setState_code(String state_code) {
            this.state_code = state_code;
        }

        public String getState_code() {
            return state_code;
        }
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

        public void setShip_bill_number(String ship_bill_number) {
            this.ship_bill_number = ship_bill_number;
        }

        public String getShip_bill_number() {
            return ship_bill_number;
        }

        public void setShip_bill_date(String ship_bill_date) {
            this.ship_bill_date = ship_bill_date;
        }

        public String getShip_bill_date() {
            return ship_bill_date;
        }

        public void setCountry_code(String country_code) {
            this.country_code = country_code;
        }

        public String getCountry_code() {
            return country_code;
        }

        public void setForeign_currency(String foreign_currency) {
            this.foreign_currency = foreign_currency;
        }

        public String getForeign_currency() {
            return foreign_currency;
        }

        public void setRefund_claim(String refund_claim) {
            this.refund_claim = refund_claim;
        }

        public String getRefund_claim() {
            return refund_claim;
        }

        public void setPort_code(String port_code) {
            this.port_code = port_code;
        }

        public String getPort_code() {
            return port_code;
        }

        public void setExport_duty(String export_duty) {
            this.export_duty = export_duty;
        }

        public String getExport_duty() {
            return export_duty;
        }
        public String refund_claim;
        public String port_code;
        public String export_duty;
    }

    public class PaymentDetails {
        public String bank_account_number;
        public double paid_balance_amount;
        public double credit_days;
        public String credit_transfer;

        public void setBank_account_number(String bank_account_number) {
            this.bank_account_number = bank_account_number;
        }

        public String getBank_account_number() {
            return bank_account_number;
        }

        public void setPaid_balance_amount(double paid_balance_amount) {
            this.paid_balance_amount = paid_balance_amount;
        }

        public double getPaid_balance_amount() {
            return paid_balance_amount;
        }

        public void setCredit_days(double credit_days) {
            this.credit_days = credit_days;
        }

        public double getCredit_days() {
            return credit_days;
        }

        public void setCredit_transfer(String credit_transfer) {
            this.credit_transfer = credit_transfer;
        }

        public String getCredit_transfer() {
            return credit_transfer;
        }

        public void setDirect_debit(String direct_debit) {
            this.direct_debit = direct_debit;
        }

        public String getDirect_debit() {
            return direct_debit;
        }

        public void setBranch_or_ifsc(String branch_or_ifsc) {
            this.branch_or_ifsc = branch_or_ifsc;
        }

        public String getBranch_or_ifsc() {
            return branch_or_ifsc;
        }

        public void setPayment_mode(String payment_mode) {
            this.payment_mode = payment_mode;
        }

        public String getPayment_mode() {
            return payment_mode;
        }

        public void setPayee_name(String payee_name) {
            this.payee_name = payee_name;
        }

        public String getPayee_name() {
            return payee_name;
        }

        public void setPayment_instruction(String payment_instruction) {
            this.payment_instruction = payment_instruction;
        }

        public String getPayment_instruction() {
            return payment_instruction;
        }

        public void setPayment_term(String payment_term) {
            this.payment_term = payment_term;
        }

        public String getPayment_term() {
            return payment_term;
        }

        public void setOutstanding_amount(double outstanding_amount) {
            this.outstanding_amount = outstanding_amount;
        }

        public double getOutstanding_amount() {
            return outstanding_amount;
        }
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

        public void setInvoice_period_start_date(String invoice_period_start_date) {
            this.invoice_period_start_date = invoice_period_start_date;
        }

        public String getInvoice_period_start_date() {
            return invoice_period_start_date;
        }

        public void setInvoice_period_end_date(String invoice_period_end_date) {
            this.invoice_period_end_date = invoice_period_end_date;
        }

        public String getInvoice_period_end_date() {
            return invoice_period_end_date;
        }
    }

    public class PrecedingDocumentDetail {
        public String reference_of_original_invoice;
        public String preceding_invoice_date;

        public void setReference_of_original_invoice(String reference_of_original_invoice) {
            this.reference_of_original_invoice = reference_of_original_invoice;
        }

        public String getReference_of_original_invoice() {
            return reference_of_original_invoice;
        }

        public void setPreceding_invoice_date(String preceding_invoice_date) {
            this.preceding_invoice_date = preceding_invoice_date;
        }

        public String getPreceding_invoice_date() {
            return preceding_invoice_date;
        }

        public void setOther_reference(String other_reference) {
            this.other_reference = other_reference;
        }

        public String getOther_reference() {
            return other_reference;
        }
        public String other_reference;
    }

    public class ContractDetail {
        public String receipt_advice_number;
        public String receipt_advice_date;
        public String batch_reference_number;
        public String contract_reference_number;
        public String other_reference;

        public void setReceipt_advice_number(String receipt_advice_number) {
            this.receipt_advice_number = receipt_advice_number;
        }

        public String getReceipt_advice_number() {
            return receipt_advice_number;
        }

        public void setReceipt_advice_date(String receipt_advice_date) {
            this.receipt_advice_date = receipt_advice_date;
        }

        public String getReceipt_advice_date() {
            return receipt_advice_date;
        }

        public void setBatch_reference_number(String batch_reference_number) {
            this.batch_reference_number = batch_reference_number;
        }

        public String getBatch_reference_number() {
            return batch_reference_number;
        }

        public void setContract_reference_number(String contract_reference_number) {
            this.contract_reference_number = contract_reference_number;
        }

        public String getContract_reference_number() {
            return contract_reference_number;
        }

        public void setOther_reference(String other_reference) {
            this.other_reference = other_reference;
        }

        public String getOther_reference() {
            return other_reference;
        }

        public void setProject_reference_number(String project_reference_number) {
            this.project_reference_number = project_reference_number;
        }

        public String getProject_reference_number() {
            return project_reference_number;
        }

        public void setVendor_po_reference_number(String vendor_po_reference_number) {
            this.vendor_po_reference_number = vendor_po_reference_number;
        }

        public String getVendor_po_reference_number() {
            return vendor_po_reference_number;
        }

        public void setVendor_po_reference_date(String vendor_po_reference_date) {
            this.vendor_po_reference_date = vendor_po_reference_date;
        }

        public String getVendor_po_reference_date() {
            return vendor_po_reference_date;
        }
        public String project_reference_number;
        public String vendor_po_reference_number;
        public String vendor_po_reference_date;
    }

    public class ReferenceDetails {
        public String invoice_remarks;
        public DocumentPeriodDetails document_period_details;
        public List<PrecedingDocumentDetail> preceding_document_details;

        public void setInvoice_remarks(String invoice_remarks) {
            this.invoice_remarks = invoice_remarks;
        }

        public String getInvoice_remarks() {
            return invoice_remarks;
        }

        public void setDocument_period_details(GenerateEinvoice.DocumentPeriodDetails document_period_details) {
            this.document_period_details = document_period_details;
        }

        public GenerateEinvoice.DocumentPeriodDetails getDocument_period_details() {
            return document_period_details;
        }

        public void setPreceding_document_details(List<GenerateEinvoice.PrecedingDocumentDetail> preceding_document_details) {
            this.preceding_document_details = preceding_document_details;
        }

        public List<GenerateEinvoice.PrecedingDocumentDetail> getPreceding_document_details() {
            return preceding_document_details;
        }

        public void setContract_details(List<GenerateEinvoice.ContractDetail> contract_details) {
            this.contract_details = contract_details;
        }

        public List<GenerateEinvoice.ContractDetail> getContract_details() {
            return contract_details;
        }
        public List<ContractDetail> contract_details;
    }

    public class AdditionalDocumentDetail {
        public String supporting_document_url;
        public String supporting_document;
        public String additional_information;

        public void setSupporting_document_url(String supporting_document_url) {
            this.supporting_document_url = supporting_document_url;
        }

        public String getSupporting_document_url() {
            return supporting_document_url;
        }

        public void setSupporting_document(String supporting_document) {
            this.supporting_document = supporting_document;
        }

        public String getSupporting_document() {
            return supporting_document;
        }

        public void setAdditional_information(String additional_information) {
            this.additional_information = additional_information;
        }

        public String getAdditional_information() {
            return additional_information;
        }
    }

    public class ValueDetails {
        public double total_assessable_value;
        public double total_cgst_value;
        public double total_sgst_value;

        public void setTotal_assessable_value(double total_assessable_value) {
            this.total_assessable_value = total_assessable_value;
        }

        public double getTotal_assessable_value() {
            return total_assessable_value;
        }

        public void setTotal_cgst_value(double total_cgst_value) {
            this.total_cgst_value = total_cgst_value;
        }

        public double getTotal_cgst_value() {
            return total_cgst_value;
        }

        public void setTotal_sgst_value(double total_sgst_value) {
            this.total_sgst_value = total_sgst_value;
        }

        public double getTotal_sgst_value() {
            return total_sgst_value;
        }

        public void setTotal_igst_value(double total_igst_value) {
            this.total_igst_value = total_igst_value;
        }

        public double getTotal_igst_value() {
            return total_igst_value;
        }

        public void setTotal_cess_value(double total_cess_value) {
            this.total_cess_value = total_cess_value;
        }

        public double getTotal_cess_value() {
            return total_cess_value;
        }

        public void setTotal_cess_value_of_state(double total_cess_value_of_state) {
            this.total_cess_value_of_state = total_cess_value_of_state;
        }

        public double getTotal_cess_value_of_state() {
            return total_cess_value_of_state;
        }

        public void setTotal_discount(double total_discount) {
            this.total_discount = total_discount;
        }

        public double getTotal_discount() {
            return total_discount;
        }

        public void setTotal_other_charge(double total_other_charge) {
            this.total_other_charge = total_other_charge;
        }

        public double getTotal_other_charge() {
            return total_other_charge;
        }

        public void setTotal_invoice_value(double total_invoice_value) {
            this.total_invoice_value = total_invoice_value;
        }

        public double getTotal_invoice_value() {
            return total_invoice_value;
        }

        public void setRound_off_amount(double round_off_amount) {
            this.round_off_amount = round_off_amount;
        }

        public double getRound_off_amount() {
            return round_off_amount;
        }

        public void setTotal_invoice_value_additional_currency(double total_invoice_value_additional_currency) {
            this.total_invoice_value_additional_currency = total_invoice_value_additional_currency;
        }

        public double getTotal_invoice_value_additional_currency() {
            return total_invoice_value_additional_currency;
        }
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

        public void setTransporter_id(String transporter_id) {
            this.transporter_id = transporter_id;
        }

        public String getTransporter_id() {
            return transporter_id;
        }

        public void setTransporter_name(String transporter_name) {
            this.transporter_name = transporter_name;
        }

        public String getTransporter_name() {
            return transporter_name;
        }

        public void setTransportation_mode(String transportation_mode) {
            this.transportation_mode = transportation_mode;
        }

        public String getTransportation_mode() {
            return transportation_mode;
        }

        public void setTransportation_distance(String transportation_distance) {
            this.transportation_distance = transportation_distance;
        }

        public String getTransportation_distance() {
            return transportation_distance;
        }

        public void setTransporter_document_number(String transporter_document_number) {
            this.transporter_document_number = transporter_document_number;
        }

        public String getTransporter_document_number() {
            return transporter_document_number;
        }

        public void setTransporter_document_date(String transporter_document_date) {
            this.transporter_document_date = transporter_document_date;
        }

        public String getTransporter_document_date() {
            return transporter_document_date;
        }

        public void setVehicle_number(String vehicle_number) {
            this.vehicle_number = vehicle_number;
        }

        public String getVehicle_number() {
            return vehicle_number;
        }

        public void setVehicle_type(String vehicle_type) {
            this.vehicle_type = vehicle_type;
        }

        public String getVehicle_type() {
            return vehicle_type;
        }
        public String transporter_document_number;
        public String transporter_document_date;
        public String vehicle_number;
        public String vehicle_type;
    }

    public class BatchDetails {
        public String name;
        public String expiry_date;
        public String warranty_date;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setExpiry_date(String expiry_date) {
            this.expiry_date = expiry_date;
        }

        public String getExpiry_date() {
            return expiry_date;
        }

        public void setWarranty_date(String warranty_date) {
            this.warranty_date = warranty_date;
        }

        public String getWarranty_date() {
            return warranty_date;
        }
    }

    public class AttributeDetail {
        public String item_attribute_details;
        public String item_attribute_value;

        public void setItem_attribute_details(String item_attribute_details) {
            this.item_attribute_details = item_attribute_details;
        }

        public String getItem_attribute_details() {
            return item_attribute_details;
        }

        public void setItem_attribute_value(String item_attribute_value) {
            this.item_attribute_value = item_attribute_value;
        }

        public String getItem_attribute_value() {
            return item_attribute_value;
        }
    }

    public class ItemList {
        public String item_serial_number;
        public String product_description;
        public String is_service;
        public String hsn_code;
        public String bar_code;

        public void setItem_serial_number(String item_serial_number) {
            this.item_serial_number = item_serial_number;
        }

        public String getItem_serial_number() {
            return item_serial_number;
        }

        public void setProduct_description(String product_description) {
            this.product_description = product_description;
        }

        public String getProduct_description() {
            return product_description;
        }

        public void setIs_service(String is_service) {
            this.is_service = is_service;
        }

        public String getIs_service() {
            return is_service;
        }

        public void setHsn_code(String hsn_code) {
            this.hsn_code = hsn_code;
        }

        public String getHsn_code() {
            return hsn_code;
        }

        public void setBar_code(String bar_code) {
            this.bar_code = bar_code;
        }

        public String getBar_code() {
            return bar_code;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        public double getQuantity() {
            return quantity;
        }

        public void setFree_quantity(double free_quantity) {
            this.free_quantity = free_quantity;
        }

        public double getFree_quantity() {
            return free_quantity;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit_price(double unit_price) {
            this.unit_price = unit_price;
        }

        public double getUnit_price() {
            return unit_price;
        }

        public void setTotal_amount(double total_amount) {
            this.total_amount = total_amount;
        }

        public double getTotal_amount() {
            return total_amount;
        }

        public void setPre_tax_value(double pre_tax_value) {
            this.pre_tax_value = pre_tax_value;
        }

        public double getPre_tax_value() {
            return pre_tax_value;
        }

        public void setDiscount(double discount) {
            this.discount = discount;
        }

        public double getDiscount() {
            return discount;
        }

        public void setOther_charge(double other_charge) {
            this.other_charge = other_charge;
        }

        public double getOther_charge() {
            return other_charge;
        }

        public void setAssessable_value(double assessable_value) {
            this.assessable_value = assessable_value;
        }

        public double getAssessable_value() {
            return assessable_value;
        }

        public void setGst_rate(double gst_rate) {
            this.gst_rate = gst_rate;
        }

        public double getGst_rate() {
            return gst_rate;
        }

        public void setIgst_amount(double igst_amount) {
            this.igst_amount = igst_amount;
        }

        public double getIgst_amount() {
            return igst_amount;
        }

        public void setCgst_amount(double cgst_amount) {
            this.cgst_amount = cgst_amount;
        }

        public double getCgst_amount() {
            return cgst_amount;
        }

        public void setSgst_amount(double sgst_amount) {
            this.sgst_amount = sgst_amount;
        }

        public double getSgst_amount() {
            return sgst_amount;
        }

        public void setCess_rate(double cess_rate) {
            this.cess_rate = cess_rate;
        }

        public double getCess_rate() {
            return cess_rate;
        }

        public void setCess_amount(double cess_amount) {
            this.cess_amount = cess_amount;
        }

        public double getCess_amount() {
            return cess_amount;
        }

        public void setCess_nonadvol_amount(double cess_nonadvol_amount) {
            this.cess_nonadvol_amount = cess_nonadvol_amount;
        }

        public double getCess_nonadvol_amount() {
            return cess_nonadvol_amount;
        }

        public void setState_cess_rate(double state_cess_rate) {
            this.state_cess_rate = state_cess_rate;
        }

        public double getState_cess_rate() {
            return state_cess_rate;
        }

        public void setState_cess_amount(double state_cess_amount) {
            this.state_cess_amount = state_cess_amount;
        }

        public double getState_cess_amount() {
            return state_cess_amount;
        }

        public void setState_cess_nonadvol_amount(double state_cess_nonadvol_amount) {
            this.state_cess_nonadvol_amount = state_cess_nonadvol_amount;
        }

        public double getState_cess_nonadvol_amount() {
            return state_cess_nonadvol_amount;
        }

        public void setTotal_item_value(double total_item_value) {
            this.total_item_value = total_item_value;
        }

        public double getTotal_item_value() {
            return total_item_value;
        }

        public void setCountry_origin(String country_origin) {
            this.country_origin = country_origin;
        }

        public String getCountry_origin() {
            return country_origin;
        }

        public void setOrder_line_reference(String order_line_reference) {
            this.order_line_reference = order_line_reference;
        }

        public String getOrder_line_reference() {
            return order_line_reference;
        }

        public void setProduct_serial_number(String product_serial_number) {
            this.product_serial_number = product_serial_number;
        }

        public String getProduct_serial_number() {
            return product_serial_number;
        }

        public void setBatch_details(GenerateEinvoice.BatchDetails batch_details) {
            this.batch_details = batch_details;
        }

        public GenerateEinvoice.BatchDetails getBatch_details() {
            return batch_details;
        }

        public void setAttribute_details(List<GenerateEinvoice.AttributeDetail> attribute_details) {
            this.attribute_details = attribute_details;
        }

        public List<GenerateEinvoice.AttributeDetail> getAttribute_details() {
            return attribute_details;
        }
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

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setUser_gstin(String user_gstin) {
            this.user_gstin = user_gstin;
        }

        public String getUser_gstin() {
            return user_gstin;
        }

        public void setData_source(String data_source) {
            this.data_source = data_source;
        }

        public String getData_source() {
            return data_source;
        }

        public void setTransaction_details(GenerateEinvoice.TransactionDetails transaction_details) {
            this.transaction_details = transaction_details;
        }

        public GenerateEinvoice.TransactionDetails getTransaction_details() {
            return transaction_details;
        }

        public void setDocument_details(GenerateEinvoice.DocumentDetails document_details) {
            this.document_details = document_details;
        }

        public GenerateEinvoice.DocumentDetails getDocument_details() {
            return document_details;
        }

        public void setSeller_details(GenerateEinvoice.SellerDetails seller_details) {
            this.seller_details = seller_details;
        }

        public GenerateEinvoice.SellerDetails getSeller_details() {
            return seller_details;
        }

        public void setBuyer_details(GenerateEinvoice.BuyerDetails buyer_details) {
            this.buyer_details = buyer_details;
        }

        public GenerateEinvoice.BuyerDetails getBuyer_details() {
            return buyer_details;
        }

        public void setDispatch_details(GenerateEinvoice.DispatchDetails dispatch_details) {
            this.dispatch_details = dispatch_details;
        }

        public GenerateEinvoice.DispatchDetails getDispatch_details() {
            return dispatch_details;
        }

        public void setShip_details(GenerateEinvoice.ShipDetails ship_details) {
            this.ship_details = ship_details;
        }

        public GenerateEinvoice.ShipDetails getShip_details() {
            return ship_details;
        }

        public void setExport_details(GenerateEinvoice.ExportDetails export_details) {
            this.export_details = export_details;
        }

        public GenerateEinvoice.ExportDetails getExport_details() {
            return export_details;
        }

        public void setPayment_details(GenerateEinvoice.PaymentDetails payment_details) {
            this.payment_details = payment_details;
        }

        public GenerateEinvoice.PaymentDetails getPayment_details() {
            return payment_details;
        }

        public void setReference_details(GenerateEinvoice.ReferenceDetails reference_details) {
            this.reference_details = reference_details;
        }

        public GenerateEinvoice.ReferenceDetails getReference_details() {
            return reference_details;
        }

        public void setAdditional_document_details(List<GenerateEinvoice.AdditionalDocumentDetail> additional_document_details) {
            this.additional_document_details = additional_document_details;
        }

        public List<GenerateEinvoice.AdditionalDocumentDetail> getAdditional_document_details() {
            return additional_document_details;
        }

        public void setEwaybill_details(GenerateEinvoice.EwaybillDetails ewaybill_details) {
            this.ewaybill_details = ewaybill_details;
        }

        public GenerateEinvoice.EwaybillDetails getEwaybill_details() {
            return ewaybill_details;
        }

        public void setValue_details(GenerateEinvoice.ValueDetails value_details) {
            this.value_details = value_details;
        }

        public GenerateEinvoice.ValueDetails getValue_details() {
            return value_details;
        }

        public void setItem_list(List<GenerateEinvoice.ItemList> item_list) {
            this.item_list = item_list;
        }

        public List<GenerateEinvoice.ItemList> getItem_list() {
            return item_list;
        }
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

        public void setAckNo(long AckNo) {
            this.AckNo = AckNo;
        }

        public long getAckNo() {
            return AckNo;
        }

        public void setAckDt(String AckDt) {
            this.AckDt = AckDt;
        }

        public String getAckDt() {
            return AckDt;
        }

        public void setIrn(String Irn) {
            this.Irn = Irn;
        }

        public String getIrn() {
            return Irn;
        }

        public void setSignedInvoice(String SignedInvoice) {
            this.SignedInvoice = SignedInvoice;
        }

        public String getSignedInvoice() {
            return SignedInvoice;
        }

        public void setSignedQRCode(String SignedQRCode) {
            this.SignedQRCode = SignedQRCode;
        }

        public String getSignedQRCode() {
            return SignedQRCode;
        }

        public void setEwbNo(String EwbNo) {
            this.EwbNo = EwbNo;
        }

        public String getEwbNo() {
            return EwbNo;
        }

        public void setEwbDt(String EwbDt) {
            this.EwbDt = EwbDt;
        }

        public String getEwbDt() {
            return EwbDt;
        }

        public void setEwbValidTill(String EwbValidTill) {
            this.EwbValidTill = EwbValidTill;
        }

        public String getEwbValidTill() {
            return EwbValidTill;
        }

        public void setQRCodeUrl(String QRCodeUrl) {
            this.QRCodeUrl = QRCodeUrl;
        }

        public String getQRCodeUrl() {
            return QRCodeUrl;
        }

        public void setEinvoicePdf(String EinvoicePdf) {
            this.EinvoicePdf = EinvoicePdf;
        }

        public String getEinvoicePdf() {
            return EinvoicePdf;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getStatus() {
            return Status;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public String getAlert() {
            return alert;
        }

        public void setError(boolean error) {
            this.error = error;
        }

        public boolean isError() {
            return error;
        }
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

        public void setMessage(GenerateEinvoice.Message message) {
            this.message = message;
        }

        public GenerateEinvoice.Message getMessage() {
            return message;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setInfoDtls(String InfoDtls) {
            this.InfoDtls = InfoDtls;
        }

        public String getInfoDtls() {
            return InfoDtls;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public String getRequestId() {
            return requestId;
        }
        public String InfoDtls;
        public String status;
        public int code;
        public String requestId;
    }

    public class INVRootObject {
        public Results results;

        public void setResults(GenerateEinvoice.Results results) {
            this.results = results;
        }

        public GenerateEinvoice.Results getResults() {
            return results;
        }
    }
    
       
       
}
