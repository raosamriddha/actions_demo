package org.lattice.spectrum_backend_final.dao.manager;

import static org.lattice.spectrum_backend_final.services.handler.ManualModeHandler.abvSettingsGlobal;
import static org.lattice.spectrum_backend_final.services.handler.ManualModeHandler.auxPumpSettingsGlobal;
import static org.lattice.spectrum_backend_final.services.handler.ManualModeHandler.kfKonduitGlobal;
import static org.lattice.spectrum_backend_final.services.handler.ManualModeHandler.mainPumpGlobal;
import static org.lattice.spectrum_backend_final.services.handler.ManualModeHandler.manualFilterDetails;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.ABVSetting;
import org.lattice.spectrum_backend_final.beans.AuxPumpSetting;
import org.lattice.spectrum_backend_final.beans.SystemSetting;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.Converter;
import org.lattice.spectrum_backend_final.dao.util.GraphMapper;
import org.lattice.spectrum_backend_final.dao.util.KonduitUtil;
import org.lattice.spectrum_backend_final.dao.util.Logger;
import org.lattice.spectrum_backend_final.exception.HardwareValidationException;
import org.lattice.spectrum_backend_final.exception.InvalidRecipeException;
import org.lattice.spectrum_backend_final.exception.TrialRunException;
import org.lattice.spectrum_backend_final.services.handler.ManualModeHandler;

import com.corundumstudio.socketio.SocketIOServer;
import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.MsgHandler.RxListener;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.PressureID;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.PressureUnits;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ScaleID;
import com.lattice.spectrum.ComLibrary.comHandler.CommHandler;
import com.lattice.spectrum.ComLibrary.comHandler.PortDisconnectListener;
import com.lattice.spectrum.ComLibrary.comHandler.PortNotFoundException;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.FlowMeterManager;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.ModeCalculator;
import com.lattice.spectrum.ModeLibrary.Managers.ElementryManagers.PressureManager;
import com.lattice.spectrum.ModeLibrary.Managers.nonrun.FluxModeManager;
import com.lattice.spectrum.ModeLibrary.ModeProp.ModeProp;


public class DeviceManager {

	/**
	 * Socket server object which use to manage socket events.
	 */
	private SocketIOServer socketServer = null;

	/**
	 * Reference of {@link ModeProp} used to change any device configuration during trial run or pause.
	 */
	private ModeProp modeProp = null;

	/**
	 * Used to store instance of a recipe for calculation and other validation purposes.
	 */
	private JSONObject recipeJson = null;

	/**
	 * This boolean variable is used to mute alarm.
	 * This will be true whenever a mute alarm event is generated else false.
	 */
	private boolean isMuteCall = false;

	/**
	 * 0 for auto mode and 1 for manual mode
	 */
	private int modeType = 2;

	/**
	 * Used to identify whether the socket connection is already established or not.
	 */
	private boolean isSocketSet = false;

	/**
	 * Variable to store the value of feed start weight during trial configuration.
	 */
	private double feedStartWt = 0;

	//todo check the uses  and remove
	private boolean isDisconnected = true;

	/**
	 * Used as a check to send live data.
	 * <p>If true than only send live data else not.</p>
	 */
	private boolean runMainPumpList = false; // todo rename the variable as its purpose is changed


	private boolean isFirstDModeFinished = false;
	private boolean isSecondDModeFinished = false;
	private int konduitCh1Type;
	private int konduitCh2Type;

	private int vacuumModeStep = 0;


	private double diaFiltrationVol1 = 0;
	private double diaFiltrationVol2 = 0;

	private static String pumpName;
	private static String connectedPortName;

	private LinkedList<Double> queue = new LinkedList<>();

	private Double mainPumpFlowRate = 0d;



	private double permeateFlowRate;

	/**
	 * If true then cleaning mode is in pause state.
	 */
	private boolean isCleaningPause = false;

	/**
	 * status to check weather connection happening or not.
	 */
	private boolean isConnecting = false;

	private String cleaningMessage;

	private int qPermFrequency = 0;
	private boolean isFeedScaleOverride = false;

	private JSONObject minMaxPumpSpeedJson = null;

	private int flowRateCount = 0;


	private boolean isConnectionListRunning = false;

	private static DeviceManager deviceManager = null;

	public static DecimalFormat decimalFormat = new DecimalFormat(ApiConstant.UPTO_TWO_DECIMAL_PLACES, Converter.symbols);
	public static DecimalFormat decimalFormatForUV = new DecimalFormat(ApiConstant.UPTO_THREE_DECIMAL_PLACES, Converter.symbols);
	public static DecimalFormat decimalFormatForPH = new DecimalFormat(ApiConstant.UPTO_ONE_DECIMAL_PLACES, Converter.symbols);

	private static boolean isMainPumpReadingComplete = false;
	private static boolean isMainPumpConnected = false;

	private static PortDisconnectListener portDisconnectListener = new PortDisconnectListener() {
		@Override
		public void disconnected(String portName) {

			//                    if (isConnected[0]) {
			//                        isConnected[0] = false;
			BasicUtility.systemPrint("inside", "disconnect : isPaused : "+TrialManager.getInstance().isPaused());
			DeviceManager.getInstance().sendPortDisconnectCallback();
			DeviceManager.getInstance().minMaxPumpSpeedJson = null;
			boolean isReconnected = false;
			JSONObject liveDataJson = new JSONObject();

			try {

				BasicUtility.systemPrint("modetype "+DeviceManager.getInstance().getModeType()+" isRunning2", TrialManager.getInstance().isRunning()+" : isPaused : "+TrialManager.getInstance().isPaused());
				if (DeviceManager.getInstance().getModeType() == 1 && TrialManager.getInstance().isRunning()) {

					PressureManager.get().stop();

					// saving trial log
					if (TrialManager.getInstance().getTrialRunSettingId() != 0) {

						LogManager.getInstance().insertTrialLog(
								0,
								TrialManager.getInstance().getTrialRunSettingId(),
								ApiConstant.TRIAL_ABRUPT_PAUSE_ACTION,
								ApiConstant.LOG_TYPE_TRIAL_AUTO_LOG
								);

					} else {
						throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
					}
				}

				if (TrialManager.getInstance().isPaused() || DeviceManager.getInstance().getModeType() == 1) {
					BasicUtility.systemPrint("inside", "reconnect");
					isReconnected = DisconnectManager.getInstance().reconnect(pumpName, connectedPortName, portDisconnectListener);
					BasicUtility.systemPrint("isRunning2", TrialManager.getInstance().isRunning());
					if(isReconnected && DeviceManager.getInstance().getModeType() == 1 && TrialManager.getInstance().isRunning()){

						ManualModeHandler.getInstance().onReconnect();

						// saving trial log
						if (TrialManager.getInstance().getTrialRunSettingId() != 0) {

							LogManager.getInstance().insertTrialLog(
									DbConnectionManager.getInstance().getTokenManager().getUserId(),
									TrialManager.getInstance().getTrialRunSettingId(),
									ApiConstant.TRIAL_AUTO_RESUME_ACTION,
									ApiConstant.LOG_TYPE_TRIAL
									);
						} else {
							throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
						}
					}

				} else if (DeviceManager.getInstance().getModeType() == 0) {
					BasicUtility.systemPrint("inside", "reconnectWithResume");
					isReconnected = DisconnectManager.getInstance().reconnectWithResume(pumpName, connectedPortName, portDisconnectListener);
				}

				liveDataJson.put(ApiConstant.TYPE, ApiConstant.EVENT);
				liveDataJson.put(ApiConstant.IS_FINISHED, TrialManager.getInstance().isFinished());

				if (DeviceManager.getInstance().getModeType() != 2) {
					if (isReconnected) {
						liveDataJson.put(ApiConstant.MESSAGE, ApiConstant.RECONNECTED);
					} else {
						liveDataJson.put(ApiConstant.MESSAGE, ApiConstant.RECONNECTION_FAILED);
						DeviceManager.getInstance().setConnecting(false);
						if(DeviceManager.getInstance().getModeType() == 0)
							TrialManager.getInstance().setPaused(true);
					}
					DeviceManager.getInstance().broadcastLiveData(liveDataJson);
				}

				sLog.d(this, liveDataJson);
			} catch (Exception ex) {
				ex.printStackTrace();
			}


		}
	};

