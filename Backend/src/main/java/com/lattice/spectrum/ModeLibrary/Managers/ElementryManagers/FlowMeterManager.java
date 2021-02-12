/*
 * ============================================================================
 * Copyright (c) 2020 Lattice Innovation.
 * Created  : 05/06/20, 2:36 PM
 * Modified : 05/06/20, 2:35 PM
 * Author   : Anuj Pathak
 * ============================================================================
 */

package com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers;
import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxListener;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.ModeProp.KonduitProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.ModeProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.KonduitChannel;


public class FlowMeterManager {

    private static FlowMeterManager flowMeterManager;

    public static FlowMeterManager get() {
        if (flowMeterManager == null) {
            flowMeterManager = new FlowMeterManager();
        }
        return flowMeterManager;
    }

    private ModeProp modeProp;
    private double[] volume={0,0};
    private double[] previousFlowRate ={0,0};
    private double[] currentFlowRate={0,0};
    private KonduitProp konduitChannel1,konduitChannel2;
    private double oldTime;
    public void start(ModeProp modeProp) {
        this.modeProp= modeProp;
        volume[0]=0;
        volume[1]=0;
        previousFlowRate[0]=0;
        previousFlowRate[1]=0;
        currentFlowRate[0]=0;
        currentFlowRate[1]=0;
        uvListener.startRxListening();
        oldTime= System.currentTimeMillis();
        konduitChannel1 = modeProp.getKonduit(KonduitChannel.CHANNEL1);
        konduitChannel2 = modeProp.getKonduit(KonduitChannel.CHANNEL2);
        sLog.d(FlowMeterManager.this,"Start FlowMeter");
    }
    private final RxListener uvListener = new RxListener(ComLib.get().getKonduitInfo()) {
        @Override
        public void OnReceive() {
            double currentTime = System.currentTimeMillis();
            if(konduitChannel1!=null) {
                currentFlowRate[0] = ComLib.get().getKonduitInfo().getProbeUV_AU(0) * konduitChannel1.getXValue() + konduitChannel1.getYValue();
            }else{
                currentFlowRate[0] =0;
            }
            if(konduitChannel2!=null) {
                currentFlowRate[1] = ComLib.get().getKonduitInfo().getProbeUV_AU(1) * konduitChannel2.getXValue() + konduitChannel2.getYValue();
            }else{
                currentFlowRate[1] = 0;
            }
            double timeDiff = (Math.round((currentTime-oldTime)/1000.0));
            if(timeDiff==1){
                volume[0]=volume[0]+(currentFlowRate[0]/60);
                volume[1]=volume[1]+(currentFlowRate[1]/60);
            }else{
                double[] avgFlowRate = {(previousFlowRate[0]+currentFlowRate[0])/2,(previousFlowRate[1]+currentFlowRate[1])/2};
                volume[0]=volume[0]+((avgFlowRate[0]*timeDiff)/60);
                volume[1]=volume[1]+((avgFlowRate[0]*timeDiff)/60);
            }

            previousFlowRate = currentFlowRate.clone();
            oldTime = System.currentTimeMillis();
        }
    };


    public void stop(){
        sLog.d(FlowMeterManager.this,"Stop FlowMeter");
        uvListener.stopRxListening();
    }

    public double getCurrentFlowRate(int probeId) {
        return currentFlowRate[probeId];
    }

    public double getVolume(int probeId) {
        return volume[probeId];
    }
}
