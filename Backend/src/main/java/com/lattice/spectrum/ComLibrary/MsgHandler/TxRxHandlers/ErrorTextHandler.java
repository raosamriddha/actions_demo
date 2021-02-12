/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 28/2/19 10:08 AM
 * Modified : 28/2/19 10:07 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.MsgHandler.TxRxHandlers;

import com.lattice.spectrum.ComLibrary.MsgHandler.TxRxModuleHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommTxResult;
import com.lattice.spectrum.ComLibrary.utility.sLog;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.*;

/**
 * used to retrieve e text if an e is reported by the pump
 *
 * @author Anuj Pathak
 */
public class ErrorTextHandler extends TxRxModuleHandler {

    //    private HashMap<Byte, String> errorText = new HashMap<>();
    private final StringBuilder temp = new StringBuilder();

    @Override
    public void reset() {
        temp.setLength(0);
    }

    public ErrorTextHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, REQUEST_ERROR_TEXT_ID, REQUEST_ERROR_TEXT_LENGTH, REPORT_ERROR_TEXT_ID, REPORT_ERROR_TEXT_LENGTH);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
//        String s = errorText.get(fifo.get(2));
        byte[] ch = new byte[16];
        for (int i = 0; i < 16; i++) {
            ch[i] = fifo.get(i + 4);
        }
        temp.append(new String(ch, StandardCharsets.UTF_8));
        if (ch[15] != 0) {
            requestText(fifo.get(2), (byte) (fifo.get(3) + 1));
            if (enableLog) {
//                sLog.d(this, "\n\n" + getClass().getSimpleName());
                sLog.d(this, "requesting next chunk");
            }
        } else {
            String[] ScaleTypeList = temp.toString().split(", ");
//            sLog.d(this, "\n\n" + getClass().getSimpleName());
            sLog.d(this, "list received {" + temp.toString() + "}");
            if (enableLog) {
                for (String st : ScaleTypeList)
                    sLog.d(this, st);
            }
        }
    }

    /**
     * init receiving e message
     *
     * @param errorNumber errorNumber
     * @param chunkID     chunk id to be requested
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult requestText(byte errorNumber, byte chunkID) {
        if (chunkID == 0) {
//            errorText.put(errorNumber, "");
            temp.setLength(0);

        }
        bytes[2] = errorNumber;
        bytes[3] = chunkID;
        return TransmitPacket(bytes);
    }

    /**
     * must call requestText() before this function
     *
     * @return e test received from pump
     */
    public String getErrorText() {
        return temp.toString();
    }

}
