package org.lattice.spectrum_backend_final.dao.manager;

import com.lattice.spectrum.ComLibrary.utility.sLog;
import org.lattice.spectrum_backend_final.beans.NotesDescSetup;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.DbConstant;
import org.lattice.spectrum_backend_final.dao.util.NotesDescMapper;
import org.lattice.spectrum_backend_final.dao.util.NotesDescSetupMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotesDescSetupManager {
    private NotesDescSetup notesDescSetup;

    public NotesDescSetup getNotesDescSetup() {
        return notesDescSetup;
    }

    public void setNotesDescSetup(NotesDescSetup notesDescSetup) {
        this.notesDescSetup = notesDescSetup;
    }

    public void saveNotesDescSetupToDb() {
        long trialLogId;
        if(notesDescSetup == null){
            throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }

        final Connection conn = DbConnectionManager.getInstance().getConnection();
        PreparedStatement saveNotesDescSetupPS = null;
        NotesDescSetupMapper notesDescSetupMapper = new NotesDescSetupMapper();
        try{
            conn.setAutoCommit(false);
            trialLogId = LogManager.getInstance().insertTrialLogGetLogId(
                    0,
                    TrialManager.getInstance().getTrialRunSettingId(),
                    ApiConstant.BLANK_QUOTE,
                    ApiConstant.LOG_TYPE_TRIAL_AUTO_LOG,
                    conn
            );

            notesDescSetup.setTrialLogId(trialLogId);
            sLog.d(this, notesDescSetup);
            saveNotesDescSetupPS = conn.prepareStatement(DbConstant.SAVE_NOTES_DESCRIPTION_SETUP_QUERY);
            saveNotesDescSetupPS = notesDescSetupMapper.saveNotesDescriptionSetupMapper(notesDescSetup, saveNotesDescSetupPS);
            saveNotesDescSetupPS.executeUpdate();
            conn.commit();

        } catch (Exception ex) {
            ex.printStackTrace();
            if(conn != null){
                try {
                    conn.rollback();
                } catch (SQLException sqlEx) {
                    sqlEx.printStackTrace();
                }
            }
//            throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }finally {
            DbConnectionManager.closeDBConnection(null, saveNotesDescSetupPS, conn);
        }
    }
}
