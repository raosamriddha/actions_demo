package org.lattice.spectrum_backend_final.dao.util;

import org.lattice.spectrum_backend_final.beans.NotesDescription;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class NotesDescMapper {

    public PreparedStatement saveNotesDescriptionMapper(NotesDescription notesDescription, PreparedStatement saveNotesDescPS) throws SQLException {
        saveNotesDescPS.setLong(1, notesDescription.getTrialLogId());
        saveNotesDescPS.setString(2, notesDescription.getSubModeName());
        saveNotesDescPS.setInt(3, notesDescription.getStep());
        saveNotesDescPS.setString(4, notesDescription.getEndPointName());
        saveNotesDescPS.setDouble(5, notesDescription.getEndPointValue());
        saveNotesDescPS.setInt(6, notesDescription.getStatus());
        return saveNotesDescPS;
    }
}
