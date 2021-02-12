/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 10/7/19 12:03 PM
 * Modified : 5/7/19 10:04 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.ModeProp.type;

public enum OperationMode {
    /**
     * elimentry
     */
    IDLE,
    /**
     * operation modes
     */
    M, CD_MODE, CDC_MODE, CDDC_MODE, CDCD_MODE, CFC_MODE, CFDC_MODE,
    /**
     * run mode
     */
    TubeCalib_MODE, C_MODE, D_MODE, CF_MODE, HARVEST_MODE, VACUUM_MODE,
    /**
     * non run mode
     */
    FluxCV, FluxCF, NWP, Flushing, Cleaning
}