	private double percentage;


	private boolean[] isConnected = {false};

	public static PortDisconnectListener getPortDisconnectListener() {
		return portDisconnectListener;
	}

	public static void setPortDisconnectListener(PortDisconnectListener portDisconnectListener) {
		DeviceManager.portDisconnectListener = portDisconnectListener;
	}

	public boolean[] getIsConnected() {
		return isConnected;
	}

	public void setIsConnected(boolean[] isConnected) {
		this.isConnected = isConnected;
	}

	public Double getMainPumpFlowRate() {
		return mainPumpFlowRate;
	}

	public boolean isConnecting() {
		return isConnecting;
	}

	public void setConnecting(boolean connecting) {
		isConnecting = connecting;
	}

	public static RxListener connectionListener = new RxListener(ComLib.get().getMainPump()) {
		@Override
		public void OnReceive() {
			DeviceManager.getInstance().deviceLiveConnectionList();
			if (DeviceManager.getInstance().isRunMainPumpList()) {
				isMainPumpReadingComplete = true;
				isMainPumpConnected = true;
				if (getInstance().isDataReadingCompleted()) {
					getInstance().sendLiveData(ComLib.get(), true);
					getInstance().resetReadingFlag();
				}
			}
		}
	};


	public static RxListener pumpKeyListener = new RxListener(ComLib.get().getPumpKeyHandler()) {
		@Override
		public void OnReceive() {
			BasicUtility.systemPrint("inside", "mutelistener");

			if (ComLib.get().getPumpKeyHandler().isAlarmKey()) {
				BasicUtility.systemPrint("inside", "mutecall");
				ComLib.get().getPumpKeyHandler().start(false, true, true, true);
				if (!DeviceManager.getInstance().isMuteCall()) {
					DeviceManager.getInstance().setMuteCall(true);
				} else {
					DeviceManager.getInstance().setMuteCall(false);
				}
			}
		}
	};

	public static RxListener abvErrorTextListener = new RxListener(ComLib.get().getErrorText()) {
		@Override
		public void OnReceive() {
			JSONObject abvErrorJson = new JSONObject();
			abvErrorJson.put(ApiConstant.TYPE, ApiConstant.TYPE_ABV_ERROR);
			abvErrorJson.put(ApiConstant.MESSAGE, ApiConstant.ABV_ERROR_MESSAGE);
			sLog.d(this, abvErrorJson);
			DeviceManager.getInstance().broadcastDeviceLiveConnectionList(abvErrorJson);
		}
	};


	public static DeviceManager getInstance() {

		synchronized (DeviceManager.class) {
			if (deviceManager == null) {
				deviceManager = new DeviceManager();
			}
		}

		return deviceManager;
	}

	private DeviceManager() {
	}

	public void reset() {
		modeProp = null;
		recipeJson = null;
		minMaxPumpSpeedJson = null;
		percentage = 0;
		permeateFlowRate = 0;
		qPermFrequency = 0;
		modeType = 2;
		isCleaningPause = false;
		cleaningMessage = null;
		konduitCh1Type = 0;
		konduitCh2Type = 0;
		vacuumModeStep = 0;
		isConnecting = false;
		TrialManager.getInstance().setPaused(false);
		TrialManager.getInstance().setTableHeader(new JSONObject());
		TrialManager.getInstance().setSubModeStep(0);
		TrialManager.getInstance().setCaptureSystemLog(true);
		isFeedScaleOverride = false;
		mainPumpFlowRate = 0d;

		//        EndPointSettingManager.getInstance().setEndPointSettings(null);

		diaFiltrationVol1 = 0;
		diaFiltrationVol2 = 0;

		ComLib.get().getPumpKeyHandler().start(true, true, true, true);
		isMuteCall = false;

		isFirstDModeFinished = false;
		isSecondDModeFinished = false;

		flowRateCount = 0;

		TrialManager.getInstance().setFinished(false);
		TrialManager.getInstance().setCurrentSubMode(null);
		TrialManager.getInstance().setSuperMode(null);
		TrialManager.getInstance().setTrialCompleted(false);

		BufferManager.getInstance().setIndex(0);
		TrialDurationManager.getInstance().resetDuration();
		queue = new LinkedList<>();
		TrialManager.getInstance().setBufferLeftDataExecuteCounter(0);
		TrialManager.getInstance().setHardFault(false);

	}

	public int getKonduitCh1Type() {
		return konduitCh1Type;
	}

	public void setKonduitCh1Type(int konduitCh1Type) {
		this.konduitCh1Type = konduitCh1Type;
	}

	public int getKonduitCh2Type() {
		return konduitCh2Type;
	}

	public void setKonduitCh2Type(int konduitCh2Type) {
		this.konduitCh2Type = konduitCh2Type;
	}

	public int getFlowRateCount() {
		return flowRateCount;
	}

	public void setFlowRateCount(int flowRateCount) {
		this.flowRateCount = flowRateCount;
	}

	public boolean isFirstDModeFinished() {
		return isFirstDModeFinished;
	}

	public void setFirstDModeFinished(boolean firstDModeFinished) {
		isFirstDModeFinished = firstDModeFinished;
	}

	public boolean isSecondDModeFinished() {
		return isSecondDModeFinished;
	}

	public void setSecondDModeFinished(boolean secondDModeFinished) {
		isSecondDModeFinished = secondDModeFinished;
	}

	public boolean isCleaningPause() {
		return isCleaningPause;
	}

	public void setCleaningPause(boolean cleaningPause) {
		isCleaningPause = cleaningPause;
	}

	public String getCleaningMessage() {
		return cleaningMessage;
	}

	public void setCleaningMessage(String cleaningMessage) {
		this.cleaningMessage = cleaningMessage;
	}

	public void setqPermFrequency(int qPermFrequency) {
		this.qPermFrequency = qPermFrequency;
	}

	public int getqPermFrequency() {
		return qPermFrequency;
	}

	public boolean isFeedScaleOverride() {
		return isFeedScaleOverride;
	}

	public void setFeedScaleOverride(boolean feedScaleOverride) {
		isFeedScaleOverride = feedScaleOverride;
	}

	public boolean isMuteCall() {
		return isMuteCall;
	}

	public void setMuteCall(boolean muteCall) {
		isMuteCall = muteCall;
	}

	public ModeProp getModeProp() {
		return modeProp;
	}

	public void setModeProp(ModeProp modeProp) {
		this.modeProp = modeProp;
	}

	public JSONObject getRecipeJson() {
		return recipeJson;
	}

	public void setRecipeJson(JSONObject recipeJson) {
		this.recipeJson = recipeJson;
	}

	public double getFeedStartWt() {
		return feedStartWt;
	}

	public void setFeedStartWt(double feedStartWt) {
		this.feedStartWt = feedStartWt;
	}

	public boolean isSocketSet() {
		return isSocketSet;
	}

	public void setSocketSet(boolean socketSet) {
		isSocketSet = socketSet;
	}

	public int getModeType() {
		return modeType;
	}

	public void setModeType(int modeType) {
		this.modeType = modeType;
	}

	public boolean isDisconnected() {
		return isDisconnected;
	}

	public void setDisconnected(boolean disconnected) {
		isDisconnected = disconnected;
	}

	public double getPermeateFlowRate() {
		return permeateFlowRate;
	}

	public void setPermeateFlowRate(double permeateFlowRate) {
		this.permeateFlowRate = permeateFlowRate;
	}

	public int getVacuumModeStep() {
		return vacuumModeStep;
	}

	public void setVacuumModeStep(int vacuumModeStep) {
		this.vacuumModeStep = vacuumModeStep;
	}

	public boolean isConnectionListRunning() {
		return isConnectionListRunning;
	}

	public void setConnectionListRunning(boolean connectionListRunning) {
		isConnectionListRunning = connectionListRunning;
	}

	public SocketIOServer getSocketServer() {
		return socketServer;
	}

