package org.lattice.spectrum_backend_final.dao.util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.lattice.spectrum_backend_final.beans.SystemSetting;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;

public final class SystemSettingsMapper {

	public final PreparedStatement insertSystemSettingsStatementMapper(SystemSetting systemSetting,
			PreparedStatement insertSystemSetting) throws SQLException {
		insertSystemSetting.setInt(1, systemSetting.getPressureUnit());
		insertSystemSetting.setInt(2, systemSetting.getWeightUnit());
		insertSystemSetting.setInt(3, systemSetting.getVolumeUnit());
		insertSystemSetting.setInt(4, systemSetting.getLengthUnit());
		insertSystemSetting.setInt(5, systemSetting.getDecimalPressure());
		insertSystemSetting.setInt(6, systemSetting.getDecimalWeight());
		insertSystemSetting.setInt(7, systemSetting.getDecimalVolume());
		insertSystemSetting.setInt(8, systemSetting.getDecimalLength());
		insertSystemSetting.setInt(9, systemSetting.getDecimalFlowRate());
		insertSystemSetting.setInt(10, systemSetting.getDateFormat());
		insertSystemSetting.setInt(11, systemSetting.getIsAUXkonduit());
		insertSystemSetting.setObject(12, systemSetting.getUvMin());
		insertSystemSetting.setObject(13, systemSetting.getUvMax());
		insertSystemSetting.setObject(14, systemSetting.getPhMin());
		insertSystemSetting.setObject(15, systemSetting.getPhMax());
		insertSystemSetting.setObject(16, systemSetting.getTurbidityMin());
		insertSystemSetting.setObject(17, systemSetting.getTurbidityMax());
		insertSystemSetting.setObject(18, systemSetting.getTotalizerMin());
		insertSystemSetting.setObject(19, systemSetting.getTotalizerMax());
		insertSystemSetting.setObject(20, System.currentTimeMillis());
		insertSystemSetting.setObject(21, System.currentTimeMillis());
		insertSystemSetting.setInt(22, systemSetting.getIsAUXkonduit_2());
		insertSystemSetting.setObject(23, systemSetting.getPhMin_2());
		insertSystemSetting.setObject(24, systemSetting.getPhMax_2());
		insertSystemSetting.setObject(25, systemSetting.getTurbidityMin_2());
		insertSystemSetting.setObject(26, systemSetting.getTurbidityMax_2());
		insertSystemSetting.setObject(27, systemSetting.getTotalizerMin_2());
		insertSystemSetting.setObject(28, systemSetting.getTotalizerMax_2());
		insertSystemSetting.setObject(29, systemSetting.getProteinConcMin());
		insertSystemSetting.setObject(30, systemSetting.getProteinConcMax());
		insertSystemSetting.setObject(31, systemSetting.getProteinConcMin_2());
		insertSystemSetting.setObject(32, systemSetting.getProteinConcMax_2());
		insertSystemSetting.setInt(33, systemSetting.getTotalizerUnit());
		insertSystemSetting.setInt(34, systemSetting.getTotalizerUnit_2());
		return insertSystemSetting;
	}

