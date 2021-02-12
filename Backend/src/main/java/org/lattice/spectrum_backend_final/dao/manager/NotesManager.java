package org.lattice.spectrum_backend_final.dao.manager;

import org.json.JSONArray;
import org.lattice.spectrum_backend_final.beans.Notes;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.DbConstant;
import org.lattice.spectrum_backend_final.dao.util.NotesMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotesManager {

    private NotesMapper notesMapper = new NotesMapper();

    public void saveNotes(Notes notes) {

        Connection conn = null;
        PreparedStatement saveNotesPS = null;

        try {
            if(
                    ApiConstant.BLANK_QUOTE.equals(notes.getNotes()) ||
                    ApiConstant.BLANK_SPACE.equals(notes.getNotes()) ||
                    notes.getNotes() == null
            ){
                return;
            }
            notes.setPostedBy(DbConnectionManager.getInstance().getTokenManager().getUsername());
            conn = DbConnectionManager.getInstance().getConnection();
            conn.setAutoCommit(false);

            saveNotesPS = conn.prepareStatement(DbConstant.ADD_TRIAL_LOG_QUERY);
            notesMapper.saveNotesStatementMapper(notes, saveNotesPS)
                    .executeUpdate();

            conn.commit();

        } catch (Exception ex) {

            try {
                if(conn != null)
                    conn.rollback();
            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            }
            ex.printStackTrace();
            throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } finally {
            DbConnectionManager.closeDBConnection(null, saveNotesPS, conn);
        }

    }

    public void saveNotesDS(Notes notes, Connection conn) {

        PreparedStatement saveNotesPS = null;

        try {
            if(
                    ApiConstant.BLANK_QUOTE.equals(notes.getNotes()) ||
                            ApiConstant.BLANK_SPACE.equals(notes.getNotes()) ||
                            notes.getNotes() == null
            ){
                return;
            }
            notes.setPostedBy(DbConnectionManager.getInstance().getTokenManager().getUsername());
            conn.setAutoCommit(false);

            saveNotesPS = conn.prepareStatement(DbConstant.ADD_TRIAL_LOG_QUERY);
            notesMapper.saveNotesStatementMapper(notes, saveNotesPS)
                    .executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }
    }

}
