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
import com.lattice.spectrum.ComLibrary.comHandler.CommTxResult;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ComLibrary.utility.ufx;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Base class handler Supporting only timeout capabilities
 * this class should not be used to create instances of it
 * because it is inherited by different more specific handler
 *
 * @author Anuj Pathak
 */
public abstract class BaseModuleHandler {

    /**
     * used to enable or disable debugging logs of specific handler
     */
    protected transient final boolean enableLog;
    /**
     * comHandler MsgHandler instance shared by all handler for communication
     */
    transient final CommHandler commHandler;

    /**
     *
     */
    private static final ScheduledThreadPoolExecutor scheduledExecutorService;

    static {
        scheduledExecutorService = new ScheduledThreadPoolExecutor(32);
        scheduledExecutorService.setRemoveOnCancelPolicy(true);
    }

    /**
     *
     */
    private transient TimeoutListener timeoutListener;
    private transient ScheduledFuture<?> timeoutScheduled;

    public TimeoutListener getTimeoutListener() {
        return timeoutListener;
    }

    /**
     * creating an instance of Basic Module using following parameters
     *
     * @param enableLog   if true enables debugging d
     * @param commHandler Communication handler instance to send and receive packet (must be same for all handlers)
     */
    BaseModuleHandler(boolean enableLog, CommHandler commHandler) {
        this.enableLog = enableLog;
        this.commHandler = commHandler;
    }

    /**
     * sets timeout superCallback to be called if a timeout to a command occur
     *
     * @param listener instance on TimeoutListener
     */
    public void setTimeoutListener(TimeoutListener listener) {
        this.timeoutListener = listener;
    }

    /**
     *
     */
    public void removeTimeoutListener() {
        this.timeoutListener = null;
    }

    /**
     * check whether a timeout is already scheduled or not
     *
     * @return true if a timeout is scheduled
     */
    private boolean isTimeoutScheduled() {
        return timeoutListener != null && timeoutScheduled != null;
    }

    /**
     * remove previously registered timeout from timeout handler
     */
    public void cancelTimeout() {
        if (isTimeoutScheduled()) {
            timeoutScheduled.cancel(false);
            timeoutScheduled = null;
        }
    }

    /**
     * schedule a timeout to occur at millis defined by superCallback
     *
     * @param millis tick in millis from current time
     */
    void scheduleTimeout(long millis) {
        if (timeoutListener != null) {
            timeoutScheduled = scheduledExecutorService.schedule(timeoutListener, millis, TimeUnit.MILLISECONDS);
        }
    }


    protected CommTxResult TransmitPacket(byte[] bytes) {
        CommTxResult m;
        if (isTimeoutScheduled()) {
            if (enableLog) {
//                sLog.d(this, "\n\n" + classname);
                sLog.d(this, "Can't send packet waiting for timeout");
            }
            return CommTxResult.WAITING_TIMEOUT;
        } else {
            if ((m = commHandler.transmitBytes(bytes)) == CommTxResult.TX_SUCCESS) {
                if (enableLog) {
//                    sLog.d(this, "\n\n" + classname);
                    sLog.d(this, "TX: " + ufx.bytesToHex(bytes));
                }
            }
            return m;
        }
    }

    protected CommTxResult TransmitPacket(byte[] bytes, long millis) {
        CommTxResult m;
        if (isTimeoutScheduled()) {
            if (enableLog) {
//                sLog.d(this, "\n\n" + classname);
                sLog.d(this, "Can't send packet waiting for timeout");
            }
            return CommTxResult.WAITING_TIMEOUT;
        } else {
            if ((m = commHandler.transmitBytes(bytes)) == CommTxResult.TX_SUCCESS) {
                if (enableLog) {
//                    sLog.d(this, "\n\n" + classname);
                    sLog.d(this, "TX: " + ufx.bytesToHex(bytes));
                }
                scheduleTimeout(millis);
            }
            return m;
        }
    }

}
