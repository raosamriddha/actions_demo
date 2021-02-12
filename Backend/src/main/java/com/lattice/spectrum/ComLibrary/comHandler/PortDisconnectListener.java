/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 10/7/19 10:57 AM
 * Modified : 10/7/19 10:57 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ComLibrary.comHandler;

public interface PortDisconnectListener {

    /**
     * get called in case stream is not received for interval of time
     *
     * @param portName portName disconnected from
     */
    void disconnected(String portName);

}