	public void setSocketServer(SocketIOServer socketServer) {
		this.socketServer = socketServer;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public void setIsMainPumpConnected(boolean isMainPumpConnected) {
		DeviceManager.isMainPumpConnected = isMainPumpConnected;
	}

	private boolean isDataReadingCompleted() {
		return /*isPressureReadingComplete &&
                isValveReadingComplete &&*/
				isMainPumpReadingComplete
				/*&&
                isScaleReadingComplete &&
                isKonduitReadingComplete &&
                isAuxPumpReadingComplete*/;
	}

	public boolean isRunMainPumpList() {
		return runMainPumpList;
	}

	public void setRunMainPumpList(boolean runMainPumpList) {
		this.runMainPumpList = runMainPumpList;
	}

	public synchronized void startRxListener() {

		BasicUtility.systemPrint("listener", "started");

		//        pumpListener.startRxListening();
		//        auxPumpListener.startRxListening();
		//        scaleListener.startRxListening();
		//        konduitListener.startRxListening();
		//        valveListener.startRxListening();
		setRunMainPumpList(true);


	}

	public void stopRxListener() {

		BasicUtility.systemPrint("listener", "stopped");
		//        pumpListener.stopRxListening();
		//        auxPumpListener.stopRxListening();
		//        scaleListener.stopRxListening();
		//        konduitListener.stopRxListening();
		//        valveListener.stopRxListening();
		setRunMainPumpList(false);
	}

	private void resetReadingFlag() {
		isMainPumpReadingComplete = false;
	}

	public HashMap<String, Object> calculatedFields(double retentateFlowRate) throws SQLException {

		double filterFlux = 0;
		double surfaceArea = 0;
		KonduitUtil konduitUtil = new KonduitUtil();
		double totalSurfaceArea = 0;
		String shearString = ApiConstant.NOT_APPLICABLE;
		double shear = 0;
		double vt = 0;
		double permStartWt = 0;
		double totalPermTubeHoldup = 0;
		double permeateFlowRate;
		double permWt = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE);
		double permWtforVt;
		if(konduitUtil.isTotalizerMode()){
			permeateFlowRate = konduitUtil.getTotalizerFlowrate();
			permWtforVt = FlowMeterManager.get().getVolume(0);
		}else{
			permeateFlowRate = this.permeateFlowRate;
			permWtforVt = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE);
		}
		if (ApiConstant.YES.equalsIgnoreCase(recipeJson.getString(ApiConstant.PERMEATE_HOLD_UP_VOLUME_CALCULATION))) {
			totalPermTubeHoldup = recipeJson.getDouble(ApiConstant.TOTAL_PERM_TUBE_HOLDUP);
		}
		//        if (BasicUtility.getInstance().isRunMode(recipeJson.getString(ApiConstant.MODE_NAME))) {
		//            permStartWt = modeProp.getPremStartWt();
		//        }

		double totalPermWtForVT = permWtforVt + recipeJson.getDouble(ApiConstant.TOTAL_PERM_TUBE_HOLDUP);
		double totalPermWt = permWt + totalPermTubeHoldup;
		int loopCount;
		HashMap<String, Object> calculatedFieldsHM = null;
		double shearTypeNumber = 0;


		/**
		 * used to calculate ABV state.
		 */
		int totalAbv = recipeJson.getJSONArray(ApiConstant.ABV).length();
		double[] abvState = new double[totalAbv];
		double curPos = 0;
		double openPos = 0;
		double closePos = 0;

		HashMap<String, Double> tubePos = null;


