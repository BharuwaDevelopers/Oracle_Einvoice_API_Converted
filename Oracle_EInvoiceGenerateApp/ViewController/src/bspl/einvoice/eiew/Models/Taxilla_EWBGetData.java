package bspl.einvoice.eiew.Models;

import java.sql.*;
import java.util.*;

public class Taxilla_EWBGetData {

    public static ResultSet GetDistinctData(String EINVType, String SessionID) throws SQLException {
        ResultSet rs = null;
        String sqlstr = "";
        if (EINVType.equals("GEWB")) {
            sqlstr = "SELECT DISTINCT DOC_NO, CDKEY, EINVPASSWORD, EINVUSERNAME, EFUSERNAME, EFPASSWORD, IRN, EWBNO FROM einvoice_generate_temp WHERE ID = ?";
        } else if (EINVType.equals("CEWB")) {
            sqlstr = "SELECT DISTINCT DOC_NO, EINVPASSWORD, EINVUSERNAME, EFUSERNAME, EFPASSWORD, EWBNO FROM einvoice_generate WHERE DOC_No = ? AND status IS NULL";
        } else if (EINVType.equals("NUSER") || EINVType.equals("BULK_EWB")) {
            sqlstr = "SELECT DISTINCT EWAY_USERNAME, EWAY_PASSWORD, EWAY_CLIENT_ID, EWAY_CLIENT_SECRET, EWAYBILL_GST, EWAY_CLIENT_USERNAME, EWAY_CLIENT_PASSWORD FROM unit";
        }

        try (Connection conn = OraDBConnection.OrclConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlstr)) {
            if (!EINVType.equals("NUSER") && !EINVType.equals("BULK_EWB")) {
                pstmt.setString(1, SessionID);
            }
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return rs;
    }

    public static ResultSet GetEWBData(String EINVType, String SessionID, String Doc_No) throws SQLException {
        ResultSet rs = null;
        String sqlstr = "";
        if (EINVType.equals("GEWB")) {
            sqlstr = "SELECT * FROM einvoice_generate_temp WHERE ID = ? AND DOC_NO = ?";
        } else if (EINVType.equals("CEWB")) {
            sqlstr = "SELECT * FROM einvoice_generate WHERE EWBNO = ?";
        } else if (EINVType.equals("UPD_V_NO") || EINVType.equals("CONSO_EWB") || EINVType.equals("REJ_EWB") || EINVType.equals("GET_EWB") || EINVType.equals("BULK_EWB")) {
            sqlstr = "SELECT * FROM EWB_GEN_STD WHERE EWAY_BILL_NO = ?";
        }

        try (Connection conn = OraDBConnection.getOrclConnection();
             PreparedStatement pstmt = conn.prepareStatement(sqlstr)) {
            if (EINVType.equals("GEWB")) {
                pstmt.setString(1, SessionID);
                pstmt.setString(2, Doc_No);
            } else {
                pstmt.setString(1, SessionID);
            }
            rs = pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return rs;
    }
}
