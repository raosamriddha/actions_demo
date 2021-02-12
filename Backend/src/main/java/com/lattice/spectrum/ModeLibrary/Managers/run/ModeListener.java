/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 5/7/19 9:42 AM
 * Modified : 11/6/19 12:14 PM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.Managers.run;

public abstract class ModeListener {

    public abstract void callback(ModeEvent id, Object... obj);
}