		try {
			loopCount = 0;
			if (recipeJson != null) {


				while (loopCount < totalAbv) {

					JSONObject abvJson = recipeJson.getJSONArray(ApiConstant.ABV).getJSONObject(loopCount);


					tubePos = BasicUtility.getInstance()
							.getTubePosition(
									recipeJson,
									abvJson
									);


					openPos = tubePos.get(ApiConstant.OPEN_POSITION);

					closePos = tubePos.get(ApiConstant.CLOSED_POSITION);

					curPos = ComLib.get().getValveInfo().getValveCurPos(loopCount);
					abvState[loopCount] = (curPos - openPos) * 100 / (closePos - openPos);
					BasicUtility.systemPrint("openPos2-----" + openPos + "-----closePos2", closePos);

					loopCount++;
				}

			} else {
				throw new InvalidRecipeException(ApiConstant.VALVE_POSITION_NOT_SET_ERROR_MESSAGE);
			}


			surfaceArea = recipeJson.getDouble(ApiConstant.SURF_AREA);
			calculatedFieldsHM = new HashMap<>();

			if (
					ApiConstant.FILTER_TYPE_IS_CASSETTE.equalsIgnoreCase(recipeJson.getString(ApiConstant.FILTER_TYPE))
					) {

				totalSurfaceArea = surfaceArea * recipeJson.getInt(ApiConstant.NO_OF_CASSETTE);
				filterFlux = (permeateFlowRate * 0.06) / totalSurfaceArea;
				vt = (totalPermWtForVT - permStartWt) / (totalSurfaceArea * 1000);
				if(
						!recipeJson.getString(ApiConstant.NO_OF_CHANNEL).equals(ApiConstant.BLANK_QUOTE) &&
						!recipeJson.getString(ApiConstant.WIDTH).equals(ApiConstant.BLANK_QUOTE) &&
						!recipeJson.getString(ApiConstant.HEIGHT).equals(ApiConstant.BLANK_QUOTE)
						){
					shearTypeNumber = (6) / (60 * recipeJson.getDouble(ApiConstant.WIDTH) * (Math.pow(recipeJson.getDouble(ApiConstant.HEIGHT), 2)));

					shear = shearTypeNumber * retentateFlowRate / recipeJson.getInt(ApiConstant.NO_OF_CASSETTE) / recipeJson.getInt(ApiConstant.NO_OF_CHANNEL);
					shearString = String.valueOf(shear);
				}

			} else if (ApiConstant.FILTER_TYPE_IS_HOLLOW_FIBER.equalsIgnoreCase(recipeJson.getString(ApiConstant.FILTER_TYPE))) {

				filterFlux = (permeateFlowRate * 0.06 * 10000) / surfaceArea;
				shear = (533.33 * retentateFlowRate) / recipeJson.getDouble(ApiConstant.FIBER_COUNT) / 3.14285 / Math.pow(recipeJson.getDouble(ApiConstant.FIBER_ID), 3);
				shearString = String.valueOf(shear);
				surfaceArea /= 10000;
				vt = (totalPermWtForVT - permStartWt) / (surfaceArea * 1000);

			}


			calculatedFieldsHM.put(ApiConstant.FLUX, filterFlux);
			calculatedFieldsHM.put(ApiConstant.SHEAR, shearString);
			calculatedFieldsHM.put(ApiConstant.TOTAL_PERM_WEIGHT, totalPermWt);
			calculatedFieldsHM.put(ApiConstant.VT, vt);

			if (totalAbv != 0) {

				calculatedFieldsHM.put(ApiConstant.ABV_1, abvState[0]);

				if (totalAbv == 2) {

					calculatedFieldsHM.put(ApiConstant.ABV_2, abvState[1]);

				}
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
		return calculatedFieldsHM;

	}

	public JSONObject calculatedNonRunFields(JSONObject liveDataJson) {

		String tcfString = ApiConstant.BLANK_QUOTE;
		String waterFluxString = ApiConstant.BLANK_QUOTE;
		String waterFlux20DegreeString = ApiConstant.BLANK_QUOTE;
		String waterPermString = ApiConstant.BLANK_QUOTE;
		String nwpString = ApiConstant.BLANK_QUOTE;
		try {

				double tcf = 0;
				double waterFlux = 0;
				double waterFlux20Degree = 0;
				double waterPerm = 0;
				double nwp = 0;
				double feedPressure = liveDataJson.getDouble(ApiConstant.FEED_PRESSURE);
				double temp = 0;
				double permeateFlowRate = liveDataJson.getDouble(ApiConstant.PERMEATE_FLOW_RATE);
				double surfaceArea = 0;
				String pumpName = null;
				
				if (modeType == 0) {
					surfaceArea = recipeJson.getDouble(ApiConstant.SURF_AREA);
					if (ApiConstant.FILTER_TYPE_IS_CASSETTE.equalsIgnoreCase(recipeJson.getString(ApiConstant.FILTER_TYPE))) {
						surfaceArea *= 10000;
					}
					pumpName = recipeJson.getString(ApiConstant.PUMP_NAME);
				} else if (modeType == 1) {
					surfaceArea = getManualFilterSurfaceArea();
					if (isFilterCassette()) {
						surfaceArea *= 10000;
						sLog.d(this, "surfaceArea isFilterCassette");
						sLog.d(this, surfaceArea);
					}
					if (mainPumpGlobal != null) {
						pumpName = mainPumpGlobal.getPumpName();
						sLog.d(this, "pumpName");
						sLog.d(this, pumpName);
					}
				}
				double tmp = liveDataJson.getDouble(ApiConstant.TMP_PRESSURE);
				String tempString = ApiConstant.BLANK_QUOTE;
				String tempLiveString = ApiConstant.BLANK_QUOTE;

				tempString = BasicUtility.getInstance().getTemperature();
				if (!ApiConstant.BLANK_QUOTE.equals(tempString) && tempString != null) {
					temp = Double.parseDouble(tempString);
				} else {
					if(liveDataJson.getString(ApiConstant.TEMPERATURE_1).equals(ApiConstant.NOT_APPLICABLE)){
						tempLiveString = ApiConstant.NOT_APPLICABLE;
					}else{
						temp = liveDataJson.getDouble(ApiConstant.TEMPERATURE_1);
					}
				}
				if (feedPressure != 0 && !tempLiveString.equals(ApiConstant.NOT_APPLICABLE)) {
					tcf = 0.0005057 * Math.pow(temp, 2) - 0.04525991 * temp + 1.70483703;
					tcfString = decimalFormat.format(tcf);
					tcfString = tcfString.replaceAll(ApiConstant.REGEX_TO_REMOVE_NEGATIVE_ZERO, ApiConstant.BLANK_QUOTE);
				} else {
					tcfString = ApiConstant.NOT_APPLICABLE;
				}
				if (feedPressure == 0 || permeateFlowRate == 0) {
					waterFluxString = ApiConstant.NOT_APPLICABLE;
				} else {
					waterFlux = liveDataJson.getDouble(ApiConstant.FLUX);
					waterFluxString = decimalFormat.format(waterFlux);
				}
				switch (pumpName) {
				case ApiConstant.KR2I:
				case ApiConstant.KROSFLOFS15:
					if (feedPressure != 0 && waterFlux != 0 && tcf != 0) {
						waterFlux20Degree = waterFlux * tcf;
						waterFlux20DegreeString = decimalFormat.format(waterFlux20Degree);
					} else {
						waterFlux20DegreeString = ApiConstant.NOT_APPLICABLE;
					}
					if (waterFlux == 0 || tmp == 0) {
						waterPermString = ApiConstant.NOT_APPLICABLE;
					} else {
						waterPerm = waterFlux / tmp;
						waterPermString = decimalFormat.format(waterPerm);
					}
					if (tmp == 0 || feedPressure == 0 || waterFlux20Degree == 0) {
						nwpString = ApiConstant.NOT_APPLICABLE;
					} else {
						nwp = waterFlux20Degree / tmp;
						nwpString = decimalFormat.format(nwp);
					}
					break;
				case ApiConstant.KMPI:
				case ApiConstant.KROSFLOFS500:
					if (feedPressure != 0 && tmp != 0) {
						waterFlux20Degree = waterFlux * tcf;
						waterPerm = waterFlux / tmp;
						nwp = waterFlux20Degree / tmp;
						waterFlux20DegreeString = decimalFormat.format(waterFlux20Degree);
						waterPermString = decimalFormat.format(waterPerm);
						nwpString = decimalFormat.format(nwp);
					} else {
						waterFlux20DegreeString = ApiConstant.NOT_APPLICABLE;
						waterPermString = ApiConstant.NOT_APPLICABLE;
						nwpString = ApiConstant.NOT_APPLICABLE;
					}
					break;
				default:
					throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
				}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (modeType == 1 && (manualFilterDetails == null || manualFilterDetails.isEmpty())) {
			
			liveDataJson.put(ApiConstant.TEMPERATURE_CORRECTION_FACTOR, tcfString);
			liveDataJson.put(ApiConstant.WATER_FLUX, ApiConstant.NOT_APPLICABLE);
			liveDataJson.put(ApiConstant.WATER_FLUX_20_DEGREE, ApiConstant.NOT_APPLICABLE);
			liveDataJson.put(ApiConstant.WATER_PERMEABILITY, ApiConstant.NOT_APPLICABLE);
			liveDataJson.put(ApiConstant.NWP, ApiConstant.NOT_APPLICABLE);
		} else {
			
			liveDataJson.put(ApiConstant.TEMPERATURE_CORRECTION_FACTOR, tcfString);
			liveDataJson.put(ApiConstant.WATER_FLUX, waterFluxString);
			liveDataJson.put(ApiConstant.WATER_FLUX_20_DEGREE, waterFlux20DegreeString);
			liveDataJson.put(ApiConstant.WATER_PERMEABILITY, waterPermString);
			liveDataJson.put(ApiConstant.NWP, nwpString);
		}
		
		return liveDataJson;
	}

	private final boolean isFilterCassette() {
		if (manualFilterDetails != null && !manualFilterDetails.isEmpty()) {
			if (ApiConstant.CASSETTE.equals(manualFilterDetails.getString(ApiConstant.FILTER_TYPE))) {
				return true;
			}
		}
		return false;
	}

	private final double getManualFilterSurfaceArea() {
		if (manualFilterDetails != null && !manualFilterDetails.isEmpty()) {
			return manualFilterDetails.getDouble(ApiConstant.SURF_AREA);
		}
		return 0;
	}

	public void broadcastLiveData(JSONObject liveDataJson) {
		if (socketServer != null) {

			socketServer.getBroadcastOperations().sendEvent(ApiConstant.LIVE_DATA_ADD_EVENT, liveDataJson.toString());
		} else {
			BasicUtility.systemPrint("socketserver_add", "isnull");
		}
	}

	public void broadcastLiveData(JSONArray liveDataJsonArray) {
		if (socketServer != null) {
			socketServer.getBroadcastOperations().sendEvent(ApiConstant.LIVE, liveDataJsonArray.toString());
		} else {
			BasicUtility.systemPrint("socketserver_add", "isnull");
		}
	}

	public void broadcastDeviceLiveConnectionList(JSONObject deviceLiveConnectionListJson) {
		if (socketServer != null) {

			socketServer.getBroadcastOperations().sendEvent(ApiConstant.CONNECTION_LIST_EVENT, deviceLiveConnectionListJson.toString());
		}
	}

	public void broadcastExpectedValue(JSONObject liveDataJson) {
		if (socketServer != null) {
			socketServer.getBroadcastOperations().sendEvent(ApiConstant.TUBING_EVENT, liveDataJson.toString());
		} else {
			BasicUtility.systemPrint("socketserver_add", "isnull");
		}
	}

	public void broadcastPressureEvent(JSONObject liveDataJson) {
		if (socketServer != null) {
			socketServer.getBroadcastOperations().sendEvent(ApiConstant.PRESSURE_EVENT, liveDataJson.toString());
		} else {
			BasicUtility.systemPrint("socketserver_add", "isnull");
		}
	}

	private double calculatePermeateFlowRate(double permeateFlowRate, double permeateScale) {
		int queueSize;
		queue.add(permeateScale);
		queueSize = queue.size();

		if(qPermFrequency == 1 || (queueSize - 1) < qPermFrequency){
			this.permeateFlowRate = permeateFlowRate;
		}
		else if (qPermFrequency == (queueSize - 1)) {
			double first = queue.removeFirst();
			double last = queue.getLast();
			this.permeateFlowRate = (last - first) * (60d / qPermFrequency);
			sLog.d("first : "+first+" : last : "+last);
			this.permeateFlowRate = (this.permeateFlowRate > 0)?this.permeateFlowRate:0;
		}
		sLog.d("queueSize : "+queueSize+" : qPermFrequency : "+qPermFrequency+" : permeateFlowRate : "+this.permeateFlowRate);
		return qPermFrequency;
	}

	public void sendLiveData(ComLib spectrumCommLib, boolean muteAlarm) {

		String timeStamp = BasicUtility.getInstance().convertUnixToDateGraph(System.currentTimeMillis() / 1000);
		JSONObject convertedLiveDataJson = null;


		double concentrationFactor = 0.00;
		String conductivity1String = ApiConstant.NOT_APPLICABLE;
		String conductivity2String = ApiConstant.NOT_APPLICABLE;
		String temperature1String = ApiConstant.NOT_APPLICABLE;
		String temperature2String = ApiConstant.NOT_APPLICABLE;
		String konduitValue1String = ApiConstant.NOT_APPLICABLE;
		String konduitValue2String = ApiConstant.NOT_APPLICABLE;
		KonduitUtil konduitUtil = new KonduitUtil();

		try {

			if (modeProp != null && modeType == 0) {

				if (isFirstDModeFinished && !isSecondDModeFinished) {

					diaFiltrationVol2 = modeProp.getDV();

				} else if (!isFirstDModeFinished) {

					diaFiltrationVol1 = modeProp.getDV();
				}


				concentrationFactor = modeProp.getCF();
			}


			if (modeType == 0 && recipeJson != null) {

				try {
					synchronized (this) {

						ValidationManager.isConnectionValid(recipeJson, true);
					}
				} catch (HardwareValidationException e) {
					e.printStackTrace();
				}

			}


			JSONObject liveDataJson = new JSONObject();

			int loopCount = 0;


			String type = ApiConstant.LIVE;
			double permeatePressure = spectrumCommLib.getPressureInfo().getChannelReadings_Psi(PressureID.PERMEATE_CHANNEL);
			double feedPressure = spectrumCommLib.getPressureInfo().getChannelReadings_Psi(PressureID.FEED_CHANNEL);
			double retentatePressure = spectrumCommLib.getPressureInfo().getChannelReadings_Psi(PressureID.RETENTATE_CHANNEL);
			double tmpPressure = spectrumCommLib.getPressureInfo().getChannelReadings_Psi(PressureID.TMP_CHANNEL);
			double permeateFlowRateLive = 0;

			String mainPumpTubeSize = null;

			double speedFactor = 0;

			double mainPumpFlowRate = 0;

			if (
					modeType == 0 &&
					recipeJson != null
					) {
				mainPumpTubeSize = recipeJson.getString(ApiConstant.PUMP_TUBING_SIZE);
				speedFactor = BasicUtility.speedFactor(recipeJson.getString(ApiConstant.PUMP_NAME), recipeJson.getString(ApiConstant.DIRECTION), recipeJson.getInt(ApiConstant.HEAD_COUNT));

				mainPumpFlowRate = BasicUtility.speedToFlowRate(
						BasicUtility.getInstance().getConnectedPumpName(),
						spectrumCommLib.getMainPump().getMotorSpeed(),
						mainPumpTubeSize,
						speedFactor
						);
			} else if (modeType == 1) {
				// manual code
				if (mainPumpGlobal != null) {

					sLog.d(this, mainPumpGlobal);
					speedFactor = BasicUtility.speedFactor(mainPumpGlobal.getPumpName(), mainPumpGlobal.getDirection(), 1);
					mainPumpTubeSize = mainPumpGlobal.getTubingSize();
					mainPumpFlowRate = BasicUtility.speedToFlowRate(
							BasicUtility.getInstance().getConnectedPumpName(),
							spectrumCommLib.getMainPump().getMotorSpeed(),
							mainPumpTubeSize,
							speedFactor
							);

				}


			}


			int auxPump1Speed = spectrumCommLib.getAuxPump().curAuxRPM(0);
			int auxPump2Speed = spectrumCommLib.getAuxPump().curAuxRPM(1);

			Double permeateTotal = FlowMeterManager.get().getVolume(0);
			Double totalPermTubeHoldup = 0.0;
			if (recipeJson != null && ApiConstant.YES.equalsIgnoreCase(recipeJson.getString(ApiConstant.PERMEATE_HOLD_UP_VOLUME_CALCULATION))) {
				totalPermTubeHoldup = recipeJson.getDouble(ApiConstant.TOTAL_PERM_TUBE_HOLDUP);
			}
			Double permeateTotalWithHoldUp = permeateTotal + totalPermTubeHoldup;

			double auxPump1FlowRate = 0;
			double auxPump2FlowRate = 0;

			if (
					modeType == 0 &&
					recipeJson != null
					) {

				while (loopCount < recipeJson.getJSONArray(ApiConstant.AUX_PUMP).length()) {

					JSONObject auxJson = recipeJson.getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(loopCount);

					if (auxJson.getInt(ApiConstant.TYPE) == 0) {
						auxPump1FlowRate = BasicUtility.speedToFlowRate(
								auxJson.getString(ApiConstant.AUX_PUMP_TYPE),
								auxPump1Speed,
								auxJson.getString(ApiConstant.AUX_TUBING_SIZE),
								1
								);
					} else {
						auxPump2FlowRate = BasicUtility.speedToFlowRate(
								auxJson.getString(ApiConstant.AUX_PUMP_TYPE),
								auxPump2Speed,
								auxJson.getString(ApiConstant.AUX_TUBING_SIZE),
								1);

					}

					loopCount++;

				}

			} else if (modeType == 1) {
				// manual code

				if (!auxPumpSettingsGlobal.isEmpty()) {
					for (AuxPumpSetting auxPumpSetting : auxPumpSettingsGlobal) {
						//						log.info("auxPumpSetting: "+ auxPumpSetting);

						if (auxPumpSetting.getType() == 0) {
							auxPump1FlowRate = BasicUtility.speedToFlowRate(
									auxPumpSetting.getAuxPumpType(),
									auxPump1Speed,
									auxPumpSetting.getTubingSize(),
									1);
						} else {
							auxPump2FlowRate = BasicUtility.speedToFlowRate(
									auxPumpSetting.getAuxPumpType(),
									auxPump2Speed,
									auxPumpSetting.getTubingSize(),
									1);
						}
					}

				}

			}


			double kFactor1 = 1;
			double kFactor2 = 1;

			if (
					modeType == 0 &&
					recipeJson != null
					) {

				if (
						recipeJson.has(ApiConstant.K_FACTOR_CH_1) &&
						!ApiConstant.BLANK_QUOTE.equals(recipeJson.getString(ApiConstant.K_FACTOR_CH_1))
						) {
					kFactor1 = recipeJson.getDouble(ApiConstant.K_FACTOR_CH_1);
				}

				if (
						recipeJson.has(ApiConstant.K_FACTOR_CH_2) &&
						!ApiConstant.BLANK_QUOTE.equals(recipeJson.getString(ApiConstant.K_FACTOR_CH_2))
						) {
					kFactor2 = recipeJson.getDouble(ApiConstant.K_FACTOR_CH_2);
				}

			} else if (modeType == 1) {
				// manual code set k-factor
				if (kfKonduitGlobal.getCh1KFactor() != null) {

					kFactor1 = Double.valueOf(kfKonduitGlobal.getCh1KFactor());
				}
				if (kfKonduitGlobal.getCh2KFactor() != null) {

					kFactor2 = Double.valueOf(kfKonduitGlobal.getCh2KFactor());

				}

			}

			konduitValue1String = konduitUtil.getKonduitReading(1);
			konduitValue2String = konduitUtil.getKonduitReading(2);

			if(ComLib.get().getKonduitInfo().isKFConduitConnected()){

				double conductivity1 = kFactor1 * spectrumCommLib.getKonduitInfo().getProbeConductivity_mS(0);
				conductivity1String = decimalFormat.format(conductivity1);
				double conductivity2 = kFactor2 * spectrumCommLib.getKonduitInfo().getProbeConductivity_mS(1);
				conductivity2String = decimalFormat.format(conductivity2);

				double temperature1 = spectrumCommLib.getKonduitInfo().getProbeTemperature_Celcius(0);
				temperature1String = decimalFormat.format(temperature1);
				double temperature2 = spectrumCommLib.getKonduitInfo().getProbeTemperature_Celcius(1);
				temperature2String = decimalFormat.format(temperature2);

			}else{
				konduitValue1String = ApiConstant.NOT_APPLICABLE;
				konduitValue2String = ApiConstant.NOT_APPLICABLE;
			}


			double feedScale = 0;

			if (DeviceManager.getInstance().getModeType() == 0) {

				feedScale = ModeCalculator.get().getFeedTrueWt();
				if (
						recipeJson != null &&
						!BasicUtility.getInstance().isRunMode(recipeJson.getString(ApiConstant.MODE_NAME))

						) {
					if(
							ApiConstant.FLUX_C.equals(recipeJson.getString(ApiConstant.MODE_NAME)) ||
							ApiConstant.FLUX_CV.equals(recipeJson.getString(ApiConstant.MODE_NAME))
							){
						feedScale = FluxModeManager.get().getFeedTrueWt();
					}else{
						feedScale = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE);
					}
				}

				if (BasicUtility.getInstance().isRunMode(recipeJson.getString(ApiConstant.MODE_NAME))) {

					percentage = modeProp.getSubModePercentComplete();
				}

			} else if (DeviceManager.getInstance().getModeType() == 1) {
				feedScale = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE);
			}

			double mPermeate = spectrumCommLib.getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE);


			int mainPumpSpeed = BasicUtility.getRpmFromDegreePerSecond(BasicUtility.getInstance().getConnectedPumpName(), spectrumCommLib.getMainPump().getMotorSpeed());


			mainPumpSpeed = Math.abs(mainPumpSpeed);

			double deltaP = feedPressure - retentatePressure;


			double feedFlowRate = Math.abs(mainPumpFlowRate);
			if(konduitUtil.isTotalizerMode()){
				permeateFlowRate = FlowMeterManager.get().getCurrentFlowRate(0);
			}else{
				permeateFlowRateLive = spectrumCommLib.getScaleInfo().scaleFlowRate_mlpm(ScaleID.PERMEATE_SCALE);
				BasicUtility.systemPrint("permeateFlowRateLive1",permeateFlowRateLive);
				calculatePermeateFlowRate(permeateFlowRateLive, mPermeate);
				BasicUtility.systemPrint("permeateFlowRate1",permeateFlowRate);
			}


			//            if (spectrumCommLib.getScaleInfo().scaleFlowRate_mlpm(ScaleID.PERMEATE_SCALE) != 0) {

			//            }

			double retentateFlowRate = feedFlowRate - permeateFlowRate;


			double flux = 0;
			String fluxString = String.valueOf(flux);
			String shear = ApiConstant.NOT_APPLICABLE;
			double totalPermWt = 0;
			double vt = 0;
			String abv1 = ApiConstant.NOT_APPLICABLE;
			String abv2 = ApiConstant.NOT_APPLICABLE;

			if (modeType == 0) {

				HashMap<String, Object> calculatedFieldsHM = calculatedFields(retentateFlowRate);

				flux = (double) calculatedFieldsHM.get(ApiConstant.FLUX);
				fluxString = decimalFormat.format(flux);
				fluxString = fluxString.replaceAll(ApiConstant.REGEX_TO_REMOVE_NEGATIVE_ZERO, ApiConstant.BLANK_QUOTE);

				double abvValue;

				if (calculatedFieldsHM.containsKey(ApiConstant.ABV_1)) {
					abv1 = decimalFormat.format((double) calculatedFieldsHM.get(ApiConstant.ABV_1));
					abvValue = Double.parseDouble(abv1);
					if (abvValue < 0) {
						abv1 = "0.00";
					}
				}

				if (calculatedFieldsHM.containsKey(ApiConstant.ABV_2)) {
					abv2 = decimalFormat.format((double) calculatedFieldsHM.get(ApiConstant.ABV_2));
					abvValue = Double.parseDouble(abv2);
					if (abvValue < 0) {
						abv2 = "0.00";
					}
				}
				String shearString = calculatedFieldsHM.get(ApiConstant.SHEAR).toString();
				if (shearString.equalsIgnoreCase(ApiConstant.NOT_APPLICABLE)) {
					shear = ApiConstant.NOT_APPLICABLE;
				} else {
					shear = decimalFormat.format(Double.parseDouble(shearString));
					shear = shear.replaceAll(ApiConstant.REGEX_TO_REMOVE_NEGATIVE_ZERO, ApiConstant.BLANK_QUOTE);
				}
				totalPermWt = (double) calculatedFieldsHM.get(ApiConstant.TOTAL_PERM_WEIGHT);
				vt = (double) calculatedFieldsHM.get(ApiConstant.VT);
			} else if (modeType == 1) {

				// manual abv code
				if (abvSettingsGlobal.length > 0) {
					for (ABVSetting abvSetting : abvSettingsGlobal) {

						if (abvSetting != null && abvSetting.getTubingSize() != null) {
							HashMap<String, Double> tubePos = BasicUtility.getInstance().getTubePosition(abvSetting.getTubingSize(), abvSetting.getAbvType());

							double openPosition = tubePos.get(ApiConstant.OPEN_POSITION);
							double closePosition = tubePos.get(ApiConstant.CLOSED_POSITION);
							double abvValue;
							int currentPosition = ComLib.get().getValveInfo().getValveCurPos(abvSetting.getType());
							if (abvSetting.getType() == 0) {
								abv1 = decimalFormat.format((currentPosition - openPosition) * 100 / (closePosition - openPosition));
								abvValue = Double.parseDouble(abv1);
								if (abvValue < 0) {
									abv1 = "0.00";
								}
							} else {
								abv2 = decimalFormat.format((currentPosition - openPosition) * 100 / (closePosition - openPosition));
								abvValue = Double.parseDouble(abv2);
								if (abvValue < 0) {
									abv2 = "0.00";
								}
							}
						}

					}
				}
				
				double permWtforVt = 0;

				// calculating flux and shear in manual mode
				if (manualFilterDetails != null && !manualFilterDetails.isEmpty()) {
						sLog.d(this, "manualFilterDetails");
						sLog.d(this, manualFilterDetails);
						double surfaceArea = manualFilterDetails.getDouble(ApiConstant.SURF_AREA);
						permWtforVt = ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE);
						if (ApiConstant.CASSETTE.equals(manualFilterDetails.getString(ApiConstant.FILTER_TYPE))) {
							final double totalSurfaceArea = manualFilterDetails.getDouble(ApiConstant.TOTAL_SURF_AREA);
							flux = (this.permeateFlowRate * 0.06) / totalSurfaceArea;
							sLog.d(this, "flux");
							sLog.d(this, flux);
							
							// calculate shear
							if (manualFilterDetails.getInt(ApiConstant.IS_NON_REPLIGEN) == 0) {
								double shearTypeNumber = (6) / (60 * manualFilterDetails.getDouble(ApiConstant.WIDTH) * (Math.pow(manualFilterDetails.getDouble(ApiConstant.HEIGHT), 2)));
								sLog.d(this, "shearTypeNumber");
								sLog.d(this, shearTypeNumber);
								
								double shearManual = shearTypeNumber * retentateFlowRate / manualFilterDetails.getInt(ApiConstant.NO_OF_CASSETTE) / manualFilterDetails.getInt(ApiConstant.NO_OF_CHANNEL);
								sLog.d(this, "shearManual");
								sLog.d(this, shearManual);
								shear = decimalFormat.format(shearManual);
								shear = shear.replaceAll(ApiConstant.REGEX_TO_REMOVE_NEGATIVE_ZERO, ApiConstant.BLANK_QUOTE);
							}
							vt = permWtforVt / (totalSurfaceArea * 1000);
						} else if (ApiConstant.FILTER_TYPE_IS_HOLLOW_FIBER.equals(manualFilterDetails.getString(ApiConstant.FILTER_TYPE))) {
							flux = (this.permeateFlowRate * 0.06 * 10000) / surfaceArea;
							sLog.d(this, "flux");
							sLog.d(this, flux);
							
							// calculate shear
							if (manualFilterDetails.getInt(ApiConstant.IS_NON_REPLIGEN) == 0) {
								double shearManual = (533.33 * retentateFlowRate) / manualFilterDetails.getDouble(ApiConstant.FIBER_COUNT) / 3.14285 / Math.pow(manualFilterDetails.getDouble(ApiConstant.FIBER_ID), 3);
								sLog.d(this, "shearManual");
								sLog.d(this, shearManual);
								shear = decimalFormat.format(shearManual);
								shear = shear.replaceAll(ApiConstant.REGEX_TO_REMOVE_NEGATIVE_ZERO, ApiConstant.BLANK_QUOTE);
								}
							
							surfaceArea /= 10000;
							vt = permWtforVt / (surfaceArea * 1000);
						}
		
				} else {
					
					fluxString = ApiConstant.NOT_APPLICABLE;
		
				}
				fluxString = decimalFormat.format(flux);
				fluxString = fluxString.replaceAll(ApiConstant.REGEX_TO_REMOVE_NEGATIVE_ZERO, ApiConstant.BLANK_QUOTE);

			}

