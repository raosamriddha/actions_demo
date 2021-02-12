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
import com.lattice.spectrum.ComLibrary.utility.ufx;

import java.util.ArrayList;

import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.*;

/**
 * requests and handle limiting endPointValue from pump
 *
 * @author Anuj Pathak
 */
public class LimitingValueHandler extends TxRxModuleHandler {

    private int mainPumpMaxWeight;
    private int mainPumpMinWeight;
    private int diafilterationPumpMaxRPM;
    private int permeatePumpMaxRPM;

    @Override
    public void reset() {
        mainPumpMaxWeight = 0;
        mainPumpMinWeight = 0;
        diafilterationPumpMaxRPM = 0;
        permeatePumpMaxRPM = 0;
    }

    public LimitingValueHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, REQUEST_LIMITING_VALUE_ID, REQUEST_LIMITING_VALUE_LENGTH, LIMITING_VALUE_ID, LIMITING_VALUE_LENGTH);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
        mainPumpMaxWeight = ufx.BytesToInt(fifo, 2, 5, true, true);
        mainPumpMinWeight = ufx.BytesToInt(fifo, 7, 5, true, true);
        diafilterationPumpMaxRPM = ufx.BytesToInt(fifo, 12, 2, true, true);
        permeatePumpMaxRPM = ufx.BytesToInt(fifo, 14, 2, true, true);
        if (enableLog) {
            sLog.d(this, ufx.bytesToHex(fifo, 0, rxLength + 2));
            sLog.d(this);
        }
    }

    /**
     * request to supply limiting values
     *
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult requestData() {
        return TransmitPacket(bytes);
    }

    /**
     * @return max main pump weight reading
     */
    public int getMainPumpMaxWeight() {
        return mainPumpMaxWeight;
    }

    /**
     * @return min main pump weight reading
     */
    public int getMainPumpMinWeight() {
        return mainPumpMinWeight;
    }

    /**
     * @return max diafilteration pump RPM
     */
    public int getDiafilterationPumpMaxRPM() {
        return diafilterationPumpMaxRPM;
    }

    /**
     * @return max permeate pump RPM
     */
    public int getPermeatePumpMaxRPM() {
        return permeatePumpMaxRPM;
    }

}
