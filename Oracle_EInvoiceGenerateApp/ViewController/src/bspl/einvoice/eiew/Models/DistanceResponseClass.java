package bspl.einvoice.eiew.Models;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DistanceResponseClass {

    public static String GoogleDistAPI(int frompin, int topin) {
        String InvRData = "";
        String InvResult = "";
        int dist = 0;
        try {
            String URL = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + frompin + "&destinations=" + topin + "&key=AIzaSyBi_rPFuYXG-77gPrtzAh-IHqqLmrlrrAs";
            ClsDynamic.WriteLog(URL);
            HttpURLConnection connection = (HttpURLConnection) new URL(URL).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() == 200) {
                Scanner scanner = new Scanner(connection.getInputStream());
                while (scanner.hasNext()) {
                    InvResult += scanner.nextLine();
                }
                scanner.close();

                ObjectMapper mapper = new ObjectMapper();
                Root value2 = mapper.readValue(InvResult, Root.class);
                if ("OK".equals(value2.status)) {
                    InvRData = String.valueOf(value2.rows.get(0).elements.get(0).distance.value);
                    dist = (int) (Double.parseDouble(InvRData) / 1000);
                }
            }
            return InvRData = String.valueOf(dist);
        } catch (IOException ex) {
            ClsDynamic.WriteLog(ex.getMessage());
            return InvRData = "0";
        }
    }

    public static String EwaybillDistance(int frompin, int topin, String AccessToken) {
        String InvRData = "";
        String InvResult = "";
        try {
            String URL = "http://clientbasic.mastersindia.co/distance?access_token=" + AccessToken + "&fromPincode=" + frompin + "&toPincode=" + topin;
            HttpURLConnection connection = (HttpURLConnection) new URL(URL).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            if (connection.getResponseCode() == 200) {
                Scanner scanner = new Scanner(connection.getInputStream());
                while (scanner.hasNext()) {
                    InvResult += scanner.nextLine();
                }
                scanner.close();

                ObjectMapper mapper = new ObjectMapper();
                MIDistanceRoot DistData = mapper.readValue(InvResult, MIDistanceRoot.class);
                if ("Success".equals(DistData.results.status)) {
                    InvRData = String.valueOf(DistData.results.distance);
                }
            }
            return InvRData;
        } catch (IOException ex) {
            ClsDynamic.WriteLog(ex.getMessage());
            return InvRData = "";
        }
    }
}

class Distance {
    public String text;
    public int value;
}

class Duration {
    public String text;
    public int value;
}

class Element {
    public Distance distance;
    public Duration duration;
    public String status;
}

class Row {
    public List<Element> elements;
}

class Root {
    public List<String> destination_addresses;
    public List<String> origin_addresses;
    public List<Row> rows;
    public String status;
}

class MIDistanceResults {
    public int distance;
    public String status;
    public int code;
}

class MIDistanceRoot {
    public MIDistanceResults results;
}