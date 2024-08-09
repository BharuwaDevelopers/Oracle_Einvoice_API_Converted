package bspl.einvoice.eiew.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class Taxilla_OracleDataInsert {
    public Taxilla_OracleDataInsert() {
        super();
    }
    
    public static void updateDataOracle(SuccessRoot obj, DataTable dt) {
            String commandText = "UPDATE einvoice_generate_temp SET IRN=?, ACKNo=?, ACKDATE=?, SIGNEDQRCODE=?, SIGNEDINVOICE=?, EWBNO=?, EWBDT=?, EWBVALIDTILL=?, QRCODEURL=?, EINVOICEPDF=? WHERE DOC_NO=?";

            try (Connection connection = DriverManager.getConnection(OraDBConnection.getOrclConnection())) {
                PreparedStatement command = connection.prepareStatement(commandText);

                // Use set methods to assign parameters.
                command.setString(1, obj.getResult().getIrn());
                command.setString(2, obj.getResult().getAckNo());
                command.setString(3, obj.getResult().getAckDt());
                command.setString(4, obj.getResult().getSignedQRCode());
                command.setClob(5, new javax.sql.rowset.serial.SerialClob(obj.getResult().getSignedInvoice().toCharArray()));

                command.setString(6, obj.getResult().getEwbNo());
                command.setString(7, obj.getResult().getEwbDt());
                command.setString(8, obj.getResult().getEwbValidTill());
                command.setString(9, "");
                command.setString(10, "");

                command.setString(11, dt.getRows().get(0).get("DOC_No").toString());

                connection.setAutoCommit(false);
                int rowsAffected = command.executeUpdate();
                connection.commit();
                // System.out.println("RowsAffected: " + rowsAffected);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
}
