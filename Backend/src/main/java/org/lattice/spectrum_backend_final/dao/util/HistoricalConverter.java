package org.lattice.spectrum_backend_final.dao.util;

import java.sql.SQLException;

import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.TrialTableLogs;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.services.handler.RequestHandler;

public class HistoricalConverter {

	private HistoricalConverter() {
	}

	private static String pumpName = null;
	private static JSONObject auxType = null;
	private static int totalizerUnit = 0;
	private static int totalizerUnit_2 = 0;

	public static void assignPumpNameAndAuxType(final int trialRunSettingId) throws SQLException {
		if (RequestHandler.getTrialRunSettingId() == 0) {
			pumpName = BasicUtility.getInstance().getMainPumpName(trialRunSettingId);
			auxType = BasicUtility.getInstance().getAuxPumpType(trialRunSettingId);
			RequestHandler.setTrialRunSettingId(trialRunSettingId);
		}
	}

	public static void resetPumpNameValues() {
		pumpName = null;
		auxType = null;
	}

	public static String getMainPumpName() {
		return pumpName;
	}

	public static void setTotalizerUnits(final String totalizer1, final String totalizer2) {
		if (totalizer1 != null) {
			totalizerUnit = Integer.valueOf(totalizer1);
		}
		if (totalizer2 != null) {
			totalizerUnit_2 = Integer.valueOf(totalizer2);
		}
	}

	public static void setTotalizerUnits(int totalizer1, int totalizer2) {
		totalizerUnit = totalizer1;
		totalizerUnit_2 = totalizer2;
	}

	public static void resetTotalizerUnits() {
		totalizerUnit = 0;
		totalizerUnit_2 = 0;
	}

	public static int getTotalizerUnit() {
		return totalizerUnit;
	}

	public static int getTotalizerUnit_2() {
		return totalizerUnit_2;
	}

	public static String convertFlowRate(final double flowRate) throws SQLException {
		if (ApiConstant.KMPI.equalsIgnoreCase(pumpName) || ApiConstant.KROSFLOFS500.equalsIgnoreCase(pumpName)) {
			return Converter.setDecimalPlace(flowRate / 1000, RequestHandler.getStSystemSetting().getDecimalFlowRate());
		}
		return Converter.setDecimalPlace(flowRate, RequestHandler.getStSystemSetting().getDecimalFlowRate());
	}

	public static String convertTotalizer_1(final double flowRate) throws SQLException {
		if (totalizerUnit == 1) {
			return Converter.setDecimalPlace(flowRate / 1000, RequestHandler.getStSystemSetting().getDecimalFlowRate());
		}
		return Converter.setDecimalPlace(flowRate, RequestHandler.getStSystemSetting().getDecimalFlowRate());
	}

	public static String convertTotalizer_2(final double flowRate) throws SQLException {
		if (totalizerUnit_2 == 1) {
			return Converter.setDecimalPlace(flowRate / 1000, RequestHandler.getStSystemSetting().getDecimalFlowRate());
		}
		return Converter.setDecimalPlace(flowRate, RequestHandler.getStSystemSetting().getDecimalFlowRate());
	}

	public static String convertPressure(final double pressure) throws SQLException {
		if (!ApiConstant.PSI.equalsIgnoreCase(
				Converter.systemSettingPressureMapper(RequestHandler.getStSystemSetting().getPressureUnit()))) {
			return Converter.setDecimalPlace(
					Converter.unitConverter(pressure,
							Converter.systemSettingPressureMapper(
									RequestHandler.getStSystemSetting().getPressureUnit())),
					RequestHandler.getStSystemSetting().getDecimalPressure());
		}
		return Converter.setDecimalPlace(pressure, RequestHandler.getStSystemSetting().getDecimalPressure());
	}

	public static String convertScale(final double scale) throws SQLException {
		if (!ApiConstant.GRAM.equalsIgnoreCase(
				Converter.systemSettingWeightMapper(RequestHandler.getStSystemSetting().getWeightUnit()))) {
			return Converter
					.setDecimalPlace(
							Converter.unitConverter(scale,
									Converter.systemSettingWeightMapper(
											RequestHandler.getStSystemSetting().getWeightUnit())),
							RequestHandler.getStSystemSetting().getDecimalWeight());
		}
		return Converter.setDecimalPlace(scale, RequestHandler.getStSystemSetting().getDecimalWeight());
	}

