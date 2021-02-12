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
import com.lattice.spectrum.ComLibrary.StaticDefinitions.PressureID;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.PressureUnits;
import com.lattice.spectrum.ComLibrary.comHandler.CommHandler;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ComLibrary.utility.ufx;

import java.util.ArrayList;

import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.PRESSURE_READINGS_ID;
import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.PRESSURE_READINGS_LENGTH;
import static com.lattice.spectrum.ComLibrary.StaticDefinitions.PressureID.*;

/**
 * handler for handling Pressure Readings passed by the pump. this packet is supposed to have a frequency of 1Hz
 *
 * @author Anuj Pathak
 */
public class PressureInfoHandler extends RxOnlyModuleHandler {

    private double scaleFactor;
    private PressureUnits unit;

    private final boolean[] channelStatus = new boolean[4];
    private final double[] channelReadings = new double[4];
//    private double feed_pressure, permeate_pressure, retentate_pressure, tmp_pressure;
//    private boolean feed_pressure_connected, permeate_pressure_connected, retentate_pressure_connected;

    @Override
    public void reset() {
        channelStatus[FEED_CHANNEL.ordinal()] = false;
        channelStatus[PERMEATE_CHANNEL.ordinal()] = false;
        channelStatus[RETENTATE_CHANNEL.ordinal()] = false;
        channelReadings[FEED_CHANNEL.ordinal()] = 0;
        channelReadings[PERMEATE_CHANNEL.ordinal()] = 0;
        channelReadings[RETENTATE_CHANNEL.ordinal()] = 0;
        channelReadings[TMP_CHANNEL.ordinal()] = 0;
    }

    public PressureInfoHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, PRESSURE_READINGS_ID, PRESSURE_READINGS_LENGTH);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
        unit = PressureUnits.values()[fifo.get(2) & 0x03];
        scaleFactor = Math.pow(0.1, (fifo.get(2) >> 2) & 0x07);
        channelStatus[FEED_CHANNEL.ordinal()] = ((fifo.get(2) >> 5) & 0x01) == 0x01;
        channelStatus[PERMEATE_CHANNEL.ordinal()] = ((fifo.get(2) >> 6) & 0x01) == 0x01;
        channelStatus[RETENTATE_CHANNEL.ordinal()] = ((fifo.get(2) >> 7) & 0x01) == 0x01;
        channelReadings[FEED_CHANNEL.ordinal()] = ufx.BytesToInt(fifo, 3, 2, true, true);
        channelReadings[PERMEATE_CHANNEL.ordinal()] = ufx.BytesToInt(fifo, 5, 2, true, true);
        channelReadings[RETENTATE_CHANNEL.ordinal()] = ufx.BytesToInt(fifo, 7, 2, true, true);
        channelReadings[TMP_CHANNEL.ordinal()] = ufx.BytesToInt(fifo, 9, 2, true, true);
        if (enableLog) {
            sLog.d(this, "RX: " + ufx.bytesToHex(fifo, 0, rxLength + 2));
            sLog.d(this);
        }
    }

    /**
     * @return get reading resolution
     */
    public double getScaleFactor() {
        return scaleFactor;
    }

    /**
     * @return pressure unit mBar or PSI
     */
    public PressureUnits getUnit() {
        return unit;
    }

    /**
     * @param channel_ID pressure channel id
     * @return true is channel transducer is connected or not
     */
    public boolean getChannelStatus(PressureID channel_ID) {
        return channelStatus[channel_ID.ordinal()];
    }

    /**
     * @param channel_ID pressure channel id
     * @return valve in psi
     */
    public double getChannelReadings_Psi(PressureID channel_ID) {
        switch (unit) {
            case PSI:
                return scaleFactor * channelReadings[channel_ID.ordinal()];
            case mBar:
                return ufx.mBarToPsi(scaleFactor * channelReadings[channel_ID.ordinal()]);
            case Bar:
                return ufx.mBarToPsi(scaleFactor * channelReadings[channel_ID.ordinal()])*1000;
        }
        return 0;
    }

    /**
     * @param channel_ID pressure channel id
     * @return valve in mbar
     */
    public double getChannelReadings_mBar(PressureID channel_ID) {
        switch (unit) {
            case PSI:
                return ufx.psiToMBar(scaleFactor * channelReadings[channel_ID.ordinal()]);
            case mBar:
                return scaleFactor * channelReadings[channel_ID.ordinal()];
            case Bar:
                return scaleFactor * channelReadings[channel_ID.ordinal()]*1000;
        }
        return 0;
    }

    /**
     * @param channel_ID pressure channel id
     * @return valve in bar
     */
    public double getChannelReadings_Bar(PressureID channel_ID) {
        switch (unit) {
            case PSI:
                return ufx.psiToMBar(scaleFactor * channelReadings[channel_ID.ordinal()])/1000;
            case mBar:
                return scaleFactor * channelReadings[channel_ID.ordinal()]/1000;
            case Bar:
                return scaleFactor * channelReadings[channel_ID.ordinal()];
        }
        return 0;
    }
}
