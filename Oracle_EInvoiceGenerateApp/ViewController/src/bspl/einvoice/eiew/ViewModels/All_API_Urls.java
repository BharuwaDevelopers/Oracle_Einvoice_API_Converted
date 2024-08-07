package bspl.einvoice.eiew.ViewModels;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;

public class All_API_Urls {
    public static String EinvLoginUrl = "https://clientbasic.mastersindia.co/oauth/access_token";
    public static String EinvGenerateUrl = "https://clientbasic.mastersindia.co/generateEinvoice";
    public static String EinvcancelUrl = "https://clientbasic.mastersindia.co/cancelEinvoice";
    public static String EinvGetUrl = "https://clientbasic.mastersindia.co/getEinvoiceData?";
    public static String EwbByIrnUrl = "https://clientbasic.mastersindia.co/generateEwaybillByIrn";
    public static String bulkeinvoice = "https://clientbasic.mastersindia.co/bulkEinvoiceGenerate";

    public static String EWBPROD_Auth_API = "https://pro.mastersindia.co/oauth/access_token";
    public static String Reg_User_MI_API = "https://pro.mastersindia.co/bussiness/checkGstin";

    public static String EWBGenerateUrl = "https://pro.mastersindia.co/ewayBillsGenerate";
    public static String CancelEWBUrl = "https://pro.mastersindia.co/ewayBillCancel";

    public static String Upd_Vno_EWBUrl = "https://clientbasic.mastersindia.co/updateVehicleNumber";
    public static String Consolidated_EWBUrl = "https://clientbasic.mastersindia.co/consolidatedEwayBillsGenerate";
    public static String Reject_EWBUrl = "https://clientbasic.mastersindia.co/ewayBillReject";
    public static String Get_EWBUrl = "https://clientbasic.mastersindia.co/getEwayBillData?";
    public static String Bulk_EWBUrl = "https://clientbasic.mastersindia.co/bulkEwayBillsGenerate";
    public static String Delete_Hold_EWBUrl = "https://clientbasic.mastersindia.co/deleteHoldEwayBill";
    public static String Extend_Validity_EWBUrl = "https://clientbasic.mastersindia.co/ewayBillValidityExtend";
    public static String Update_TransporterId_EWBUrl = "https://clientbasic.mastersindia.co/transporterIdUpdate";
    public static String Calculate_Dist_EWBUrl = "http://clientbasic.mastersindia.co/distance?";

    public static void ignoreBadCertificates() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
