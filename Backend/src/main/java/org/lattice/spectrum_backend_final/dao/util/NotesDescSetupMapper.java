package org.lattice.spectrum_backend_final.dao.util;

import org.lattice.spectrum_backend_final.beans.NotesDescSetup;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class NotesDescSetupMapper {

    public PreparedStatement saveNotesDescriptionSetupMapper(NotesDescSetup notesDescSetup, PreparedStatement saveNotesDescSetupPS) throws SQLException {
        saveNotesDescSetupPS.setLong(1, notesDescSetup.getTrialLogId());
        if(notesDescSetup.getTmp() != null){
            saveNotesDescSetupPS.setDouble(2, notesDescSetup.getTmp());
        }else{
            saveNotesDescSetupPS.setNull(2, Types.DOUBLE);
        }
        saveNotesDescSetupPS.setString(3, notesDescSetup.getDuration());
        if(notesDescSetup.getPermeateWeight() != null){
            saveNotesDescSetupPS.setDouble(4, notesDescSetup.getPermeateWeight());
        }else{
            saveNotesDescSetupPS.setNull(4, Types.DOUBLE);
        }
        if(notesDescSetup.getPermeateTotal() != null){
            saveNotesDescSetupPS.setDouble(5, notesDescSetup.getPermeateTotal());
        }else{
            saveNotesDescSetupPS.setNull(5, Types.DOUBLE);
        }
        if(notesDescSetup.getFlowRate() != null){
            saveNotesDescSetupPS.setDouble(6, notesDescSetup.getFlowRate());
        }else{
            saveNotesDescSetupPS.setNull(6, Types.DOUBLE);
        }
        saveNotesDescSetupPS.setDouble(7, notesDescSetup.getStep());
        saveNotesDescSetupPS.setDouble(8, notesDescSetup.getStatus());
        saveNotesDescSetupPS.setString(9, notesDescSetup.getModeName());
        return saveNotesDescSetupPS;
    }
}
