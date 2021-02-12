/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 28/2/19 10:08 AM
 * Modified : 28/2/19 10:07 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.MsgHandler;

import com.lattice.spectrum.ComLibrary.comHandler.CommHandler;

/**
 * basic structure of a command and response handler. packets that are only supposed to command and response are handled by this handler
 *
 * @author Anuj Pathak
 */
public abstract class TxRxModuleHandler extends RxOnlyModuleHandler {

    protected transient final byte[] bytes;

    /**
     * creating an instance of Basic Module using following parameters
     *
     * @param enableLog   if true enables debugging d
     * @param commHandler Communication handler instance to send and receive packet (must be same for all handlers)
     * @param txID        packet id this should transmit
     * @param txLength    packet length linked with tx packet id
     * @param rxID        packet id it is supposed to decode
     * @param rxLength    packet length linked with given packet id
     */
    public TxRxModuleHandler(boolean enableLog, CommHandler commHandler, byte txID, byte txLength, byte rxID, byte rxLength) {
        super(enableLog, commHandler, rxID, rxLength);
        bytes = new byte[1 + 1 + txLength];
        bytes[0] = txID; // id
        bytes[1] = txLength; // len inc csum
    }

}
