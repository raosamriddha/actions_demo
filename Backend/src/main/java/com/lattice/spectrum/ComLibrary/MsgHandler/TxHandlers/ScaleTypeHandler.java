/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 28/2/19 10:08 AM
 * Modified : 28/2/19 10:07 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.MsgHandler.TxHandlers;

import com.lattice.spectrum.ComLibrary.MsgHandler.TxOnlyModuleHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommTxResult;

import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.SET_SCALE_TYPE_ID;
import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.SET_SCALE_TYPE_LENGTH;

/**
 * handles scale type set commands
 *
 * @author Anuj Pathak
 */
public class ScaleTypeHandler extends TxOnlyModuleHandler {

    public ScaleTypeHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, SET_SCALE_TYPE_ID, SET_SCALE_TYPE_LENGTH);
    }

    /**
     * sets scale type to be used by the pump
     * use physically connected types only
     *
     * @param feedScaleType     feed scale type
     * @param permeateScaleType permeate scale type
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult setValue(int feedScaleType, int permeateScaleType) {
        bytes[2] = (byte) feedScaleType;
        bytes[3] = (byte) permeateScaleType;
        return TransmitPacket(bytes);
    }
}
