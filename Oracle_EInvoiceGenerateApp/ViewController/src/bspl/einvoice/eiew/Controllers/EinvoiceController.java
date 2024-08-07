package bspl.einvoice.eiew.Controllers;

import bspl.einvoice.eiew.ViewModels.EwaybillByIrn;
import bspl.einvoice.eiew.ViewModels.GetAccessToken;
import bspl.einvoice.eiew.ViewModels.GetDataFromDB;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Controller
public class EinvoiceController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/GenEinvoice")
    public String genEinvoice(@RequestParam String ptype, @RequestParam String DocumentId, @RequestParam String dbunit, @RequestParam String CanCode, @RequestParam String Eitype, Model model) {
        String Provider = "TAXILLA";
        String dbname = "", Pwd = "", DbUser = "", dbServer = "";
        List<DbDetailsModel> dbdtl = new ArrayList<>();
        String path = Paths.get("ExcelFile", "dbserverdtl.xlsx").toString();

        if (new File(path).exists()) {
            try (FileInputStream fis = new FileInputStream(path); Workbook workbook = WorkbookFactory.create(fis)) {
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    dbdtl.add(new DbDetailsModel(
                        row.getCell(0).toString(),
                        row.getCell(1).toString(),
                        row.getCell(2).toString(),
                        row.getCell(3).toString(),
                        row.getCell(4).toString()
                    ));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        List<DbDetailsModel> filtereddbdtl = dbdtl.stream()
            .filter(user -> user.getUnitcode().equals(dbunit))
            .collect(Collectors.toList());

        for (DbDetailsModel user : filtereddbdtl) {
            dbname = user.getDbname();
            DbUser = user.getDbuser();
            Pwd = user.getDbpassword();
            dbServer = user.getServerip();
        }

        String AppType = "";
        if (ptype == null || DocumentId == null || dbname == null || Pwd == null || DbUser == null || dbServer == null || Eitype == null) {
            model.addAttribute("Processed", "Parameter provided should not be null or Empty");
            return "index";
        }

        if ("CINV".equals(ptype)) {
            int canCode = Integer.parseInt(CanCode);
            if (canCode <= 0 || canCode > 4) {
                model.addAttribute("Processed", "While Cancel Einvoive Cancel code should be >0 and <=4 ");
                return "index";
            }
        }

        OraDBConnection.setConnectionData(DbUser, Pwd, dbServer + ":1521/" + dbname);

        try {
            DataTable dtapptype = GetDataFromDB.getDistinctData("APPTYPE", DocumentId, Eitype);
            if (dtapptype.getRows().size() > 0) {
                AppType = dtapptype.getRows().get(0).get("value_1").toString();

                DataTable dtinvewb = new DataTable();
                DataTable dt1 = GetDataFromDB.getDistinctData("GETID", DocumentId, Eitype);
                if (dt1.getRows().size() > 0) {
                    dtinvewb = dt1;
                } else {
                    dtinvewb = GetDataFromDB.getDistinctData("CHECKINVEWB", DocumentId, Eitype);
                }

                if (dtinvewb.getRows().size() > 0) {
                    if ("B".equals(AppType) && "GEWB".equals(ptype.toUpperCase())) {
                        String Rdata = dtinvewb.getRows().get(0).get("ITEM_BARCDE").toString();
                        if ("CH".equals(Rdata)) {
                            AppType = "EW";
                        }
                    } else if ("B".equals(AppType) && "CEWB".equals(ptype.toUpperCase())) {
                        AppType = "EW";
                    } else if ("B".equals(AppType) && dtinvewb.getRows().get(0).get("BILLFROM_GSTIN").toString().equals(dtinvewb.getRows().get(0).get("BILLTO_GSTIN").toString())) {
                        AppType = "EW";
                        ptype = "GEWB";
                    }
                } else {
                    model.addAttribute("Processed", "No Data found for Ewaybill ");
                }
            } else {
                model.addAttribute("Processed", "Parameter type is not define");
                return "index";
            }

            DataTable dtInvL = new DataTable();
            if ("TAXILLA".equals(Provider.toUpperCase())) {
                if ("EI".equals(AppType.toUpperCase())) {
                    if ("GINV".equals(ptype.toUpperCase().trim())) {
                        dtInvL = GetDataFromDB.getDistinctData("GINV", DocumentId, Eitype);
                        if (dtInvL.getRows().size() > 0) {
                            String accessToken = Taxilla_EinvoiceAPI.accessToken(dtInvL).get();
                            AccessTokenResult value2 = JsonConvert.deserializeObject(accessToken, AccessTokenResult.class);
                            String requestId = value2.getJti();
                            String tokenType = value2.getTokenType();
                            String auth = value2.getAccessToken();
                            if (!auth.isEmpty()) {
                                for (int i = 0; i < dtInvL.getRows().size(); i++) {
                                    DataTable dtinv = GetDataFromDB.getEinvoiceData("GINV", DocumentId, dtInvL.getRows().get(i).get("DOC_No").toString());
                                    if (dtinv.getRows().size() > 0) {
                                        String jfile = Taxilla_GenerateEinvoice.generateIRNJsonFile(dtinv, "N");
                                        if (!jfile.isEmpty()) {
                                            ClsDynamic.jsonLog(jfile, dtInvL.getRows().get(0).get("DOC_No").toString().replace("/", "_"));
                                            String finalString = ClsDynamic.generateUniqueNumber();
                                            String RInvdata = Taxilla_EinvoiceAPI.generateIrn(dtinv, jfile, finalString, DocumentId, auth, "N").get();
                                            if ("1".equals(RInvdata)) {
                                                model.addAttribute("Processed", "Einvoice Generated successfully");
                                            } else {
                                                model.addAttribute("Processed", RInvdata);
                                            }
                                        } else {
                                            model.addAttribute("Processed", jfile);
                                        }
                                    } else {
                                        model.addAttribute("Processed", "No Data Found");
                                    }
                                }
                            } else {
                                model.addAttribute("Processed", "Accesstoken not generated please check error message in your table " + accessToken);
                            }
                        } else {
                            model.addAttribute("Processed", "No Data Found");
                        }
                    } else if ("CINV".equals(ptype.toUpperCase().trim())) {
                        dtInvL = GetDataFromDB.getDistinctData("CINV", DocumentId, Eitype);
                        if (dtInvL.getRows().size() > 0) {
                            String accessToken = Taxilla_EinvoiceAPI.accessToken(dtInvL).get();
                            AccessTokenResult value2 = JsonConvert.deserializeObject(accessToken, AccessTokenResult.class);
                            String requestId = value2.getJti();
                            String tokenType = value2.getTokenType();
                            String auth = value2.getAccessToken();
                            if (!accessToken.isEmpty()) {
                                for (int i = 0; i < dtInvL.getRows().size(); i++) {
                                    String finalString = ClsDynamic.generateUniqueNumber();
                                    String jfile = Taxilla_CancelIrnClasses.cancelJsonFile(dtInvL, DocumentId, accessToken, CanCode);
                                    String RInvdata = Taxilla_EinvoiceAPI.cancelInvoice(dtInvL, jfile, finalString, tokenType, auth).get();
                                    if ("1".equals(RInvdata)) {
                                        model.addAttribute("Processed", "Einvoice Canceled");
                                    } else {
                                        model.addAttribute("Processed", RInvdata);
                                    }
                                }
                            } else {
                                model.addAttribute("Processed", "Accesstoken not generated please check error message in your table");
                            }
                        } else {
                            model.addAttribute("Processed", "No Data Found");
                        }
                    } else if ("GETINVBYIRN".equals(ptype.toUpperCase().trim())) {
                        dtInvL = GetDataFromDB.getDistinctData("GETINVBYIRN", DocumentId, Eitype);
                        if (dtInvL.getRows().size() > 0) {
                            String accessToken = Taxilla_EinvoiceAPI.accessToken(dtInvL).get();
                            AccessTokenResult value2 = JsonConvert.deserializeObject(accessToken, AccessTokenResult.class);
                            String requestId = value2.getJti();
                            String tokenType = value2.getTokenType();
                            String auth = value2.getAccessToken();
                            if (!accessToken.isEmpty()) {
                                String finalString = ClsDynamic.generateUniqueNumber();
                                String RInvdata = Taxilla_EinvoiceAPI.detailInvoiceByIrn(dtInvL, DocumentId, finalString, tokenType, auth).get();
                            }
                        }
                    } else if ("EWBBYIRN".equals(ptype.toUpperCase().trim())) {
                        dtInvL = GetDataFromDB.getDistinctData("EWBBYIRN", DocumentId, Eitype);
                        if (dtInvL.getRows().size() > 0) {
                            String accessToken = Taxilla_EinvoiceAPI.accessToken(dtInvL).get();
                            AccessTokenResult value2 = JsonConvert.deserializeObject(accessToken, AccessTokenResult.class);
                            String requestId = value2.getJti();
                            String tokenType = value2.getTokenType();
                            String auth = value2.getAccessToken();
                            if (!accessToken.isEmpty()) {
                                String finalString = ClsDynamic.generateUniqueNumber();
                                String jfile = Taxilla_EwaybillByIrnClasses.generateEwaybillIrnJson(dtInvL, DocumentId, accessToken);
                                String RInvdata = Taxilla_EinvoiceAPI.generateEwaybillIrn(dtInvL, jfile, finalString, tokenType, auth).get();
                            }
                        }
                    } else if ("CEWBBYIRN".equals(ptype.toUpperCase().trim())) {
                        dtInvL = GetDataFromDB.getDistinctData("CEWBBYIRN", DocumentId, Eitype);
                        if (dtInvL.getRows().size() > 0) {
                            String accessToken = Taxilla_EinvoiceAPI.accessToken(dtInvL).get();
                            AccessTokenResult value2 = JsonConvert.deserializeObject(accessToken, AccessTokenResult.class);
                            String requestId = value2.getJti();
                            String tokenType = value2.getTokenType();
                            String auth = value2.getAccessToken();
                            if (!accessToken.isEmpty()) {
                                String finalString = ClsDynamic.generateUniqueNumber();
                                String jfile = Taxilla_EwaybillByIrnClasses.cancelEwaybillJson(dtInvL, DocumentId, accessToken);
                                String RInvdata = Taxilla_EinvoiceAPI.cancelEwaybill(dtInvL, jfile, finalString, tokenType, auth).get();
                            }
                        }
                    } else if ("EXTQR".equals(ptype.toUpperCase().trim())) {
                        dtInvL = GetDataFromDB.getDistinctData("EXTQR", DocumentId, Eitype);
                        if (dtInvL.getRows().size() > 0) {
                            String accessToken = Taxilla_EinvoiceAPI.accessToken(dtInvL).get();
                            AccessTokenResult value2 = JsonConvert.deserializeObject(accessToken, AccessTokenResult.class);
                            String requestId = value2.getJti();
                            String tokenType = value2.getTokenType();
                            String auth = value2.getAccessToken();
                            if (!accessToken.isEmpty()) {
                                String finalString = ClsDynamic.generateUniqueNumber();
                                String jfile = Taxilla_EwaybillByIrnClasses.extractQRCodeJson(dtInvL, DocumentId, accessToken);
                                String RInvdata = Taxilla_EinvoiceAPI.extractQRCode(dtInvL, jfile, finalString, tokenType, auth).get();
                            }
                        }
                    } else if ("EXTINV".equals(ptype.toUpperCase().trim())) {
                        dtInvL = GetDataFromDB.getDistinctData("EXTINV", DocumentId, Eitype);
                        if (dtInvL.getRows().size() > 0) {
                            String accessToken = Taxilla_EinvoiceAPI.accessToken(dtInvL).get();
                            AccessTokenResult value2 = JsonConvert.deserializeObject(accessToken, AccessTokenResult.class);
                            String requestId = value2.getJti();
                            String tokenType = value2.getTokenType();
                            String auth = value2.getAccessToken();
                            if (!accessToken.isEmpty()) {
                                String finalString = ClsDynamic.generateUniqueNumber();
                                String jfile = Taxilla_EwaybillByIrnClasses.extractSignedInvoiceJson(dtInvL, DocumentId, accessToken);
                                String RInvdata = Taxilla_EinvoiceAPI.extractSignedInvoice(dtInvL, jfile, finalString, tokenType, auth).get();
                            }
                        }
                    }
else if (ptype.toUpperCase().trim().equals("GQRIMG")) {
    dtInvL = GetDataFromDB.GetDistinctData("GQRIMG", DocumentId, Eitype);
    if (dtInvL.getRowCount() > 0) {
        String access_token = Taxilla_EinvoiceAPI.AccessToken(dtInvL).get();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> value2 = new Gson().fromJson(access_token, type);
        String requestid = value2.get("jti");
        String tokentype = value2.get("token_type");
        String auth = value2.get("access_token");
        if (!access_token.isEmpty()) {
            String finalString = ClsDynamic.GenerateUniqueNumber();
            String jfile = Taxilla_EwaybillByIrnClasses.ExtractQR_Code_Json(dtInvL, DocumentId, access_token);
            String RInvdata = Taxilla_EinvoiceAPI.Generate_QRImage(dtInvL, jfile, finalString, tokentype, auth).get();
        }
    }
} else if (ptype.toUpperCase().trim().equals("GetQRIMG")) {
    dtInvL = GetDataFromDB.GetDistinctData("GetQRIMG", DocumentId, Eitype);
    if (dtInvL.getRowCount() > 0) {
        String access_token = Taxilla_EinvoiceAPI.AccessToken(dtInvL).get();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> value2 = new Gson().fromJson(access_token, type);
        String requestid = value2.get("jti");
        String tokentype = value2.get("token_type");
        String auth = value2.get("access_token");
        if (!access_token.isEmpty()) {
            String finalString = ClsDynamic.GenerateUniqueNumber();
            String RInvdata = Taxilla_EinvoiceAPI.Get_QRImage(dtInvL, "", finalString, tokentype, auth).get();
        }
    }
} else if (AppType.toUpperCase().equals("EW")) {
    if (ptype.toUpperCase().equals("GEWB")) {
        DataTable dtEWBL = new DataTable();
        dtEWBL = Taxilla_EWBGetData.GetDistinctData("GEWB", DocumentId);
        if (dtEWBL.getRowCount() > 0) {
            String access_token = Taxilla_EinvoiceAPI.AccessToken(dtEWBL).get();
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> value2 = new Gson().fromJson(access_token, type);
            String requestid = value2.get("jti");
            String tokentype = value2.get("token_type");
            String auth = value2.get("access_token");
            if (!auth.isEmpty()) {
                for (int i = 0; i < dtEWBL.getRowCount(); i++) {
                    DataTable dtEWB = new DataTable();
                    dtEWB = Taxilla_EWBGetData.GetEWBData("GEWB", DocumentId, dtEWBL.getRow(i).get("DOC_NO").toString());
                    if (dtEWB.getRowCount() > 0) {
                        String jsondata = Taxilla_GenEwaybillClasses.EWBJsonFile(dtEWB, access_token);
                        if (!jsondata.isEmpty()) {
                            ClsDynamic.JsonLog(jsondata, "EWB_" + dtEWBL.getRow(i).get("DOC_NO").toString().replace("/", "_"));
                            String finalString = ClsDynamic.GenerateUniqueNumber();
                            String RInvdata = Taxilla_EwaybillAPI.GenEwayBill(dtEWB, jsondata, finalString, tokentype, auth).get();
                            if (RInvdata.equals("1")) {
                                ViewBag.Processed = "Ewaybill Generated Successfully";
                            } else {
                                ViewBag.Processed = "Some Error While generating ewaybill " + RInvdata;
                            }
                        } else {
                            ViewBag.Processed = "Some Error While generating ewaybill json file ";
                        }
                    }
                }
            } else {
                ViewBag.Processed = "Accesstoken not generated please check error message in your table";
            }
        } else {
            ViewBag.Processed = "No Data Found";
        }
    } else if (ptype.toUpperCase().equals("CEWB")) {
        DataTable dtEWBL = new DataTable();
        dtEWBL = Taxilla_EWBGetData.GetDistinctData("CEWB", DocumentId);
        if (dtEWBL.getRowCount() > 0) {
            String access_token = Taxilla_EinvoiceAPI.AccessToken(dtEWBL).get();
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> value2 = new Gson().fromJson(access_token, type);
            String requestid = value2.get("jti");
            String tokentype = value2.get("token_type");
            String auth = value2.get("access_token");
            if (!auth.isEmpty()) {
                for (int i = 0; i < dtEWBL.getRowCount(); i++) {
                    String finalString = ClsDynamic.GenerateUniqueNumber();
                    DataTable dtEWB = new DataTable();
                    dtEWB = Taxilla_EWBGetData.GetEWBData("CEWB", dtEWBL.getRow(0).get("EWBNO").toString(), dtEWBL.getRow(0).get("DOC_NO").toString());
                    if (dtEWB.getRowCount() > 0) {
                        String jsondata = Taxilla_CanEwaybillClasses.GenerateCancelJson(dtEWB, access_token);
                        String RInvdata = Taxilla_EwaybillAPI.CancelEwayBill(dtEWB, jsondata, finalString, tokentype, auth).get();
                        if (RInvdata.equals("1")) {
                            ViewBag.Processed = "Ewaybill canceled";
                        } else {
                            ViewBag.Processed = RInvdata;
                        }
                    }
                }
            } else {
                ViewBag.Processed = "Accesstoken not generated please check error message in your table";
            }
        } else {
            ViewBag.Processed = "No Data Found";
        }
    }
} else if (AppType.toUpperCase().equals("B")) {
    if (ptype.toUpperCase().trim().equals("GINV")) {
        dtInvL = GetDataFromDB.GetDistinctData("GINV", DocumentId, Eitype);
        if (dtInvL.getRowCount() > 0) {
            String access_token = Taxilla_EinvoiceAPI.AccessToken(dtInvL).get();
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> value2 = new Gson().fromJson(access_token, type);
            String requestid = value2.get("jti");
            String tokentype = value2.get("token_type");
            String auth = value2.get("access_token");
            if (!auth.isEmpty()) {
                for (int i = 0; i < dtInvL.getRowCount(); i++) {
                    DataTable dtinv = new DataTable();
                    dtinv = GetDataFromDB.GetEinvoiceData("GINV", DocumentId, dtInvL.getRow(i).get("DOC_No").toString());
                    if (dtinv.getRowCount() > 0) {
                        if (dtInvL.getRow(i).get("IRN").toString().isEmpty()) {
                            String jfile = Taxilla_GenerateEinvoice.GenerateIRNJsonFile(dtinv, "Y");
                            if (!jfile.isEmpty()) {
                                String finalString = ClsDynamic.GenerateUniqueNumber();
                                String RInvdata = Taxilla_EinvoiceAPI.GenerateIrn(dtinv, jfile, finalString, DocumentId, auth, "Y").get();
                                if (RInvdata.equals("1") || RInvdata.equals("2")) {
                                    ViewBag.Processed = "Einvoice and Eway bill Generated successfully";
                                } else if (RInvdata.equals("3")) {
                                    ViewBag.Processed = "Einvoice Generated successfully and Some Error while Generating Ewaybill so Please check Error message and Try Again";
                                } else {
                                    ViewBag.Processed = RInvdata;
                                }
                            } else {
                                ViewBag.Processed = "Error while generating in json file " + jfile.toString();
                            }
                        } else {
                            if (!dtinv.getRow(i).get("IRN").toString().isEmpty()) {
                                String jfile = Taxilla_EwaybillByIrnClasses.Generate_EwaybillIrn_Json(dtinv, dtinv.getRow(i).get("IRN").toString(), "");
                                if (!jfile.isEmpty()) {
                                    ClsDynamic.JsonLog(jfile, "EWBBYIRN_" + dtInvL.getRow(i).get("DOC_No").toString());
                                    String finalString = ClsDynamic.GenerateUniqueNumber();
                                    String RInvdata = Taxilla_EinvoiceAPI.Generate_EwaybillIrn(dtinv, jfile, finalString, DocumentId, auth).get();
                                    if (RInvdata.equals("1")) {
                                        ViewBag.Processed = "Eway bill Generated successfully";
                                    } else {
                                        ViewBag.Processed = RInvdata;
                                    }
                                } else {
                                    ViewBag.Processed = "Error while generating in json file ";
                                }
                            }
                        }
                    }
                }
            } else {
                ViewBag.Processed = "Accesstoken not generated please check error message in your table " + access_token;
            }
        } else {
            ViewBag.Processed = "No Data Found";
        }
    } else if (ptype.toUpperCase().trim().equals("CINV")) {
        dtInvL = GetDataFromDB.GetDistinctData("CINV", DocumentId, Eitype);
        if (dtInvL.getRowCount() > 0) {
            String access_token = Taxilla_EinvoiceAPI.AccessToken(dtInvL).get();
            Type type = new TypeToken<Map<String, String>>(){}.getType();
            Map<String, String> value2 = new Gson().fromJson(access_token, type);
            String requestid = value2.get("jti");
            String tokentype = value2.get("token_type");
            String auth = value2.get("access_token");
            if (!access_token.isEmpty()) {
                for (int i = 0; i < dtInvL.getRowCount(); i++) {
                    if (!dtInvL.getRow(i).get("EWBNO").toString().isEmpty()) {
                        String finalString = ClsDynamic.GenerateUniqueNumber();
                        String CEWbJfile = Taxilla_EwaybillByIrnClasses.Cancel_Ewaybill_Json(dtInvL, DocumentId, access_token);
                        String CEWBRInvdata = Taxilla_EwaybillAPI.CancelEwayBill(dtInvL, CEWbJfile, finalString, tokentype, auth).get();
                        if (CEWBRInvdata.equals("1")) {
                            String jfile = Taxilla_CancelIrnClasses.CancelJsonFile(dtInvL, DocumentId, access_token, CanCode);
                            String RInvdata = Taxilla_EinvoiceAPI.CancelInvoice(dtInvL, jfile, finalString, tokentype, auth).get();
                            if (RInvdata.equals("1")) {
                                ViewBag.Processed = "Eway bill and Einvoice Canceled";
                            } else {
                                ViewBag.Processed = CEWBRInvdata;
                            }
                        } else {
                            ViewBag.Processed = CEWBRInvdata;
                        }
                    } else {
                        String finalString = ClsDynamic.GenerateUniqueNumber();
                        String jfile = Taxilla_CancelIrnClasses.CancelJsonFile(dtInvL, DocumentId, access_token, CanCode);
                        String RInvdata = Taxilla_EinvoiceAPI.CancelInvoice(dtInvL, jfile, finalString, tokentype, auth).get();
                        if (RInvdata.equals("1")) {
                            ViewBag.Processed = "Einvoice Canceled";
                        } else {
                            ViewBag.Processed = RInvdata;
                        }
                    }
                }
            } else {
                ViewBag.Processed = "Accesstoken not generated please check error message in your table";
            }
        } else {
            ViewBag.Processed = "No data Found";
        }
    }
}
else if (ptype.toUpperCase().trim().equals("GETINVBYIRN")) {
    dtInvL = GetDataFromDB.GetDistinctData("GETINVBYIRN", DocumentId, Eitype);
    if (dtInvL.getRows().size() > 0) {
        CompletableFuture<String> accessTokenFuture = Taxilla_EinvoiceAPI.AccessToken(dtInvL);
        String access_token = accessTokenFuture.get();
        AccessTokenResult value2 = new Gson().fromJson(access_token, AccessTokenResult.class);
        String requestid = value2.jti;
        String tokentype = value2.token_type;
        String auth = value2.access_token;
        if (!access_token.isEmpty()) {
            String finalString = ClsDynamic.GenerateUniqueNumber();
            CompletableFuture<String> RInvdataFuture = Taxilla_EinvoiceAPI.DetailInvoiceByIrn(dtInvL, DocumentId, finalString, tokentype, auth);
            String RInvdata = RInvdataFuture.get();
        }
    }
} else if (ptype.toUpperCase().trim().equals("EWBBYIRN")) {
    dtInvL = GetDataFromDB.GetDistinctData("EWBBYIRN", DocumentId, Eitype);
    if (dtInvL.getRows().size() > 0) {
        CompletableFuture<String> accessTokenFuture = Taxilla_EinvoiceAPI.AccessToken(dtInvL);
        String access_token = accessTokenFuture.get();
        AccessTokenResult value2 = new Gson().fromJson(access_token, AccessTokenResult.class);
        String requestid = value2.jti;
        String tokentype = value2.token_type;
        String auth = value2.access_token;
        if (!access_token.isEmpty()) {
            for (int i = 0; i < dtInvL.getRows().size(); i++) {
                String finalString = ClsDynamic.GenerateUniqueNumber();
                DataTable dtinv = GetDataFromDB.GetEinvoiceData("EWBBYIRN", DocumentId, dtInvL.getRows().get(i).get("DOC_No").toString());
                if (dtinv.getRows().size() > 0) {
                    String jfile = Taxilla_EwaybillByIrnClasses.Generate_EwaybillIrn_Json(dtinv, DocumentId, access_token);
                    CompletableFuture<String> RInvdataFuture = Taxilla_EinvoiceAPI.Generate_EwaybillIrn(dtinv, jfile, finalString, tokentype, auth);
                    String RInvdata = RInvdataFuture.get();

                    if (RInvdata.equals("1")) {
                        ViewBag.Processed = "Eway bill Generated successfully by IRN";
                    } else {
                        ViewBag.Processed = RInvdata;
                    }
                }
            }
        }
    } else {
        ViewBag.Processed = "No Data Found";
    }
} else if (ptype.toUpperCase().trim().equals("CEWBBYIRN")) {
    dtInvL = GetDataFromDB.GetDistinctData("CEWBBYIRN", DocumentId, Eitype);
    if (dtInvL.getRows().size() > 0) {
        CompletableFuture<String> accessTokenFuture = Taxilla_EinvoiceAPI.AccessToken(dtInvL);
        String access_token = accessTokenFuture.get();
        AccessTokenResult value2 = new Gson().fromJson(access_token, AccessTokenResult.class);
        String requestid = value2.jti;
        String tokentype = value2.token_type;
        String auth = value2.access_token;
        if (!access_token.isEmpty()) {
            for (int i = 0; i < dtInvL.getRows().size(); i++) {
                String finalString = ClsDynamic.GenerateUniqueNumber();
                String jfile = Taxilla_EwaybillByIrnClasses.Cancel_Ewaybill_Json(dtInvL, DocumentId, access_token);
                CompletableFuture<String> RInvdataFuture = Taxilla_EinvoiceAPI.Cancel_Ewaybill(dtInvL, jfile, finalString, tokentype, auth);
                String RInvdata = RInvdataFuture.get();
            }
        }
    }
} else {
    if (AppType.equals("EI")) {
        if (ptype.equals("GINV")) {
            dtInvL = GetDataFromDB.GetDistinctData("GINV", DocumentId, Eitype);
            if (dtInvL.getRows().size() > 0) {
                CompletableFuture<String> accessTokenFuture = GetAccessToken.Einvoice_API_Login(dtInvL);
                String access_token = accessTokenFuture.get();
                if (!access_token.isEmpty()) {
                    for (int i = 0; i < dtInvL.getRows().size(); i++) {
                        DataTable dtinv = GetDataFromDB.GetEinvoiceData("GINV", DocumentId, dtInvL.getRows().get(i).get("DOC_No").toString());
                        if (dtinv.getRows().size() > 0) {
                            String jfile = GenerateEinvoice.GenerateJsonFile(dtinv, access_token);
                            CompletableFuture<String> RInvdataFuture = GenerateEinvoice.generateInvoice(jfile, access_token, dtinv);
                            String RInvdata = RInvdataFuture.get();
                            if (RInvdata.equals("1")) {
                                System.out.println("einvoice generated");
                            } else {
                                System.out.println(RInvdata);
                            }
                        } else {
                            System.out.println("No Data Found");
                        }
                    }
                }
            }
        } else if (ptype.equals("CINV")) {
            dtInvL = GetDataFromDB.GetDistinctData("CINV", DocumentId, Eitype);
            if (dtInvL.getRows().size() > 0) {
                CompletableFuture<String> accessTokenFuture = GetAccessToken.Einvoice_API_Login(dtInvL);
                String access_token = accessTokenFuture.get();
                if (!access_token.isEmpty()) {
                    String jfile = CanEinvoice.CancelJsonFile(dtInvL, DocumentId, access_token);
                    CompletableFuture<String> RInvdataFuture = CanEinvoice.CancelInvoice(jfile, access_token, dtInvL);
                    String RInvdata = RInvdataFuture.get();
                }
            }
        } else if (ptype.equals("DTLINV")) {
            dtInvL = GetDataFromDB.GetDistinctData("DTLINV", DocumentId, Eitype);
            if (dtInvL.getRows().size() > 0) {
                CompletableFuture<String> accessTokenFuture = GetAccessToken.Einvoice_API_Login(dtInvL);
                String access_token = accessTokenFuture.get();
                if (!access_token.isEmpty()) {
                    DataTable dtinv = GetDataFromDB.GetEinvoiceData("DTLINV", DocumentId, dtInvL.getRows().get(0).get("DOC_No").toString());
                    if (dtinv.getRows().size() > 0) {
                        CompletableFuture<String> RInvdataFuture = DtlInvoiceByIrn.DetailInvoiceByIrn(DocumentId, access_token, dtinv);
                        String RInvdata = RInvdataFuture.get();
                    }
                }
            }
        } else if (ptype.equals("EWBBYIRN")) {
            dtInvL = GetDataFromDB.GetDistinctData("EWBBYIRN", DocumentId, Eitype);
            if (dtInvL.getRows().size() > 0) {
                CompletableFuture<String> accessTokenFuture = GetAccessToken.Einvoice_API_Login(dtInvL);
                String access_token = accessTokenFuture.get();
                if (!access_token.isEmpty()) {
                    String jfile = EwaybillByIrn.Generate_EwaybillIrn_Json(dtInvL, DocumentId, access_token);
                    CompletableFuture<String> RInvdataFuture = EwaybillByIrn.Generate_EwaybillIrn(jfile, access_token, dtInvL, ptype);
                    String RInvdata = RInvdataFuture.get();
                }
            }
        }
        }
           }
                }}  } catch (Exception e) {
                    e.printStackTrace();
                }

                return "index";
            }
        }

class DbDetailsModel {
    private String unitcode;
    private String dbname;
    private String dbuser;
    private String dbpassword;
    private String serverip;

    public DbDetailsModel(String unitcode, String dbname, String dbuser, String dbpassword, String serverip) {
        this.unitcode = unitcode;
        this.dbname = dbname;
        this.dbuser = dbuser;
        this.dbpassword = dbpassword;
        this.serverip = serverip;
    }

    public String getUnitcode() {
        return unitcode;
    }

    public String getDbname() {
        return dbname;
    }

    public String getDbuser() {
        return dbuser;
    }

    public String getDbpassword() {
        return dbpassword;
    }

    public String getServerip() {
        return serverip;
    }
}

class DataTable {
    private List<Map<String, Object>> rows;

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }
}

