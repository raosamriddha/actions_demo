package org.lattice.spectrum_backend_final.dao.manager;

import java.awt.*;

public class SleepManager {

    private static SleepManager sleepManager;

    private SleepManager() {
    }

    public static SleepManager getInstance() {

        synchronized (SleepManager.class) {
            if (sleepManager == null) {
                sleepManager = new SleepManager();
            }
        }

        return sleepManager;
    }

    public void preventSleep() throws AWTException {
        Robot hal = new Robot();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    hal.delay(1000 * 30);
                    Point pObj = MouseInfo.getPointerInfo().getLocation();
                    hal.mouseMove(pObj.x + 1, pObj.y + 1);
                    pObj = MouseInfo.getPointerInfo().getLocation();
                    hal.mouseMove(pObj.x-1, pObj.y-1);
                }
            }
        }).start();


    }
}
