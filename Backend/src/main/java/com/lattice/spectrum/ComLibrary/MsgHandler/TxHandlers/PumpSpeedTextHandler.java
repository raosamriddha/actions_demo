/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 22/09/19, 12:23 PM
 * Modified : 12/09/19, 10:23 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.MsgHandler.TxHandlers;

import com.lattice.spectrum.ComLibrary.MsgHandler.TxOnlyModuleHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommTxResult;

import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.PUMP_SPEED_TEXT_ID;
import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.PUMP_SPEED_TEXT_LENGTH;

/**
 * handles Pump Speed text set commands
 *
 * 
 */
public class PumpSpeedTextHandler extends TxOnlyModuleHandler {

    public PumpSpeedTextHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, PUMP_SPEED_TEXT_ID, PUMP_SPEED_TEXT_LENGTH);
    }

    /**
     * set Pump Speed text to be used to display
     *
     * @param
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult setPumpSpeedText(String value) {
        int i=0;
        for (i = 0; (i < value.getBytes().length); i++) {
            if(i>=20){
                break;
            }
            bytes[i+2] = value.getBytes()[i];
        }
        if (i != 20) {
            for (; i < 20; i++) {
                bytes[i+2] = 0;
            }
        }
        return TransmitPacket(bytes);
    }

}
