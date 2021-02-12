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
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ScaleID;
import com.lattice.spectrum.ComLibrary.comHandler.CommHandler;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ComLibrary.utility.ufx;

import java.util.ArrayList;

import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.SCALE_INFORMATION_ID;
import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.SCALE_INFORMATION_LENGTH;

/**
 * handler for handling Weight scale information passed by the pump. this packet is supposed to have a frequency of 1Hz
 *
 * @author Anuj Pathak
 */
public class ScaleInfoHandler extends RxOnlyModuleHandler {

    private static final double[] scaleFactorTable = {0.1, 1, 0.01, 0.001};
    private double scaleFactor;
    private final iScaleInfo[] data = new iScaleInfo[]{new iScaleInfo(), new iScaleInfo()};

    @Override
    public void reset() {
        for (iScaleInfo s : data) {
            s.isConnected = false;
            s.isAuto = false;
            s.lastValve = 0;
            s.rawValue = 0;
            s.readingID = 0;
            s.type = 0;
        }
    }

    public ScaleInfoHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, SCALE_INFORMATION_ID, SCALE_INFORMATION_LENGTH);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
        data[ScaleID.FEED_SCALE.ordinal()].isConnected = ((fifo.get(2)) & 0x01) == 1;
        data[ScaleID.FEED_SCALE.ordinal()].isAuto = ((fifo.get(2) >> 1) & 0x01) == 0;
        data[ScaleID.PERMEATE_SCALE.ordinal()].isConnected = ((fifo.get(2) >> 2) & 0x01) == 1;
        data[ScaleID.PERMEATE_SCALE.ordinal()].isAuto = ((fifo.get(2) >> 3) & 0x01) == 0;
        //
        scaleFactor = scaleFactorTable[((fifo.get(2) >> 4) & 0x03)];
        //
        data[ScaleID.FEED_SCALE.ordinal()].type = fifo.get(3) & 0xFF;
        data[ScaleID.PERMEATE_SCALE.ordinal()].type = fifo.get(4) & 0xFF;
        //
        data[ScaleID.FEED_SCALE.ordinal()].lastValve = data[ScaleID.FEED_SCALE.ordinal()].rawValue;
        data[ScaleID.PERMEATE_SCALE.ordinal()].lastValve = data[ScaleID.PERMEATE_SCALE.ordinal()].rawValue;
        data[ScaleID.FEED_SCALE.ordinal()].rawValue = ufx.BytesToInt(fifo, 5, 5, true, true);
        data[ScaleID.PERMEATE_SCALE.ordinal()].rawValue = ufx.BytesToInt(fifo, 10, 5, true, true);
        data[ScaleID.FEED_SCALE.ordinal()].readingID = fifo.get(15) & 0xFF;
        data[ScaleID.PERMEATE_SCALE.ordinal()].readingID = fifo.get(16) & 0xFF;
        if (enableLog) {
            sLog.d(this, "RX: " + ufx.bytesToHex(fifo, 0, rxLength + 2));
            sLog.d(this);
        }
    }

    /**
     * @param id scale ID permeate or feed
     * @return true is scale is connected
     */
    public boolean isScaleConnected(ScaleID id) {
        return data[id.ordinal()].isConnected;
    }

    /**
     * @param id scale ID permeate or feed
     * @return true if scale is auto mode
     */
    public boolean isScaleAuto(ScaleID id) {
        return data[id.ordinal()].isAuto;
    }

    /**
     * @param id scale ID permeate or feed
     * @return scale type identifier
     */
    public int scaleType(ScaleID id) {
        return data[id.ordinal()].type;
    }

    /**
     * @param id scale ID permeate or feed
     * @return reading identifier increment with every successful reading
     */
    public int scaleReadingID(ScaleID id) {
        return data[id.ordinal()].readingID;
    }

    /**
     * @param id scale ID permeate or feed
     * @return weight in gram
     */
    public double scaleReading_gm(ScaleID id) {
        return data[id.ordinal()].rawValue * scaleFactor;
    }

    /**
     * @param id scale ID permeate or feed
     * @return weight in kilogram
     */
    public double scaleReading_kg(ScaleID id) {
        return data[id.ordinal()].rawValue * scaleFactor / 1000;
    }

    /**
     * @param id scale ID permeate or feed
     * @return calculated flowrate (change in scale wt)
     */
    public double scaleFlowRate_mlpm(ScaleID id) {
//        change is recorded per second, but unit requested is in per minute *60
        if (data[id.ordinal()].lastValve == 0) {
            return 0;
        }
        double currentFlowRate = (data[id.ordinal()].rawValue - data[id.ordinal()].lastValve) * 60 * scaleFactor;
        return currentFlowRate>0?currentFlowRate:0;
    }

    /**
     * @return total weight on feed + permeate
     */
    public double totalScaleReading_gm() {
        return scaleReading_gm(ScaleID.FEED_SCALE) + scaleReading_gm(ScaleID.PERMEATE_SCALE);
    }

    private static class iScaleInfo {
        int type;
        int rawValue;
        int lastValve;
        int readingID;
        private boolean isConnected;
        private boolean isAuto;
    }
}
