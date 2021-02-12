/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 23/12/19, 11:28 AM
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

import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.PUMP_KEY_ACTION_ID;
import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.PUMP_KEY_ACTION_LENGTH;

/**
 * handles pump key action command and response
 */
public class PumpKeyHandler extends TxRxModuleHandler {
    private boolean startStopKey = false;
    private boolean alarmKey = false;
    private boolean leftArrowKey = false;
    private boolean enterKey = false;
    private boolean rightArrowKey = false;
    private boolean upArrowKey = false;
    private boolean downArrowKey = false;
    private boolean enableStartStopKey = false;
    private boolean enableAlarmKey = false;
    private boolean enableLeftArrowKey = false;
    private boolean enableEnterKey = false;
    private boolean enableRightArrowKey = false;
    private boolean enableUpArrowKey = false;
    private boolean enableDownArrowKey = false;

    public PumpKeyHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, PUMP_KEY_ACTION_ID, PUMP_KEY_ACTION_LENGTH, PUMP_KEY_ACTION_ID, PUMP_KEY_ACTION_LENGTH);
    }

    @Override
    public void reset() {

    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
         startStopKey = (fifo.get(2) & 1) > 0;
         alarmKey =(fifo.get(2) & 2) > 0;
         leftArrowKey =(fifo.get(2) & 4) > 0;;
         enterKey =(fifo.get(2) & 8) > 0;;
         rightArrowKey =(fifo.get(2) & 16) > 0;;
         upArrowKey =(fifo.get(2) & 32) > 0;;
         downArrowKey =(fifo.get(2) & 64) > 0;;
         enableStartStopKey =(fifo.get(4) & 1) > 0;
         enableAlarmKey =(fifo.get(4) & 2) > 0;;
         enableLeftArrowKey =(fifo.get(4) & 4) > 0;;
         enableEnterKey =(fifo.get(4) & 8) > 0;;
         enableRightArrowKey =(fifo.get(4) & 16) > 0;;
         enableUpArrowKey =(fifo.get(4) & 32) > 0;;
         enableDownArrowKey =(fifo.get(4) & 64) > 0;;
        if (enableLog) {
            sLog.d(this, ufx.bytesToHex(fifo, 0, rxLength + 2));
            sLog.d(this);
        }
    }


    public CommTxResult start(boolean startStopKey, boolean alarmKey, boolean enableStartStopKey, boolean enableAlarmKey) {
        return start(startStopKey, alarmKey, false, false, false, false, false,
                enableStartStopKey, enableAlarmKey, false, false, false, false, false);
    }

    public CommTxResult start(boolean startStopKey, boolean alarmKey, boolean leftArrowKey, boolean enterKey,
                              boolean rightArrowKey, boolean upArrowKey, boolean downArrowKey
            , boolean enableStartStopKey, boolean enableAlarmKey, boolean enableLeftArrowKey, boolean enableEnterKey,
                              boolean enableRightArrowKey, boolean enableUpArrowKey, boolean enableDownArrowKey) {
        bytes[2] = (byte) ((startStopKey ? 1 : 0) | (alarmKey ? 2 : 0) | (leftArrowKey ? 4 : 0) | (enterKey ? 8 : 0)
                | (rightArrowKey ? 16 : 0) | (upArrowKey ? 32 : 0) | (downArrowKey ? 64 : 0));
        bytes[3] = 0;
        bytes[4] = (byte) ((enableStartStopKey ? 1 : 0) | (enableAlarmKey ? 2 : 0) | (enableLeftArrowKey ? 4 : 0) | (enableEnterKey ? 8 : 0)
                | (enableRightArrowKey ? 16 : 0) | (enableUpArrowKey ? 32 : 0) | (enableDownArrowKey ? 64 : 0));
        bytes[5] = 0;
        return TransmitPacket(bytes);
    }

    public boolean isStartStopKey() {
        return startStopKey;
    }

    public boolean isAlarmKey() {
        return alarmKey;
    }

    public boolean isLeftArrowKey() {
        return leftArrowKey;
    }

    public boolean isEnterKey() {
        return enterKey;
    }

    public boolean isRightArrowKey() {
        return rightArrowKey;
    }

    public boolean isUpArrowKey() {
        return upArrowKey;
    }

    public boolean isDownArrowKey() {
        return downArrowKey;
    }

    public boolean isEnableStartStopKey() {
        return enableStartStopKey;
    }

    public boolean isEnableAlarmKey() {
        return enableAlarmKey;
    }

    public boolean isEnableLeftArrowKey() {
        return enableLeftArrowKey;
    }

    public boolean isEnableEnterKey() {
        return enableEnterKey;
    }

    public boolean isEnableRightArrowKey() {
        return enableRightArrowKey;
    }

    public boolean isEnableUpArrowKey() {
        return enableUpArrowKey;
    }

    public boolean isEnableDownArrowKey() {
        return enableDownArrowKey;
    }
}
