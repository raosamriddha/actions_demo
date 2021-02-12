package org.lattice.spectrum_backend_final.dao.manager;

import com.lattice.spectrum.ComLibrary.ComLib;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.DbConstant;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.JsonMapper;
import org.lattice.spectrum_backend_final.exception.HardwareValidationException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FirmwareManager {

    public static JSONObject getFirmwareVersionFromDb() throws SQLException {

        Connection conn = null;
        PreparedStatement getFirmwareVersionPS = null;
        ResultSet getFirmwareVersionRS = null;
        JSONArray firmwareVersionJsonArray = null;

        try {
            conn = DbConnectionManager.getInstance().getConnection();
            getFirmwareVersionPS = conn.prepareStatement(DbConstant.GET_FIRMWARE_VERSION_QUERY);

            getFirmwareVersionRS = getFirmwareVersionPS.executeQuery();

            firmwareVersionJsonArray = JsonMapper.getJSONFromResultSet(getFirmwareVersionRS);

            if(firmwareVersionJsonArray.length() == 0){
                throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            }

            return firmwareVersionJsonArray.getJSONObject(0);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } finally {
            DbConnectionManager.closeDBConnection(getFirmwareVersionRS, getFirmwareVersionPS, conn);
        }
    }

    public void updateFirmwareVersionIntoDb(int softwareVersion, String interfaceVersion) throws Exception {
        Connection conn = null;
        PreparedStatement updateFirmwareVersionPS = null;

        try {
            conn = DbConnectionManager.getInstance().getConnection();

            updateFirmwareVersionPS = conn.prepareStatement(DbConstant.UPDATE_FIRMWARE_VERSION_QUERY);
            updateFirmwareVersionPS.setInt(1, softwareVersion);
            updateFirmwareVersionPS.setString(2, interfaceVersion);
            updateFirmwareVersionPS.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } finally {
            DbConnectionManager.closeDBConnection(null, updateFirmwareVersionPS, conn);
        }
    }



    public boolean isCompatibleFirmwareVersion() throws SQLException, HardwareValidationException {
        int kfcommSoftwareVersion  ;
        String kfcommInterfaceVersion ;
        int deviceSoftwareVersion ;
        String deviceInterfaceVersion;
        JSONObject firmwareVersionJson = null;


        BasicUtility.systemPrint("isconnected", ComLib.get().isConnected());
        if(!ComLib.get().isConnected()){
            throw new HardwareValidationException(ApiConstant.COM_PORT_CONNECTION_ERROR_MESSAGE);
        }

        deviceSoftwareVersion = ComLib.get().getMainPump().getMainCpuSoftwareVersion();
        deviceInterfaceVersion = ComLib.get().getMainPump().getInterfaceProcessorSoftwareVersion();

        BasicUtility.systemPrint("devSoft", deviceSoftwareVersion);
        BasicUtility.systemPrint("devInt", deviceInterfaceVersion);

        firmwareVersionJson = getFirmwareVersionFromDb();

        kfcommSoftwareVersion = firmwareVersionJson.getInt(ApiConstant.SOFTWARE_VERSION);
        kfcommInterfaceVersion = firmwareVersionJson.getString(ApiConstant.INTERFACE_VERSION);

        BasicUtility.systemPrint("kfcommSoft", kfcommSoftwareVersion);
        BasicUtility.systemPrint("kfcommInt", kfcommInterfaceVersion);

        if(
                (deviceSoftwareVersion == kfcommSoftwareVersion) &&
                        (
                                deviceInterfaceVersion.equals(kfcommInterfaceVersion) ||
                                deviceInterfaceVersion.equals("0.6.4")
                        )
        ){
            return true;
        }
        return false;
    }

    public void updateFirmwareOnDevice() throws Exception {

        String currentDir = System.getProperty(ApiConstant.USER_DIRECTORY);

        Runtime.getRuntime().exec(
                currentDir + ApiConstant.CLOSE_FRONT_END_FILE_PATH);

        Thread.sleep(1000);

        Runtime.getRuntime().exec(
                currentDir + ApiConstant.UPDATE_FIRMWARE_FILE_PATH);

    }


}
