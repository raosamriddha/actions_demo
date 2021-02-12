package org.lattice.spectrum_backend_final.dao.manager;

import com.lattice.spectrum.ComLibrary.utility.sLog;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.DbConstant;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.property.ResourceManager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.prefs.Preferences;

/**
 * This class manages all the registry operations to avoid piracy.
 */
public class RegistryManager {

    /**
     * Used to modify values in the HKEY_CURRENT_USERS (HKCU) registry.
     */
    private static final Preferences prefsLocal = Preferences.userRoot();

    /**
     * Used to modify values in the HKEY_LOCAL_MACHINE (HKLM) registry.
     */
    private static final Preferences prefsGlobal = Preferences.systemRoot();

    /**
     * Creates an entry set (key-value pair) in the HKEY_CURRENT_USERS (HKCU) registry.
     * @param key key of the entry set.
     * @param value value of the entry set.
     */
    public static void createLocal(String key, String value){
        prefsLocal.put(key, value);
    }

    /**
     * Creates an entry set (key-value pair) in the HKEY_LOCAL_MACHINE (HKLM) registry.
     * @param key key of the entry set.
     * @param value value of the entry set.
     */
    public static void createGlobal(String key, String value) throws IOException {
        prefsGlobal.put(key, value);
    }



    /**
     * Get the value from HKEY_CURRENT_USERS (HKCU) if the key is present, else returns default set value.
     * @param key key of the entry set.
     * @param defaultValue default value to return if key not present.
     */
    public static String getLocal(String key, String defaultValue){
        return prefsLocal.get(key, defaultValue);
    }

    /**
     * Get the value from HKEY_LOCAL_MACHINE (HKLM) if the key is present, else returns default set value.
     * @param key key of the entry set.
     * @param defaultValue default value to return if key not present.
     */
    public static String getGlobal(String key, String defaultValue){
        return prefsGlobal.get(key, defaultValue);
    }


    /**
     * Delete the entry set related to key from HKEY_CURRENT_USERS (HKCU).
     * @param key key of the entry set.
     */
    public static void deleteLocal(String key){
        prefsLocal.remove(key);
    }

    /**
     * Delete the entry set related to key from HKEY_LOCAL_MACHINE (HKLM).
     * @param key key of the entry set.
     */
    public static void deleteGlobal(String key){
        prefsGlobal.remove(key);
    }


    /**
     * Get registry hash from database.
     * @return returns registry hash.
     * @throws Exception if any exception occurs.
     */
    private static String getRegistryDbHash() throws Exception {
        Connection conn = null;
        PreparedStatement getRegistryDbHashPS = null;
        ResultSet getRegistryDbHashRS = null;
        String registryHash = null;

        try {
            conn = DbConnectionManager.getInstance().getConnection();
            getRegistryDbHashPS = conn.prepareStatement(DbConstant.GET_REGISTRY_HASH_QUERY);
            getRegistryDbHashRS = getRegistryDbHashPS.executeQuery();
            if(getRegistryDbHashRS.next()){
                registryHash = getRegistryDbHashRS.getString(1);
            }
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }finally {
            DbConnectionManager.closeDBConnection(null, getRegistryDbHashPS, conn);
        }
        return registryHash;
    }

    /**
     * Create an entry of registry hash into db
     * @throws Exception if any exception occurs.
     */
    private static void createRegistryHashIntoDb(String registryHash) throws Exception {
        Connection conn = null;
        PreparedStatement createRegistryHashPS = null;

        try {
            conn = DbConnectionManager.getInstance().getConnection();
            createRegistryHashPS = conn.prepareStatement(DbConstant.UPDATE_REGISTRY_HASH_QUERY);
            createRegistryHashPS.setString(1, registryHash);
            createRegistryHashPS.executeUpdate();
        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }finally {
            DbConnectionManager.closeDBConnection(null, createRegistryHashPS, conn);
        }
    }

    /**
     * create an entry in registry as well as in db.
     * @throws Exception if any exception occurs.
     */
    public static void createRegistryHash() throws Exception {
        String registryHash = BasicUtility.generateRandomName(32);
        createLocal(ResourceManager.getProperty(ApiConstant.REGISTRY_HASH_KEY), registryHash);
        createGlobal(ResourceManager.getProperty(ApiConstant.REGISTRY_HASH_KEY), registryHash);
        createRegistryHashIntoDb(registryHash);
    }

    /**
     * Check weather a software is pirated or not.
     * @return true if not pirated else false.
     * @throws Exception if any exception occurs.
     */
    public static boolean isSoftwarePirated() throws Exception {
        String registryLocalHash = null;
        String registryGlobalHash = null;
        String registryDbHash = null;
        registryLocalHash = getLocal(ResourceManager.getProperty(ApiConstant.REGISTRY_HASH_KEY), null);
        registryGlobalHash = getGlobal(ResourceManager.getProperty(ApiConstant.REGISTRY_HASH_KEY), null);
        if(registryGlobalHash == null && registryLocalHash == null){
            return true;
        }
        registryDbHash = getRegistryDbHash();
        // if hash is not present in local but present in Global then copy it into local
        if(registryLocalHash == null){
            createLocal(ResourceManager.getProperty(ApiConstant.REGISTRY_HASH_KEY), registryGlobalHash);
            registryLocalHash = getLocal(ResourceManager.getProperty(ApiConstant.REGISTRY_HASH_KEY), null);
        }
        // if hash is present in both local and global but they are unequal
        // then replace global/local if any one is correct
        else if(!registryLocalHash.equals(registryGlobalHash)){
            // to handle case for first time install with Global hash null
            if(registryLocalHash.equals(registryDbHash)){
                createGlobal(ResourceManager.getProperty(ApiConstant.REGISTRY_HASH_KEY), registryLocalHash);
            }
            // to handle case when another user install for first time
            else if(registryDbHash.equals(registryGlobalHash)){
                createLocal(ResourceManager.getProperty(ApiConstant.REGISTRY_HASH_KEY), registryGlobalHash);
                registryLocalHash = getLocal(ResourceManager.getProperty(ApiConstant.REGISTRY_HASH_KEY), null);
            }
        }

        if(registryDbHash.equals(registryLocalHash)){
            return false;
        }
        return true;
    }

}