			this.mainPumpFlowRate = Math.abs(mainPumpFlowRate);
			liveDataJson.put(ApiConstant.TYPE, type);
			liveDataJson.put(ApiConstant.PERMEATE_PRESSURE, permeatePressure);
			liveDataJson.put(ApiConstant.FEED_PRESSURE, feedPressure);
			liveDataJson.put(ApiConstant.RETENTATE_PRESSURE, retentatePressure);
			liveDataJson.put(ApiConstant.TMP_PRESSURE, tmpPressure);
			liveDataJson.put(ApiConstant.MAIN_PUMP_FLOW_RATE, Math.abs(mainPumpFlowRate));
			liveDataJson.put(ApiConstant.AUX_PUMP_1_FLOW_RATE, "" + auxPump1FlowRate);
			liveDataJson.put(ApiConstant.AUX_PUMP_2_FLOW_RATE, "" + auxPump2FlowRate);
			liveDataJson.put(ApiConstant.CONDUCTIVITY_1, conductivity1String);
			liveDataJson.put(ApiConstant.CONDUCTIVITY_2, conductivity2String);
			liveDataJson.put(ApiConstant.TEMPERATURE_1, temperature1String);
			liveDataJson.put(ApiConstant.TEMPERATURE_2, temperature2String);
			liveDataJson.put(ApiConstant.KONDUIT_CH_1, konduitValue1String);
			liveDataJson.put(ApiConstant.KONDUIT_CH_2, konduitValue2String);
			liveDataJson.put(ApiConstant.KONDUIT_CH_1_TYPE, konduitCh1Type);
			liveDataJson.put(ApiConstant.KONDUIT_CH_2_TYPE, konduitCh2Type);
			liveDataJson.put(ApiConstant.FEED_SCALE, feedScale);
			liveDataJson.put(ApiConstant.M_PERMEATE, mPermeate);

