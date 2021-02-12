package com.lattice.spectrum.ModeLibrary.ModeProp.type;

public enum KonduitChannel {

    CHANNEL1(0), CHANNEL2(1);

    private int channelID;

    public int getChannelID() {
        return channelID;
    }

    KonduitChannel(int channelID) {
        this.channelID = channelID;
    }
}
