package bspl.einvoice.eiew.Models;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleTypes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import java.util.List;

public class Taxilla_OracelDataInsert {
    public Taxilla_OracelDataInsert() {
        super();
    }
    
    public static void updateDataOracle(successRoot obj, List<DataRow> dt) throws SQLException {
           String commandText = "UPDATE einvoice_generate_temp SET IRN=?, ACKNo=?, ACKDATE=?, SIGNEDQRCODE=?, SIGNEDINVOICE=?, EWBNO=?, EWBDT=?, EWBVALIDTILL=?, QRCODEURL=?, EINVOICEPDF=? WHERE DOC_NO=?";
           
           try (Connection connection = DriverManager.getConnection(OraDBConnection.OrclConnection());
                OraclePreparedStatement command = (OraclePreparedStatement) connection.prepareStatement(commandText)) {

               command.setString(1, obj.result.Irn);
               command.setString(2, obj.result.AckNo);
               command.setString(3, obj.result.AckDt);
               command.setString(4, obj.result.SignedQRCode);
               command.setClob(5, obj.result.SignedInvoice);
               command.setString(6, obj.result.EwbNo);
               command.setString(7, obj.result.EwbDt);
               command.setString(8, obj.result.EwbValidTill);
               command.setString(9, "");
               command.setString(10, "");
               command.setString(11, dt.get(0).get("DOC_No").toString());

               connection.setAutoCommit(false);
               int rowsAffected = command.executeUpdate();
               connection.commit();
               // System.out.println("RowsAffected: " + rowsAffected);
           }
       }
    
    
}
