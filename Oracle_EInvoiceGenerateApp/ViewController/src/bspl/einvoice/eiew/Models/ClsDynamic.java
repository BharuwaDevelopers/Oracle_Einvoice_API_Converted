package bspl.einvoice.eiew.Models;


import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

//package Einv_EwayBill_WebApp.Models;

public class ClsDynamic {

    public static String generateUniqueNumber() {
        long j = 1;
        for (byte b : UUID.randomUUID().toString().getBytes()) {
            j *= ((int) b + 1);
        }
        String finalString = String.format("%x", j - System.currentTimeMillis());
        return finalString;
    }

    public static void deleteFiles(String dirName) throws IOException {
        Path dirPath = Paths.get(dirName);
        DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath);

        for (Path entry : stream) {
            File file = entry.toFile();
            if (file.isFile()) {
                if (file.lastModified() < System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000) {
                    file.delete();
                }
            }
        }
    }

    public static void writeLog(String ex) throws IOException {
        String logFilePath = Paths.get(System.getProperty("user.dir"), "ErrorLogs").toString();
        String logFile = logFilePath + "\\" + UUID.randomUUID().toString() + ".txt";
        String delPath = Paths.get(System.getProperty("user.dir"), "ErrorLogs").toString();
        deleteFiles(delPath);

        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write("Error Date :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
            writer.write("Exception :" + ex + "\n");
        }
    }

    public static void writeLog(String ex, String docName) throws IOException {
        String logFilePath = Paths.get(System.getProperty("user.dir"), "ErrorLogs").toString();
        String logFile = logFilePath + "\\" + docName + ".txt";
        String delPath = Paths.get(System.getProperty("user.dir"), "ErrorLogs").toString();
        deleteFiles(delPath);

        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write("Error Date :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
            writer.write("Exception :" + ex + "\n");
        }
    }

    public static void jsonLog(String ex, String docName) throws IOException {
        String logFilePath = Paths.get(System.getProperty("user.dir"), "JsonLogs").toString();
        String logFile = logFilePath + "\\" + docName + ".txt";
        String delPath = Paths.get(System.getProperty("user.dir"), "JsonLogs").toString();
        deleteFiles(delPath);

        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write("Error Date :" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n");
            writer.write("Exception :" + ex + "\n");
        }
    }

    public static String updateErrorLog(String errorData, String docNo) {
        String rval = "";
        String dQuote = "'";
        String sqlStr = "update einvoice_generate_temp set ERRORMSG='" + errorData.replace(dQuote, "") + "' where DOC_NO='" + docNo + "'";
        int i = DataLayer.executeNonQuery(OraDBConnection.OrclConnection(), sqlStr);
        return rval = Integer.toString(i);
    }
}
