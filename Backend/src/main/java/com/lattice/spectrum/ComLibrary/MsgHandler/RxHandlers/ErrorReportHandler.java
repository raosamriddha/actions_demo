/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 28/2/19 10:08 AM
 * Modified : 28/2/19 10:07 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.MsgHandler.RxHandlers;

import com.lattice.spectrum.ComLibrary.MsgHandler.RxOnlyModuleHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommHandler;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ComLibrary.utility.ufx;

import java.util.ArrayList;

import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.REPORT_ERROR_ID;
import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.REPORT_ERROR_LENGTH;

/**
 * handler for handling e reports
 *
 * @author Anuj Pathak
 */
public class ErrorReportHandler extends RxOnlyModuleHandler {

    private Integer errorCmd;
    private Integer errorNumber;

    public ErrorReportHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, REPORT_ERROR_ID, REPORT_ERROR_LENGTH);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
        errorCmd = fifo.get(2) & 0xFF;
        errorNumber = fifo.get(3) & 0xFF;//ufx.BytesToInt(fifo, 3, 2, true, false);
        if (enableLog) {
            sLog.d(this, "RX: " + ufx.bytesToHex(fifo, 0, rxLength + 2));
            sLog.d(this);
//            sLog.d(this, "Cmd : " + errorCmd + " \t\terror Number : " + errorNumber);
        }
    }

    /**
     * @return command number that results in an e
     */
    public int getErrorCmd() {
        return errorCmd;
    }

    /**
     * @return e number used by pump, this number is used to get complete e text from pump
     */
    public int getErrorNumber() {
        return errorNumber;
    }

    @Override
    public void reset() {
        errorCmd = null;
        errorNumber = null;
    }
}
