package org.lattice.spectrum_backend_final.dao.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.Notes;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.dao.manager.DeviceManager;
import org.lattice.spectrum_backend_final.dao.manager.TrialManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotesMapper {

    public PreparedStatement saveNotesStatementMapper(Notes notes,
                                                      PreparedStatement saveNotesPS) throws SQLException {

        saveNotesPS.setInt(1, DbConnectionManager.getInstance().getTokenManager().getUserId());
        saveNotesPS.setInt(2, TrialManager.getInstance().getTrialRunSettingId());
        saveNotesPS.setString(3, notes.getNotes());
        saveNotesPS.setInt(4, 0);
        saveNotesPS.setString(5, BasicUtility.getInstance().getCurrentTimestamp());
        saveNotesPS.setString(6, BasicUtility.getInstance().getCurrentTimestamp());

        return saveNotesPS;

    }

//    public JSONArray fetchNotesStatementMapper(ResultSet getAllNotesRS) throws SQLException {
//
//
//        JSONArray notesJsonArray = new JSONArray();
//
//
//        while (getAllNotesRS.next()){
//            Notes note = new Notes();
//            note.setNotesId(getAllNotesRS.getInt(ApiConstant.NOTES_ID));
//            note.setNotes(getAllNotesRS.getString(ApiConstant.NOTES));
//            note.setPostedBy(getAllNotesRS.getString(ApiConstant.POSTED_BY));
//            note.setCreatedOn(getAllNotesRS.getString(ApiConstant.CREATED_ON));
//
//            notesJsonArray.put(new JSONObject(note));
//        }
//
//        return notesJsonArray;
//    }
}
