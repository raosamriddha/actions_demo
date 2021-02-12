package org.lattice.spectrum_backend_final.dao.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.ABVSetting;
import org.lattice.spectrum_backend_final.beans.AuxPumpSetting;
import org.lattice.spectrum_backend_final.beans.KFKonduitSetting;
import org.lattice.spectrum_backend_final.beans.MainPumpSetting;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.DbConstant;
import org.lattice.spectrum_backend_final.dao.constants.QueryConstant;
import org.lattice.spectrum_backend_final.dao.util.AlarmSettingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lattice.spectrum.ComLibrary.ComLib;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ScaleID;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.USBConfiguration;
import com.lattice.spectrum.ComLibrary.StaticDefinitions.ValveConnectorConfiguration;
import com.lattice.spectrum.ComLibrary.utility.sLog;
import com.lattice.spectrum.ComLibrary.utility.ufx;

public class ManualModeManager {

	private static final Logger log = LoggerFactory.getLogger(ManualModeManager.class);

	private static ManualModeManager KFConduitManager;

	public static ManualModeManager getInstance() {

		synchronized (ManualModeManager.class) {
			if (KFConduitManager == null) {
				KFConduitManager = new ManualModeManager();
			}
		}
		return KFConduitManager;
	}

	public boolean isTrialRunSettingExists(final Integer trialRunSettingId) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchTrialSettingStmt = null;
		ResultSet resultSet = null;
		try {
			fetchTrialSettingStmt = con.prepareStatement(QueryConstant.CHECK_TRIAL_RUN_SETTING_EXISTS);
			fetchTrialSettingStmt.setInt(1, trialRunSettingId);
			log.info(QueryConstant.CHECK_TRIAL_RUN_SETTING_EXISTS);
			resultSet = fetchTrialSettingStmt.executeQuery();
			int count = 0;
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			return (count > 0) ? true : false;
		} catch (final Exception ex) {
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchTrialSettingStmt, con);
		}
	}

	public int saveKFConduitSetting(final KFKonduitSetting kfKonduitSetting) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement insertKFKonduitSettingStmt = null;
		try {
			con.setAutoCommit(false);
			insertKFKonduitSettingStmt = con.prepareStatement(QueryConstant.INSERT_KF_KONDUIT_SETTING,
					Statement.RETURN_GENERATED_KEYS);
			insertKFKonduitSettingStmt.setString(1, kfKonduitSetting.getCh1KFactor());
			insertKFKonduitSettingStmt.setString(2, kfKonduitSetting.getCh2KFactor());
			insertKFKonduitSettingStmt.setInt(3, kfKonduitSetting.getTrialRunSettingId());
			log.info(QueryConstant.INSERT_KF_KONDUIT_SETTING);
			final int affectedRows = insertKFKonduitSettingStmt.executeUpdate();
			if (affectedRows == 0) {
				return 0;
			}
			final ResultSet generatedKeys = insertKFKonduitSettingStmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				con.commit();
			} else {
				return 0;
			}
			return generatedKeys.getInt(1);
		} catch (final Exception ex) {
			try {
				con.rollback();
			} catch (final SQLException sqlEx) {
				log.error(ApiConstant.EXCEPTION, sqlEx);
			}
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, insertKFKonduitSettingStmt, con);
		}
	}

	public boolean isKFConduitDetailsExists(final int trialRunSettingId) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchKFConduitSettingStmt = null;
		ResultSet resultSet = null;
		try {
			fetchKFConduitSettingStmt = con.prepareStatement(QueryConstant.CHECK_KF_KONDUIT_SETTING_EXISTS);
			fetchKFConduitSettingStmt.setInt(1, trialRunSettingId);
			log.info(QueryConstant.CHECK_KF_KONDUIT_SETTING_EXISTS);
			resultSet = fetchKFConduitSettingStmt.executeQuery();
			int count = 0;
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			return (count > 0) ? true : false;
		} catch (final Exception ex) {
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchKFConduitSettingStmt, con);
		}
	}

	public int updateKFConduitDetails(final KFKonduitSetting kfKonduitSetting) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement updateKFConduitDetailsStmt = null;
		int affectedRows = 0;

		try {

			con.setAutoCommit(false);
			updateKFConduitDetailsStmt = con.prepareStatement(QueryConstant.UPDATE_KF_CONDUIT_DETAILS);
			updateKFConduitDetailsStmt.setString(1, kfKonduitSetting.getCh1KFactor());
			updateKFConduitDetailsStmt.setString(2, kfKonduitSetting.getCh2KFactor());
			updateKFConduitDetailsStmt.setInt(3, kfKonduitSetting.getTrialRunSettingId());
			log.info(QueryConstant.UPDATE_KF_CONDUIT_DETAILS);
			affectedRows = updateKFConduitDetailsStmt.executeUpdate();
			if (affectedRows < 1) {
				return affectedRows;
			}
			con.commit();
		} catch (final Exception ex) {
			try {
				con.rollback();
			} catch (final SQLException sqlEx) {
				log.error(ApiConstant.EXCEPTION, sqlEx.getMessage());
			}
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, updateKFConduitDetailsStmt, con);
		}
		return affectedRows;
	}

	public int saveAuxPumpSetting(final AuxPumpSetting auxPumpSetting) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement insertAUXSettingStmt = null;
		try {
			con.setAutoCommit(false);
			insertAUXSettingStmt = con.prepareStatement(QueryConstant.INSERT_AUX_PUMP_SETTING,
					Statement.RETURN_GENERATED_KEYS);
			insertAUXSettingStmt.setString(1, auxPumpSetting.getAuxPumpType());
			insertAUXSettingStmt.setString(2, auxPumpSetting.getTubingSize());
			insertAUXSettingStmt.setString(3, auxPumpSetting.getFlowRate());
			insertAUXSettingStmt.setInt(4, auxPumpSetting.getType());
			insertAUXSettingStmt.setString(5, auxPumpSetting.getSpeed());
			insertAUXSettingStmt.setInt(6, auxPumpSetting.getTrialRunSettingId());
			log.info(QueryConstant.INSERT_AUX_PUMP_SETTING);
			final int affectedRows = insertAUXSettingStmt.executeUpdate();
			if (affectedRows == 0) {
				return 0;
			}
			final ResultSet generatedKeys = insertAUXSettingStmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				con.commit();
			} else {
				return 0;
			}
			return generatedKeys.getInt(1);
		} catch (final Exception ex) {
			try {
				con.rollback();
			} catch (final SQLException sqlEx) {
				log.error(ApiConstant.EXCEPTION, sqlEx);
			}
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, insertAUXSettingStmt, con);
		}
	}

	public boolean isAuxPumpDetailsExists(final int trialRunSettingId, final int type) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchAUXSettingStmt = null;
		ResultSet resultSet = null;
		try {
			fetchAUXSettingStmt = con.prepareStatement(QueryConstant.CHECK_AUX_PUMP_EXISTS);
			fetchAUXSettingStmt.setInt(1, trialRunSettingId);
			fetchAUXSettingStmt.setInt(2, type);
			log.info(QueryConstant.CHECK_AUX_PUMP_EXISTS);
			resultSet = fetchAUXSettingStmt.executeQuery();
			int count = 0;
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			return (count > 0) ? true : false;
		} catch (final Exception ex) {
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchAUXSettingStmt, con);
		}
	}

	public int updateAuxPumpDetails(final AuxPumpSetting auxPumpSetting) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement updateAUXDetailsStmt = null;
		int affectedRows = 0;
		try {

			con.setAutoCommit(false);
			updateAUXDetailsStmt = con.prepareStatement(QueryConstant.UPDATE_AUX_PUMP_DETAILS);
			updateAUXDetailsStmt.setString(1, auxPumpSetting.getAuxPumpType());
			updateAUXDetailsStmt.setString(2, auxPumpSetting.getTubingSize());
			updateAUXDetailsStmt.setString(3, auxPumpSetting.getFlowRate());
			updateAUXDetailsStmt.setString(4, auxPumpSetting.getSpeed());
			updateAUXDetailsStmt.setInt(5, auxPumpSetting.getType());
			updateAUXDetailsStmt.setInt(6, auxPumpSetting.getTrialRunSettingId());
			log.info(QueryConstant.UPDATE_AUX_PUMP_DETAILS);
			affectedRows = updateAUXDetailsStmt.executeUpdate();
			if (affectedRows < 1) {
				return affectedRows;
			}
			con.commit();
		} catch (final Exception ex) {
			try {
				con.rollback();
			} catch (final SQLException sqlEx) {
				log.error(ApiConstant.EXCEPTION, sqlEx.getMessage());
			}
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, updateAUXDetailsStmt, con);
		}
		return affectedRows;
	}

	// todo add abv done
	public int saveABVSetting(final ABVSetting abvSetting) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement insertABVSettingStmt = null;
		try {
			con.setAutoCommit(false);
			insertABVSettingStmt = con.prepareStatement(QueryConstant.INSERT_ABV_SETTING,
					Statement.RETURN_GENERATED_KEYS);
			insertABVSettingStmt.setInt(1, abvSetting.getType());
			insertABVSettingStmt.setString(2, abvSetting.getAbvType());
			insertABVSettingStmt.setString(3, abvSetting.getPosition());
			insertABVSettingStmt.setString(4, abvSetting.getTubingSize());
			insertABVSettingStmt.setString(5, abvSetting.getAbvMode());
			insertABVSettingStmt.setString(6, abvSetting.getOperatingPressure());
			insertABVSettingStmt.setString(7, abvSetting.getAbvTarget());
			insertABVSettingStmt.setInt(8, abvSetting.getTrialRunSettingId());
			log.info(QueryConstant.INSERT_ABV_SETTING);
			final int affectedRows = insertABVSettingStmt.executeUpdate();
			if (affectedRows == 0) {
				return 0;
			}
			final ResultSet generatedKeys = insertABVSettingStmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				con.commit();
			} else {
				return 0;
			}
			return generatedKeys.getInt(1);
		} catch (final Exception ex) {
			try {
				con.rollback();
			} catch (final SQLException sqlEx) {
				log.error(ApiConstant.EXCEPTION, sqlEx);
			}
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, insertABVSettingStmt, con);
		}
	}

	public boolean isABVDetailsExists(final int trialRunSettingId, final Integer type) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchABVSettingStmt = null;
		ResultSet resultSet = null;
		try {
			fetchABVSettingStmt = con.prepareStatement(QueryConstant.CHECK_ABV_EXISTS);
			fetchABVSettingStmt.setInt(1, trialRunSettingId);
			fetchABVSettingStmt.setInt(2, type);
			log.info(QueryConstant.CHECK_ABV_EXISTS);
			resultSet = fetchABVSettingStmt.executeQuery();
			int count = 0;
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			return (count > 0) ? true : false;
		} catch (final Exception ex) {
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchABVSettingStmt, con);
		}
	}

	// todo add abv
	public int updateABVDetails(final ABVSetting abvSetting) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement updateABVDetailsStmt = null;
		int affectedRows = 0;
		try {
			con.setAutoCommit(false);
			updateABVDetailsStmt = con.prepareStatement(QueryConstant.UPDATE_ABV_DETAILS);
			updateABVDetailsStmt.setString(1, abvSetting.getAbvType());
			updateABVDetailsStmt.setString(2, abvSetting.getPosition());
			updateABVDetailsStmt.setString(3, abvSetting.getTubingSize());
			updateABVDetailsStmt.setString(4, abvSetting.getAbvMode());
			updateABVDetailsStmt.setString(5, abvSetting.getOperatingPressure());
			updateABVDetailsStmt.setString(6, abvSetting.getAbvTarget());
			updateABVDetailsStmt.setInt(7, abvSetting.getType());
			updateABVDetailsStmt.setInt(8, abvSetting.getTrialRunSettingId());
			log.info(QueryConstant.UPDATE_ABV_DETAILS);
			affectedRows = updateABVDetailsStmt.executeUpdate();
			if (affectedRows < 1) {
				return affectedRows;
			}
			con.commit();
		} catch (final Exception ex) {
			try {
				con.rollback();
			} catch (final SQLException sqlEx) {
				log.error(ApiConstant.EXCEPTION, sqlEx.getMessage());
			}
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, updateABVDetailsStmt, con);
		}
		return affectedRows;
	}

	public int saveMainPumpSetting(final MainPumpSetting mainPumpSetting) {
		final Connection con = DbConnectionManager.getInstance().getConnection();

		PreparedStatement insertMainPumpSettingStmt = null;
		try {
			con.setAutoCommit(false);
			insertMainPumpSettingStmt = con.prepareStatement(QueryConstant.INSERT_MAIN_PUMP_SETTING,
					Statement.RETURN_GENERATED_KEYS);
			insertMainPumpSettingStmt.setString(1, mainPumpSetting.getPumpName());
			insertMainPumpSettingStmt.setString(2, mainPumpSetting.getSpeed());
			insertMainPumpSettingStmt.setString(3, mainPumpSetting.getFlowRate());
			insertMainPumpSettingStmt.setString(4, mainPumpSetting.getDirection());
			insertMainPumpSettingStmt.setInt(5, mainPumpSetting.getTrialRunSettingId());
			insertMainPumpSettingStmt.setString(6, mainPumpSetting.getTubingSize());
			log.info(QueryConstant.INSERT_MAIN_PUMP_SETTING);
			final int affectedRows = insertMainPumpSettingStmt.executeUpdate();
			if (affectedRows == 0) {
				return 0;
			}
			final ResultSet generatedKeys = insertMainPumpSettingStmt.getGeneratedKeys();
			if (generatedKeys.next()) {
				con.commit();
			} else {
				return 0;
			}
			return generatedKeys.getInt(1);
		} catch (final Exception ex) {
			try {
				con.rollback();
			} catch (final SQLException sqlEx) {
				log.error(ApiConstant.EXCEPTION, sqlEx);
			}
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, insertMainPumpSettingStmt, con);
		}
	}

	public boolean isMainPumpDetailsExists(final int trialRunSettingId) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchMainPumpSettingStmt = null;
		ResultSet resultSet = null;
		try {
			fetchMainPumpSettingStmt = con.prepareStatement(QueryConstant.CHECK_MAIN_PUMP_EXISTS);
			fetchMainPumpSettingStmt.setInt(1, trialRunSettingId);
			log.info(QueryConstant.CHECK_MAIN_PUMP_EXISTS);
			resultSet = fetchMainPumpSettingStmt.executeQuery();
			int count = 0;
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			return (count > 0) ? true : false;
		} catch (final Exception ex) {
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchMainPumpSettingStmt, con);
		}
	}

	public int updateMainPumpDetails(final MainPumpSetting mainPumpSetting) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement updateMainPumpDetailsStmt = null;
		int affectedRows = 0;
		try {
			con.setAutoCommit(false);
			updateMainPumpDetailsStmt = con.prepareStatement(QueryConstant.UPDATE_MAIN_PUMP_DETAILS);
			updateMainPumpDetailsStmt.setString(1, mainPumpSetting.getPumpName());
			updateMainPumpDetailsStmt.setString(2, mainPumpSetting.getSpeed());
			updateMainPumpDetailsStmt.setString(3, mainPumpSetting.getFlowRate());
			updateMainPumpDetailsStmt.setString(4, mainPumpSetting.getDirection());
			updateMainPumpDetailsStmt.setString(5, mainPumpSetting.getTubingSize());
			updateMainPumpDetailsStmt.setInt(6, mainPumpSetting.getTrialRunSettingId());
			log.info(QueryConstant.UPDATE_MAIN_PUMP_DETAILS);
			affectedRows = updateMainPumpDetailsStmt.executeUpdate();
			if (affectedRows < 1) {
				return affectedRows;
			}
			con.commit();
		} catch (final Exception ex) {
			try {
				con.rollback();
			} catch (final SQLException sqlEx) {
				log.error(ApiConstant.EXCEPTION, sqlEx.getMessage());
			}
			log.error(ApiConstant.EXCEPTION, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, updateMainPumpDetailsStmt, con);
		}
		return affectedRows;
	}

	public JSONObject tareScaleManager(final int scale) {
		final JSONObject jsonObject = new JSONObject();

		if (scale == 0) {
			// permeate scale
			if (ComLib.get().getScaleInfo().isScaleConnected(ScaleID.PERMEATE_SCALE)) {
				ComLib.get().getScaleTare().start(false, true);
				final double timeout = ufx.time() + 10;
				while (ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.PERMEATE_SCALE) > 0) {
					try {
						Thread.sleep(1000);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
					if (timeout <= ufx.time()) {
						return jsonObject.put(ApiConstant.ERROR, ApiConstant.PERMEATE_SCALE_TIMEOUT);
					}
				}
				return jsonObject.put(ApiConstant.SUCCESS, ApiConstant.PERMEATE_SCALE_TARED_SUCCESS);
			} else {
				return jsonObject.put(ApiConstant.ERROR, ApiConstant.PERMEATE_SCALE_NOT_FOUND);
			}
		} else {
			// feed scale
			if (ComLib.get().getScaleInfo().isScaleConnected(ScaleID.FEED_SCALE)) {
				ComLib.get().getScaleTare().start(true, false);
				final double timeout = ufx.time() + 10;
				while (ComLib.get().getScaleInfo().scaleReading_gm(ScaleID.FEED_SCALE) > 0) {
					try {
						Thread.sleep(1000);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
					if (timeout <= ufx.time()) {
						return jsonObject.put(ApiConstant.ERROR, ApiConstant.FEED_SCALE_TARE_TIMEOUT);
					}
				}
				return jsonObject.put(ApiConstant.SUCCESS, ApiConstant.FEED_SCALE_TARED_SUCCESS);
			} else {
				return jsonObject.put(ApiConstant.ERROR, ApiConstant.FEED_SCALE_NOT_FOUND);
			}
		}
	}

	// Old configuration related methods

	public void deleteFailedTrialDetails(final int trialRunSettingId) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement deleteFailedTrialStmt = null;
		try {
			deleteFailedTrialStmt = con.prepareStatement(QueryConstant.DELETE_FAILED_TRIAL_RUN_SETTINGS);
			deleteFailedTrialStmt.setInt(1, trialRunSettingId);
			final int affectedRows = deleteFailedTrialStmt.executeUpdate();
			if (affectedRows == 0) {
				return;
			}
			deleteFailedTrialStmt.close();
			deleteFailedTrialStmt = con.prepareStatement(QueryConstant.DELETE_FAILED_ALARMS);
			deleteFailedTrialStmt.setInt(1, trialRunSettingId);
			deleteFailedTrialStmt.executeUpdate();

			deleteFailedTrialStmt.close();
			deleteFailedTrialStmt = con.prepareStatement(QueryConstant.DELETE_FAILED_AUX);
			deleteFailedTrialStmt.setInt(1, trialRunSettingId);
			deleteFailedTrialStmt.executeUpdate();

			deleteFailedTrialStmt.close();
			deleteFailedTrialStmt = con.prepareStatement(QueryConstant.DELETE_FAILED_KONDUIT);
			deleteFailedTrialStmt.setInt(1, trialRunSettingId);
			deleteFailedTrialStmt.executeUpdate();

			deleteFailedTrialStmt.close();
			deleteFailedTrialStmt = con.prepareStatement(QueryConstant.DELETE_FAILED_ABV);
			deleteFailedTrialStmt.setInt(1, trialRunSettingId);
			deleteFailedTrialStmt.executeUpdate();

			deleteFailedTrialStmt.close();
			deleteFailedTrialStmt = con.prepareStatement(QueryConstant.DELETE_FAILED_MAIN_PUMP);
			deleteFailedTrialStmt.setInt(1, trialRunSettingId);
			deleteFailedTrialStmt.executeUpdate();

		} catch (final Exception ex) {
			ex.printStackTrace();
		} finally {
			DbConnectionManager.closeDBConnection(null, deleteFailedTrialStmt, con);
		}
	}

	public final int saveNewTrialConfig(final JSONObject manualJsonObject) {
		final Connection conn = DbConnectionManager.getInstance().getConnection();
		try {
			if (!manualJsonObject.getJSONObject(ApiConstant.DETAILS).has(ApiConstant.DIGITAL_USERNAME)) {
				manualJsonObject.getJSONObject(ApiConstant.DETAILS).put(ApiConstant.DIGITAL_USERNAME,
						ApiConstant.BLANK_QUOTE);
			}

			manualJsonObject.getJSONObject(ApiConstant.DETAILS).put(ApiConstant.FILTER,
					manualJsonObject.getJSONObject(ApiConstant.FILTER));
			return RunManager.getInstance().saveTrialRunSetting(manualJsonObject.getJSONObject(ApiConstant.DETAILS),
					conn);
		} catch (final Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		} finally {
			DbConnectionManager.closeDBConnection(null, null, conn);
		}
	}

	public void saveNewAlarmConfig(final JSONObject manualJsonObject, final int trialRunSettingId) {
		if (!manualJsonObject.getJSONArray(ApiConstant.ALARMS_KEY).isEmpty()) {
			final Connection conn = DbConnectionManager.getInstance().getConnection();
			PreparedStatement saveAlarmPS = null;
			try {
				saveAlarmPS = conn.prepareStatement(DbConstant.SAVE_ALARM_SETTING_QUERY);
				AlarmSettingMapper.saveAlarmSettingStatementMapper(
						manualJsonObject.getJSONArray(ApiConstant.ALARMS_KEY).getJSONObject(0), saveAlarmPS,
						trialRunSettingId).executeUpdate();
			} catch (final Exception e) {
				e.printStackTrace();
			} finally {
				DbConnectionManager.closeDBConnection(null, saveAlarmPS, conn);
			}
		}
	}

	public void saveNewABVConfig(final JSONObject manualJsonObject, final int trialRunSettingId) {
		if (!manualJsonObject.getJSONArray(ApiConstant.ABV).isEmpty()) {
			final JSONArray abvJsonArray = manualJsonObject.getJSONArray(ApiConstant.ABV);
			for (int i = 0; i < abvJsonArray.length(); i++) {
				final ABVSetting abvSetting = new ABVSetting();
				abvSetting.setType(abvJsonArray.getJSONObject(i).optInt(ApiConstant.TYPE));
				abvSetting.setPosition(abvJsonArray.getJSONObject(i).optString(ApiConstant.POSITION));
				abvSetting.setTubingSize(abvJsonArray.getJSONObject(i).optString(ApiConstant.MANUAL_TUBING_SIZE));
				abvSetting.setAbvType(abvJsonArray.getJSONObject(i).optString(ApiConstant.MANUAL_ABV_TYPE));
				abvSetting.setAbvTarget(abvJsonArray.getJSONObject(i).optString(ApiConstant.MANUAL_ABV_TARGET));
				abvSetting.setAbvMode(abvJsonArray.getJSONObject(i).optString(ApiConstant.MANUAL_ABV_MODE));
				abvSetting
						.setOperatingPressure(abvJsonArray.getJSONObject(i).optString(ApiConstant.MANUAL_OP_PRESSURE));
				abvSetting.setTrialRunSettingId(trialRunSettingId);
				saveABVSetting(abvSetting);
			}
		}
	}

	public void saveNewAuxPumpConfig(final JSONObject manualJsonObject, final int trialRunSettingId) {
		if (!manualJsonObject.getJSONArray(ApiConstant.AUX_PUMP).isEmpty()) {
			final JSONArray auxJsonArray = manualJsonObject.getJSONArray(ApiConstant.AUX_PUMP);
			for (int i = 0; i < auxJsonArray.length(); i++) {
				final AuxPumpSetting auxPumpSetting = new AuxPumpSetting();
				auxPumpSetting
						.setAuxPumpType(auxJsonArray.getJSONObject(i).optString(ApiConstant.MANUAL_AUX_PUMP_TYPE));
				auxPumpSetting.setFlowRate(auxJsonArray.getJSONObject(i).optString(ApiConstant.MANUAL_FLOW_RATE));
				auxPumpSetting.setSpeed(auxJsonArray.getJSONObject(i).optString(ApiConstant.SPEED));
				auxPumpSetting.setTubingSize(auxJsonArray.getJSONObject(i).optString(ApiConstant.MANUAL_TUBING_SIZE));
				auxPumpSetting.setType(auxJsonArray.getJSONObject(i).optInt(ApiConstant.TYPE));
				auxPumpSetting.setTrialRunSettingId(trialRunSettingId);
				saveAuxPumpSetting(auxPumpSetting);
			}
		}
	}

	public void saveNewMainPumpConfig(final JSONObject manualJsonObject, final int trialRunSettingId) {
		final MainPumpSetting mainPumpSetting = new MainPumpSetting();
		mainPumpSetting.setDirection(
				manualJsonObject.getJSONObject(ApiConstant.MAIN_PUMP_KEY).optString(ApiConstant.DIRECTION));
		mainPumpSetting.setFlowRate(
				manualJsonObject.getJSONObject(ApiConstant.MAIN_PUMP_KEY).optString(ApiConstant.FLOW_RATE));
		mainPumpSetting.setPumpName(
				manualJsonObject.getJSONObject(ApiConstant.MAIN_PUMP_KEY).optString(ApiConstant.MAIN_PUMP_NAME));
		mainPumpSetting
				.setSpeed(manualJsonObject.getJSONObject(ApiConstant.MAIN_PUMP_KEY).optString(ApiConstant.SPEED));
		mainPumpSetting.setTubingSize(
				manualJsonObject.getJSONObject(ApiConstant.MAIN_PUMP_KEY).optString(ApiConstant.TUBING_SIZE));
		mainPumpSetting.setTrialRunSettingId(trialRunSettingId);
		saveMainPumpSetting(mainPumpSetting);
	}

	public void saveNewKonduitConfig(final JSONObject manualJsonObject, final int trialRunSettingId) {
		if (!manualJsonObject.getJSONObject(ApiConstant.KF_KONDUIT_KEY).isEmpty()) {
			final KFKonduitSetting kfKonduitSetting = new KFKonduitSetting();
			kfKonduitSetting.setCh1KFactor(
					manualJsonObject.getJSONObject(ApiConstant.KF_KONDUIT_KEY).optString(ApiConstant.K_FACTOR_CH_1));
			kfKonduitSetting.setCh2KFactor(
					manualJsonObject.getJSONObject(ApiConstant.KF_KONDUIT_KEY).optString(ApiConstant.K_FACTOR_CH_2));
			kfKonduitSetting.setTrialRunSettingId(trialRunSettingId);
			saveKFConduitSetting(kfKonduitSetting);
		}
	}

	public final boolean updateManualDetails(final JSONObject requestJsonObject, final int trialRunSettingId) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement updateManualStmt = null;
		try {
			con.setAutoCommit(false);
			updateManualStmt = con.prepareStatement(QueryConstant.UPDATE_MANUAL_TRIAL_DETAILS);
			updateManualStmt.setString(1, requestJsonObject.getString(ApiConstant.VALVE_2_CONNECTOR_COLUMN));
			updateManualStmt.setInt(2, trialRunSettingId);
			sLog.d(this, QueryConstant.UPDATE_MANUAL_TRIAL_DETAILS);
			final int affectedRows = updateManualStmt.executeUpdate();
			if (affectedRows < 1) {
				return false;
			}
			con.commit();
			return true;
		} catch (final Exception ex) {
			try {
				con.rollback();
			} catch (final SQLException sqlEx) {
				sLog.d(this, sqlEx.getMessage());
			}
			sLog.d(this, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, updateManualStmt, con);
		}
	}

	public void setValveToComLib(final String valve, final int trialRunSettingId) {
		sLog.d(this, "setValveToComLib");
		if (ApiConstant.VALVE_2_IS_VALVE.equals(valve)) {
			ComLib.get().getConfigureAuxiliary().configure(ValveConnectorConfiguration.USE_FOR_VALVE,
					USBConfiguration.DO_RESET);
			sLog.d(this, "USE_FOR_VALVE");
		} else if (ApiConstant.VALVE_2_IS_KONDUIT.equals(valve)) {
			ComLib.get().getConfigureAuxiliary().configure(ValveConnectorConfiguration.USE_FOR_KFCONDUIT,
					USBConfiguration.DO_RESET);
			sLog.d(this, "USE_FOR_KFCONDUIT");
		}
	}

	public final int saveManualFilterAtRun(JSONObject jsonObject, Connection conn, int trialRunSettingId) {

		PreparedStatement insertFilterStmt = null;
		try {
			Integer noOfCassetes = getCassetteValue(jsonObject);
			insertFilterStmt = conn.prepareStatement(QueryConstant.INSERT_MANUAL_FILTER);
			insertFilterStmt.setString(1, jsonObject.getString(ApiConstant.FILTER_TYPE));
			insertFilterStmt.setString(2, jsonObject.getString(ApiConstant.PART_NO));
			insertFilterStmt.setObject(3, noOfCassetes);
			insertFilterStmt.setInt(4, jsonObject.getInt(ApiConstant.IS_NON_REPLIGEN));
			insertFilterStmt.setString(5, jsonObject.optString(ApiConstant.SURF_AREA));
			insertFilterStmt.setInt(6, trialRunSettingId);
			sLog.d(this, QueryConstant.INSERT_MANUAL_FILTER);
			final int resultRows = insertFilterStmt.executeUpdate();
			if (resultRows <= 0) {
				throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
			}
			return resultRows;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}
	}

	private final Integer getCassetteValue(JSONObject jsonObject) {
		if (!jsonObject.isNull(ApiConstant.NO_OF_CASSETTE)) {
			return jsonObject.getInt(ApiConstant.NO_OF_CASSETTE);
		}
		return null;
	}

	public final boolean saveOrUpdateManualFilter(JSONObject jsonObject, int trialRunSettingId) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement updateManualFilterStmt = null;
		int affectedRows;
		try {
			con.setAutoCommit(false);
			if (isManualFilterExists(trialRunSettingId)) {
				Integer noOfCassetes = getCassetteValue(jsonObject);
				updateManualFilterStmt = con.prepareStatement(QueryConstant.UPDATE_MANUAL_FILTER);
				updateManualFilterStmt.setString(1, jsonObject.getString(ApiConstant.FILTER_TYPE));
				updateManualFilterStmt.setString(2, jsonObject.getString(ApiConstant.PART_NO));
				updateManualFilterStmt.setObject(3, noOfCassetes);
				updateManualFilterStmt.setObject(4, jsonObject.getInt(ApiConstant.IS_NON_REPLIGEN));
				updateManualFilterStmt.setString(5, jsonObject.getString(ApiConstant.SURF_AREA));
				updateManualFilterStmt.setInt(6, trialRunSettingId);
				sLog.d(this, QueryConstant.UPDATE_MANUAL_FILTER);
				affectedRows = updateManualFilterStmt.executeUpdate();
			} else {
				affectedRows = saveManualFilterAtRun(jsonObject, con, trialRunSettingId);
			}
			if (affectedRows < 1) {
				con.rollback();
				return false;
			}
			con.commit();
			return true;
		} catch (final Exception ex) {
			try {
				con.rollback();
			} catch (final SQLException sqlEx) {
				sLog.d(this, sqlEx.getMessage());
			}
			sLog.d(this, ex);
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, updateManualFilterStmt, con);
		}
	}

	private final boolean isManualFilterExists(final int trialRunSettingId) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement checkMfilterStmt = null;
		ResultSet resultSet = null;
		try {
			checkMfilterStmt = con.prepareStatement(QueryConstant.CHECK_MANAUL_FILTER_EXISTS);
			checkMfilterStmt.setInt(1, trialRunSettingId);
			sLog.d(this, QueryConstant.CHECK_MANAUL_FILTER_EXISTS);
			resultSet = checkMfilterStmt.executeQuery();
			int count = 0;
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			return (count > 0) ? true : false;
		} catch (final Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, checkMfilterStmt, con);
		}
	}

}