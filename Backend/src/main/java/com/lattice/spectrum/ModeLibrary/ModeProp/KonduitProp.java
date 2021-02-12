

package com.lattice.spectrum.ModeLibrary.ModeProp;

import com.lattice.spectrum.ModeLibrary.ModeProp.type.KonduitChannel;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.KonduitType;

public class KonduitProp {

    private KonduitType type;
    private KonduitChannel channel;
    private Double xValue;
    private Double yValue;

    public KonduitProp(KonduitType type, KonduitChannel channel, Double xValue, Double yValue) {
        this.type = type;
        this.channel = channel;
        this.xValue = xValue;
        this.yValue = yValue;
    }

    public KonduitType getType() {
        return type;
    }

    public int getChannelID() {
        return channel.getChannelID();
    }

    public Double getXValue() {
        return xValue;
    }

    public Double getYValue() {
        return yValue;
    }
}
