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
 * used to obtain Auxiliary pump type supported by the pump
 *
 * @author Anuj Pathak
 */
public class AuxPumpTypeListHandler extends TxRxModuleHandler {

    private String listString;
    private String[] AuxPumpTypeList;

    @Override
    public void reset() {
        listString = null;
        AuxPumpTypeList = null;
    }

    public AuxPumpTypeListHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, REQUEST_AUX_PUMP_LIST_ID, REQUEST_AUX_PUMP_LIST_LENGTH, AUX_PUMP_TYPE_LIST_ID, AUX_PUMP_TYPE_LIST_LENGTH);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
        Byte id = fifo.get(2);
        byte[] ch = new byte[16];
        for (int i = 0; i < 16; i++) {
            ch[i] = fifo.get(i + 3);
        }
        if (id == 0) {
            listString = new String(ch, StandardCharsets.UTF_8);
        } else {
            listString += new String(ch, StandardCharsets.UTF_8);
        }
        if (ch[15] != 0) {
            requestList(id + 1);
            if (enableLog) {
//                sLog.d(this, "\n\n" + getClass().getSimpleName());
                sLog.d(this, "requesting next chunk");
            }
        } else {
            AuxPumpTypeList = listString.split(", ");
//            sLog.d(this, "\n\n" + getClass().getSimpleName());
            sLog.d(this, "list received {" + listString + "}");
            if (enableLog) {
                for (String s : AuxPumpTypeList)
                    sLog.d(this, s);
            }
            listString = null;
        }
    }

    /**
     * init reading pump type list
     *
     * @param chunkID must be 0
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult requestList(int chunkID) {
        bytes[2] = (byte) chunkID;
        return TransmitPacket(bytes);
    }

    /**
     * requestList() must be called prior to this
     *
     * @return a list of pump type available to choose from
     */
    public String[] getAuxPumpTypeList() {
        return AuxPumpTypeList;
    }


}
