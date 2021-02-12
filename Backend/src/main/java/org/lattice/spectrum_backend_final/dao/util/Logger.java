package org.lattice.spectrum_backend_final.dao.util;

import java.io.File;
import java.io.PrintStream;

import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.OperationMode;

public class Logger {

    // Store current System.out before assigning a new value
    private static PrintStream console = System.out;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void setTrialRunOutputLogToFile(OperationMode modeName, boolean setLogToFile){
//        PrintStream toFile = new PrintStream(new FileOutputStream("C:/Users/RAHUL/Desktop/Log"));

        try{

            console = System.out;
            String path = "./log/auto_"+modeName+"_"+BasicUtility.getInstance().getCurrentTimestamp()+".log";

            path = path.replaceAll("[()?:!,;{}]+", "_");

            BasicUtility.systemPrint("path", path);



            PrintStream toFile = new PrintStream(new File(path));

            if(setLogToFile){
                System.setOut(toFile);
                System.setErr(toFile);
            }else{
                System.setOut(console);
                System.setErr(console);
            }
            // Add kfcomm software, interface and firmware version.
            sLog.d(DbConnectionManager.getInstance().getKfCommVersion());
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }

    public static void setRecipeOutputLogToFile(boolean setLogToFile){
//        PrintStream toFile = new PrintStream(new FileOutputStream("C:/Users/RAHUL/Desktop/Log"));

        try{

            console = System.out;
            String path = "./log/recipe_"+BasicUtility.getInstance().getCurrentTimestamp()+".log";

            path = path.replaceAll("[()?:!,;{}]+", "_");

            BasicUtility.systemPrint("path", path);



            PrintStream toFile = new PrintStream(new File(path));

            if(setLogToFile){
                System.setOut(toFile);
                System.setErr(toFile);
            }else{
                System.setOut(console);
                System.setErr(console);
            }
            // Add kfcomm software, interface and firmware version.
            sLog.d(DbConnectionManager.getInstance().getKfCommVersion());
        }catch (Exception ex){
        ex.printStackTrace();
    }


    }

    public static void setAutoConnectLogToFile(boolean setLogToFile) {
//        PrintStream toFile = new PrintStream(new FileOutputStream("C:/Users/RAHUL/Desktop/Log"));

        try{

            console = System.out;
            String path = "./log/auto_connect_"+BasicUtility.getInstance().getCurrentTimestamp()+".log";

            path = path.replaceAll("[()?:!,;{}]+", "_");

            BasicUtility.systemPrint("path", path);



            PrintStream toFile = new PrintStream(new File(path));

            if(setLogToFile){
                System.setOut(toFile);
                System.setErr(toFile);
            }else{
                System.setOut(console);
                System.setErr(console);
            }
            // Add kfcomm software, interface and firmware version.
            sLog.d(DbConnectionManager.getInstance().getKfCommVersion());
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }


    public static void setManualLogToFile(boolean setLogToFile) {
//        PrintStream toFile = new PrintStream(new FileOutputStream("C:/Users/RAHUL/Desktop/Log"));

        try{

            console = System.out;
            String path = "./log/manual_"+BasicUtility.getInstance().getCurrentTimestamp()+".log";

            path = path.replaceAll("[()?:!,;{}]+", "_");

            BasicUtility.systemPrint("path", path);



            PrintStream toFile = new PrintStream(new File(path));

            if(setLogToFile){
                System.setOut(toFile);
                System.setErr(toFile);
            }else{
                System.setOut(console);
                System.setErr(console);
            }
            // Add kfcomm software, interface and firmware version.
            sLog.d(DbConnectionManager.getInstance().getKfCommVersion());
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }

    public static void setSAALogToFile(boolean setLogToFile) {
//        PrintStream toFile = new PrintStream(new FileOutputStream("C:/Users/RAHUL/Desktop/Log"));

        try{

            console = System.out;
            String path = "./log/user_"+BasicUtility.getInstance().getCurrentTimestamp()+".log";

            path = path.replaceAll("[()?:!,;{}]+", "_");

            BasicUtility.systemPrint("path", path);



            PrintStream toFile = new PrintStream(new File(path));

            if(setLogToFile){
                System.setOut(toFile);
                System.setErr(toFile);
            }else{
                System.setOut(console);
                System.setErr(console);
            }
            // Add kfcomm software, interface and firmware version.
            sLog.d(DbConnectionManager.getInstance().getKfCommVersion());
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }

    public static void setPressureCalibrationLogToFile(boolean setLogToFile){
//      PrintStream toFile = new PrintStream(new FileOutputStream("C:/Users/RAHUL/Desktop/Log"));

        try{

            console = System.out;
            String path = "./log/pressureCal_"+BasicUtility.getInstance().getCurrentTimestamp()+".log";

            path = path.replaceAll("[()?:!,;{}]+", "_");

            BasicUtility.systemPrint("path", path);



            PrintStream toFile = new PrintStream(new File(path));

            if(setLogToFile){
                System.setOut(toFile);
                System.setErr(toFile);
            }else{
                System.setOut(console);
                System.setErr(console);
            }
            // Add kfcomm software, interface and firmware version.
            sLog.d(DbConnectionManager.getInstance().getKfCommVersion());
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }

    public static void setTubingCalibrationLogToFile(boolean setLogToFile){
//      PrintStream toFile = new PrintStream(new FileOutputStream("C:/Users/RAHUL/Desktop/Log"));

        try{

            console = System.out;
            String path = "./log/tubingCal_"+BasicUtility.getInstance().getCurrentTimestamp()+".log";

            path = path.replaceAll("[()?:!,;{}]+", "_");

            BasicUtility.systemPrint("path", path);



            PrintStream toFile = new PrintStream(new File(path));

            if(setLogToFile){
                System.setOut(toFile);
                System.setErr(toFile);
            }else{
                System.setOut(console);
                System.setErr(console);
            }
            // Add kfcomm software, interface and firmware version.
            sLog.d(DbConnectionManager.getInstance().getKfCommVersion());
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }


    public static void setLicenceLogToFile(boolean setLogToFile) {
//      PrintStream toFile = new PrintStream(new FileOutputStream("C:/Users/RAHUL/Desktop/Log"));

        try{

            console = System.out;
            String path = "./log/licence_"+BasicUtility.getInstance().getCurrentTimestamp()+".log";

            path = path.replaceAll("[()?:!,;{}]+", "_");

            BasicUtility.systemPrint("path", path);



            PrintStream toFile = new PrintStream(new File(path));

            if(setLogToFile){
                System.setOut(toFile);
                System.setErr(toFile);
            }else{
                System.setOut(console);
                System.setErr(console);
            }
            // Add kfcomm software, interface and firmware version.
            sLog.d(DbConnectionManager.getInstance().getKfCommVersion());
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }

    public static void setMigrationLogToFile(boolean setLogToFile) {
//      PrintStream toFile = new PrintStream(new FileOutputStream("C:/Users/RAHUL/Desktop/Log"));

        try{

            console = System.out;
            String path = "./log/migration_"+BasicUtility.getInstance().getCurrentTimestamp()+".log";

            path = path.replaceAll("[()?:!,;{}]+", "_");

            BasicUtility.systemPrint("path", path);



            PrintStream toFile = new PrintStream(new File(path));

            if(setLogToFile){
                System.setOut(toFile);
                System.setErr(toFile);
            }else{
                System.setOut(console);
                System.setErr(console);
            }
            // Add kfcomm software, interface and firmware version.
            sLog.d(DbConnectionManager.getInstance().getKfCommVersion());
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }

    public static void setDBEncryptLogToFile(final boolean setLogToFile) {

        try{

            console = System.out;
            String path = "./log/dbEncrypt_"+BasicUtility.getInstance().getCurrentTimestamp()+".log";

            path = path.replaceAll("[()?:!,;{}]+", "_");

            BasicUtility.systemPrint("path", path);
            PrintStream toFile = new PrintStream(new File(path));

            if(setLogToFile){
                System.setOut(toFile);
                System.setErr(toFile);
            }else{
                System.setOut(console);
                System.setErr(console);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }


    }

    /**
     * Used to log info.
     * @param obj Used to get current object reference.
     * @param msg Append method name and message to it.
     * @param httpStatus Response status.
     */
    public static void info(Object obj, String msg, int httpStatus) {
        System.out.println(BasicUtility.getInstance().getCurrentTimestamp() + ": " + obj.getClass().getSimpleName() +  ": INFO: "+DbConnectionManager.getInstance().getTokenManager().getUsername() + " : " + msg + ": STATUS: "+httpStatus);
    }
    
    /**
     * Used to log info.
     * @param obj Used to get current object reference.
     * @param msg Append method name and message to it.
     * @param httpStatus Response status.
     * @param obj2 Used to print json data.
     */
    public static void info(Object obj, String msg, int httpStatus, Object obj2) {
        System.out.println(BasicUtility.getInstance().getCurrentTimestamp() + ": " + obj.getClass().getSimpleName() +  ": INFO: "+DbConnectionManager.getInstance().getTokenManager().getUsername() + " : " + msg + ": STATUS: "+httpStatus+ " : " + gson.toJson(obj2));
    }

    /**
     * Used to log info.
     * @param obj Used to get current object reference.
     * @param msg Append method name and message to it.
     */
    public static void info(Object obj, String msg) {
        System.out.println(BasicUtility.getInstance().getCurrentTimestamp() + ": " + obj.getClass().getSimpleName() +  ": INFO: "+DbConnectionManager.getInstance().getTokenManager().getUsername() + " : "  + msg );
    }

    /**
     * Used to log info.
     * @param msg Append method name and message to it.
     */
    public static void info(String msg) {
        System.out.println(BasicUtility.getInstance().getCurrentTimestamp() + ": INFO: "+DbConnectionManager.getInstance().getTokenManager().getUsername() + " : "  + msg );
    }

    /**
     * Used to log error.
     * @param obj Used to get current object reference.
     * @param msg Append method name and message to it.
     * @param httpStatus Response status.
     */
    public static void error(Object obj, String msg, int httpStatus) {
        System.out.println(BasicUtility.getInstance().getCurrentTimestamp() + ": " + obj.getClass().getSimpleName() +  ": ERROR: "+DbConnectionManager.getInstance().getTokenManager().getUsername() + " : " + msg + ": STATUS: "+httpStatus);
    }

    /**
     * Used to log error.
     * @param obj Used to get current object reference.
     * @param msg Append method name and message to it.
     */
    public static void error(Object obj, String msg) {
        System.out.println(BasicUtility.getInstance().getCurrentTimestamp() + ": " + obj.getClass().getSimpleName() +  ": ERROR: "+DbConnectionManager.getInstance().getTokenManager().getUsername() + " : "  + msg );
    }

    /**
     * Used to log error.
     * @param msg Append method name and message to it.
     */
    public static void error(String msg) {
        System.out.println(BasicUtility.getInstance().getCurrentTimestamp() + ": ERROR: "+DbConnectionManager.getInstance().getTokenManager().getUsername() + " : "  + msg );
    }

    /**
     * Used to log error.
     * @param obj Used to get current object reference.
     * @param msg Append method name and message to it.
     * @param httpStatus Response status.
     * @param ex Exception object.
     */
    public static void error(Object obj, String msg, int httpStatus, Exception ex) {
        System.out.println(BasicUtility.getInstance().getCurrentTimestamp() + ": " + obj.getClass().getSimpleName() +  ": ERROR: "+DbConnectionManager.getInstance().getTokenManager().getUsername() + " : " + msg + ": STATUS: "+httpStatus);
        ex.printStackTrace();
    }

    /**
     * Used to log error.
     * @param obj Used to get current object reference.
     * @param msg Append method name and message to it.
     * @param httpStatus Response status.
     * @param msgJSON to print json.
     */
    public static void error(Object obj, String msg, int httpStatus, Object msgJSON) {
        System.out.println(BasicUtility.getInstance().getCurrentTimestamp() + ": " + obj.getClass().getSimpleName() +  ": ERROR: "+DbConnectionManager.getInstance().getTokenManager().getUsername() + " : " + msg + ": STATUS: " + httpStatus + " : " + gson.toJson(msgJSON));
    }
    
    /**
     * @param ex Exception object.
     */
    public static void error(Object obj, String msg, Exception ex) {
        System.out.println(BasicUtility.getInstance().getCurrentTimestamp() + ": " + obj.getClass().getSimpleName() +  ": ERROR: "+DbConnectionManager.getInstance().getTokenManager().getUsername() + " : " + msg);
        ex.printStackTrace();
    }

    /**
     * Used to log error.
     * @param msg Append method name and message to it.
     * @param ex Exception object.
     */
    public static void error(String msg, Exception ex) {
        System.out.println(BasicUtility.getInstance().getCurrentTimestamp() + ": ERROR: "+DbConnectionManager.getInstance().getTokenManager().getUsername() + " : " + msg );
        ex.printStackTrace();
    }

    /**
     * Used for debugging purpose.
     * @param obj1 Used to get current object reference.
     * @param msg Append method name and message to it.
     * @param obj2 Object to add in log as json format.
     */
    public static void debug(Object obj1, String msg, Object obj2) {
        System.out.println(BasicUtility.getInstance().getCurrentTimestamp() + ": " + obj1.getClass().getSimpleName() +  ": DEBUG: "+DbConnectionManager.getInstance().getTokenManager().getUsername() + " : "  + msg + " : " + gson.toJson(obj2));
    }

    /**
     * Used for debugging purpose.
     * @param obj1 Used to get current object reference.
     * @param msg Append method name and message to it.
     * @param obj2 Object to add in log as json format.
     * @param httpStatus Response status.
     */
    public static void debug(Object obj1, String msg, Object obj2, int httpStatus) {
        System.out.println(BasicUtility.getInstance().getCurrentTimestamp() + ": " + obj1.getClass().getSimpleName() +  ": DEBUG: "+DbConnectionManager.getInstance().getTokenManager().getUsername() + ": STATUS: " + httpStatus + " : "  + msg + " : " + gson.toJson(obj2));
    }

    /**
     * Used for debugging purpose.
     * @param msg Append method name and message to it.
     * @param obj Object to add in log as json format.
     */
    public static void debug(String msg, Object obj) {
        System.out.println(BasicUtility.getInstance().getCurrentTimestamp() +": DEBUG: "+DbConnectionManager.getInstance().getTokenManager().getUsername() + " : "  + msg + " : " + gson.toJson(obj));
    }

    /**
     * Used for debugging purpose.
     * @param obj Used to get current object reference.
     * @param msg Append method name and message to it.
     */
    public static void debug(Object obj, String msg) {
        System.out.println(BasicUtility.getInstance().getCurrentTimestamp() + ": " + obj.getClass().getSimpleName() +  ": DEBUG: "+DbConnectionManager.getInstance().getTokenManager().getUsername() + " : "  + msg );
    }
}
