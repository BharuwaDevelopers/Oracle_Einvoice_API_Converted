package bspl.einvoice.eiew.ViewModels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OracelDataInsert {
    
    public static void updateDataOracle(INVRootObject obj, DataTable dt) {
           try {
               if (obj.getResults().getCode() != 200) {
                   String commandText = "UPDATE einvoice_generate_temp SET ERRORMSG=? WHERE DOC_NO = ?";
                   try (Connection connection = DriverManager.getConnection(OraDBConnection.OrclConnection)) {
                       PreparedStatement command = connection.prepareStatement(commandText);

                       command.setString(1, obj.getResults().getErrorMessage());
                       command.setString(2, dt.getRows().get(0).get("DOC_No").toString());

                       int rowsAffected = command.executeUpdate();
                   }

                   String commandText1 = "UPDATE Einv_Temp SET ERRORMSG=? WHERE IDENTIFIER = ?";
                   try (Connection connection = DriverManager.getConnection(OraDBConnection.OrclConnection)) {
                       PreparedStatement command = connection.prepareStatement(commandText1);

                       command.setString(1, obj.getResults().getErrorMessage());
                       command.setString(2, dt.getRows().get(0).get("DOC_No").toString());

                       int rowsAffected = command.executeUpdate();
                   }

                   String commandText2 = "UPDATE Einv_Temp1 SET ERRORMSG=? WHERE IDENTIFIER = ?";
                   try (Connection connection = DriverManager.getConnection(OraDBConnection.OrclConnection)) {
                       PreparedStatement command = connection.prepareStatement(commandText2);

                       command.setString(1, obj.getResults().getErrorMessage());
                       command.setString(2, dt.getRows().get(0).get("DOC_No").toString());

                       int rowsAffected = command.executeUpdate();
                   }
               } else {
                   String commandText = "UPDATE einvoice_generate_temp SET IRN=?, ACKNo=?, ACKDATE=?, SIGNEDQRCODE=?, SIGNEDINVOICE=?, EWBNO=?, EWBDT=?, EWBVALIDTILL=?, QRCODEURL=?, EINVOICEPDF=? WHERE DOC_NO = ?";
                   try (Connection connection = DriverManager.getConnection(OraDBConnection.OrclConnection)) {
                       PreparedStatement command = connection.prepareStatement(commandText);

                       command.setString(1, obj.getResults().getMessage().getIrn());
                       command.setString(2, obj.getResults().getMessage().getAckNo());
                       command.setString(3, obj.getResults().getMessage().getAckDt());
                       command.setString(4, obj.getResults().getMessage().getSignedQRCode());
                       command.setString(5, obj.getResults().getMessage().getSignedInvoice());
                       command.setString(6, obj.getResults().getMessage().getEwbNo());
                       command.setString(7, obj.getResults().getMessage().getEwbDt());
                       command.setString(8, obj.getResults().getMessage().getEwbValidTill());
                       command.setString(9, obj.getResults().getMessage().getQRCodeUrl());
                       command.setString(10, obj.getResults().getMessage().getEinvoicePdf());
                       command.setString(11, dt.getRows().get(0).get("DOC_No").toString());

                       int rowsAffected = command.executeUpdate();
                   }

                   String commandText1 = "UPDATE Einv_Temp SET IRN_NO=? WHERE IDENTIFIER = ?";
                   try (Connection connection = DriverManager.getConnection(OraDBConnection.OrclConnection)) {
                       PreparedStatement command = connection.prepareStatement(commandText1);

                       command.setString(1, obj.getResults().getMessage().getIrn());
                       command.setString(2, dt.getRows().get(0).get("DOC_No").toString());

                       int rowsAffected = command.executeUpdate();
                   }

                   String commandText2 = "UPDATE Einv_Temp1 SET IRN_NO=? WHERE IDENTIFIER = ?";
                   try (Connection connection = DriverManager.getConnection(OraDBConnection.OrclConnection)) {
                       PreparedStatement command = connection.prepareStatement(commandText2);

                       command.setString(1, obj.getResults().getMessage().getIrn());
                       command.setString(2, dt.getRows().get(0).get("DOC_No").toString());

                       int rowsAffected = command.executeUpdate();
                   }
               }
           } catch (SQLException ex) {
               // ErrorLog.WriteLog(ex);
           }
       }

       public static void insertDataOracle(INVRootObject obj, DataTable dt) {
           try {
               if (obj.getResults().getCode() != 200) {
                   String commandText = "UPDATE einvoice_generate_temp SET ERRORMSG=? WHERE DOC_NO = ?";
                   try (Connection connection = DriverManager.getConnection(OraDBConnection.OrclConnection)) {
                       PreparedStatement command = connection.prepareStatement(commandText);

                       command.setString(1, obj.getResults().getErrorMessage());
                       command.setString(2, dt.getRows().get(0).get("DOC_No").toString());

                       int rowsAffected = command.executeUpdate();
                   }
               } else {
                   String commandText = "INSERT INTO EINVOICE_REF_SETU(GSTIN, IRN, DOC_TYP, DOC_NO, DOC_DT, SIGNEDQRCODE, ACKDATE, ACKNO, SIGNEDINVOICE, EWBNO, EWBDT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                   try (Connection connection = DriverManager.getConnection(OraDBConnection.OrclConnection)) {
                       PreparedStatement command = connection.prepareStatement(commandText);

                       command.setString(1, dt.getRows().get(0).get("GSTIN").toString());
                       command.setString(2, obj.getResults().getMessage().getIrn());
                       command.setString(3, "GEINV");
                       command.setString(4, dt.getRows().get(0).get("DOC_No").toString());
                       command.setString(5, dt.getRows().get(0).get("DOC_DT").toString());
                       command.setString(6, obj.getResults().getMessage().getSignedQRCode());
                       command.setString(7, obj.getResults().getMessage().getAckDt());
                       command.setString(8, obj.getResults().getMessage().getAckNo());
                       command.setString(9, obj.getResults().getMessage().getSignedInvoice());
                       command.setString(10, obj.getResults().getMessage().getEwbNo());
                       command.setString(11, obj.getResults().getMessage().getEwbDt());

                       int rowsAffected = command.executeUpdate();
                   }
               }
           } catch (SQLException ex) {
               // ErrorLog.WriteLog(ex);
           }
       }

       public static void insertCancelDataOracle(CancelRoot obj, DataTable dt) {
           try {
               if (!obj.getResults().getStatus().equals("200")) {
                   Date date = new Date();
                   SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                   String date1 = dateFormat.format(date);
                   String sqlstr = "INSERT INTO Finance.EINVOICE_CANCEL(GSTIN, IRN, CNLREM, CNLRSN, STATUS, ERRORMSG, ERRORCODE, CANCELDATE, DOC_NO, DOC_Date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                   try (Connection connection = DriverManager.getConnection(OraDBConnection.OrclConnection)) {
                       PreparedStatement command = connection.prepareStatement(sqlstr);

                       command.setString(1, dt.getRows().get(0).get("GSTIN").toString());
                       command.setString(2, dt.getRows().get(0).get("IRN").toString());
                       command.setString(3, "Cancel");
                       command.setString(4, "Not res");
                       command.setString(5, obj.getResults().getStatus());
                       command.setString(6, obj.getResults().getErrorMessage());
                       command.setString(7, obj.getResults().getCode());
                       command.setString(8, date1);
                       command.setString(9, dt.getRows().get(0).get("DOC_NO").toString());
                       command.setString(10, convertDateTimeOracleFormat(dt.getRows().get(0).get("Dates").toString()));

                       int rowsAffected = command.executeUpdate();
                   }
               } else {
                   String sqlstr = "INSERT INTO Finance.EINVOICE_CANCEL(GSTIN, IRN, CNLREM, CNLRSN, STATUS, ERRORMSG, ERRORCODE, CANCELDATE, DOC_NO, DOC_Date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                   try (Connection connection = DriverManager.getConnection(OraDBConnection.OrclConnection)) {
                       PreparedStatement command = connection.prepareStatement(sqlstr);

                       command.setString(1, dt.getRows().get(0).get("GSTIN").toString());
                       command.setString(2, obj.getResults().getMessage().getIrn());
                       command.setString(3, "");
                       command.setString(4, "");
                       command.setString(5, obj.getResults().getStatus());
                       command.setString(6, "");
                       command.setString(7, "");
                       command.setString(8, obj.getResults().getMessage().getCancelDate());
                       command.setString(9, dt.getRows().get(0).get("DOC_NO").toString());
                       command.setString(10, convertDateTimeOracleFormat(dt.getRows().get(0).get("Dates").toString()));

                       int rowsAffected = command.executeUpdate();
                   }
               }
           } catch (SQLException ex) {
               // ErrorLog.WriteLog(ex);
           }
       }

       public static void updateEWBByIRNNO(SuccessEwbByIrn obj, DataTable dt) {
           try {
               if (obj.getResults().getCode() != 200) {
                   String commandText = "UPDATE einvoice_generate_temp SET ERRORMSG=? WHERE DOC_NO = ?";
                   try (Connection connection = DriverManager.getConnection(OraDBConnection.OrclConnection)) {
                       PreparedStatement command = connection.prepareStatement(commandText);

                       command.setString(1, obj.getResults().getErrorMessage());
                       command.setString(2, dt.getRows().get(0).get("DOC_No").toString());

                       int rowsAffected = command.executeUpdate();
                   }
               } else {
                   String commandText = "UPDATE einvoice_generate_temp SET EWBNO=?, EWBDT=?, EWBVALIDTILL=? WHERE DOC_NO = ?";
                   try (Connection connection = DriverManager.getConnection(OraDBConnection.OrclConnection)) {
                       PreparedStatement command = connection.prepareStatement(commandText);

                       command.setString(1, obj.getResults().getMessage().getEwbNo());
                       command.setString(2, obj.getResults().getMessage().getEwbDt());
                       command.setString(3, obj.getResults().getMessage().getEwbValidTill());
                       command.setString(4, dt.getRows().get(0).get("DOC_No").toString());

                       int rowsAffected = command.executeUpdate();
                   }
               }
           } catch (SQLException ex) {
               // ErrorLog.WriteLog(ex);
           }
       }

       public static String convertDateTimeOracleFormat(String dateVal) {
           if (!dateVal.isEmpty()) {
               SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
               return "to_date('" + sdf.format(new Date(dateVal)) + "','MM/dd/yyyy')";
           } else {
               return "''";
           }
       }
}
