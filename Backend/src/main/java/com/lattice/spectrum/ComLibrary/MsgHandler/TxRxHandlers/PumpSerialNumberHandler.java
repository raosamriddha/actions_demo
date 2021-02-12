/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 22/09/19, 11:52 AM
 * Modified : 12/09/19, 10:23 AM
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

import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.*;

/**
 * handles Heartbeat command and response
 *
 */
public class PumpSerialNumberHandler extends TxRxModuleHandler {
    private long[] value = new long[4];
    @Override
    public void reset() {
            value = new long[4];
    }

    public PumpSerialNumberHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, PUMP_SERIAL_NUMBER, REQUEST_PUMP_SERIAL_NUMBER_LENGTH, PUMP_SERIAL_NUMBER, RESULT_PUMP_SERIAL_NUMBER_LENGTH);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
        value[0]=ufx.BytesToLong(fifo, 2, 4, true, false);
        value[1]=ufx.BytesToLong(fifo, 6, 4, true, false);
        value[2]=ufx.BytesToLong(fifo, 10, 4, true, false);
        value[3]=ufx.BytesToLong(fifo, 14, 4, true, false);
        if (enableLog) {
            sLog.d(this, "RX: " + ufx.bytesToHex(fifo, 0, rxLength + 2));
            sLog.d(this, "device ID : "+getDeviceNumber());
        }
    }

    /**
     * Send heartbeat packet
     *
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult requestPumpNumber() {
        return TransmitPacket(bytes);
    }

    public String getDeviceNumber(){
        return value[0]+""+value[1]+""+value[2]+""+value[3];
    }

}
