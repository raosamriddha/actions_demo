package org.lattice.spectrum_backend_final.dao.manager;

import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.DbConstant;
import org.lattice.spectrum_backend_final.dao.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CfrManager {


    /**
     * Updates cfr status for internal use.
     */
    public void update21CfrStatus() {

        final Connection conn = DbConnectionManager.getInstance().getConnection();
        PreparedStatement update21CfrStatusPS = null;
        ResultSet update21CfrStatusRS = null;

        try {
            update21CfrStatusPS = conn.prepareStatement(DbConstant.GET_21_CFR_STATUS);
            update21CfrStatusRS = update21CfrStatusPS.executeQuery();

            if (update21CfrStatusRS.next()) {

                DbConnectionManager.getInstance()
                        .getCfrStatus()
                        .setStatusId(update21CfrStatusRS.getInt(ApiConstant.STATUS_ID));

                String status = PasswordUtil.decrypt(
                        update21CfrStatusRS.getString(ApiConstant.STATUS),
                        ApiConstant.SECRET
                );

                DbConnectionManager.getInstance()
                        .getCfrStatus()
                        .setStatus(Integer.parseInt(status));

            } else {
                throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public int get21CfrStatus() throws Exception {

    	// 0 - KFComm2
    	// 1 - KFComm2C
        final Connection conn = DbConnectionManager.getInstance().getConnection();
        PreparedStatement get21CfrStatusPS = null;
        ResultSet get21CfrStatusRS = null;
        int status;


        get21CfrStatusPS = conn.prepareStatement(DbConstant.GET_21_CFR_STATUS);
        get21CfrStatusRS = get21CfrStatusPS.executeQuery();

        if (get21CfrStatusRS.next()) {

            String statusString = PasswordUtil.decrypt(
                    get21CfrStatusRS.getString(ApiConstant.STATUS),
                    ApiConstant.SECRET
            );

            status = Integer.parseInt(statusString);
        } else {
            throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }


        return status;


    }
}