			liveDataJson.put(ApiConstant.DELTA_P, deltaP);

			liveDataJson.put(ApiConstant.TIMESTAMP, timeStamp);
			liveDataJson.put(ApiConstant.MAIN_PUMP_SPEED, String.valueOf(mainPumpSpeed));
			liveDataJson.put(ApiConstant.AUX_PUMP_1_SPEED, decimalFormat.format(auxPump1Speed));
			liveDataJson.put(ApiConstant.AUX_PUMP_2_SPEED, decimalFormat.format(auxPump2Speed));
			liveDataJson.put(ApiConstant.FEED_FLOW_RATE, feedFlowRate);
			liveDataJson.put(ApiConstant.PERMEATE_FLOW_RATE, permeateFlowRate);
			liveDataJson.put(ApiConstant.RETENTATE_FLOW_RATE, retentateFlowRate);
			liveDataJson.put(ApiConstant.CONCENTRATION_FACT, decimalFormat.format(concentrationFactor));
			liveDataJson.put(ApiConstant.DIAFILTRATION_VOL_1, decimalFormat.format(diaFiltrationVol1));
			liveDataJson.put(ApiConstant.DIAFILTRATION_VOL_2, decimalFormat.format(diaFiltrationVol2));
			liveDataJson.put(ApiConstant.PERMEATE_TOTAL_KEY, permeateTotal);
			liveDataJson.put(ApiConstant.PERMEATE_TOTAL_WITH_HOLDUP, permeateTotalWithHoldUp);

