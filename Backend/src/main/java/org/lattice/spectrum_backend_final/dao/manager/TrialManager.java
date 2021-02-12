package org.lattice.spectrum_backend_final.dao.manager;

import static org.lattice.spectrum_backend_final.services.handler.ManualModeHandler.abvSettingsGlobal;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.ABVSetting;
import org.lattice.spectrum_backend_final.beans.Notes;
import org.lattice.spectrum_backend_final.beans.NotesDescSetup;
import org.lattice.spectrum_backend_final.beans.NotesDescription;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.Converter;
import org.lattice.spectrum_backend_final.dao.util.EndPointUtil;
import org.lattice.spectrum_backend_final.dao.util.KonduitUtil;
import org.lattice.spectrum_backend_final.dao.util.Logger;
import org.lattice.spectrum_backend_final.dao.util.Translator;
import org.lattice.spectrum_backend_final.enums.LiveDataType;
import org.lattice.spectrum_backend_final.exception.HardwareValidationException;
import org.lattice.spectrum_backend_final.exception.InvalidCredentialException;
import org.lattice.spectrum_backend_final.exception.TrialRunException;
import org.lattice.spectrum_backend_final.services.handler.ManualModeHandler;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.ModeError;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.PressureManager;
import com.lattice.spectrum.ModeLibrary.Managers.nonrun.NonRunModeManager;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeEvent;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeException;
import com.lattice.spectrum.ModeLibrary.Managers.run.ModeListener;
import com.lattice.spectrum.ModeLibrary.Managers.run.RunModeManager;
import com.lattice.spectrum.ModeLibrary.ModeProp.AlarmProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.AuxProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.FlowProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.KonduitProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.ModeProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.PermPumpProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.RecirculationProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.ValveProp;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.KonduitType;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.OperationMode;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.PressureTarget;
import com.lattice.spectrum.ModeLibrary.ModeProp.type.TotalizerMode;


public class TrialManager {

	private boolean isRunning = false;
	private boolean isFinished = false;
	private static TrialManager trialManager;
	private boolean isPaused = false;
	private int bufferLeftDataExecuteCounter = 0;

	private int subModeStep = 0;

	private JSONObject tableHeader = new JSONObject();

	private boolean isAlarmActive = false;

	private String currentSubMode = null;
	private String superMode = null;
	private int trialRunSettingId = 0;
	private boolean isTrialCompleted = false;
	/**
	 * Boolean flag used to determine whether to capture log of particular callback or not.
	 * <p>If true than captures log as notes else ignore it.</p>
	 */
	private boolean captureSystemLog = true;

    /**
     * Boolean flag to manage the alarm stop indicator while 30 second data collection
     */
	private boolean isHardFault = false;


	private TrialManager() {
	}


