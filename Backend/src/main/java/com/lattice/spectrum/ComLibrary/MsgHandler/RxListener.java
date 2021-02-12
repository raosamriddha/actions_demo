/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 3/7/19 1:32 PM
 * Modified : 3/7/19 12:25 PM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.MsgHandler;

import com.lattice.spectrum.ComLibrary.utility.sLog;

/**
 * Funtional Interface to create a Receive Listener that handles OnReceive callback
 *
 * @author Anuj Pathak
 */
public abstract class RxListener {

    private final RxOnlyModuleHandler rxChannel;
    private boolean active;

//    public RxListener() {
//    }

    public RxListener(RxOnlyModuleHandler rxChannel) {
        this.rxChannel = rxChannel;
    }

    /**
     * detached this superCallback object from the rxChannel it is attached
     * OnReceive will get called on message reception
     */
    public void startRxListening() {
        sLog.d(this, rxChannel.getRxListener().size());
        if (rxChannel == null) {
            throw new NullPointerException("Listening Channel can not be null");
        }
        reset();
        rxChannel.addRxListener(this);
        active = true;
        sLog.d(this, rxChannel.getRxListener().size());
    }

    /**
     * detached this superCallback object from the rxChannel it is attached
     * OnReceive will not be called
     */
    public void stopRxListening() {
        sLog.d(this, rxChannel.getRxListener().size());
        active = false;
//        rxChannel.removeListener(this);
        sLog.d(this, rxChannel.getRxListener().size());
    }

    /**
     * weak function to be override if its object uses some state variable
     */
    public void reset() {
    }

    /**
     * callback function. called only when packet is successfully received and decoded
     */
    public abstract void OnReceive();

    public boolean isActive() {
        return active;
    }

}
