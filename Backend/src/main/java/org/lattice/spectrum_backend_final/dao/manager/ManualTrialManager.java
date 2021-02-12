package org.lattice.spectrum_backend_final.dao.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.ABVSetting;
import org.lattice.spectrum_backend_final.beans.AuxPumpSetting;
import org.lattice.spectrum_backend_final.beans.KFKonduitSetting;
import org.lattice.spectrum_backend_final.beans.MainPumpSetting;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.QueryConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lattice.spectrum.ComLibrary.utility.sLog;

public class ManualTrialManager {

	private static final Logger log = LoggerFactory.getLogger(ManualTrialManager.class);

	private static ManualTrialManager trialManager;

	private ManualTrialManager() {
	}

	public static ManualTrialManager getInstance() {

		synchronized (ManualTrialManager.class) {
			if (trialManager == null) {
				trialManager = new ManualTrialManager();
			}
		}

		return trialManager;
	}

	public List<AuxPumpSetting> getAuxPumpSetting(int trialRunSettingId) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchAuxPumpStmt = null;
		ResultSet resultSet = null;
		List<AuxPumpSetting> auxPumpSettings = new ArrayList<AuxPumpSetting>();
		try {
			fetchAuxPumpStmt = con.prepareStatement(QueryConstant.FETCH_AUX_PUMPS_SETTING);
			fetchAuxPumpStmt.setInt(1, trialRunSettingId);
			log.info(QueryConstant.FETCH_AUX_PUMPS_SETTING);
			resultSet = fetchAuxPumpStmt.executeQuery();
			while (resultSet.next()) {
				AuxPumpSetting auxPumpSetting = new AuxPumpSetting();
				auxPumpSetting.setAuxPumpType(resultSet.getString(ApiConstant.AUX_PUMP_TYPE));
				auxPumpSetting.setFlowRate(resultSet.getString(ApiConstant.FLOW_RATE));
				auxPumpSetting.setSpeed(resultSet.getString(ApiConstant.PUMP_SPEED));
				auxPumpSetting.setTubingSize(resultSet.getString(ApiConstant.AUX_TUBING_SIZE));
				auxPumpSetting.setType(resultSet.getInt(ApiConstant.TYPE));
				auxPumpSettings.add(auxPumpSetting);
			}
			return auxPumpSettings;
		} catch (Exception ex) {
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchAuxPumpStmt, con);
		}
	}

	public List<ABVSetting> getABVSettings(int trialRunSettingId) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchAbvSettingsStmt = null;
		ResultSet resultSet = null;
		List<ABVSetting> abvSettings = new ArrayList<ABVSetting>();
		try {
			fetchAbvSettingsStmt = con.prepareStatement(QueryConstant.FETCH_ABV_SETTING);
			fetchAbvSettingsStmt.setInt(1, trialRunSettingId);
			log.info(QueryConstant.FETCH_ABV_SETTING);
			resultSet = fetchAbvSettingsStmt.executeQuery();
			while (resultSet.next()) {
				ABVSetting abvSetting = new ABVSetting();
				abvSetting.setType(resultSet.getInt(ApiConstant.TYPE));
				abvSetting.setPosition(resultSet.getString(ApiConstant.POSITION));
				abvSetting.setTubingSize(resultSet.getString(ApiConstant.TUBING_SIZE));
				abvSetting.setAbvType(resultSet.getString(ApiConstant.ABV_TYPE));
				abvSetting.setAbvTarget(resultSet.getString(ApiConstant.ABV_TARGET));
				abvSetting.setAbvMode(resultSet.getString(ApiConstant.ABV_MODE));
				abvSetting.setOperatingPressure(resultSet.getString(ApiConstant.OP_PRESSURE));
				abvSettings.add(abvSetting);
			}
			return abvSettings;
		} catch (Exception ex) {
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchAbvSettingsStmt, con);
		}
	}

	public MainPumpSetting getMainPumpSetting(int trialRunSettingId) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchMainPumpStmt = null;
		ResultSet resultSet = null;
		MainPumpSetting mainPumpSetting = null;
		try {
			fetchMainPumpStmt = con.prepareStatement(QueryConstant.FETCH_MAIN_PUMP_SETTING);
			fetchMainPumpStmt.setInt(1, trialRunSettingId);
			log.info(QueryConstant.FETCH_MAIN_PUMP_SETTING);
			resultSet = fetchMainPumpStmt.executeQuery();
			if (resultSet.next()) {
				mainPumpSetting = new MainPumpSetting();
				mainPumpSetting.setPumpName(resultSet.getString(ApiConstant.MAIN_PUMP_NAME));
				mainPumpSetting.setDirection(resultSet.getString(ApiConstant.DIRECTION));
				mainPumpSetting.setFlowRate(resultSet.getString(ApiConstant.FLOW_RATE));
				mainPumpSetting.setSpeed(resultSet.getString(ApiConstant.SPEED));
				mainPumpSetting.setTubingSize(resultSet.getString(ApiConstant.TUBING_SIZE));
			}
			return mainPumpSetting;
		} catch (Exception ex) {
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchMainPumpStmt, con);
		}
	}

	public JSONObject getMainPumpSettingJSON(int trialRunSettingId) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchMainPumpStmt = null;
		ResultSet resultSet = null;
		JSONObject mainPumpSettingJSON = null;
		try {
			fetchMainPumpStmt = con.prepareStatement(QueryConstant.FETCH_MAIN_PUMP_SETTING);
			fetchMainPumpStmt.setInt(1, trialRunSettingId);
			log.info(QueryConstant.FETCH_MAIN_PUMP_SETTING);
			resultSet = fetchMainPumpStmt.executeQuery();
			if (resultSet.next()) {
				mainPumpSettingJSON = new JSONObject();
				mainPumpSettingJSON.put(ApiConstant.MAIN_PUMP_NAME, resultSet.getString(ApiConstant.MAIN_PUMP_NAME));
				mainPumpSettingJSON.put(ApiConstant.DIRECTION, resultSet.getString(ApiConstant.DIRECTION));
				mainPumpSettingJSON.put(ApiConstant.FLOW_RATE, resultSet.getString(ApiConstant.FLOW_RATE));
				mainPumpSettingJSON.put(ApiConstant.SPEED, resultSet.getString(ApiConstant.SPEED));
				mainPumpSettingJSON.put(ApiConstant.TUBING_SIZE, resultSet.getString(ApiConstant.TUBING_SIZE));
			}
			return mainPumpSettingJSON;
		} catch (Exception ex) {
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchMainPumpStmt, con);
		}
	}

	public KFKonduitSetting getKFKonduitSetting(int trialRunSettingId) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchKFKonduitStmt = null;
		ResultSet resultSet = null;
		KFKonduitSetting kfKonduitSetting = new KFKonduitSetting();
		try {
			fetchKFKonduitStmt = con.prepareStatement(QueryConstant.FETCH_KF_CONDUIT_SETTING);
			fetchKFKonduitStmt.setInt(1, trialRunSettingId);
			log.info(QueryConstant.FETCH_KF_CONDUIT_SETTING);
			resultSet = fetchKFKonduitStmt.executeQuery();
			if (resultSet.next()) {
				kfKonduitSetting.setCh1KFactor(resultSet.getString(ApiConstant.K_FACTOR_CH_1));
				kfKonduitSetting.setCh2KFactor(resultSet.getString(ApiConstant.K_FACTOR_CH_2));
			}
			return kfKonduitSetting;
		} catch (Exception ex) {
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchKFKonduitStmt, con);
		}
	}

	public JSONObject getKFKonduitJSON(int trialRunSettingId) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchKFKonduitStmt = null;
		ResultSet resultSet = null;
		JSONObject kfKonduitSettingJSON = new JSONObject();
		try {
			fetchKFKonduitStmt = con.prepareStatement(QueryConstant.FETCH_KF_CONDUIT_SETTING);
			fetchKFKonduitStmt.setInt(1, trialRunSettingId);
			log.info(QueryConstant.FETCH_KF_CONDUIT_SETTING);
			resultSet = fetchKFKonduitStmt.executeQuery();
			if (resultSet.next()) {
				kfKonduitSettingJSON.put(ApiConstant.K_FACTOR_CH_1, resultSet.getString(ApiConstant.K_FACTOR_CH_1));
				kfKonduitSettingJSON.put(ApiConstant.K_FACTOR_CH_2, resultSet.getString(ApiConstant.K_FACTOR_CH_2));
			}
			return kfKonduitSettingJSON;
		} catch (Exception ex) {
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchKFKonduitStmt, con);
		}
	}

	public final AuxPumpSetting getAUXSettingByType(int trialRunSettingId, int type) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchAuxPumpStmt = null;
		ResultSet resultSet = null;
		AuxPumpSetting auxPumpSetting = null;
		try {
			fetchAuxPumpStmt = con.prepareStatement(QueryConstant.FETCH_AUX_PUMPS_SETTING_BY_TYPE);
			fetchAuxPumpStmt.setInt(1, trialRunSettingId);
			fetchAuxPumpStmt.setInt(2, type);
			log.info(QueryConstant.FETCH_AUX_PUMPS_SETTING_BY_TYPE);
			resultSet = fetchAuxPumpStmt.executeQuery();
			if (resultSet.next()) {
				auxPumpSetting = new AuxPumpSetting();
				auxPumpSetting.setAuxPumpType(resultSet.getString(ApiConstant.AUX_PUMP_TYPE));
				auxPumpSetting.setFlowRate(resultSet.getString(ApiConstant.FLOW_RATE));
				auxPumpSetting.setSpeed(resultSet.getString(ApiConstant.PUMP_SPEED));
				auxPumpSetting.setTubingSize(resultSet.getString(ApiConstant.AUX_TUBING_SIZE));
				auxPumpSetting.setType(resultSet.getInt(ApiConstant.TYPE));
			}
			return auxPumpSetting;
		} catch (Exception ex) {
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchAuxPumpStmt, con);
		}
	}

	public final ABVSetting getABVSettingByType(int trialRunSettingId, int type) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchABVStmt = null;
		ResultSet resultSet = null;
		ABVSetting abvSetting = null;
		try {
			fetchABVStmt = con.prepareStatement(QueryConstant.FETCH_ABV_SETTING_BY_TYPE);
			fetchABVStmt.setInt(1, trialRunSettingId);
			fetchABVStmt.setInt(2, type);
			log.info(QueryConstant.FETCH_ABV_SETTING_BY_TYPE);
			resultSet = fetchABVStmt.executeQuery();
			if (resultSet.next()) {
				abvSetting = new ABVSetting();
				abvSetting.setType(resultSet.getInt(ApiConstant.TYPE));
				abvSetting.setPosition(resultSet.getString(ApiConstant.POSITION));
				abvSetting.setTubingSize(resultSet.getString(ApiConstant.TUBING_SIZE));
				abvSetting.setAbvType(resultSet.getString(ApiConstant.ABV_TYPE));
				abvSetting.setAbvTarget(resultSet.getString(ApiConstant.ABV_TARGET));
				abvSetting.setAbvMode(resultSet.getString(ApiConstant.ABV_MODE));
				abvSetting.setOperatingPressure(resultSet.getString(ApiConstant.OP_PRESSURE));
			}
			return abvSetting;
		} catch (Exception ex) {
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchABVStmt, con);
		}
	}

	public final JSONObject getManualFilterDetails(int trialRunSettingId) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchFilterStmt = null;
		ResultSet resultSet = null;
		JSONObject resultJSON = new JSONObject();
		try {
			fetchFilterStmt = con.prepareStatement(QueryConstant.FETCH_MANUAL_FILTER);
			fetchFilterStmt.setInt(1, trialRunSettingId);
			sLog.d(this, QueryConstant.FETCH_MANUAL_FILTER);
			resultSet = fetchFilterStmt.executeQuery();
			if (resultSet.next()) {
				resultJSON.put(ApiConstant.FILTER_TYPE, resultSet.getString(ApiConstant.FILTER_TYPE));
				resultJSON.put(ApiConstant.PART_NO, resultSet.getString(ApiConstant.PART_NO));
				resultJSON.put(ApiConstant.NO_OF_CASSETTE, resultSet.getObject(ApiConstant.NO_OF_CASSETTE));
				resultJSON.put(ApiConstant.IS_NON_REPLIGEN, resultSet.getObject(ApiConstant.IS_NON_REPLIGEN));
				resultJSON.put(ApiConstant.SURF_AREA, resultSet.getString(ApiConstant.SURF_AREA));
				if (ApiConstant.FILTER_TYPE_IS_HOLLOW_FIBER.equals(resultJSON.getString(ApiConstant.FILTER_TYPE))) {
					ManualTrialManager.getInstance().getHFDetails(resultJSON);
				} else if (ApiConstant.CASSETTE.equals(resultJSON.getString(ApiConstant.FILTER_TYPE))) {
					ManualTrialManager.getInstance().getCassetteDetails(resultJSON);
				}
			}
			return resultJSON;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchFilterStmt, con);
		}
	}

	public void getHFDetails(JSONObject manualFilterJSON) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchFilterStmt = null;
		ResultSet resultSet = null;
		try {
			fetchFilterStmt = con.prepareStatement(QueryConstant.FETCH_MANUAL_HF_DETAILS);
			fetchFilterStmt.setString(1, manualFilterJSON.getString(ApiConstant.PART_NO));
			sLog.d(this, QueryConstant.FETCH_MANUAL_HF_DETAILS);
			resultSet = fetchFilterStmt.executeQuery();
			if (resultSet.next()) {
				manualFilterJSON.put(ApiConstant.FIBER_COUNT, resultSet.getInt(ApiConstant.FIBER_COUNT));
				manualFilterJSON.put(ApiConstant.FIBER_ID, resultSet.getDouble(ApiConstant.FIBER_ID));
				if (manualFilterJSON.getInt(ApiConstant.IS_NON_REPLIGEN) == 0) {
					manualFilterJSON.put(ApiConstant.SURF_AREA, resultSet.getDouble(ApiConstant.SURFACE_AREA));
				}
				manualFilterJSON.put(ApiConstant.ECS, resultSet.getDouble(ApiConstant.COLUMN_ECS));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchFilterStmt, con);
		}
	}

	public void getCassetteDetails(JSONObject manualFilterJSON) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchFilterStmt = null;
		ResultSet resultSet = null;
		try {
			fetchFilterStmt = con.prepareStatement(QueryConstant.FETCH_MANUAL_CASSETTE_DETAILS);
			fetchFilterStmt.setString(1, manualFilterJSON.getString(ApiConstant.PART_NO));
			sLog.d(this, QueryConstant.FETCH_MANUAL_CASSETTE_DETAILS);
			resultSet = fetchFilterStmt.executeQuery();
			if (resultSet.next()) {
				manualFilterJSON.put(ApiConstant.HEIGHT, resultSet.getDouble(ApiConstant.HEIGHT));
				manualFilterJSON.put(ApiConstant.WIDTH, resultSet.getDouble(ApiConstant.WIDTH));
				manualFilterJSON.put(ApiConstant.NO_OF_CHANNEL, resultSet.getInt(ApiConstant.NO_OF_CHANNEL));
				if (manualFilterJSON.getInt(ApiConstant.IS_NON_REPLIGEN) == 0) {
					manualFilterJSON.put(ApiConstant.SURF_AREA, resultSet.getDouble(ApiConstant.SURFACE_AREA));
				}
				manualFilterJSON.put(ApiConstant.ECS, resultSet.getDouble(ApiConstant.COLUMN_ECS));
				computeTotalSurfaceArea(manualFilterJSON);
			} else {
				computeTotalSurfaceArea(manualFilterJSON);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchFilterStmt, con);
		}
	}

	private void computeTotalSurfaceArea(JSONObject manualFilterJSON) {
		if (!manualFilterJSON.isNull(ApiConstant.NO_OF_CASSETTE)) {
			if (manualFilterJSON.getInt(ApiConstant.IS_NON_REPLIGEN) == 0) {
				manualFilterJSON.put(ApiConstant.TOTAL_SURF_AREA, manualFilterJSON.getInt(ApiConstant.NO_OF_CASSETTE)
						* manualFilterJSON.getDouble(ApiConstant.SURF_AREA));
			} else if (manualFilterJSON.getString(ApiConstant.SURF_AREA) != null
					&& !manualFilterJSON.getString(ApiConstant.SURF_AREA).isEmpty()) {
				manualFilterJSON.put(ApiConstant.TOTAL_SURF_AREA, manualFilterJSON.getInt(ApiConstant.NO_OF_CASSETTE)
						* manualFilterJSON.getDouble(ApiConstant.SURF_AREA));
			} else {
				manualFilterJSON.put(ApiConstant.TOTAL_SURF_AREA, JSONObject.NULL);
			}
		} else {
			manualFilterJSON.put(ApiConstant.TOTAL_SURF_AREA, JSONObject.NULL);
			manualFilterJSON.put(ApiConstant.NO_OF_CASSETTE, JSONObject.NULL);
		}
	}

}