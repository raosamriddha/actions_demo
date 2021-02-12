package org.lattice.spectrum_backend_final.dao.manager;


import com.lattice.spectrum.ComLibrary.utility.sLog;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.TrialBuffer;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.DbConstant;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.BufferMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BufferManager {

    private static BufferManager bufferManager;

    private BufferMapper bufferMapper = new BufferMapper();

    private TrialBuffer[] trialBuffer = new TrialBuffer[ApiConstant.BUFFER_SIZE];

    private PreparedStatement liveDataPS;
    private int index = 0;

    public static BufferManager getInstance() {

        synchronized (BufferManager.class) {
            if (bufferManager == null) {
                bufferManager = new BufferManager();
            }
        }

        return bufferManager;
    }

    public BufferManager() {
    }

    public int getIndex() {
        return index;
    }


    public void setIndex(int index) {
        this.index = index;
    }

    public TrialBuffer[] getTrialBuffer() {
        return trialBuffer;
    }

    public void setTrialBuffer(TrialBuffer[] trialBuffer) {
        this.trialBuffer = trialBuffer;
    }

    public void executeBuffer() throws SQLException {

        int localIndex;
        for (localIndex = 0; localIndex < ApiConstant.BUFFER_SIZE; localIndex++) {


            liveDataPS = bufferMapper.bufferToStatementMapper(liveDataPS, trialBuffer, localIndex);
            liveDataPS.addBatch();
        }


            if (localIndex == ApiConstant.BUFFER_SIZE) {
                try {

                    liveDataPS.executeBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }


        }

    }

    public void executeBufferLeftData() throws SQLException {

        int localIndex;
        int flag = 0;

        Connection conn = null;
        PreparedStatement liveDataPS1 = null;

        try {

            if (trialBuffer[0] == null) {
                BasicUtility.systemPrint("inside", "null1");
                return;
            }

            conn = DbConnectionManager.getInstance().getConnection();
            liveDataPS1 = conn.prepareStatement(DbConstant.SAVE_LIVE_DATA_TO_DB);


            for (localIndex = 0; localIndex < ApiConstant.BUFFER_SIZE; localIndex++) {

                if (trialBuffer[localIndex] == null) {
                    flag = 1;
                    BasicUtility.systemPrint("inside", "null2");
                    break;
                }

                liveDataPS1 = bufferMapper.bufferToStatementMapper(liveDataPS1, trialBuffer, localIndex);

                liveDataPS1.addBatch();

            }

            if (flag == 1) {

                sLog.d(this, trialBuffer);
                BasicUtility.systemPrint("execute", "buffer");
                liveDataPS1.executeBatch();
                trialBuffer = new TrialBuffer[ApiConstant.BUFFER_SIZE];

            }


        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DbConnectionManager.closeDBConnection(null, liveDataPS1, conn);
        }

    }

    public synchronized void pushIntoBuffer(JSONObject liveDataJson) throws Exception {


        if (trialBuffer[index] == null) {
            trialBuffer[index] = new TrialBuffer();
        }



        trialBuffer[index] = bufferMapper.jsonToBufferMapper(liveDataJson, trialBuffer, index);
        index++;

        if (index == ApiConstant.BUFFER_SIZE) {
            index = 0;
            Connection conn = null;

            try {

                conn = DbConnectionManager.getInstance().getConnection();
                liveDataPS = conn.prepareStatement(DbConstant.SAVE_LIVE_DATA_TO_DB);
                executeBuffer();

                //flushing the buffer
                trialBuffer = new TrialBuffer[ApiConstant.BUFFER_SIZE];

            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                DbConnectionManager.closeDBConnection(null, liveDataPS, conn);
            }

        }

    }

    public void setPausedToBuffer(int isPaused){
        int localIndex;
        for (localIndex = 0; localIndex < ApiConstant.BUFFER_SIZE; localIndex++) {

            if (trialBuffer[localIndex] != null) {
                if(localIndex+1 == ApiConstant.BUFFER_SIZE ){
                }else if(trialBuffer[localIndex+1] == null){
                    BasicUtility.systemPrint("inside", "isPausedBuffer");
                    trialBuffer[localIndex].setIsPaused(isPaused);
                    break;
                }
            }

        }
    }

}
