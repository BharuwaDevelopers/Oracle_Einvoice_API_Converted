package bspl.einvoice.eiew.ViewModels;


import java.sql.*;
import java.util.*;


public class GetDataFromDB {

    public static List<Map<String, Object>> getDistinctData(String EINVType, String SessionID, String Eitype) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        String sqlstr = "";

        if (EINVType.equals("APPTYPE")) {
            sqlstr = "SELECT '" + Eitype + "' AS value_1 FROM dual";
        }

        if (EINVType.equals("CHECKINVEWB")) {
            sqlstr = "SELECT ITEM_BARCDE, BILLFROM_GSTIN, BILLTO_GSTIN FROM einvoice_generate_temp WHERE ID='" + SessionID + "'";
        }

        if (EINVType.equals("GETID")) {
            sqlstr = "SELECT DISTINCT ID, DOC_NO, CDKEY, EINVPASSWORD, EINVUSERNAME, EFUSERNAME, EFPASSWORD, IRN, EWBNO, ITEM_BARCDE, BILLFROM_GSTIN, BILLTO_GSTIN FROM einvoice_generate_temp WHERE DOC_NO='" + SessionID + "'";
        }

        if (EINVType.equals("GINV")) {
            sqlstr = "SELECT DISTINCT DOC_NO, CDKEY, EINVPASSWORD, EINVUSERNAME, EFUSERNAME, EFPASSWORD, IRN, EWBNO FROM einvoice_generate_temp WHERE ID='" + SessionID + "'";
        }

        if (EINVType.equals("CINV")) {
            sqlstr = "SELECT DISTINCT DOC_NO, EINVPASSWORD, EINVUSERNAME, EFUSERNAME, EFPASSWORD, IRN, GSTIN, EWBNO FROM einvoice_generate WHERE DOC_NO = '" + SessionID + "' AND IRN IS NOT NULL AND status IS NULL";
        }

        if (EINVType.equals("DTLINV")) {
            sqlstr = "SELECT DISTINCT DOC_NO, EINVPASSWORD, EINVUSERNAME, EFUSERNAME, EFPASSWORD FROM einvoice_generate_temp WHERE IRN='" + SessionID + "'";
        }

        if (EINVType.equals("EWBBYIRN")) {
            sqlstr = "SELECT DISTINCT DOC_NO, EINVPASSWORD, EINVUSERNAME, EFUSERNAME, EFPASSWORD, IRN FROM einvoice_generate_temp WHERE DOC_NO='" + SessionID + "'";
        }

        if (EINVType.equals("BULKINV")) {
            sqlstr = "SELECT DISTINCT DOC_NO, EINVPASSWORD, EINVUSERNAME, EFUSERNAME, EFPASSWORD FROM einvoice_generate_temp WHERE IRN='" + SessionID + "'";
        }

        result = executeQuery(sqlstr);
        return result;
    }

    public static List<Map<String, Object>> getEinvoiceData(String EINVType, String SessionID, String Doc_No) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        String sqlstr = "";

        if (EINVType.equals("GINV")) {
            sqlstr = "SELECT * FROM einvoice_generate_temp WHERE id='" + SessionID + "' AND DOC_No='" + Doc_No + "'";
        }

        if (EINVType.equals("CINV") || EINVType.equals("EWBBYIRN")) {
            sqlstr = "SELECT * FROM einvoice_generate WHERE DOC_No='" + Doc_No + "' AND IRN IS NOT NULL";
        }

        if (EINVType.equals("DTLINV")) {
            sqlstr = "SELECT * FROM einvoice_generate_temp WHERE IRN='" + SessionID + "'";
        }

        if (EINVType.equals("BULKINV")) {
            sqlstr = "SELECT * FROM einvoice_generate_temp WHERE DOC_No='" + Doc_No + "' AND IRN IS NULL";
        }

        result = executeQuery(sqlstr);
        return result;
    }

    private static List<Map<String, Object>> executeQuery(String query) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "username", "password");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(metaData.getColumnName(i), rs.getObject(i));
                }
                resultList.add(row);
            }
        }
        return resultList;
    }
}