	public static TrialManager getInstance() {

		synchronized (TrialManager.class) {
			if (trialManager == null) {
				trialManager = new TrialManager();
			}
		}

		return trialManager;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean running) {

		isRunning = running;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setFinished(boolean finished) {
		isFinished = finished;
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void setPaused(boolean paused) {
		Logger.info("Setting pause to : "+paused);
		isPaused = paused;
	}

    public boolean isHardFault() {
		return isHardFault;
	}

    public void setHardFault(boolean hardFault) {
        isHardFault = hardFault;
    }

    public boolean isCaptureSystemLog() {
		return captureSystemLog;
	}

	public void setCaptureSystemLog(boolean captureSystemLog) {
		this.captureSystemLog = captureSystemLog;
	}

	public void setSubModeStep(int subModeStep) {
		this.subModeStep = subModeStep;
	}

	public int getTrialRunSettingId() {
		return trialRunSettingId;
	}


	public void setTrialRunSettingId(int trialRunSettingId) {
		this.trialRunSettingId = trialRunSettingId;
	}

	public int getBufferLeftDataExecuteCounter() {
		return bufferLeftDataExecuteCounter;
	}

	public void setBufferLeftDataExecuteCounter(int bufferLeftDataExecuteCounter) {
		this.bufferLeftDataExecuteCounter = bufferLeftDataExecuteCounter;
	}

	public JSONObject setFinishedJson(JSONObject liveDataJson) {
		liveDataJson.put(ApiConstant.TYPE, ApiConstant.EVENT);
		liveDataJson.put(ApiConstant.IS_FINISHED, isFinished());

		return liveDataJson;
	}

	public JSONObject getTableHeader() {
		return tableHeader;
	}

	public void setTableHeader(JSONObject tableHeader) {
		this.tableHeader = tableHeader;
	}

	public void setCurrentSubMode(String currentSubMode) {
		this.currentSubMode = currentSubMode;
	}

	public void setSuperMode(String superMode) {
		this.superMode = superMode;
	}

	public void setTrialCompleted(boolean trialCompleted) {
		isTrialCompleted = trialCompleted;
	}

	private LiveDataType getDummyCallBackType(Object event) {

		if (event instanceof ModeEvent) {
			if (
					event == ModeEvent.FEED_PRESSURE_ALARM_HIGH_STOP ||
					event == ModeEvent.FEED_PRESSURE_ALARM_HIGH_ALARM ||
					event == ModeEvent.FEED_PRESSURE_ALARM_LOW_ALARM ||
					event == ModeEvent.FEED_PRESSURE_ALARM_LOW_STOP ||
					event == ModeEvent.PERMEATE_PRESSURE_ALARM_HIGH_STOP ||
					event == ModeEvent.PERMEATE_PRESSURE_ALARM_HIGH_ALARM ||
					event == ModeEvent.PERMEATE_PRESSURE_ALARM_LOW_ALARM ||
					event == ModeEvent.PERMEATE_PRESSURE_ALARM_LOW_STOP ||
					event == ModeEvent.FEED_WEIGHT_ALARM_HIGH_STOP ||
					event == ModeEvent.FEED_WEIGHT_ALARM_HIGH_ALARM ||
					event == ModeEvent.FEED_WEIGHT_ALARM_LOW_ALARM ||
					event == ModeEvent.FEED_WEIGHT_ALARM_LOW_STOP ||
					event == ModeEvent.PERMEATE_WEIGHT_ALARM_HIGH_STOP ||
					event == ModeEvent.PERMEATE_WEIGHT_ALARM_HIGH_ALARM ||
					event == ModeEvent.PERMEATE_WEIGHT_ALARM_LOW_ALARM ||
					event == ModeEvent.PERMEATE_WEIGHT_ALARM_LOW_STOP ||
					event == ModeEvent.KONDUIT2_ALARM_HIGH_STOP ||
					event == ModeEvent.KONDUIT2_ALARM_HIGH_ALARM ||
					event == ModeEvent.KONDUIT1_ALARM_HIGH_ALARM ||
					event == ModeEvent.KONDUIT1_ALARM_HIGH_STOP
					) {

				BasicUtility.systemPrint("hey", "0");

				return LiveDataType.ALARM;


			}
		}
		BasicUtility.systemPrint("hey", "00");

		return LiveDataType.EVENT;

	}

	private String getAlarmNature(ModeEvent id) {

		if (
				id == ModeEvent.FEED_PRESSURE_ALARM_LOW_ALARM ||
				id == ModeEvent.PERMEATE_PRESSURE_ALARM_LOW_ALARM ||
				id == ModeEvent.FEED_WEIGHT_ALARM_LOW_ALARM ||
				id == ModeEvent.PERMEATE_WEIGHT_ALARM_LOW_ALARM


				) {

			return ApiConstant.LOW;

		} else if (
				id == ModeEvent.FEED_PRESSURE_ALARM_HIGH_ALARM ||
				id == ModeEvent.PERMEATE_PRESSURE_ALARM_HIGH_ALARM ||
				id == ModeEvent.FEED_WEIGHT_ALARM_HIGH_ALARM ||
				id == ModeEvent.PERMEATE_WEIGHT_ALARM_HIGH_ALARM ||
				id == ModeEvent.KONDUIT2_ALARM_HIGH_ALARM ||
				id == ModeEvent.KONDUIT1_ALARM_HIGH_ALARM ||
				id == ModeEvent.FLOW_METER_CH1_ALARM_HIGH_ALARM ||
				id == ModeEvent.FLOW_METER_CH2_ALARM_HIGH_ALARM
				) {

			return ApiConstant.HIGH;

		} else if (
				id == ModeEvent.PERMEATE_PRESSURE_ALARM_HIGH_STOP ||
				id == ModeEvent.FEED_PRESSURE_ALARM_HIGH_STOP ||
				id == ModeEvent.FEED_PRESSURE_ALARM_LOW_STOP ||
				id == ModeEvent.PERMEATE_PRESSURE_ALARM_LOW_STOP ||
				id == ModeEvent.FEED_WEIGHT_ALARM_HIGH_STOP ||
				id == ModeEvent.FEED_WEIGHT_ALARM_LOW_STOP ||
				id == ModeEvent.PERMEATE_WEIGHT_ALARM_HIGH_STOP ||
				id == ModeEvent.PERMEATE_WEIGHT_ALARM_LOW_STOP ||
				id == ModeEvent.KONDUIT2_ALARM_HIGH_STOP ||
				id == ModeEvent.KONDUIT1_ALARM_HIGH_STOP ||
				id == ModeEvent.FLOW_METER_CH1_ALARM_HIGH_STOP ||
				id == ModeEvent.FLOW_METER_CH2_ALARM_HIGH_STOP
				) {

			return ApiConstant.STOP;
		}

		return ApiConstant.INVALID;
	}

	private String getAlarmStopDescription(Object enumString) {

		if (enumString instanceof ModeEvent) {

			if (
					enumString == ModeEvent.FEED_PRESSURE_ALARM_HIGH_STOP ||
					enumString == ModeEvent.FEED_PRESSURE_ALARM_HIGH_ALARM ||
					enumString == ModeEvent.FEED_PRESSURE_ALARM_LOW_ALARM ||
					enumString == ModeEvent.FEED_PRESSURE_ALARM_LOW_STOP) {

				return ApiConstant.ALARM_DESCRIPTION_FEED_PRESSURE;

			} else if (

					enumString == ModeEvent.PERMEATE_PRESSURE_ALARM_HIGH_STOP ||
					enumString == ModeEvent.PERMEATE_PRESSURE_ALARM_HIGH_ALARM ||
					enumString == ModeEvent.PERMEATE_PRESSURE_ALARM_LOW_ALARM ||
					enumString == ModeEvent.PERMEATE_PRESSURE_ALARM_LOW_STOP) {

				return ApiConstant.ALARM_DESCRIPTION_PERMEATE_PRESSURE;

			} else if (

					enumString == ModeEvent.FEED_WEIGHT_ALARM_HIGH_STOP ||
					enumString == ModeEvent.FEED_WEIGHT_ALARM_HIGH_ALARM ||
					enumString == ModeEvent.FEED_WEIGHT_ALARM_LOW_ALARM ||
					enumString == ModeEvent.FEED_WEIGHT_ALARM_LOW_STOP) {

				return ApiConstant.ALARM_DESCRIPTION_FEED_WEIGHT;

			} else if (

					enumString == ModeEvent.PERMEATE_WEIGHT_ALARM_HIGH_STOP ||
					enumString == ModeEvent.PERMEATE_WEIGHT_ALARM_HIGH_ALARM ||
					enumString == ModeEvent.PERMEATE_WEIGHT_ALARM_LOW_ALARM ||
					enumString == ModeEvent.PERMEATE_WEIGHT_ALARM_LOW_STOP) {

				return ApiConstant.ALARM_DESCRIPTION_PERMEATE_WEIGHT;

			} else if (

					enumString == ModeEvent.KONDUIT2_ALARM_HIGH_STOP ||
					enumString == ModeEvent.KONDUIT2_ALARM_HIGH_ALARM) {

				return ApiConstant.ALARM_DESCRIPTION_KONDUIT_CH_2;

			} else if (

					enumString == ModeEvent.KONDUIT1_ALARM_HIGH_ALARM ||
					enumString == ModeEvent.KONDUIT1_ALARM_HIGH_STOP
					) {

				return ApiConstant.ALARM_DESCRIPTION_KONDUIT_CH_1;


			} else if (

					enumString == ModeEvent.FLOW_METER_CH1_ALARM_HIGH_ALARM ||
					enumString == ModeEvent.FLOW_METER_CH1_ALARM_HIGH_STOP ||
					enumString == ModeEvent.FLOW_METER_CH2_ALARM_HIGH_STOP ||
					enumString == ModeEvent.FLOW_METER_CH2_ALARM_HIGH_ALARM
					) {

				return ApiConstant.ALARM_DESCRIPTION_PERMEATE_TOTAL;
			}

		}
		return ApiConstant.INVALID;


	}

	public boolean isAlarmActive() {
		return isAlarmActive;
	}

	public void setAlarmActive(boolean alarmActive) {
		isAlarmActive = alarmActive;
	}

	private String getModeName() {

		String modeName = null;

		if (DeviceManager.getInstance().getModeType() == 0) {

			String recipeModeName = DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.MODE_NAME);
			if (BasicUtility.getInstance().isRunMode(recipeModeName) && !ApiConstant.VACUUM_MODE.equals(recipeModeName)) {

				modeName = currentSubMode +
						ApiConstant.BLANK_SPACE +
						ApiConstant.OF +
						ApiConstant.BLANK_SPACE +
						superMode;
			} else {
				modeName = superMode;
			}
		} else if (DeviceManager.getInstance().getModeType() == 1) {
			modeName = ApiConstant.MANUAL;
		}

		return modeName;

	}

	private String getAlarmStopValue(Object enumString, double value) throws SQLException {

		String desc = getAlarmStopDescription(enumString);
		String alarmStopValue = null;

		if (
				ApiConstant.ALARM_DESCRIPTION_FEED_PRESSURE.equalsIgnoreCase(desc) ||
				ApiConstant.ALARM_DESCRIPTION_PERMEATE_PRESSURE.equalsIgnoreCase(desc)
				) {
			String pressureUnit = Converter.systemSettingPressureMapper(Converter.getSystemSetting().getPressureUnit());

			int pressureDecimalPlace = Converter.getSystemSetting().getDecimalPressure();

			value = Converter.unitConverter(value, pressureUnit);
			alarmStopValue = Converter.setDecimalPlace(value, pressureDecimalPlace) +
					ApiConstant.BLANK_SPACE +
					pressureUnit;
		} else if (
				ApiConstant.ALARM_DESCRIPTION_FEED_WEIGHT.equalsIgnoreCase(desc) ||
				ApiConstant.ALARM_DESCRIPTION_PERMEATE_WEIGHT.equalsIgnoreCase(desc)
				) {
			String weightUnit = Converter.systemSettingWeightMapper(Converter.getSystemSetting().getWeightUnit());

			int weightDecimalPlace = Converter.getSystemSetting().getDecimalWeight();

			value = Converter.unitConverter(value, weightUnit);
			alarmStopValue = Converter.setDecimalPlace(value, weightDecimalPlace) +
					ApiConstant.BLANK_SPACE +
					weightUnit;
		} else if (
				ApiConstant.ALARM_DESCRIPTION_PERMEATE_TOTAL.equalsIgnoreCase(desc)

				) {
			String volumeUnit = Converter.systemSettingVolumeMapper(Converter.getSystemSetting().getVolumeUnit());

			int volumeDecimalPlace = Converter.getSystemSetting().getDecimalVolume();

			value = Converter.unitConverter(value, volumeUnit);
			alarmStopValue = Converter.setDecimalPlace(value, volumeDecimalPlace) +
					ApiConstant.BLANK_SPACE +
					volumeUnit;
		} else if (
				ApiConstant.ALARM_DESCRIPTION_KONDUIT_CH_1.equalsIgnoreCase(desc) ||
				ApiConstant.ALARM_DESCRIPTION_KONDUIT_CH_2.equalsIgnoreCase(desc)

				) {
			alarmStopValue = Converter.getAuxConverter(value);

		}
		return alarmStopValue;
	}

	public void disableConnection() {
		DeviceManager.getInstance().setModeType(2);
		setRunning(false);
		setFinished(true);
		DeviceManager.getInstance().setSocketSet(false);
		DeviceManager.getInstance().stopRxListener();
	}

	/**
	 * Function to manage all the callbacks generated from middleware.
	 * Callbacks are used for saving notes , sending live event messages to frontend
	 * and to perform some action as per callback type.
	 * @param id {@link ModeEvent} type of callback.
	 * @param alarmJson Alarm json object.
	 * @param obj Array of objects consist {@link ModeError}, {@link ModeException}, {@link ModeEvent}, {@link OperationMode}
	 * @throws Exception
	 */
	public void setDummyListenerLiveData(ModeEvent id, JSONObject alarmJson, Object... obj) throws Exception {
		JSONObject liveDataJson = new JSONObject();
		String modeName = "";
		String currentMode = null;
		if(DeviceManager.getInstance().getRecipeJson() != null){
			currentMode = DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.MODE_NAME);
		}
		boolean isPermeateConnected = true;
		NotesDescription notesDescription = null;
		NotesDescSetup notesDescSetup = null;


		if (
				(obj.length != 0) &&
				(id == ModeEvent.HARD_FAULT || id == ModeEvent.SOFT_FAULT) &&
				alarmJson != null
				) {

			String message = BasicUtility.enumToStringConversion(obj[0]);
			String deviceText = BasicUtility.getInstance().getDeviceAlarmIndicator((ModeEvent) obj[0]);
			liveDataJson.put(ApiConstant.TYPE, ApiConstant.ALARM);
			liveDataJson.put(ApiConstant.MODE_NAME, getModeName());
			liveDataJson.put(ApiConstant.TRIAL_RUN_SETTING_ID, alarmJson.getInt(ApiConstant.TRIAL_RUN_SETTING_ID));
			if (obj.length != 3) {
				liveDataJson.put(ApiConstant.ALARM_STOP_DESCRIPTION, getAlarmStopDescription(obj[0]));
			} else {
				message = new KonduitUtil().getKonduitText((KonduitType) obj[2]) + message;
				deviceText = new KonduitUtil().getKonduitTypeToDeviceText((KonduitType) obj[2]) + deviceText;
				liveDataJson.put(ApiConstant.ALARM_STOP_DESCRIPTION, BasicUtility.enumToStringConversion(obj[2]) + getAlarmStopDescription(obj[0]));
			}
			liveDataJson.put(ApiConstant.ALARM_STOP_VALUE, String.valueOf(obj[1]));
			liveDataJson.put(ApiConstant.NATURE, getAlarmNature((ModeEvent) obj[0]));
			liveDataJson.put(ApiConstant.TIMESTAMP, BasicUtility.getInstance().getCurrentTimestamp());
			liveDataJson.put(ApiConstant.MESSAGE, message);

			//set alarm indicator
			if (!DeviceManager.getInstance().isMuteCall()) {
				BasicUtility.systemPrint("inside", "unmute");
				AlarmManager.getInstance().setDeviceAlarmIndicator(deviceText);
				isAlarmActive = true;
			} else {
				AlarmManager.getInstance().muteAlarmIndicator(deviceText);
			}

			//save alarm history to db
			AlarmManager.getInstance().saveAlarmHistory(liveDataJson);
			liveDataJson.put(ApiConstant.ALARM_STOP_VALUE, getAlarmStopValue(obj[0], (double) obj[1]));

			if (id == ModeEvent.HARD_FAULT) {

				if (DeviceManager.getInstance().getModeType() == 1) {

					try {
						BasicUtility.systemPrint("manual", "stop");
						ManualModeHandler.getInstance().stopManualMode();
						DeviceManager.getInstance().resetFlowRateIndicator();
						liveDataJson.put(ApiConstant.IS_FINISHED, true);
						ManualModeHandler.isTrialStarted = 0;
						RunManager.getInstance().updateTrialRunStatus(
								TrialManager.getInstance().getTrialRunSettingId(),
								BasicUtility.getInstance().getCurrentTimestamp(),
								ApiConstant.TRIAL_TERMINATE_STATUS
								);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (DeviceManager.getInstance().getModeType() == 0) {
					// reset flow rate indicator to 0.
					DeviceManager.getInstance().resetSpeedAndFlowRateIndicator(0);
					isHardFault = true;
				}
				BasicUtility.systemPrint("stopped hard fault", "dummy");
				liveDataJson.put(ApiConstant.TYPE, ApiConstant.ALARM);
			}

		} else {

			if (obj.length != 0) {
				setCaptureSystemLog(true);
				if (ModeError.UNABLE_TO_MAINTAIN_PRESSURE == obj[0]) {

					for (int index = 0; index < obj.length; index++) {

						modeName = modeName + BasicUtility.enumToStringConversion(obj[index]);

					}
				} else if(id != ModeEvent.STOP){
					modeName = BasicUtility.enumToStringConversion(obj[0]);
				}

				if (ModeException.PERMEATE_SCALE_MISSING == obj[0]) {

					isPermeateConnected = false;
				}

				// Below conditions are used to manage the DV1 and DV2 value switch.
				if (
						DeviceManager.getInstance().getRecipeJson() != null &&
						(
								ApiConstant.CDDC_MODE.equals(DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.MODE_NAME))
								) &&
						id == ModeEvent.SUB_MODE_FINISHED
						) {
					if ((int) obj[1] == 1) {

						// setting firstDModeFinished to true to track DV1 and DV2.
						DeviceManager.getInstance().setFirstDModeFinished(true);
					} else if ((int) obj[1] == 2) {

						// setting SecondDModeFinished to true to track DV1 and DV2.
						DeviceManager.getInstance().setSecondDModeFinished(true);
					}

				} else if (
						DeviceManager.getInstance().getRecipeJson() != null &&
						(
								ApiConstant.CDCD_MODE.equals(DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.MODE_NAME))
								) &&
						id == ModeEvent.SUB_MODE_FINISHED
						) {
					if ((int) obj[1] == 1) {

						// setting firstDModeFinished to true to track DV1 and DV2.
						DeviceManager.getInstance().setFirstDModeFinished(true);
					} /*else if ((int) obj[1] == 3) {

						// setting SecondDModeFinished to true to track DV1 and DV2.
						DeviceManager.getInstance().setSecondDModeFinished(true);
					}*/
				}
				else if (
						DeviceManager.getInstance().getRecipeJson() != null &&
						(
								ApiConstant.CDC_MODE.equals(DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.MODE_NAME)) ||
								ApiConstant.CFDC_MODE.equals(DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.MODE_NAME))
								) &&
						id == ModeEvent.SUB_MODE_FINISHED
						) {
					if ((int) obj[1] == 1) {

						// setting firstDModeFinished to true to track DV1 and DV2.
						DeviceManager.getInstance().setFirstDModeFinished(true);
					}
				}
			}
			if (id == ModeEvent.RAMP_UP_DONE) {
				setCaptureSystemLog(true);

				isTrialCompleted = false;
				superMode = DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.MODE_NAME);