			/*************************************** Calculated fields ******************************************/

			liveDataJson.put(ApiConstant.FLUX, fluxString);
			liveDataJson.put(ApiConstant.ABV_1, abv1);
			liveDataJson.put(ApiConstant.ABV_2, abv2);
			liveDataJson.put(ApiConstant.SHEAR, shear);
			liveDataJson.put(ApiConstant.TOTAL_PERM_WEIGHT, totalPermWt);
			liveDataJson.put(ApiConstant.VT, decimalFormat.format(vt));
			liveDataJson.put(ApiConstant.PERCENTAGE, Double.parseDouble(decimalFormat.format(percentage)));
			liveDataJson.put(ApiConstant.TIMESTAMP_2, BasicUtility.getInstance().getCurrentTimestamp());
			liveDataJson.put(ApiConstant.DURATION, TrialDurationManager.getInstance().getDuration());


			/****************************************************************************************/


			if (TrialManager.getInstance().isAlarmActive() && muteAlarm && !TrialManager.getInstance().isHardFault()) {

				//set alarm indicator
				AlarmManager.getInstance().stopDeviceAlarmIndicator();
				TrialManager.getInstance().setAlarmActive(false);
			}


			liveDataJson = calculatedNonRunFields(liveDataJson);

			convertedLiveDataJson = Converter.convertLiveData(liveDataJson);


			BasicUtility.systemPrint("converted", "liveData");
			sLog.d(this, convertedLiveDataJson);

