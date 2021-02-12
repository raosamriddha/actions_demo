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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * basic structure of a Receive only handler. packets that are only supposed t receive type only are handled by this handler
 *
 * @author Anuj Pathak
 */
public abstract class RxOnlyModuleHandler extends BaseModuleHandler {
    //

    public List<RxListener> getRxListener() {
        return rxListener;
    }

    /**
     * Listener this handle will call on successful decoding of packet
     */
    private transient final List<RxListener> rxListener = Collections.synchronizedList(new ArrayList<RxListener>());
    /**
     * Receive ID of the packet this handler is handling
     */
    public final transient byte rxID;
    /**
     * Receive Length of the packet this handler is handling
     */
    public final transient byte rxLength;

    /**
     * creating an instance of Receive Only MsgHandler using following parameters
     *
     * @param enableLog   if true enables debugging d
     * @param commHandler Communication handler instance to send and receive packet (must be same for all handlers)
     * @param rxID        packet id it is supposed to decode
     * @param rxLength    packet length linked with given packet id
     */
    public RxOnlyModuleHandler(boolean enableLog, CommHandler commHandler, byte rxID, byte rxLength) {
        super(enableLog, commHandler);
        this.rxID = rxID;
        this.rxLength = rxLength;
        this.commHandler.addModule(this);
    }

    /**
     * decodes read stream of byte depending on pkt type
     *
     * @param fifo byte arrayList
     */
    public abstract void decode(ArrayList<Byte> fifo);

    /**
     * set call back superCallback on successful decoding of a pkt
     *
     * @param listener callback
     */
    void addRxListener(RxListener listener) {
        if (listener != null) {
            synchronized (rxListener) {
                if (!rxListener.contains(listener)) {
                    rxListener.add(listener);
                }
            }
        }
    }

    /**
     * method to reset the state of module
     */
    public abstract void reset();

/*
    void removeListener(RxListener listener) {
        if (listener != null) {
            synchronized (rxListener) {
                rxListener.remove(listener);
            }
        }
    }*/
}