	public final SystemSetting fetchSystemSettingsResultMapper(ResultSet resultSet) throws SQLException {
		SystemSetting systemSetting = new SystemSetting();
		systemSetting.setSystemId(resultSet.getInt(ApiConstant.COLUMN_SYSTEM_ID));
		systemSetting.setPressureUnit(resultSet.getInt(ApiConstant.COLUMN_PRESSURE_UNIT));
		systemSetting.setWeightUnit(resultSet.getInt(ApiConstant.COLUMN_WEIGHT_UNIT));
		systemSetting.setVolumeUnit(resultSet.getInt(ApiConstant.COLUMN_VOLUME_UNIT));
		systemSetting.setLengthUnit(resultSet.getInt(ApiConstant.COLUMN_LENGTH_UNIT));
		systemSetting.setDecimalPressure(resultSet.getInt(ApiConstant.COLUMN_DECIMAL_PRESSURE));
		systemSetting.setDecimalWeight(resultSet.getInt(ApiConstant.COLUMN_DECIMAL_WEIGHT));
		systemSetting.setDecimalFlowRate(resultSet.getInt(ApiConstant.COLUMN_DECIMAL_FLOW_RATE));
		systemSetting.setDecimalVolume(resultSet.getInt(ApiConstant.COLUMN_DECIMAL_VOLUME));
		systemSetting.setDecimalLength(resultSet.getInt(ApiConstant.COLUMN_DECIMAL_LENGTH));
		systemSetting.setDateFormat(resultSet.getInt(ApiConstant.COLUMN_DATE_FORMAT));
		systemSetting.setIsAUXkonduit(resultSet.getInt(ApiConstant.IS_AUX_KONDUIT));
		systemSetting.setIsAUXkonduit_2(resultSet.getInt(ApiConstant.IS_AUX_KONDUIT_2));
		systemSetting.setUvMin((Double) resultSet.getObject(ApiConstant.COLUMN_UV_MIN));
		systemSetting.setUvMax((Double) resultSet.getObject(ApiConstant.COLUMN_UV_MAX));
		systemSetting.setPhMin((Double) resultSet.getObject(ApiConstant.COLUMN_PH_MIN));
		systemSetting.setPhMax((Double) resultSet.getObject(ApiConstant.COLUMN_PH_MAX));
		systemSetting.setPhMin_2((Double) resultSet.getObject(ApiConstant.COLUMN_PH_MIN_2));
		systemSetting.setPhMax_2((Double) resultSet.getObject(ApiConstant.COLUMN_PH_MAX_2));
		systemSetting.setTurbidityMin((Double) resultSet.getObject(ApiConstant.COLUMN_TURBIDITY_MIN));
		systemSetting.setTurbidityMax((Double) resultSet.getObject(ApiConstant.COLUMN_TURBIDITY_MAX));
		systemSetting.setTurbidityMin_2((Double) resultSet.getObject(ApiConstant.COLUMN_TURBIDITY_MIN_2));
		systemSetting.setTurbidityMax_2((Double) resultSet.getObject(ApiConstant.COLUMN_TURBIDITY_MAX_2));
		systemSetting.setTotalizerMin((Double) resultSet.getObject(ApiConstant.COLUMN_TOTALIZER_MIN));
		systemSetting.setTotalizerMax((Double) resultSet.getObject(ApiConstant.COLUMN_TOTALIZER_MAX));
		systemSetting.setTotalizerMin_2((Double) resultSet.getObject(ApiConstant.COLUMN_TOTALIZER_MIN_2));
		systemSetting.setTotalizerMax_2((Double) resultSet.getObject(ApiConstant.COLUMN_TOTALIZER_MAX_2));
		systemSetting.setProteinConcMin((Double) resultSet.getObject(ApiConstant.COLUMN_PROTEIN_CONC_MIN));
		systemSetting.setProteinConcMax((Double) resultSet.getObject(ApiConstant.COLUMN_PROTEIN_CONC_MAX));
		systemSetting.setProteinConcMin_2((Double) resultSet.getObject(ApiConstant.COLUMN_PROTEIN_CONC_MIN_2));
		systemSetting.setProteinConcMax_2((Double) resultSet.getObject(ApiConstant.COLUMN_PROTEIN_CONC_MAX_2));
		systemSetting.setTotalizerUnit(resultSet.getInt(ApiConstant.COLUMN_TOTALIZER_UNIT));
		systemSetting.setTotalizerUnit_2(resultSet.getInt(ApiConstant.COLUMN_TOTALIZER_UNIT_2));
		systemSetting.setIsActive(resultSet.getInt(ApiConstant.IS_ACTIVE));
		return systemSetting;
	}

//	public PreparedStatement updateSystemSettingsStatementMapper(SystemSetting systemSetting,
//			PreparedStatement updateSystemSettingStmt) throws SQLException {
//		updateSystemSettingStmt.setInt(1, systemSetting.getPressureUnit());
//		updateSystemSettingStmt.setInt(2, systemSetting.getWeightUnit());
//		updateSystemSettingStmt.setInt(3, systemSetting.getVolumeUnit());
//		updateSystemSettingStmt.setInt(4, systemSetting.getLengthUnit());
//		updateSystemSettingStmt.setInt(5, systemSetting.getDecimalPressure());
//		updateSystemSettingStmt.setInt(6, systemSetting.getDecimalWeight());
//		updateSystemSettingStmt.setInt(7, systemSetting.getDecimalVolume());
//		updateSystemSettingStmt.setInt(8, systemSetting.getDecimalLength());
//		updateSystemSettingStmt.setInt(9, systemSetting.getDecimalFlowRate());
//		updateSystemSettingStmt.setInt(10, systemSetting.getDateFormat());
//		updateSystemSettingStmt.setInt(11, systemSetting.getIsAUXkonduit());
//		updateSystemSettingStmt.setObject(12, systemSetting.getUvMin());
//		updateSystemSettingStmt.setObject(13, systemSetting.getUvMax());
//		updateSystemSettingStmt.setObject(14, systemSetting.getPhMin());
//		updateSystemSettingStmt.setObject(15, systemSetting.getPhMax());
//		updateSystemSettingStmt.setObject(16, systemSetting.getTurbidityMin());
//		updateSystemSettingStmt.setObject(17, systemSetting.getTurbidityMax());
//		updateSystemSettingStmt.setObject(18, systemSetting.getTotalizerMin());
//		updateSystemSettingStmt.setObject(19, systemSetting.getTotalizerMax());
//		updateSystemSettingStmt.setObject(20, System.currentTimeMillis());
//		updateSystemSettingStmt.setInt(21, systemSetting.getSystemId());
//		updateSystemSettingStmt.setInt(22, systemSetting.getIsAUXkonduit_2());
//		updateSystemSettingStmt.setObject(23, systemSetting.getPhMin_2());
//		updateSystemSettingStmt.setObject(24, systemSetting.getPhMax_2());
//		updateSystemSettingStmt.setObject(25, systemSetting.getTurbidityMin_2());
//		updateSystemSettingStmt.setObject(26, systemSetting.getTurbidityMax_2());
//		updateSystemSettingStmt.setObject(27, systemSetting.getTotalizerMin_2());
//		updateSystemSettingStmt.setObject(28, systemSetting.getTotalizerMax_2());
//		updateSystemSettingStmt.setObject(29, systemSetting.getProteinConcMin());
//		updateSystemSettingStmt.setObject(30, systemSetting.getProteinConcMax());
//		updateSystemSettingStmt.setObject(31, systemSetting.getProteinConcMin_2());
//		updateSystemSettingStmt.setObject(32, systemSetting.getProteinConcMax_2());
//		return updateSystemSettingStmt;
//	}
}