			sLog.d(this, " is Running call  " + TrialManager.getInstance().isRunning());
			// save to db
			if (TrialManager.getInstance().isRunning()) {
				JSONArray convertedLiveDataJsonArray = GraphMapper.getGraphData(convertedLiveDataJson);
				broadcastLiveData(convertedLiveDataJsonArray);
				liveDataJson = Converter.setUnUsedFieldToNA(liveDataJson);
				BufferManager.getInstance().pushIntoBuffer(liveDataJson);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	public void deviceLiveConnectionList() {

		ComLib spectrumCommLib = ComLib.get();
		JSONArray scaleJsonArray = new JSONArray();
		JSONArray pressureJsonArray = new JSONArray();
		JSONObject deviceLiveConnectionListJson = new JSONObject();
		String connectedPumpName = BasicUtility.getInstance().getConnectedPumpName();


		deviceLiveConnectionListJson.put(ApiConstant.TYPE, ApiConstant.LIVE);

		scaleJsonArray.put(BasicUtility.getDeviceJsonObject(ApiConstant.FEED, spectrumCommLib.getScaleInfo().isScaleConnected(ScaleID.FEED_SCALE)));
		scaleJsonArray.put(BasicUtility.getDeviceJsonObject(ApiConstant.PERMEATE, spectrumCommLib.getScaleInfo().isScaleConnected(ScaleID.PERMEATE_SCALE)));

		deviceLiveConnectionListJson.put(ApiConstant.SCALE, scaleJsonArray);

		deviceLiveConnectionListJson.put(ApiConstant.AUX_PUMP_1_CONNECTED, spectrumCommLib.getAuxPump().isAuxConnected(0));
		deviceLiveConnectionListJson.put(ApiConstant.AUX_PUMP_2_CONNECTED, spectrumCommLib.getAuxPump().isAuxConnected(1));

		deviceLiveConnectionListJson.put(ApiConstant.KF_KONDUIT, new JSONObject().put(ApiConstant.IS_CONNECTED, spectrumCommLib.getKonduitInfo().isKFConduitConnected()));
		deviceLiveConnectionListJson.put(ApiConstant.IS_MAIN_PUMP_CONNECTED, ComLib.get().isConnected());

		deviceLiveConnectionListJson.put(ApiConstant.PUMP_TYPE, connectedPumpName);

		deviceLiveConnectionListJson.put(ApiConstant.PRESSURE_KEY, getPressureInfo());

		if (minMaxPumpSpeedJson == null) {

			minMaxPumpSpeedJson = BasicUtility.getPumpMinMaxRpm(connectedPumpName);
		}

		deviceLiveConnectionListJson.put(ApiConstant.MIN_SPEED, minMaxPumpSpeedJson.getString(ApiConstant.MIN_SPEED));
		deviceLiveConnectionListJson.put(ApiConstant.MAX_SPEED, minMaxPumpSpeedJson.getString(ApiConstant.MAX_SPEED));
		deviceLiveConnectionListJson.put(ApiConstant.DEVICE_ID, spectrumCommLib.getPumpSerialNumberHandler().getDeviceNumber());

		sLog.d(this, deviceLiveConnectionListJson);

		broadcastDeviceLiveConnectionList(deviceLiveConnectionListJson);
	}

	private JSONObject getPressureInfo() {

		JSONObject pressureJsonObject = new JSONObject();
		final boolean isFeedConnected = ComLib.get().getPressureInfo().getChannelStatus(PressureID.FEED_CHANNEL);
		final boolean isPermConnected = ComLib.get().getPressureInfo().getChannelStatus(PressureID.PERMEATE_CHANNEL);
		final boolean isRetConnected = ComLib.get().getPressureInfo().getChannelStatus(PressureID.RETENTATE_CHANNEL);

		pressureJsonObject.put(ApiConstant.IS_FEED_ACTIVE, isFeedConnected);
		if (isFeedConnected) {

			pressureJsonObject.put(ApiConstant.FEED_VALUE, ComLib.get().getPressureInfo().getChannelReadings_Psi(PressureID.FEED_CHANNEL));
		}
		pressureJsonObject.put(ApiConstant.IS_PERMEATE_ACTIVE, isPermConnected);
		if (isPermConnected) {
			pressureJsonObject.put(ApiConstant.PERMEATE_VALUE, ComLib.get().getPressureInfo().getChannelReadings_Psi(PressureID.PERMEATE_CHANNEL));
		}
		pressureJsonObject.put(ApiConstant.IS_RETENTATE_ACTIVE, isRetConnected);
		if (isRetConnected) {
			pressureJsonObject.put(ApiConstant.RETENTATE_VALUE, ComLib.get().getPressureInfo().getChannelReadings_Psi(PressureID.RETENTATE_CHANNEL));
		}
		return pressureJsonObject;

	}

	public void getLiveTrialData(SocketIOServer socketServer) {


		this.socketServer = socketServer;

		synchronized (this) {
			// from here it collect data after starting rxlistener.
			startRxListener();
		}

	}

	public JSONArray getAllComPort() {

		String[] ports = ComLib.get().getAllPorts();

		JSONArray portJsonArray = new JSONArray();


		for (int index = 0; index < ports.length; index++) {

			JSONObject portJsonObject = new JSONObject();
			portJsonObject.put(ApiConstant.PORT_NAME, ports[index]);

			portJsonArray.put(portJsonObject);
		}

		BasicUtility.systemPrint("ports", portJsonArray);
		return portJsonArray;

	}

	public void sendPortDisconnectCallback() {

		if (recipeJson != null && modeType == 0) {
			RunManager.getInstance().recirculationPause(true);
		} else if (modeType == 1 && ManualModeHandler.isTrialStarted != 0) {
			BufferManager.getInstance().setPausedToBuffer(1);
		}
		isDisconnected = true;
		isMainPumpConnected = false;

		JSONObject mainPumpDisconnectJson = new JSONObject();
		mainPumpDisconnectJson.put(ApiConstant.TYPE, ApiConstant.DISCONNECT);
		broadcastDeviceLiveConnectionList(mainPumpDisconnectJson);

		sLog.d(this, mainPumpDisconnectJson);
	}

	public String connectToPort(String portName) throws Exception {

		BasicUtility.systemPrint("inside", "2");

		if (portName.equals(CommHandler.get().getConnectedPortName())) {
			BasicUtility.systemPrint("disconnected", CommHandler.get().getConnectedPortName());

			if (!isConnectionListRunning) {

				isConnectionListRunning = true;
				isMainPumpConnected = true;
				connectionListener.startRxListening();
			}
			return portName;


		}

		ComLib.get().connectToPortName(portName, 1000, new PortDisconnectListener() {
			@Override
			public void disconnected(String portName) {
				sendPortDisconnectCallback();
			}
		});

		ComLib.get().getMainPump().setSpeed(0);
		ComLib.get().getAuxPump().setAuxiliaryPumpSettings(3, 3, true, true, 0, 0, 1, 1);

		try {
			Thread.sleep(2200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}


		String pumpName = BasicUtility.getInstance().getConnectedPumpName();

		BasicUtility.systemPrint("isDisconnected", isDisconnected);

		if (pumpName == null) {
			throw new Exception(ApiConstant.INVALID_PORT);
		} else {
			if (!isConnectionListRunning) {

				isConnectionListRunning = true;
				isMainPumpConnected = true;
				connectionListener.startRxListening();
			}
		}

		return portName;

	}

	public void connectSocket() {
		try {
			if (!TrialManager.getInstance().isRunning()) {
				if (recipeJson != null) {

					TrialManager.getInstance().setRunning(true);
				}
				Thread.sleep(1000);

				DeviceManager.getInstance().getLiveTrialData(socketServer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String autoConnect() throws PortNotFoundException {

        String comPort;
        if (DbConnectionManager.getInstance().isDebuggingMode) {

			return ApiConstant.KR2I;
		}

		Logger.setAutoConnectLogToFile(true);

        isConnected[0] = false;
        pumpName = null;
        connectedPortName = null;
        comPort = BasicUtility.getInstance().getLastConnectedComPort();
		sLog.d(this, "Last COM Port : "+comPort);
        isConnected[0] = ComLib.get().autoConnect(false, comPort, portDisconnectListener);

        if (isConnected[0]) {
            BasicUtility.systemPrint("portName", ComLib.get().getPortName());
            ComLib.get().getPumpSerialNumberHandler().requestPumpNumber();
            isMainPumpConnected = true;
            pumpName = BasicUtility.getInstance().getConnectedPumpName();
            connectedPortName = ComLib.get().getConnetecComPort();
            if (!connectedPortName.equals(comPort)) {
				BasicUtility.getInstance().updateLastConnectedComPort(connectedPortName);
			}

			if (!isConnectionListRunning) {
				isConnectionListRunning = true;
				connectionListener.startRxListening();
			}
			try {
				SystemSetting systemSetting = new SystemSettingsManager().fetchSystemSettings();
				int pressureUnit = systemSetting.getPressureUnit();
				if (pressureUnit == 1) {
					ComLib.get().getPressureUnit().setUnit(PressureUnits.PSI);
				} else if (pressureUnit == 2) {
					ComLib.get().getPressureUnit().setUnit(PressureUnits.mBar);
				} else if (pressureUnit == 3) {
					ComLib.get().getPressureUnit().setUnit(PressureUnits.Bar);
				}

			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
			}

			return pumpName;
		}

		return pumpName;
	}

	public void setSpeedAndFlowRateIndicator(String value) {

		BasicUtility.systemPrint("pump values", value);

		ComLib.get().getPumpSpeedTextHandler().setPumpSpeedText(value);
	}

	public void resetSpeedAndFlowRateIndicator(double flowRate) {

		if(DeviceManager.getInstance().getRecipeJson() == null){
			resetFlowRateIndicator();
		}
		// if pump is KMPi or FS500 than L/min.
		else if (
				ApiConstant.KMPI.equalsIgnoreCase(DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.PUMP_NAME)) ||
				ApiConstant.KROSFLOFS500.equalsIgnoreCase(DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.PUMP_NAME))

				) {
			setSpeedAndFlowRateIndicator(
					flowRate + ApiConstant.BLANK_SPACE +
					ApiConstant.LITER_PER_MIN

					);

		} else {

			setSpeedAndFlowRateIndicator(
					flowRate +
					ApiConstant.ML_MIN

					);
		}

	}

	public void resetFlowRateIndicator() {

		setSpeedAndFlowRateIndicator("");

	}


}
