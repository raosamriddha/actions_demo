/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 3/7/19 1:32 PM
 * Modified : 28/5/19 10:42 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.MsgHandler;

/**
 * Abstract timeout superCallback class that handed OnTimeout Callback
 *
 * @author Anuj Pathak
 */
public abstract class TimeoutListener implements Runnable {

    private BaseModuleHandler channel;

    public TimeoutListener() {
    }

    public TimeoutListener(BaseModuleHandler channel) {
        this.channel = channel;
    }

    /**
     * provide delayed setting on RxChannel this will be attached to
     *
     * @param rxChannel associated channel
     * @return object to this
     */
    public TimeoutListener setRxChannel(BaseModuleHandler rxChannel) {
        this.channel = rxChannel;
        return this;
    }

    /**
     * just a forwarder
     * forwards run method to a more understandable name
     */
    @Override
    public void run() {
        onTimeout();
    }

    /**
     * callback function
     */
    public abstract void onTimeout();

    /**
     * schedules this superCallback to generate callback if nothing is received on set channel for passed milli seconds
     * superCallback automatically got canceled if any message is rx'ed on the provided channel
     *
     * @param millis timeout in millis from now
     */
    public void schedule(long millis) {
        channel.setTimeoutListener(this);
        channel.scheduleTimeout(millis);
    }

    /**
     * cancel a scheduled timeout manually if required by the program
     */
    public void cancel() {
        channel.cancelTimeout();
        channel.removeTimeoutListener();
    }
}
