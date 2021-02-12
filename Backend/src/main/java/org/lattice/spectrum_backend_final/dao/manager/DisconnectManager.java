package org.lattice.spectrum_backend_final.dao.manager;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.comHandler.PortDisconnectListener;
import com.lattice.spectrum.ComLibrary.comHandler.PortNotFoundException;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.exception.HardwareValidationException;
import org.lattice.spectrum_backend_final.exception.TrialRunException;

public class DisconnectManager {

    private boolean exit = false;
    private static DisconnectManager disconnectManager;

    private DisconnectManager() {
    }


    public static DisconnectManager getInstance() {

        synchronized (DisconnectManager.class) {
            if (disconnectManager == null) {
                disconnectManager = new DisconnectManager();
            }
        }

        return disconnectManager;
    }

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public boolean reconnect(String pumpModel, String portName, PortDisconnectListener pd) {
        int counter = 1;
//        int delay;
        boolean isConnected = false;
        exit = false;
        JSONObject liveDataJson = new JSONObject();
        DeviceManager.getInstance().setConnecting(true);
        liveDataJson.put(ApiConstant.TYPE, ApiConstant.EVENT);
        liveDataJson.put(ApiConstant.MESSAGE, ApiConstant.RECONNECT);
        liveDataJson.put(ApiConstant.IS_FINISHED, TrialManager.getInstance().isFinished());
        // send reconnect callback
        DeviceManager.getInstance().broadcastLiveData(liveDataJson);

        try {
            Thread.sleep(2000);
            BasicUtility.systemPrint("before","tryReconnect   "+pumpModel);
            isConnected = ComLib.get().autoConnect(true, portName, pd);
        } catch (PortNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BasicUtility.systemPrint("after","tryReconnect");
        while (counter <= ApiConstant.RECONNECT_ATTEMPT_COUNTER) {
            try {
                DeviceManager.getInstance().setConnecting(true);
//                delay = 4 * counter;
                Thread.sleep(2000);
                BasicUtility.systemPrint("connected port", portName);
                BasicUtility.systemPrint("isConnected1",isConnected);
                if (isConnected) {
                    DeviceManager.getInstance().setConnecting(false);
                    return true;
                }else{
                    BasicUtility.systemPrint("before","autoConnect");
                    isConnected = ComLib.get().autoConnect(true, portName, DeviceManager.getPortDisconnectListener());
                    BasicUtility.systemPrint("after","autoConnect");
                    BasicUtility.systemPrint("isConnected2",isConnected);
                    if (isConnected) {
                        DeviceManager.getInstance().setConnecting(false);
                        return true;
                    }
                }

            } catch (PortNotFoundException e) {
                e.printStackTrace();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                BasicUtility.systemPrint("reconnecting", counter);
                counter++;
            }
        }

        return false;
    }

    public boolean reconnectWithResume(String pumpModel, String portName, PortDisconnectListener pd) {
        boolean isConnected = false;
        JSONObject liveDataJson = new JSONObject();

        try {
            isConnected = reconnect(pumpModel, portName, pd);
            if (isConnected) {
//                Thread.sleep(4000);
                TrialManager.getInstance().resume();
                // saving trial log
                if (TrialManager.getInstance().getTrialRunSettingId() != 0) {

                    LogManager.getInstance().insertTrialLog(
                            0,
                            TrialManager.getInstance().getTrialRunSettingId(),
                            ApiConstant.TRIAL_AUTO_RESUME_ACTION,
                            ApiConstant.LOG_TYPE_TRIAL_AUTO_LOG
                    );
                } else {
                    throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
                }
            }
        } catch (HardwareValidationException hvEx) {
            hvEx.printStackTrace();
            liveDataJson.put(ApiConstant.IS_FINISHED, TrialManager.getInstance().isFinished());
            liveDataJson.put(ApiConstant.TYPE, ApiConstant.DISCONNECT_PAUSE);
            liveDataJson.put(ApiConstant.MESSAGE, hvEx.getMessage());
            sLog.d("", liveDataJson);
            DeviceManager.getInstance().broadcastLiveData(liveDataJson);
        } catch (TrialRunException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isConnected;
    }
}