				if (!DeviceManager.getInstance().isConnectionListRunning()) {

					DeviceManager.connectionListener.startRxListening();
					DeviceManager.getInstance().setConnectionListRunning(true);
				}

				RunManager.getInstance().updateTrialRunStatus(
						getTrialRunSettingId(),
						null,
						ApiConstant.TRIAL_RUNNING_STATUS
						);
			} else if (id == ModeEvent.PROMT_USER) {
				setCaptureSystemLog(false);

				DeviceManager.getInstance().setCleaningPause(true);
				Thread.sleep(1000);

				DeviceManager.getInstance().stopRxListener();
				isRunning = false;

			} else if (id == ModeEvent.SUPER_MODE_FINISHED) {
				setCaptureSystemLog(true);

				// update trial status
				RunManager.getInstance().updateTrialRunStatus(
						getTrialRunSettingId(),
						BasicUtility.getInstance().getCurrentTimestamp(),
						ApiConstant.TRIAL_COMPLETE_STATUS
						);

				// increase counter to execute buffer left data
				bufferLeftDataExecuteCounter++;

				isTrialCompleted = true;

			} else if (id == ModeEvent.STOP) {
				setCaptureSystemLog(true);
				// reset flow rate indicator to 0.
				DeviceManager.getInstance().resetSpeedAndFlowRateIndicator(0);
				//stop alarm indicator
				//                AlarmManager.getInstance().stopDeviceAlarmIndicator();
				if (!isTrialCompleted) {
					// update trial status
					RunManager.getInstance().updateTrialRunStatus(
							getTrialRunSettingId(),
							BasicUtility.getInstance().getCurrentTimestamp(),
							ApiConstant.TRIAL_TERMINATE_STATUS
							);
				}
				// increase counter to execute buffer left data
				bufferLeftDataExecuteCounter++;
				BasicUtility.systemPrint("stopped", "dummy");
			} else if (id == ModeEvent.PERMEATE_SCALE_TARE_TIMEOUT) {
				setCaptureSystemLog(true);
				// reset flow rate indicator to 0.
				DeviceManager.getInstance().resetSpeedAndFlowRateIndicator(0);

				disableConnection();
				liveDataJson = setFinishedJson(liveDataJson);

				// update trial status
				RunManager.getInstance().updateTrialRunStatus(
						getTrialRunSettingId(),
						BasicUtility.getInstance().getCurrentTimestamp(),
						ApiConstant.TRIAL_TERMINATE_STATUS
						);

			} else if (id == ModeEvent.SUB_MODE_STARTED) {
				BasicUtility.systemPrint("currentMode",currentMode);
				if( BasicUtility.getInstance().isRunMode(currentMode) && !ApiConstant.VACUUM_MODE.equals(currentMode)){
					currentSubMode = BasicUtility.enumToStringConversion(obj[0]);
					notesDescription = new NotesDescription();
					notesDescription.setStatus(ApiConstant.NOTES_DESC_START_STATUS);
					setCaptureSystemLog(true);
				}else if(
						ApiConstant.CLEANING.equals(currentMode) ||
						ApiConstant.FLUSHING.equals(currentMode) ||
						ApiConstant.NWP.equals(currentMode)
						){
					currentSubMode = currentMode;
					notesDescSetup = new NotesDescSetup();
					notesDescSetup.setStatus(ApiConstant.NOTES_DESC_START_STATUS);
					setCaptureSystemLog(true);
					subModeStep = (int)obj[0];
				}else if(
						ApiConstant.FLUX_C.equals(currentMode) ||
						ApiConstant.FLUX_CV.equals(currentMode)
						){
					currentSubMode = currentMode;
					notesDescSetup = new NotesDescSetup();
					notesDescSetup.setStatus(ApiConstant.NOTES_DESC_START_STATUS);
					setCaptureSystemLog(true);
					subModeStep = ((((int) obj[0]) * 3) + ((int) obj[1]));
				}else{
					setCaptureSystemLog(false);
				}

			}

			else if (id == ModeEvent.SUB_MODE_FINISHED) {
				BasicUtility.systemPrint("currentMode",currentMode);
				setCaptureSystemLog(true);
				if(ApiConstant.FLUX_CV.equalsIgnoreCase(superMode) || ApiConstant.FLUX_C.equalsIgnoreCase(superMode)) {
					subModeStep = ((((int) obj[0]) * 3) + ((int) obj[1]));
					notesDescSetup = new NotesDescSetup();
					notesDescSetup.setStatus(ApiConstant.NOTES_DESC_END_STATUS);
				}else if(BasicUtility.getInstance().isRunMode(currentMode)){
					if(ApiConstant.VACUUM_MODE.equals(currentMode)){
						setCaptureSystemLog(false);
					}else{
						notesDescription = new NotesDescription();
						notesDescription.setStatus(ApiConstant.NOTES_DESC_END_STATUS);
						subModeStep = (int)obj[1] + 1;
					}
				}else{
					notesDescSetup = new NotesDescSetup();
					notesDescSetup.setStatus(ApiConstant.NOTES_DESC_END_STATUS);
				}

				liveDataJson = BasicUtility.getInstance().getProgressBarData(obj, liveDataJson);

			} else if (id == ModeEvent.MODE_COUNTER) {
				setCaptureSystemLog(false);
				DeviceManager.getInstance().setFlowRateCount((int) obj[0] + 1);
			}else if (id == ModeEvent.FINAL_COLLECTION_TIME){
				setCaptureSystemLog(false);
				liveDataJson.put(ApiConstant.TIMER_KEY, obj[0]);
			}else if (id == ModeEvent.ALL_MODE_FINISHED){
				setCaptureSystemLog(false);
			}
			String action = modeName + BasicUtility.enumToStringConversion(id);

			// send is_Finished json
			liveDataJson = setFinishedJson(liveDataJson);
			if(captureSystemLog){

				if(notesDescription != null){
					String subMode = (currentSubMode != null && !ApiConstant.BLANK_QUOTE.equals(currentSubMode)) ? currentSubMode : superMode;
					int step;
					if( id == ModeEvent.SUB_MODE_FINISHED ){
						step = subModeStep - 1;
					}else{
						step = subModeStep;
					}
					notesDescription.setSubModeName(subMode);
					notesDescription.setStep(step + 1);
					notesDescription = EndPointUtil.getInstance().setEndPoint(notesDescription, step);
					sLog.d(this, notesDescription);
					NotesDescManager notesDescManager = new NotesDescManager();
					notesDescManager.setNotesDescription(notesDescription);
					notesDescManager.saveNotesDescriptionToDb();

				}else if(notesDescSetup != null){

					String subMode = (currentSubMode != null && !ApiConstant.BLANK_QUOTE.equals(currentSubMode)) ? currentSubMode : superMode;

					notesDescSetup.setModeName(subMode);
					notesDescSetup.setStep(subModeStep + 1);
					notesDescSetup = EndPointUtil.getInstance().setTargetStep(notesDescSetup, subModeStep);
					sLog.d(this, notesDescSetup);
					NotesDescSetupManager notesDescSetupManager = new NotesDescSetupManager();
					notesDescSetupManager.setNotesDescSetup(notesDescSetup);
					notesDescSetupManager.saveNotesDescSetupToDb();
				}else{
					sLog.d("LOG : insert trial log");
					LogManager.getInstance().insertTrialLog(
							0,
							trialRunSettingId,
							action,
							ApiConstant.LOG_TYPE_TRIAL_AUTO_LOG
							);
				}
			}

			setCaptureSystemLog(true);
			BasicUtility.systemPrint("message", "" + action);
			liveDataJson.put(ApiConstant.MESSAGE, "" + action);

		}

		if(bufferLeftDataExecuteCounter == 1){
			DeviceManager.getInstance().sendLiveData(ComLib.get(), false);
			// executes buffer left data.
			BufferManager.getInstance().executeBufferLeftData();
			disableConnection();
			liveDataJson = setFinishedJson(liveDataJson);
			if(id == ModeEvent.STOP){
				//setting recipe json to null so that after trial completion there wouldn't
				// be any recirculationPause call after port disconnection
				DeviceManager.getInstance().setRecipeJson(null);
			}
		}

		if (
				isPermeateConnected &&
				!(
						(id == ModeEvent.SUB_MODE_FINISHED ||
						id == ModeEvent.SUB_MODE_STARTED) &&
						ApiConstant.VACUUM_MODE.equals(currentMode)
						)
				) {
			sLog.d(this, liveDataJson);
			DeviceManager.getInstance().broadcastLiveData(liveDataJson);
		}

	}

	/**
	 * Start Auto mode trial including Vacuum mode.
	 * @param runJson settings required to run a trial.
	 * @param recipeJson recipe json.
	 * @throws Exception throws exception if any issue occur while performing this action.
	 */
	public void startAutoModeTrial(JSONObject runJson, JSONObject recipeJson) throws Exception {

		OperationMode opModeName = (OperationMode) BasicUtility.stringToEnumConversion(recipeJson.getString(ApiConstant.MODE_NAME));

		Logger.setTrialRunOutputLogToFile(opModeName, true);
		setFinished(false);

		JSONObject alarmJson = AlarmManager.getInstance()
				.fetchAlarmSetting(runJson.getString(ApiConstant.TRIAL_ID));
		KonduitUtil konduitUtil = new KonduitUtil();
		KonduitProp[] konduitProp ;


		sLog.d(this, recipeJson);
		sLog.d(this, runJson);

		DeviceManager.getInstance().setModeType(0);

		RecirculationProp recirculationProp = null;

		int loopCount = 0;

		int totalAbv = recipeJson.getJSONArray(ApiConstant.ABV).length();
		int totalAuxPump = recipeJson.getJSONArray(ApiConstant.AUX_PUMP).length();
		int totalSubMode = recipeJson.getJSONArray(ApiConstant.END_POINTS).length();
		double totalPermTubeHoldup = 0;

		RunModeManager runModeManager = RunModeManager.get();

		ModeProp modeProp = new ModeProp();
		AuxProp[] auxProp = null;
		ValveProp[] valveProp = null;
		AlarmProp alarmProp = null;
		FlowProp flowProp = null;

		boolean isFeedEmpty = true;
		double feedHoldUp = 0.0;

		String[] subMode = recipeJson.getString(ApiConstant.MODE_NAME).split(ApiConstant.FORWARD_SLASH);


		ModeListener dummyModeListener = new ModeListener() {
			@Override
			public void callback(ModeEvent id, Object... obj) {
				sLog.d("DummyCallback", "Event Reported [ID:" + id + ", Data:" + Arrays.toString(obj));

				try {
					setDummyListenerLiveData(id, alarmJson, obj);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		};


		if (!DbConnectionManager.getInstance().isDebuggingMode) {

			//check either recipe is valid for run or not.
			ValidationManager.isRecipeValidForRun(recipeJson, runJson);
		}

		if (konduitUtil.isTotalizerMode()) {
			modeProp.setTotalizerMode(TotalizerMode.OBSERVER);
		} else {
			modeProp.setTotalizerMode(TotalizerMode.NONE);
		}

		//set recirculation property
		recirculationProp = Translator.getInstance().translateRecirculationProp(recipeJson);


		// set flow property
		flowProp = Translator.getInstance().translateFlowProp(recipeJson);


		if (!ApiConstant.VACUUM_MODE.equalsIgnoreCase(recipeJson.getString(ApiConstant.MODE_NAME))) {

			// Translate run sub mode property.
			modeProp = Translator.getInstance().translateRunSubModeProp(recipeJson, subMode, totalAuxPump, modeProp);


			loopCount = 0;
			if (totalAuxPump > totalSubMode) {

				while (loopCount < totalAuxPump) {

					JSONObject auxJson = recipeJson.getJSONArray(ApiConstant.AUX_PUMP)
							.getJSONObject(loopCount);

					if (
							auxJson.getString(ApiConstant.PUMP_FUNCTION)
							.contains(ApiConstant.PERMEATE)
							) {

						BasicUtility.systemPrint("permeate aux id loopcount", loopCount);
						//check for permeate pump is there or not in recipe
						modeProp.setPermPump(new PermPumpProp(
								auxJson.getInt(ApiConstant.TYPE),
								ApiConstant.YES.equals(recipeJson.getString(ApiConstant.PERMEATE_STOP_FIRST))));

					}
					loopCount++;

				}

			}

		} else {

			modeProp.setRunModes(
					Translator.getInstance()
					.translateVacuumNonRunModeProp()
					);

		}


		modeProp.setSuperID(opModeName);

		//        uvCh1MeasuredValue * (systemSetting.getFlowRateMax() - systemSetting.getFlowRateMin())/(systemSetting.getUvMax() - systemSetting.getUvMin()) + systemSetting.getFlowRateMin()


		if (ApiConstant.YES.equalsIgnoreCase(recipeJson.getString(ApiConstant.PERMEATE_HOLD_UP_VOLUME_CALCULATION))) {
			totalPermTubeHoldup = recipeJson.getDouble(ApiConstant.TOTAL_PERM_TUBE_HOLDUP);
		}


		modeProp.setPermHoldUp(totalPermTubeHoldup);


		//setting aux pump property

		auxProp = Translator.getInstance().translateAuxProp(totalAuxPump, recipeJson);


		//setting ABV property

		valveProp = Translator.getInstance().translateValveProp(recipeJson, totalAbv);

		sLog.d(this, valveProp);

		if (ApiConstant.NO.equalsIgnoreCase(runJson.getString(ApiConstant.IS_FEED_EMPTY))) {
			isFeedEmpty = false;
		}

		feedHoldUp = runJson.getDouble(ApiConstant.FEED_HOLD_UP);

		if (!ApiConstant.BLANK_QUOTE.equalsIgnoreCase(runJson.getString(ApiConstant.FEED_START_WEIGHT))) {

			modeProp.setFeedStartWt(runJson.getDouble(ApiConstant.FEED_START_WEIGHT));
		}

		// set alarm property

		if (alarmJson != null) {

			alarmProp = AlarmManager.getInstance().setAlarm(alarmJson);
		}

		sLog.d(this, alarmProp);

		konduitProp = konduitUtil.getKonduitProp();
		modeProp.setKonduit(konduitProp);
		modeProp.setFeedHoldUpEmpty(isFeedEmpty);
		modeProp.setFeedHoldUp(feedHoldUp);
		modeProp.setAuxPumps(auxProp);
		modeProp.setValves(valveProp);
		modeProp.setFlowProp(flowProp);
		modeProp.setRecirculationProp(recirculationProp);
		modeProp.setAlarmProp(alarmProp);
		//        modeProp.setPressureProp(pressureProp);


		DeviceManager.getInstance().setModeProp(modeProp);
		sLog.d(this, modeProp);
		//set system setting once for trial.
		Converter.setSystemSetting();


		if (!DbConnectionManager.getInstance().isDebuggingMode) {

			runModeManager
			.setProp(modeProp)
			.setSuperCallback(dummyModeListener)
			.setup();

			// socket connection will resume if there is no exception in trial start.
			DeviceManager.getInstance().connectSocket();
		}

		RunManager.getInstance().updateTrialStartTime(
				getTrialRunSettingId(),
				BasicUtility.getInstance().getCurrentTimestamp()
				);

	}

	public void stopTrial() throws Exception {

		try {

			String modeName = DeviceManager.getInstance()
					.getRecipeJson()
					.getString(ApiConstant.MODE_NAME);

			BufferManager.getInstance().setPausedToBuffer(0);
			// saving trial log
			if (getTrialRunSettingId() != 0) {

				LogManager.getInstance().insertTrialLog(
						DbConnectionManager.getInstance().getTokenManager().getUserId(),
						getTrialRunSettingId(),
						ApiConstant.TRIAL_STOP_ACTION,
						ApiConstant.LOG_TYPE_TRIAL
						);

			} else {
				throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
			}
			if (BasicUtility.getInstance().isRunMode(modeName)) {
				BasicUtility.systemPrint("inside", "runstop");
				RunModeManager.get().stop();
			} else {
				BasicUtility.systemPrint("inside", "nonrunstop");
				NonRunModeManager.get().stop();
			}
			Thread.sleep(1000);
			disableConnection();
			DeviceManager.connectionListener.stopRxListening();
			DeviceManager.pumpKeyListener.stopRxListening();
			AlarmManager.getInstance().stopDeviceAlarmIndicator();
			DeviceManager.getInstance().resetFlowRateIndicator();
			DeviceManager.getInstance().setConnectionListRunning(false);
			DeviceManager.getInstance().setModeType(2);

			//            trialRunSettingId = 0;
		} catch (InvalidCredentialException ex) {
			throw new InvalidCredentialException(ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}

	}

	public void onPause() throws Exception {

		setRunning(false);

		DeviceManager.getInstance().stopRxListener();

		//setting paused flag to buffer
		BufferManager.getInstance().setPausedToBuffer(1);

	}

	public void pauseTrial() throws Exception {
		try {

			//stop alarm indicator
			AlarmManager.getInstance().stopDeviceAlarmIndicator();

			String modeName = DeviceManager.getInstance()
					.getRecipeJson()
					.getString(ApiConstant.MODE_NAME);

			//saving trial log
			if (getTrialRunSettingId() != 0) {

				LogManager.getInstance().insertTrialLog(
						DbConnectionManager.getInstance().getTokenManager().getUserId(),
						getTrialRunSettingId(),
						ApiConstant.TRIAL_PAUSE_ACTION,
						ApiConstant.LOG_TYPE_TRIAL
						);
			} else {
				throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
			}

			if (BasicUtility.getInstance().isRunMode(modeName)) {

				BasicUtility.systemPrint("inside", "runpause");
				RunModeManager.get().pause();
			} else {
				BasicUtility.systemPrint("inside", "nonrunpause");
				NonRunModeManager.get().pause();
			}
			isPaused = true;

			//            DeviceManager.getInstance().resetSpeedAndFlowRateIndicator();
			onPause();



		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}
	}

	public void resume() throws HardwareValidationException {

		//check either recipe is valid for run or not.
		ValidationManager.isRecipeValidForRun(DeviceManager.getInstance().getRecipeJson(), null);

		String modeName = DeviceManager.getInstance()
				.getRecipeJson()
				.getString(ApiConstant.MODE_NAME);
		if (!isRunning()) {


			if (BasicUtility.getInstance().isRunMode(modeName)) {

				BasicUtility.systemPrint("inside", "runresume");
				RunModeManager.get().resume();
			} else {

				BasicUtility.systemPrint("inside", "nonrunresume");
				NonRunModeManager.get().resume();
			}
			isPaused = false;

			//                setRunning(true);
			//                DeviceManager.getInstance().startRxListener();

			// socket connection will resume if there is no exception in trial resume.
			DeviceManager.getInstance().connectSocket();

			//reset cleaning status
			DeviceManager.getInstance().setCleaningPause(false);
			DeviceManager.getInstance().setCleaningMessage(null);
		}
	}

	public void resumeTrial(String digitalSignatureValues) throws Exception {
		try {

			JSONObject digitalSignatureJson = new JSONObject(digitalSignatureValues);


			if (DatabaseManager.getInstance().isDigitalSignatureMTVerified(digitalSignatureJson, false)) {
				// saving trial log
				if (getTrialRunSettingId() != 0) {

					LogManager.getInstance().insertTrialLog(
							BasicUtility.getInstance().getUserId(digitalSignatureJson),
							getTrialRunSettingId(),
							ApiConstant.TRIAL_RESUME_ACTION,
							ApiConstant.LOG_TYPE_TRIAL
							);
					//Adding digital signature notes
					if(digitalSignatureJson.has(ApiConstant.DIGITAL_NOTES)){
						new NotesManager().saveNotes(new Notes(digitalSignatureJson.getString(ApiConstant.DIGITAL_NOTES)));
					}
				} else {
					throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);

				}

				sLog.d(this, "Running status    ---------------   " + isRunning());
				resume();
			} else {
				throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
			}


		} catch (InvalidCredentialException ex) {
			ex.printStackTrace();
			throw new InvalidCredentialException(ex.getMessage());
		} catch (HardwareValidationException hardValEx) {
			hardValEx.printStackTrace();
			throw new HardwareValidationException(hardValEx.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}
	}

	/**
	 * @return returns current stage id of running mode.
	 */
	public int getCurrentStageId() {

		int stageId = 0;

		if (DeviceManager.getInstance().getModeProp() != null && DeviceManager.getInstance().getRecipeJson() != null) {

			String modeName = DeviceManager.getInstance()
					.getRecipeJson()
					.getString(ApiConstant.MODE_NAME);

			if (BasicUtility.getInstance().isRunMode(modeName)) {
				stageId = RunModeManager.get().getStageID();
			} else {
				stageId = NonRunModeManager.get().getStageID();
			}

		}

		return stageId;

	}


}