	private static String getConvertedAUXFlowValue(final double flowRate, String pumpType) throws SQLException {
		if (ApiConstant.IP.equalsIgnoreCase(auxType.getString(pumpType))) {
			return Converter.setDecimalPlace(flowRate / 1000, RequestHandler.getStSystemSetting().getDecimalFlowRate());
		}
		return Converter.setDecimalPlace(flowRate, RequestHandler.getStSystemSetting().getDecimalFlowRate());
	}

	public static String convertVolume(final double volume) throws SQLException {
		if (RequestHandler.getStSystemSetting().getVolumeUnit() == 2) {
			return Converter.setDecimalPlace(volume / 1000, RequestHandler.getStSystemSetting().getDecimalVolume());
		}
		return Converter.setDecimalPlace(volume, RequestHandler.getStSystemSetting().getDecimalVolume());
	}

	public static void convertTrialLogPumpFlowRate(final TrialTableLogs trialTable)
			throws NumberFormatException, SQLException {

		if (pumpName != null) {
			trialTable.setFeedFlowRate(convertFlowRate(Double.valueOf(trialTable.getFeedFlowRate())));
			trialTable.setPermeateFlowRate(convertFlowRate(Double.valueOf(trialTable.getPermeateFlowRate())));
			trialTable.setRetentateFlowRate(convertFlowRate(Double.valueOf(trialTable.getRetentateFlowRate())));
			trialTable.setMainPumpFlowRate(convertFlowRate(Double.valueOf(trialTable.getMainPumpFlowRate())));
		}

		if (auxType != null) {
			if (auxType.has(ApiConstant.AUX_PUMP_1_TYPE)) {
				trialTable.setAuxPump1FlowRate(getConvertedAUXFlowValue(
						Double.valueOf(trialTable.getAuxPump1FlowRate()), ApiConstant.AUX_PUMP_1_TYPE));
			}
			if (auxType.has(ApiConstant.AUX_PUMP_2_TYPE)) {
				trialTable.setAuxPump2FlowRate(getConvertedAUXFlowValue(
						Double.valueOf(trialTable.getAuxPump2FlowRate()), ApiConstant.AUX_PUMP_2_TYPE));
			}
		}
	}

	public static String convertAUX1FlowRate(String valueToConvert) throws NumberFormatException, SQLException {
		if (auxType != null && auxType.has(ApiConstant.AUX_PUMP_1_TYPE)) {
			return getConvertedAUXFlowValue(Double.valueOf(valueToConvert), ApiConstant.AUX_PUMP_1_TYPE);
		}
		return valueToConvert;
	}

	public static String convertAUX2FlowRate(String valueToConvert) throws NumberFormatException, SQLException {
		if (auxType != null && auxType.has(ApiConstant.AUX_PUMP_2_TYPE)) {
			return getConvertedAUXFlowValue(Double.valueOf(valueToConvert), ApiConstant.AUX_PUMP_2_TYPE);
		}
		return valueToConvert;
	}

	public static String getTotalizerUnit(int type) throws SQLException {

		if (type == 1) {
			if (totalizerUnit == 0) {
				return ApiConstant.BLANK_SPACE + ApiConstant.ML_MIN;
			} else {
				return ApiConstant.BLANK_SPACE + ApiConstant.LITER_PER_MIN;
			}
		} else if (type == 2) {
			if (totalizerUnit_2 == 0) {
				return ApiConstant.BLANK_SPACE + ApiConstant.ML_MIN;
			} else {
				return ApiConstant.BLANK_SPACE + ApiConstant.LITER_PER_MIN;
			}
		} else {
			return ApiConstant.BLANK_QUOTE;
		}

	}

	public static String getMainPumpFlowRateUnit() {
		if (ApiConstant.KMPI.equalsIgnoreCase(pumpName) || ApiConstant.KROSFLOFS500.equalsIgnoreCase(pumpName)) {
			return ApiConstant.LITER_PER_MIN;
		}
		return ApiConstant.ML_MIN;
	}

	public static String getAUX1FlowRateUnit() {
		if (auxType != null && auxType.has(ApiConstant.AUX_PUMP_1_TYPE)) {
			if (ApiConstant.IP.equalsIgnoreCase(auxType.getString(ApiConstant.AUX_PUMP_1_TYPE))) {
				return ApiConstant.LITER_PER_MIN;
			}
		}
		return ApiConstant.ML_MIN;
	}

	public static String getAUX2FlowRateUnit() {
		if (auxType != null && auxType.has(ApiConstant.AUX_PUMP_2_TYPE)) {
			if (ApiConstant.IP.equalsIgnoreCase(auxType.getString(ApiConstant.AUX_PUMP_2_TYPE))) {
				return ApiConstant.LITER_PER_MIN;
			}
		}
		return ApiConstant.ML_MIN;
	}

}
