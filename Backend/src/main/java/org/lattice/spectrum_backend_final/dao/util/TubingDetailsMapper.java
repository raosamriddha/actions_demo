package org.lattice.spectrum_backend_final.dao.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.lattice.spectrum_backend_final.beans.TubingDetails;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;

public class TubingDetailsMapper {

	public static TubingDetails fetchTubingDetailsMapper(ResultSet resultSet) throws SQLException {
		TubingDetails tubingDetails = new TubingDetails();
		tubingDetails.setTubeName(resultSet.getString(ApiConstant.TUBE_NAME));
		tubingDetails.setIdIn(resultSet.getDouble(ApiConstant.TUBE_ID_IN));
		tubingDetails.setIdMm(resultSet.getDouble(ApiConstant.TUBE_ID_MM));
		tubingDetails.setRpmMinMax(resultSet.getString(ApiConstant.TUBE_RPM_MIN_MAX));
		tubingDetails.setFlowRate(resultSet.getString(ApiConstant.TUBE_FLOW_RATE));
		tubingDetails.setMainPump(resultSet.getString(ApiConstant.TUBE_MAIN_PUMP));
		tubingDetails.setAuxPump(resultSet.getString(ApiConstant.TUBE_AUX_PUMP));
		tubingDetails.setAbvType(resultSet.getString(ApiConstant.TUBE_ABV_TYPE));
		return tubingDetails;
	}

}
