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
 * handles valve positions command and response
 *
 * @author Anuj Pathak
 */
public class ValveInfoHandler extends TxRxModuleHandler {

    private final valveData[] data = new valveData[]{new valveData(), new valveData()};

    @Override
    public void reset() {
        for (valveData v : data) {
            v.curPos = 0;
            v.targetPos = 0;
            v.state = 0;
            v.type = 0;
        }
    }

    public ValveInfoHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, SET_VALVE_POSITION_ID, SET_VALVE_POSITION_LENGTH, VALVE_POSITION_ID, VALVE_POSITION_LENGTH);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
        data[0].type = fifo.get(2) & 0x07;
        data[1].type = (fifo.get(2) >> 3) & 0x07;
        data[0].state = fifo.get(3) & 0x01;
        data[1].state = (fifo.get(3) >> 1) & 0x01;
        data[0].curPos = ufx.BytesToInt(fifo, 4, 2, true, false);
        data[1].curPos = ufx.BytesToInt(fifo, 6, 2, true, false);
        data[0].targetPos = fifo.get(8) & 0xFF;
        data[1].targetPos = fifo.get(9) & 0xFF;
        data[0].fractionPos = ufx.BytesToInt(fifo, 10, 2, true, false);
        data[1].fractionPos = ufx.BytesToInt(fifo, 12, 2, true, false);

        if (enableLog) {
            sLog.d(this, ufx.bytesToHex(fifo, 0, rxLength + 2));
            sLog.d(this);
        }
    }

    /**
     * set valve positions
     *
     * @param valve_id  valve 1 position
     * @param valve_pos valve 2 position
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult setValvePosition(int valve_id, int valve_pos,int valve_fraction_pos) {
//        valve update takes 1s
//        short hack to update internally
        if (valve_id <= 1) {
            data[valve_id].curPos = valve_pos;
            data[valve_id].fractionPos = valve_fraction_pos;
            ufx.saveIntToBytes(bytes, 3, data[0].curPos, 2, true);
            ufx.saveIntToBytes(bytes, 5, data[1].curPos, 2, true);
            ufx.saveIntToBytes(bytes, 9, data[0].fractionPos, 2, true);
            ufx.saveIntToBytes(bytes, 11, data[1].fractionPos, 2, true);

//            sLog.d(this, valve_id + ": " + valve_pos);
            return TransmitPacket(bytes);
        } else {
            throw new IllegalArgumentException("Aux Pump Id out of range");
        }
    }

    /**
     * @return valve type
     */
    public int getValveType(int valve_id) {
        return data[valve_id].type;
    }

    /**
     * @return valve 1 state
     */
    public int getValveState(int valve_id) {
        return data[valve_id].state;
    }

    /**
     * @return valve 1 position
     */
    public int getValveCurPos(int valve_id) {
        return data[valve_id].curPos;
    }

    private class valveData {
        private int type;
        private int state;
        private int curPos;
        private int targetPos;
        private int fractionPos;
    }

}
