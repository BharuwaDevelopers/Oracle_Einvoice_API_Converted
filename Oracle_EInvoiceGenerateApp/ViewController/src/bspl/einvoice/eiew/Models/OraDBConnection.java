package bspl.einvoice.eiew.Models;



public class OraDBConnection {
    static String userId = "";
    static String password = "";
    static String dataSource = "";

    public static void setConnectionData(String userId, String pwd, String dataSource) {
        OraDBConnection.userId = userId;
        OraDBConnection.password = pwd;
        OraDBConnection.dataSource = dataSource;
    }

    public static String getOrclConnection() {
        String oraConn = "User Id=" + userId + ";Password=" + password + ";" + "Data source=" + dataSource;
        // String oraConn = "User Id=FINANCE;Password=" + password + ";" + "Data source=" + dataSource + "";
        // String oraConn = "User Id=FINANCE;Password=Fin;" + "Data source=192.168.1.13:1521/KUMA";
        // oraConn = MawaiConnection;
        // oraConn = System.Configuration.ConfigurationManager.ConnectionStrings["ConnectionStrings: DefaultConnection"].ToString();
        // var builder = new ConfigurationBuilder().SetBasePath(Directory.GetCurrentDirectory()).AddJsonFile("appsettings.json", optional: true, reloadOnChange: true);
        // IConfigurationRoot configuration = builder.Build();
        // String oraConn = configuration.GetConnectionString("DefaultConnection");
        return oraConn;
    }
}


