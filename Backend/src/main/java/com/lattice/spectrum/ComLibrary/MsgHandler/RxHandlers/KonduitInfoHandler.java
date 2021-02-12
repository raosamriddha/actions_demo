/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 28/2/19 10:08 AM
 * Modified : 28/2/19 10:07 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.MsgHandler.RxHandlers;

import com.lattice.spectrum.ComLibrary.MsgHandler.RxOnlyModuleHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommHandler;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ComLibrary.utility.ufx;

import java.util.ArrayList;

import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.CONDUCTIVITY_INFO_ID;
import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.CONDUCTIVITY_INFO_LENGTH;

/**
 * handler for handling conductivity information passed by the pump. this packet is supposed to have a frequency of 1Hz
 *
 * @author Anuj Pathak
 */
public class KonduitInfoHandler extends RxOnlyModuleHandler {

    private static final double conductivityResolution = 0.01;
    private static final double TemperatureResolution = 0.1;
    private static final double UVResolution = 0.001;
    private boolean KFConduitConnected;
    //    private boolean probe1Connected;
//    private boolean probe2Connected;
    private final boolean[] probeStatus = new boolean[2];
    private final double[][] probeReading = new double[2][3];
//    private double probe1Conductivity;
//    private double probe1Temperature;
//    private double probe2Conductivity;
//    private double probe2Temperature;
//    private double UV1Absorbance;
//    private double UV2Absorbance;

    @Override
    public void reset() {
        probeStatus[0] = false;
        probeStatus[1] = false;
        probeReading[0][0] = 0;
        probeReading[1][0] = 0;
        probeReading[0][1] = 0;
        probeReading[1][1] = 0;
        probeReading[0][2] = 0;
        probeReading[1][2] = 0;
    }

    public KonduitInfoHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, CONDUCTIVITY_INFO_ID, CONDUCTIVITY_INFO_LENGTH);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
        KFConduitConnected = (fifo.get(2) & 0x01) > 0;
        probeStatus[0] = (fifo.get(2) & 0x02) > 0;
        probeStatus[1] = (fifo.get(2) & 0x04) > 0;
        //
        //
        //
        probeReading[0][0] = ufx.BytesToInt(fifo, 3, 2, true, false);
        probeReading[1][0] = ufx.BytesToInt(fifo, 5, 2, true, false);
        probeReading[0][1] = ufx.BytesToInt(fifo, 7, 2, true, true);
        probeReading[1][1] = ufx.BytesToInt(fifo, 9, 2, true, true);
        probeReading[0][2] = ufx.BytesToInt(fifo, 11, 2, true, false);
        probeReading[1][2] = ufx.BytesToInt(fifo, 13, 2, true, false);
        if (enableLog) {
            sLog.d(this, "RX: " + ufx.bytesToHex(fifo, 0, rxLength + 2));
            sLog.d(this);
        }
    }

    /**
     * @return true if KFConduit is connected to the pump
     */
    public boolean isKFConduitConnected() {
        return KFConduitConnected;
    }


    /**
     * @param probe_ID can be 0 or 1
     * @return true if probe is connected
     */
    public boolean isProbeConnected(int probe_ID) {
        return probeStatus[probe_ID - 1];
    }

    /**
     * @param probe_ID can be 0 or 1
     * @return endPointValue with 1 decimal places in mS
     */
    public double getProbeConductivity_mS(int probe_ID) {
        return conductivityResolution * probeReading[probe_ID][0];
    }

    /**
     * @param probe_ID can be 0 or 1
     * @return endPointValue with 1 decimal places in *C
     */
    public double getProbeTemperature_Celcius(int probe_ID) {
        return TemperatureResolution * probeReading[probe_ID][1];
    }

    /**
     * @param probe_ID can be 0 or 1
     * @return endPointValue with 3 decimal places in AU
     */
    public double getProbeUV_AU(int probe_ID) {
        return UVResolution * probeReading[probe_ID][2];
    }
}
