/*
 * ============================================================================
 * Copyright (c) 2019 Lattice Innovation.
 * Created  : 9/7/19 10:32 AM
 * Modified : 5/7/19 10:09 AM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.Managers;

import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ComLibrary.utility.ufx;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeListener;
import com.lattice.spectrum.ModeLibrary.ModeProp.ModeProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.RunSubModeProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.KonduitType;

public abstract class iModeManager {

    protected int stageID;
    protected ModeProp prop;
    protected RunSubModeProp subProp;
    protected ModeListener superCallback;
    protected iModeManager curModeManager;
    protected Double lastDiff;
    protected Double konduitPercent;
    protected Double initDiff;


    /**
     * @param prop hold local reference of recipe provided
     * @return instance to this to facilitate chained callback
     */
    public iModeManager setProp(ModeProp prop) {
        this.prop = prop;
        sLog.d(this, "Setting Prop");
//        sLog.d(this, prop);
        return setStage(0);
    }

    /**
     * @param stageID set current mode stage id
     * @return instance to this to facilitate chained callback
     */
    public iModeManager setStage(int stageID) {
        this.stageID = stageID;
        if (prop.getSubModeCount() > 0) {
            this.subProp = prop.getSubRunModeProp(stageID);
        }
        return this;
    }

    public int getStageID(){
        return stageID;
    }

    /**
     * @param listener set listener to be called back to report events
     * @return instance to this to facilitate chained callback
     */
    public iModeManager setSuperCallback(ModeListener listener) {
        this.superCallback = listener;
        sLog.d(this, "Setting Listener");
        return this;
    }

    /**
     * do initialization of event it does not start
     *
     * @return instance to this to facilitate chained callback
     */
    public abstract iModeManager setup();

    /**
     * start or if paused resume
     *
     * @return instance to this to facilitate chained callback
     */
    public abstract iModeManager resume();

    /**
     * pause mode
     *
     * @return instance to this to facilitate chained callback
     */
    public abstract iModeManager pause();

    /**
     * stop mode
     *
     * @return instance to this to facilitate chained callback
     */
    public abstract iModeManager stop();


    public void setKonduitValue(int probe_id, double value, KonduitType type) {
        switch (probe_id) {
            case 0:
                prop.setCurKonduitCh1(value);
                prop.setCurKonduitCh1Type(type);
                break;
            case 1:
                prop.setCurKonduitCh2(value);
                prop.setCurKonduitCh2Type(type);
                break;
        }
    }
    public boolean isEndPointReached(Double currentDiff){
        sLog.d(" end point ",currentDiff+ "       ----------- " +lastDiff);
        if(currentDiff==0){
            konduitPercent = 100d;
            return true;
        }
        if(lastDiff==null){
            initDiff = currentDiff;
            lastDiff = currentDiff;
            konduitPercent = 0d;
            return false;
        }
        konduitPercent = ufx.boundInRange((initDiff -currentDiff)*100/initDiff,0,100);
        boolean endpointReached = lastDiff*currentDiff<0;
        lastDiff = currentDiff;
        return endpointReached;
    }
}