class OraDBConnection {
    public static void setConnectionData(String dbUser, String pwd, String dbServer) {
        // Implementation for setting connection data
    }
}

class GetDataFromDB {
    public static DataTable getDistinctData(String type, String documentId, String eitype) {
        // Implementation for getting distinct data
        return new DataTable();
    }

    public static DataTable getEinvoiceData(String type, String documentId, String docNo) {
        // Implementation for getting e-invoice data
        return new DataTable();
    }
}

class Taxilla_EinvoiceAPI {
    public static CompletableFuture<String> accessToken(DataTable dtInvL) {
        // Implementation for getting access token
        return CompletableFuture.completedFuture("");
    }

    public static CompletableFuture<String> generateIrn(DataTable dtinv, String jfile, String finalString, String documentId, String auth, String type) {
        // Implementation for generating IRN
        return CompletableFuture.completedFuture("");
    }

    public static CompletableFuture<String> cancelInvoice(DataTable dtInvL, String jfile, String finalString, String tokenType, String auth) {
        // Implementation for canceling invoice
        return CompletableFuture.completedFuture("");
    }

    public static CompletableFuture<String> detailInvoiceByIrn(DataTable dtInvL, String documentId, String finalString, String tokenType, String auth) {
        // Implementation for detailing invoice by IRN
        return CompletableFuture.completedFuture("");
    }

