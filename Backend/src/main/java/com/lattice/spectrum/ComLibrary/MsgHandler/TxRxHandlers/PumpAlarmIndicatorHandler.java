/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 23/09/19, 11:06 AM
 * Modified : 23/09/19, 11:06 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.MsgHandler.TxRxHandlers;

import com.lattice.spectrum.ComLibrary.MsgHandler.TxRxModuleHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommTxResult;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ComLibrary.utility.ufx;

import java.util.ArrayList;

import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.PUMP_ALARM_INDICATOR_ID;
import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.PUMP_ALARM_INDICATOR_LENGTH;

/**
 * handles Alarm Text command and response
 *
 */
public class PumpAlarmIndicatorHandler extends TxRxModuleHandler {
    @Override
    public void reset() {

    }

    public PumpAlarmIndicatorHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, PUMP_ALARM_INDICATOR_ID, PUMP_ALARM_INDICATOR_LENGTH, PUMP_ALARM_INDICATOR_ID, PUMP_ALARM_INDICATOR_LENGTH);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
        if (enableLog) {
            sLog.d(this, "RX: " + ufx.bytesToHex(fifo, 0, rxLength + 2));
        }
    }

    /**
     * @param status 0-No Change
     *               1-Stop Alarm
     *               2-Show Alarm in both mode i.e. audio and visual
     *               3-Show Alarm in only visual mode
     *               4- Show Alarm in only audio mode
     * @param message Message value of alarm
     *
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult send(int status,String message) {
        bytes[2] = (byte) status;
        int i = 0;
        for (; (i < message.getBytes().length); i++) {
            if(i==20){
                break;
            }
            bytes[i+3] = message.getBytes()[i];
        }
        for (int j = i; i < 20; i++) {
            bytes[i+3]=" ".getBytes()[0];
        }
        return TransmitPacket(bytes);
    }

}
