package org.lattice.spectrum_backend_final.dao.manager;

import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.util.Converter;

import java.text.DecimalFormat;

public class TrialDurationManager {
    private static TrialDurationManager trialDurationManager;

    private int hour;
    private int minute;
    private int second;

    DecimalFormat timeFormat = new DecimalFormat(ApiConstant.DURATION_TIME_FORMAT, Converter.symbols);

    public static TrialDurationManager getInstance() {

        synchronized (TrialDurationManager.class) {
            if (trialDurationManager == null) {
                trialDurationManager = new TrialDurationManager();
            }
        }

        return trialDurationManager;
    }

    /**
     * Get trial duration.
     * @return Returns duration in hh:mm:ss format.
     */
    public String getDuration(){
        tick();
        StringBuilder duration = new StringBuilder()
                .append(timeFormat.format(hour))
                .append(ApiConstant.COLON)
                .append(timeFormat.format(minute))
                .append(ApiConstant.COLON)
                .append(timeFormat.format(second));
        return duration.toString();
    }

    /**
     * Get trial duration.
     * @return Returns duration in 00h:00m:00s format.
     */
    public String getDurationFormatted(){
        StringBuilder duration = new StringBuilder()
                .append(timeFormat.format(hour))
                .append("h"+ApiConstant.COLON)
                .append(timeFormat.format(minute))
                .append("m"+ApiConstant.COLON)
                .append(timeFormat.format(second)+"s");
        return duration.toString();
    }

    /**
     * Increment the duration.
     * @return Returns duration in hh:mm:ss format.
     */
    public void tick(){
        second++;
        if(second == 60){
            second = 0;
            minute++;
            if(minute == 60){
                minute = 0;
                hour++;
            }
        }
    }

    /**
     * Reset the duration to 00:00:00.
     */
    public void resetDuration(){
        hour = 0;
        minute = 0;
        second = 0;
    }
}