    public static CompletableFuture<String> generateEwaybillIrn(DataTable dtInvL, String jfile, String finalString, String tokenType, String auth) {
        // Implementation for generating e-waybill IRN
        return CompletableFuture.completedFuture("");
    }

    public static CompletableFuture<String> cancelEwaybill(DataTable dtInvL, String jfile, String finalString, String tokenType, String auth) {
        // Implementation for canceling e-waybill
        return CompletableFuture.completedFuture("");
    }

    public static CompletableFuture<String> extractQRCode(DataTable dtInvL, String jfile, String finalString, String tokenType, String auth) {
        // Implementation for extracting QR code
        return CompletableFuture.completedFuture("");
    }

    public static CompletableFuture<String> extractSignedInvoice(DataTable dtInvL, String jfile, String finalString, String tokenType, String auth) {
        // Implementation for extracting signed invoice
        return CompletableFuture.completedFuture("");
    }
}

class Taxilla_GenerateEinvoice {
    public static String generateIRNJsonFile(DataTable dtinv, String type) {
        // Implementation for generating IRN JSON file
        return "";
    }
}

class Taxilla_CancelIrnClasses {
    public static String cancelJsonFile(DataTable dtInvL, String documentId, String accessToken, String canCode) {
        // Implementation for canceling JSON file
        return "";
    }
}

class Taxilla_EwaybillByIrnClasses {
    public static String generateEwaybillIrnJson(DataTable dtInvL, String documentId, String accessToken) {
        // Implementation for generating e-waybill IRN JSON
        return "";
    }

    public static String cancelEwaybillJson(DataTable dtInvL, String documentId, String accessToken) {
        // Implementation for canceling e-waybill JSON
        return "";
    }

    public static String extractQRCodeJson(DataTable dtInvL, String documentId, String accessToken) {
        // Implementation for extracting QR code JSON
        return "";
    }

    public static String extractSignedInvoiceJson(DataTable dtInvL, String documentId, String accessToken) {
        // Implementation for extracting signed invoice JSON
        return "";
    }
}

class ClsDynamic {
    public static void jsonLog(String jfile, String docNo) {
        // Implementation for logging JSON
    }

    public static String generateUniqueNumber() {
        // Implementation for generating unique number
        return "";
    }
}

class JsonConvert {
    public static AccessTokenResult deserializeObject(String json, Class<AccessTokenResult> clazz) {
        // Implementation for deserializing JSON object
        return new AccessTokenResult();
    }
}

class AccessTokenResult {
    private String jti;
    private String tokenType;
    private String accessToken;

    public String getJti() {
        return jti;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
    

