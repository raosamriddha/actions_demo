package org.lattice.spectrum_backend_final.dao.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;

public class GraphMapper {

	public static JSONArray getGraphData(JSONObject liveDataJson) {

		BasicUtility.systemPrint("inside","mapper");
		JSONArray graphDataJson = new JSONArray();
		graphDataJson.put(0, liveDataJson.getString(ApiConstant.FEED_SCALE));
		graphDataJson.put(1, liveDataJson.getString(ApiConstant.WATER_FLUX_20_DEGREE));
		graphDataJson.put(2, liveDataJson.getString(ApiConstant.AUX_PUMP_1_FLOW_RATE));
		graphDataJson.put(3, liveDataJson.getString(ApiConstant.CONDUCTIVITY_2));
		graphDataJson.put(4, liveDataJson.getString(ApiConstant.RETENTATE_FLOW_RATE));
		graphDataJson.put(5, liveDataJson.getString(ApiConstant.TYPE));
		graphDataJson.put(6, liveDataJson.getString(ApiConstant.MAIN_PUMP_FLOW_RATE));
		graphDataJson.put(7, liveDataJson.getString(ApiConstant.FEED_PRESSURE));
		graphDataJson.put(8, liveDataJson.getInt(ApiConstant.KONDUIT_CH_1_TYPE));
		graphDataJson.put(9, liveDataJson.getString(ApiConstant.KONDUIT_CH_1));
		graphDataJson.put(10, liveDataJson.getString(ApiConstant.TEMPERATURE_CORRECTION_FACTOR));
		graphDataJson.put(11, liveDataJson.getString(ApiConstant.KONDUIT_CH_2));
		graphDataJson.put(12, liveDataJson.getDouble(ApiConstant.PERCENTAGE));
		graphDataJson.put(13, liveDataJson.getString(ApiConstant.CONDUCTIVITY_1));
		graphDataJson.put(14, liveDataJson.getString(ApiConstant.DIAFILTRATION_VOL_2));
		graphDataJson.put(15, liveDataJson.getString(ApiConstant.DELTA_P));
		graphDataJson.put(16, liveDataJson.getString(ApiConstant.DIAFILTRATION_VOL_1));
		graphDataJson.put(17, liveDataJson.getString(ApiConstant.FLUX));
		graphDataJson.put(18, liveDataJson.getString(ApiConstant.TIMESTAMP));
		graphDataJson.put(19, liveDataJson.getString(ApiConstant.CONCENTRATION_FACT));
		graphDataJson.put(20, liveDataJson.getString(ApiConstant.AUX_PUMP_1_SPEED));
		graphDataJson.put(21, liveDataJson.getString(ApiConstant.ABV_1));
		graphDataJson.put(22, liveDataJson.getString(ApiConstant.FEED_FLOW_RATE));
		graphDataJson.put(23, liveDataJson.getString(ApiConstant.SHEAR));
		graphDataJson.put(24, liveDataJson.getInt(ApiConstant.KONDUIT_CH_2_TYPE));
		graphDataJson.put(25, liveDataJson.getString(ApiConstant.NWP));
		graphDataJson.put(26, liveDataJson.getString(ApiConstant.ABV_2));
		graphDataJson.put(27, liveDataJson.getString(ApiConstant.AUX_PUMP_2_FLOW_RATE));
		graphDataJson.put(28, liveDataJson.getString(ApiConstant.TOTAL_PERM_WEIGHT));
		graphDataJson.put(29, liveDataJson.getString(ApiConstant.RETENTATE_PRESSURE));
		graphDataJson.put(30, liveDataJson.getString(ApiConstant.WATER_FLUX));
		graphDataJson.put(31, liveDataJson.getString(ApiConstant.M_PERMEATE));
		graphDataJson.put(32, liveDataJson.getString(ApiConstant.TEMPERATURE_2));
		graphDataJson.put(33, liveDataJson.getString(ApiConstant.PERMEATE_PRESSURE));
		graphDataJson.put(34, liveDataJson.getString(ApiConstant.TEMPERATURE_1));
		graphDataJson.put(35, (int) liveDataJson.getDouble(ApiConstant.MAIN_PUMP_SPEED));
		graphDataJson.put(36, liveDataJson.getString(ApiConstant.AUX_PUMP_2_SPEED));
		graphDataJson.put(37, liveDataJson.getString(ApiConstant.TMP_PRESSURE));
		graphDataJson.put(38, liveDataJson.getString(ApiConstant.VT));
		graphDataJson.put(39, liveDataJson.getString(ApiConstant.WATER_PERMEABILITY));
		graphDataJson.put(40, liveDataJson.getString(ApiConstant.PERMEATE_FLOW_RATE));
		graphDataJson.put(41, liveDataJson.getString(ApiConstant.TIMESTAMP_2));
		graphDataJson.put(42, liveDataJson.getString(ApiConstant.PERMEATE_TOTAL_KEY));
		graphDataJson.put(43, liveDataJson.getString(ApiConstant.PERMEATE_TOTAL_WITH_HOLDUP));
		graphDataJson.put(44, liveDataJson.getString(ApiConstant.DURATION));
		return graphDataJson;
	}
}
