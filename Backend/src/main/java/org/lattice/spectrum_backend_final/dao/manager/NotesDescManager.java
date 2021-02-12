package org.lattice.spectrum_backend_final.dao.manager;

import com.lattice.spectrum.ComLibrary.utility.sLog;
import org.lattice.spectrum_backend_final.beans.Notes;
import org.lattice.spectrum_backend_final.beans.NotesDescription;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.DbConstant;
import org.lattice.spectrum_backend_final.dao.util.NotesDescMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotesDescManager {
    private NotesDescription notesDescription;

    public void setNotesDescription(NotesDescription notesDescription){
        this.notesDescription = notesDescription;
    }

    public NotesDescription getNotesDescription() {
        return notesDescription;
    }

    public void saveNotesDescriptionToDb() {
        long trialLogId;
        if(notesDescription == null){
            throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }

        final Connection conn = DbConnectionManager.getInstance().getConnection();
        PreparedStatement saveNotesDescPS = null;
        NotesDescMapper notesDescMapper = new NotesDescMapper();
        try{
            conn.setAutoCommit(false);
            trialLogId = LogManager.getInstance().insertTrialLogGetLogId(
                    0,
                    TrialManager.getInstance().getTrialRunSettingId(),
                    ApiConstant.BLANK_QUOTE,
                    ApiConstant.LOG_TYPE_TRIAL_AUTO_LOG,
                    conn
            );

            notesDescription.setTrialLogId(trialLogId);
            sLog.d(this, notesDescription);
            saveNotesDescPS = conn.prepareStatement(DbConstant.SAVE_NOTES_DESCRIPTION_QUERY);
            saveNotesDescPS = notesDescMapper.saveNotesDescriptionMapper(notesDescription, saveNotesDescPS);
            saveNotesDescPS.executeUpdate();
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
            DbConnectionManager.closeDBConnection(null, saveNotesDescPS, conn);
        }
    }
}
