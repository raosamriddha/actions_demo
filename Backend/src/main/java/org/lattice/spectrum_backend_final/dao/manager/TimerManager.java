package org.lattice.spectrum_backend_final.dao.manager;

/**
 * @author RAHUL KUMAR MAURYA
 */

import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.DatabaseManager;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.Logger;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 */
public class TimerManager {

    private Timer timer;


    //Getter and Setter

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }


    /**
     * Schedules the specified task for execution after the specified delay.
     */
    public void startTimer() {

                timer = new Timer();

                BasicUtility.systemPrint("startTimer : hit");

                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        // Now saving the logs in access-log
                        try {
                            if(DbConnectionManager.getInstance().getTokenManager().getUsername() != null){
                                Logger.info(this, "startTimer: "+ApiConstant.EXIT_BROWSER);
                                DatabaseManager.getInstance().addUserAccessLog(
                                        ApiConstant.EXIT_BROWSER,
                                        DbConnectionManager.getInstance().getTokenManager().getUsername(),
                                        ApiConstant.BLANK_QUOTE
                                );
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        Logger.info(this, "startTimer: Reset token.");
                        // logout the user
                        DbConnectionManager.getInstance().getTokenManager().setToken(null);
                        DbConnectionManager.getInstance().getTokenManager().setUserType(null);
                        DbConnectionManager.getInstance().getTokenManager().setUserEmail(null);
                        DbConnectionManager.getInstance().getTokenManager().setUserFullName(null);
                        DbConnectionManager.getInstance().getTokenManager().setUsername(null);
                        DbConnectionManager.getInstance().getTokenManager().setSessionTimeout(false);
                        DbConnectionManager.getInstance().getTokenManager().setUserLoginLimitMap(new HashMap<>());


                    }
        }, ApiConstant.TIMER_DELAY);
    }


}

