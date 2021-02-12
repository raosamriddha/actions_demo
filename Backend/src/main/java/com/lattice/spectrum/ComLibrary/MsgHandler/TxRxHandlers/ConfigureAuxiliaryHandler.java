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
import com.lattice.spectrum.ComLibrary.StaticDefinitions.USBConfiguration;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ValveConnectorConfiguration;
import com.lattice.spectrum.ComLibrary.comHandler.CommHandler;
import com.lattice.spectrum.ComLibrary.comHandler.CommTxResult;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ComLibrary.utility.ufx;

import java.util.ArrayList;

import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.CONFIGURE_AUXILIARY_ID;
import static com.lattice.spectrum.ComLibrary.MsgHandler.GlobalIDs.CONFIGURE_AUXILIARY_LENGTH;

/**
 * handles configuration request and reply of auxiliary connecter
 *
 * @author Anuj Pathak
 */
public class ConfigureAuxiliaryHandler extends TxRxModuleHandler {

    private ValveConnectorConfiguration connectorConfig;
    private USBConfiguration UsbConfig;


    public ConfigureAuxiliaryHandler(boolean enableLog, CommHandler ch) {
        super(enableLog, ch, CONFIGURE_AUXILIARY_ID, CONFIGURE_AUXILIARY_LENGTH, CONFIGURE_AUXILIARY_ID, CONFIGURE_AUXILIARY_LENGTH);
    }

    /**
     * configure auxiliary
     *
     * @param vc valve2 probe configuration
     * @param uc usb configuration
     * @return Transmission Result it can be
     * TX_SUCCESS if transmitted successfully
     * TX_ERROR if send command timeouts
     * PORT_ERROR if e opening current port
     * DISCONNECTED if not connected to any com port
     * WAITING_TIMEOUT if waiting for already scheduled timeout to expire
     */
    public CommTxResult configure(ValveConnectorConfiguration vc, USBConfiguration uc) {
        int data = 0;
        data |= vc.ordinal();
        data |= uc.ordinal() << 7;
        ufx.saveIntToBytes(bytes, 2, data, 2, true);
        return TransmitPacket(bytes);
    }

    @Override
    public void decode(ArrayList<Byte> fifo) {
        int data = ufx.BytesToInt(fifo, 2, 2, true, false);
        connectorConfig = ValveConnectorConfiguration.values()[data & 0x03];
        UsbConfig = USBConfiguration.values()[(data >> 7) & 0x03];
        if (enableLog) {
            sLog.d(this, "RX: " + ufx.bytesToHex(fifo, 0, rxLength + 2));
            sLog.d(this);
        }
    }

    @Override
    public void reset() {
        connectorConfig = null;
        UsbConfig = null;
    }

    /**
     * @return valve 2 connector current configuration
     */
    public ValveConnectorConfiguration getConnectorConfig() {
        return connectorConfig;
    }

    /**
     * @return current usb configuration
     */
    public USBConfiguration getUsbConfig() {
        return UsbConfig;
    }


}
