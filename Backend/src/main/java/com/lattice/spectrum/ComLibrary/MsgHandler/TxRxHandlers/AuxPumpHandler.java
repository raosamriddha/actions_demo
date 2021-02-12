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
 * Handles Auxiliary pump settings and readings
 *
 * @author Anuj Pathak
 */
public class AuxPumpHandler extends TxRxModuleHandler {

//    private boolean diafiltrationPumpConnected;
//    private boolean permeatePumpConnected;
//    private boolean diafiltrationPumpCaptured;
//    private boolean permeatePumpCaptured;
//    private int recentDiafiltrationRPM;
//    private int recentPermeateRPM;
//    private int targetDiafiltrationRPM;
//    private int targetPermeateRPM;
//    private int diafiltrationPumpType;

    //    private int permeatePumpType;
    private final AuxPumpSettings[] data = new AuxPumpSettings[]{new AuxPumpSettings(), new AuxPumpSettings()};

    @Override
    public void reset() {
        for (AuxPumpSettings a : data) {
            a.type = -1;
            a.softSetPointRPM = 0;
            a.curRPM = 0;
            a.isCaptured = false;
            a.isConnected = false;
            a.setRPM = 0;
        }
    }

    public AuxPumpHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, SET_AUX_PUMP_SPEED_ID, SET_AUX_PUMP_SPEED_LENGTH, AUX_PUMP_STATUS_ID, AUX_PUMP_STATUS_LENGTH);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
        data[0].isConnected = (fifo.get(2) & 0x01) > 0;
        data[1].isConnected = (fifo.get(2) & 0x02) > 0;
        data[0].isCaptured = (fifo.get(2) & 0x04) > 0;
        data[1].isCaptured = (fifo.get(2) & 0x08) > 0;
        //
        //
        //
        data[0].curRPM = ufx.BytesToInt(fifo, 3, 2, true, false);
        data[1].curRPM = ufx.BytesToInt(fifo, 5, 2, true, false);
        data[0].setRPM = ufx.BytesToInt(fifo, 7, 2, true, false);
        data[1].setRPM = ufx.BytesToInt(fifo, 9, 2, true, false);
        //
        data[0].type = fifo.get(11) & 0xFF;
        data[1].type = fifo.get(12) & 0xFF;
        if (enableLog) {
            sLog.d(this, "RX: " + ufx.bytesToHex(fifo, 0, rxLength + 2));
            sLog.d(this);
        }
    }

    /**
     * configure auxiliary pump settings that are connected to main pump
     *
     * @param diafiltrationPumpState   set pump state
     * @param permeatePumpState        set pump state
     * @param useDiafiltrationPumpType use supplied pump type
     * @param usePermeatePumpType      use supplied pump type
     * @param diafiltrationRPM         set endPointTarget rpm
     * @param permeateRPM              set endPointTarget rpm
     * @param diafiltrationType        set type
     * @param permeateType             set type
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult setAuxiliaryPumpSettings(int diafiltrationPumpState, int permeatePumpState, boolean useDiafiltrationPumpType, boolean usePermeatePumpType, int diafiltrationRPM, int permeateRPM, int diafiltrationType, int permeateType) {
        bytes[2] = (byte) (diafiltrationPumpState | (permeatePumpState << 2) | (useDiafiltrationPumpType ? 0x10 : 0x00) | (usePermeatePumpType ? 0x20 : 0x00));
        ufx.saveIntToBytes(bytes, 3, /*(diaRPM * 6 / 60) * 360*/diafiltrationRPM, 2, true);
        ufx.saveIntToBytes(bytes, 5, /*(perRPM * 6 / 60) * 360*/permeateRPM, 2, true);
        bytes[7] = (byte) (diafiltrationType & 0xFF);
        bytes[8] = (byte) (permeateType & 0xFF);
//        sLog.d(this, "0: " + diafiltrationRPM + " 1: " + permeateRPM);
        return TransmitPacket(bytes);
    }

    /**
     * @param auxPumpID     0,1 ID of the pump
     * @param auxPumpTypeID identifier of aux pump type (specified by repligen)
     * @param auxRpm        rpm of the aux pump
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult setAuxPumpRPM(int auxPumpID, int auxPumpTypeID, int auxRpm) {
//        valve update takes 1s
//        short hack to update internally
        if (auxPumpID <= 1) {
            data[auxPumpID].softSetPointRPM = auxRpm;
            data[auxPumpID].type = auxPumpTypeID;
//            sLog.d(this, auxPumpID+": " + auxRpm);
            return setAuxiliaryPumpSettings(3, 3,
                    true, true,
                    data[0].softSetPointRPM, data[1].softSetPointRPM,
                    data[0].type, data[1].type);
        } else {
            throw new IllegalArgumentException("Aux Pump Id out of range");
        }
    }

    /**
     * @param auxPumpID          0,1 ID of the pump
     * @param auxPumpTypeID      identifier of aux pump type (specified by repligen)
     * @param mlpm               flow rate in ml per minute
     * @param tubeFlowConversion flow conversion number
     * @param maxRPM             max rpm of pump type
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult setAuxPump_mlpm(int auxPumpID, int auxPumpTypeID, double mlpm, double tubeFlowConversion, int maxRPM) {
//        valve update takes 1s
//        short hack to update internally
        if (auxPumpID <= 1) {
            data[auxPumpID].softSetPointRPM = (int) ufx.boundInRange(mlpm  / tubeFlowConversion, 0, maxRPM);

            data[auxPumpID].type = auxPumpTypeID;
//            sLog.d(this, auxPumpID+": " + auxRpm);
            return setAuxiliaryPumpSettings(3, 3,
                    true, true,
                    data[0].softSetPointRPM, data[1].softSetPointRPM,
                    data[0].type, data[1].type);
        } else {
            throw new IllegalArgumentException("Aux Pump Id out of range");
        }
    }

    /**
     * @param auxPumpID 0,1 ID of the pump
     * @return true if pump id is connected
     */
    public boolean isAuxConnected(int auxPumpID) {
        return data[auxPumpID].isConnected;
    }

    /**
     * @param auxPumpID 0,1 ID of the pump
     * @return true if pump id is in control of main pump
     */
    public boolean isAuxCaptured(int auxPumpID) {
        return data[auxPumpID].isCaptured;
    }

    /**
     * @param auxPumpID 0,1 ID of the pump
     * @return RPM of current aux pump selected
     */
    public int curAuxRPM(int auxPumpID) {
        return data[auxPumpID].curRPM;
    }

    /**
     * @param auxPumpID 0,1 ID of the pump
     * @return target RPM of aux pump
     */
    public int targetAuxRPM(int auxPumpID) {
        return data[auxPumpID].setRPM;
    }

    /**
     * @param auxPumpID 0,1 ID of the pump
     * @return aux type identifier set by software
     */
    public int auxType(int auxPumpID) {
        return data[auxPumpID].type;
    }

    private static class AuxPumpSettings {
        boolean isConnected;
        boolean isCaptured;
        int curRPM;
        int setRPM;
        int type;
        int softSetPointRPM;
    }
}
