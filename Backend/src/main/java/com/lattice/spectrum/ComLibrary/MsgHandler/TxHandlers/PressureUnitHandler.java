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
import com.lattice.spectrum.ComLibrary.StaticDefinitions.PressureUnits;
import com.lattice.spectrum.ComLibrary.comHandler.CommHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommTxResult;

import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.SET_PRESSURE_UNIT_ID;
import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.SET_PRESSURE_UNIT_LENGTH;

/**
 * handles pressure unit set commands
 *
 * @author Anuj Pathak
 */
public class PressureUnitHandler extends TxOnlyModuleHandler {

    public PressureUnitHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, SET_PRESSURE_UNIT_ID, SET_PRESSURE_UNIT_LENGTH);
    }

    /**
     * set pressure unit to be used by the pump
     *
     * @param unit can be either Psi or mBar
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult setUnit(PressureUnits unit) {
        bytes[2] = (byte) unit.ordinal();
        return TransmitPacket(bytes);
    }

}
