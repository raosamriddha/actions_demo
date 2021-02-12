package org.lattice.spectrum_backend_final.dao.manager;

import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertFlowRate;
import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertPressure;
import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertTotalizer_1;
import static org.lattice.spectrum_backend_final.dao.util.HistoricalConverter.convertTotalizer_2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.GraphConstants;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.GraphMapper;
import org.lattice.spectrum_backend_final.dao.util.GraphType;
import org.lattice.spectrum_backend_final.dao.util.GraphUtil;
import org.lattice.spectrum_backend_final.dao.util.HistoricalConverter;

public final class GraphManager {

	private static GraphManager graphManager;

	public static GraphManager getInstance() {

		synchronized (GraphManager.class) {
			if (graphManager == null) {
				graphManager = new GraphManager();
			}
		}
		return graphManager;
	}

	public final JSONArray fetchGraphData(final int trialRunSettingId, final int type, boolean isLive)
			throws Exception {
		String query = GraphUtil.getInstance().getGraphQueryByType(type, trialRunSettingId);
		if (query == null) {
			return new JSONArray();
		}
		try (final Connection con = DbConnectionManager.getInstance().getConnection();
				final PreparedStatement fetchGraphDataStmt = con.prepareStatement(query);) {

			fetchGraphDataStmt.setInt(1, trialRunSettingId);

			try (final ResultSet resultSet = fetchGraphDataStmt.executeQuery()) {
				return getJSONArrayFromResultSet(type, resultSet, trialRunSettingId, isLive);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private final JSONArray getJSONArrayFromResultSet(final int type, final ResultSet resultSet,
			final int trialRunSettingId, boolean isLive) throws SQLException {

		switch (type) {
		case GraphType.FLOW_RATE_VS_TIME:
			return getFlowRateTimeValues(resultSet, trialRunSettingId, isLive);

		case GraphType.PRESSURE_VS_TIME:
			return getPressureTimeValues(resultSet, isLive);

		case GraphType.FLUX_VS_TIME:
			return getFluxTimeValues(resultSet, isLive);

		case GraphType.PRESSURE_VS_CF_VS_TIME:
			return getPressureCFTimeValues(resultSet, isLive);

		case GraphType.FLUX_VS_CF:
			return getFluxCFValues(resultSet);

		case GraphType.FLUX_VS_TMP:
			return getFluxTMPValues(resultSet, trialRunSettingId);

		case GraphType.PRESSURE_VS_CF_VS_VT:
			return getPressureCFVTValues(resultSet);

		case GraphType.DV_VS_CONDUCTIVTY_VS_TIME:
			return getDVCondTimeValues(resultSet, isLive);

		case GraphType.KCH_1_VS_TEMP_VS_TIME:
			return getKCHTempTimeValues(resultSet, true, isLive);

		case GraphType.KCH_2_VS_TEMP_VS_TIME:
			return getKCHTempTimeValues(resultSet, false, isLive);

		case GraphType.FLUX_VS_CF_TIME:
			return getFluxCFTimeValues(resultSet, isLive);

		case GraphType.TEMP_VS_CONDUCTIVTY_VS_TIME:
			return getTempCondTimeValues(resultSet, isLive);
			
		case GraphType.RPM_VS_TIME:
			return getRPMTimeValues(resultSet, isLive);

		default:
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		}
	}

	private final JSONArray getRPMTimeValues(final ResultSet resultSet, boolean isLive) {
		final JSONArray jsonArray = new JSONArray();
		JSONArray valueJsonArray = null;
		try {
			if (resultSet != null && resultSet.next()) {
				if (isLive) {
					oldRPMTimePoints(resultSet, valueJsonArray, jsonArray);
				} else {
					if (resultSet.getString(ApiConstant.DURATION) != null
							&& !ApiConstant.NOT_APPLICABLE.equals(resultSet.getString(ApiConstant.DURATION))) {
						newRPMTimePoints(resultSet, jsonArray);
					} else {
						oldRPMTimePoints(resultSet, valueJsonArray, jsonArray);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return jsonArray;
		}
		return jsonArray;
	}

	private void newRPMTimePoints(ResultSet resultSet, JSONArray jsonArray) {
		StringBuilder categoryBuilder = new StringBuilder();
		StringBuilder rpmBuilder = new StringBuilder();
		try {
			categoryBuilder.append(resultSet.getString(ApiConstant.DURATION)).append(ApiConstant.GRAPH_SEPARATER);
			rpmBuilder.append(resultSet.getDouble(ApiConstant.MAIN_PUMP_SPEED)).append(ApiConstant.GRAPH_SEPARATER);

			while (resultSet.next()) {
				categoryBuilder.append(resultSet.getString(ApiConstant.DURATION)).append(ApiConstant.GRAPH_SEPARATER);
				rpmBuilder.append(resultSet.getDouble(ApiConstant.MAIN_PUMP_SPEED)).append(ApiConstant.GRAPH_SEPARATER);
			}
			categoryBuilder.deleteCharAt(categoryBuilder.length() - 1);
			rpmBuilder.deleteCharAt(rpmBuilder.length() - 1);

			jsonArray
					.put(new JSONObject()
							.put(ApiConstant.CATEGORIES,
									new JSONArray().put(
											new JSONObject().put(ApiConstant.CATEGORY, categoryBuilder.toString())))
							.put(ApiConstant.DATASET,
									new JSONArray().put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.RPM)
											.put(ApiConstant.DATA, rpmBuilder.toString()))));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void oldRPMTimePoints(ResultSet resultSet, JSONArray valueJsonArray, JSONArray jsonArray) {
		try {
			valueJsonArray = new JSONArray();
			valueJsonArray.put(0,
					BasicUtility.getInstance().convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)));
			valueJsonArray.put(1, resultSet.getDouble(ApiConstant.MAIN_PUMP_SPEED));
			jsonArray.put(valueJsonArray);
			while (resultSet.next()) {
				valueJsonArray = new JSONArray();
				valueJsonArray.put(0,
						BasicUtility.getInstance().convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)));
				valueJsonArray.put(1, resultSet.getDouble(ApiConstant.MAIN_PUMP_SPEED));
				jsonArray.put(valueJsonArray);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private final JSONArray getTempCondTimeValues(final ResultSet resultSet, boolean isLive) {
		final JSONArray jsonArray = new JSONArray();
		JSONArray valueJsonArray = null;
		try {
			if (resultSet != null && resultSet.next()) {
				if (isLive) {
					oldTempCondTimePoints(resultSet, valueJsonArray, jsonArray);
				} else {
					if (resultSet.getString(ApiConstant.DURATION) != null
							&& !ApiConstant.NOT_APPLICABLE.equals(resultSet.getString(ApiConstant.DURATION))) {
						newTempCondTimePoints(resultSet, jsonArray);
					} else {
						oldTempCondTimePoints(resultSet, valueJsonArray, jsonArray);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return jsonArray;
		}
		return jsonArray;
	}

	private void newTempCondTimePoints(ResultSet resultSet, JSONArray jsonArray) {
		StringBuilder categoryBuilder = new StringBuilder();
		StringBuilder cond1Builder = new StringBuilder();
		StringBuilder cond2Builder = new StringBuilder();
		StringBuilder temp1Builder = new StringBuilder();
		StringBuilder temp2Builder = new StringBuilder();

		try {
			categoryBuilder.append(resultSet.getString(ApiConstant.DURATION)).append(ApiConstant.GRAPH_SEPARATER);
			cond1Builder.append(Optional.ofNullable(resultSet.getObject(ApiConstant.CONDUCTIVITY_1))
					.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE)).append(ApiConstant.GRAPH_SEPARATER);
			cond2Builder.append(Optional.ofNullable(resultSet.getObject(ApiConstant.CONDUCTIVITY_2))
					.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE)).append(ApiConstant.GRAPH_SEPARATER);
			temp1Builder.append(Optional.ofNullable(resultSet.getObject(ApiConstant.TEMPERATURE_1))
					.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE)).append(ApiConstant.GRAPH_SEPARATER);
			temp2Builder.append(Optional.ofNullable(resultSet.getObject(ApiConstant.TEMPERATURE_2))
					.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE)).append(ApiConstant.GRAPH_SEPARATER);

			while (resultSet.next()) {
				categoryBuilder.append(resultSet.getString(ApiConstant.DURATION)).append(ApiConstant.GRAPH_SEPARATER);
				cond1Builder
						.append(Optional.ofNullable(resultSet.getObject(ApiConstant.CONDUCTIVITY_1))
								.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE))
						.append(ApiConstant.GRAPH_SEPARATER);
				cond2Builder
						.append(Optional.ofNullable(resultSet.getObject(ApiConstant.CONDUCTIVITY_2))
								.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE))
						.append(ApiConstant.GRAPH_SEPARATER);
				temp1Builder
						.append(Optional.ofNullable(resultSet.getObject(ApiConstant.TEMPERATURE_1))
								.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE))
						.append(ApiConstant.GRAPH_SEPARATER);
				temp2Builder
						.append(Optional.ofNullable(resultSet.getObject(ApiConstant.TEMPERATURE_2))
								.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE))
						.append(ApiConstant.GRAPH_SEPARATER);
			}
			categoryBuilder.deleteCharAt(categoryBuilder.length() - 1);
			cond1Builder.deleteCharAt(cond1Builder.length() - 1);
			cond2Builder.deleteCharAt(cond2Builder.length() - 1);
			temp1Builder.deleteCharAt(temp1Builder.length() - 1);
			temp2Builder.deleteCharAt(temp2Builder.length() - 1);

			jsonArray
					.put(new JSONObject()
							.put(ApiConstant.CATEGORIES,
									new JSONArray().put(
											new JSONObject().put(ApiConstant.CATEGORY, categoryBuilder.toString())))
							.put(ApiConstant.DATASET, new JSONArray()
									.put(new JSONObject().put(ApiConstant.SERIES_NAME, GraphConstants.CONDUCTIVITY_1)
											.put(ApiConstant.DATA, cond1Builder.toString()))
									.put(new JSONObject().put(ApiConstant.SERIES_NAME, GraphConstants.CONDUCTIVITY_2)
											.put(ApiConstant.DATA, cond2Builder.toString()))
									.put(new JSONObject().put(ApiConstant.SERIES_NAME, GraphConstants.TEMPERATURE_1)
											.put(ApiConstant.DATA, temp1Builder.toString()))
									.put(new JSONObject().put(ApiConstant.SERIES_NAME, GraphConstants.TEMPERATURE_2)
											.put(ApiConstant.DATA, temp2Builder.toString()))));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void oldTempCondTimePoints(ResultSet resultSet, JSONArray valueJsonArray, JSONArray jsonArray) {
		try {
			valueJsonArray = new JSONArray();
			valueJsonArray.put(0,
					BasicUtility.getInstance().convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)));
			valueJsonArray.put(1, Optional.ofNullable(resultSet.getObject(ApiConstant.CONDUCTIVITY_1))
					.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE));
			valueJsonArray.put(2, Optional.ofNullable(resultSet.getObject(ApiConstant.CONDUCTIVITY_2))
					.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE));
			valueJsonArray.put(3, Optional.ofNullable(resultSet.getObject(ApiConstant.TEMPERATURE_1))
					.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE));
			valueJsonArray.put(4, Optional.ofNullable(resultSet.getObject(ApiConstant.TEMPERATURE_2))
					.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE));
			jsonArray.put(valueJsonArray);
			while (resultSet.next()) {
				valueJsonArray = new JSONArray();
				valueJsonArray.put(0,
						BasicUtility.getInstance().convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)));
				valueJsonArray.put(1, Optional.ofNullable(resultSet.getObject(ApiConstant.CONDUCTIVITY_1))
						.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE));
				valueJsonArray.put(2, Optional.ofNullable(resultSet.getObject(ApiConstant.CONDUCTIVITY_2))
						.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE));
				valueJsonArray.put(3, Optional.ofNullable(resultSet.getObject(ApiConstant.TEMPERATURE_1))
						.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE));
				valueJsonArray.put(4, Optional.ofNullable(resultSet.getObject(ApiConstant.TEMPERATURE_2))
						.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE));
				jsonArray.put(valueJsonArray);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private final JSONArray getFluxCFTimeValues(ResultSet resultSet, boolean isLive) throws SQLException {
		final JSONArray jsonArray = new JSONArray();
		JSONArray valueJsonArray = null;
		if (resultSet != null && resultSet.next()) {
			if (isLive) {
				oldFluxCFTimePoints(resultSet, valueJsonArray, jsonArray);
			} else {
				if (resultSet.getString(ApiConstant.DURATION) != null
						&& !ApiConstant.NOT_APPLICABLE.equals(resultSet.getString(ApiConstant.DURATION))) {
					newFluxCFTimePoints(resultSet, jsonArray);
				} else {
					oldFluxCFTimePoints(resultSet, valueJsonArray, jsonArray);
				}
			}
		}
		return jsonArray;
	}

	private void newFluxCFTimePoints(ResultSet resultSet, JSONArray jsonArray) {
		StringBuilder categoryBuilder = new StringBuilder();
		StringBuilder fluxBuilder = new StringBuilder();
		StringBuilder cfBuilder = new StringBuilder();
		try {
			categoryBuilder.append(resultSet.getString(ApiConstant.DURATION)).append(ApiConstant.GRAPH_SEPARATER);
			fluxBuilder.append(resultSet.getDouble(ApiConstant.FLUX)).append(ApiConstant.GRAPH_SEPARATER);
			cfBuilder.append(resultSet.getDouble(ApiConstant.COLUMN_CONCENTRATION_FACTOR))
					.append(ApiConstant.GRAPH_SEPARATER);

			while (resultSet.next()) {
				categoryBuilder.append(resultSet.getString(ApiConstant.DURATION)).append(ApiConstant.GRAPH_SEPARATER);
				fluxBuilder.append(resultSet.getDouble(ApiConstant.FLUX)).append(ApiConstant.GRAPH_SEPARATER);
				cfBuilder.append(resultSet.getDouble(ApiConstant.COLUMN_CONCENTRATION_FACTOR))
						.append(ApiConstant.GRAPH_SEPARATER);
			}
			categoryBuilder.deleteCharAt(categoryBuilder.length() - 1);
			fluxBuilder.deleteCharAt(fluxBuilder.length() - 1);
			cfBuilder.deleteCharAt(cfBuilder.length() - 1);

			jsonArray
					.put(new JSONObject()
							.put(ApiConstant.CATEGORIES,
									new JSONArray().put(
											new JSONObject().put(ApiConstant.CATEGORY, categoryBuilder.toString())))
							.put(ApiConstant.DATASET, new JSONArray()
									.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.FLUX)
											.put(ApiConstant.DATA, fluxBuilder.toString()))
									.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.CONCENTRATION_FACTOR)
											.put(ApiConstant.DATA, cfBuilder.toString()))));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void oldFluxCFTimePoints(ResultSet resultSet, JSONArray valueJsonArray, JSONArray jsonArray) {
		try {
			valueJsonArray = new JSONArray();
			valueJsonArray.put(0,
					BasicUtility.getInstance().convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)));
			valueJsonArray.put(1, resultSet.getDouble(ApiConstant.FLUX));
			valueJsonArray.put(2, resultSet.getDouble(ApiConstant.COLUMN_CONCENTRATION_FACTOR));
			jsonArray.put(valueJsonArray);
			while (resultSet.next()) {
				valueJsonArray = new JSONArray();
				valueJsonArray.put(0,
						BasicUtility.getInstance().convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)));
				valueJsonArray.put(1, resultSet.getDouble(ApiConstant.FLUX));
				valueJsonArray.put(2, resultSet.getDouble(ApiConstant.COLUMN_CONCENTRATION_FACTOR));
				jsonArray.put(valueJsonArray);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private final JSONArray getKCHTempTimeValues(final ResultSet resultSet, final boolean isCh1, boolean isLive) {
		final JSONArray jsonArray = new JSONArray();
		final JSONArray responseJsonArray = new JSONArray();
		try {
			if (resultSet != null && resultSet.next()) {
				if (isLive) {
					oldKCHTempTimePoints(resultSet, jsonArray, isCh1, responseJsonArray);
				} else {
					if (resultSet.getString(ApiConstant.DURATION) != null
							&& !ApiConstant.NOT_APPLICABLE.equals(resultSet.getString(ApiConstant.DURATION))) {
						newKCHTempTimePoints(resultSet, jsonArray, isCh1, responseJsonArray);
					} else {
						oldKCHTempTimePoints(resultSet, jsonArray, isCh1, responseJsonArray);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return responseJsonArray;
		}
		return responseJsonArray;
	}

	private void newKCHTempTimePoints(ResultSet resultSet, JSONArray jsonArray, boolean isCh1,
			JSONArray responseJsonArray) {
		boolean gotType = false;
		Integer type = null;
		StringBuilder categoryBuilder = new StringBuilder();
		StringBuilder ch1Builder = new StringBuilder();
		StringBuilder ch2Builder = new StringBuilder();
		try {
			categoryBuilder.append(resultSet.getString(ApiConstant.DURATION)).append(ApiConstant.GRAPH_SEPARATER);
			if (isCh1) {
				if (!gotType) {
					type = resultSet.getInt(ApiConstant.KONDUIT_CH_1_TYPE);
					gotType = true;
				}

				if (ApiConstant.CHANNEL_1_TOTALIZER_TYPE == type) {

					if (resultSet.getObject(ApiConstant.KONDUIT_CH_1) != null) {
						ch1Builder.append(convertTotalizer_1(resultSet.getDouble(ApiConstant.KONDUIT_CH_1)))
								.append(ApiConstant.GRAPH_SEPARATER);
					} else {
						ch1Builder.append(ApiConstant.NOT_APPLICABLE).append(ApiConstant.GRAPH_SEPARATER);
					}

				} else {
					ch1Builder
							.append(Optional.ofNullable(resultSet.getObject(ApiConstant.KONDUIT_CH_1))
									.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE))
							.append(ApiConstant.GRAPH_SEPARATER);
				}

			} else {
				if (!gotType) {
					type = resultSet.getInt(ApiConstant.KONDUIT_CH_2_TYPE);
					gotType = true;
				}

				if (ApiConstant.CHANNEL_2_TOTALIZER_TYPE == type) {

					if (resultSet.getObject(ApiConstant.KONDUIT_CH_2) != null) {
						ch2Builder.append(convertTotalizer_2(resultSet.getDouble(ApiConstant.KONDUIT_CH_2)))
								.append(ApiConstant.GRAPH_SEPARATER);
					} else {
						ch2Builder.append(ApiConstant.NOT_APPLICABLE).append(ApiConstant.GRAPH_SEPARATER);
					}

				} else {
					ch2Builder
							.append(Optional.ofNullable(resultSet.getObject(ApiConstant.KONDUIT_CH_2))
									.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE))
							.append(ApiConstant.GRAPH_SEPARATER);
				}

			}

			while (resultSet.next()) {
				categoryBuilder.append(resultSet.getString(ApiConstant.DURATION)).append(ApiConstant.GRAPH_SEPARATER);
				if (isCh1) {
					if (ApiConstant.CHANNEL_1_TOTALIZER_TYPE == type) {

						if (resultSet.getObject(ApiConstant.KONDUIT_CH_1) != null) {
							ch1Builder.append(convertTotalizer_1(resultSet.getDouble(ApiConstant.KONDUIT_CH_1)))
									.append(ApiConstant.GRAPH_SEPARATER);
						} else {
							ch1Builder.append(ApiConstant.NOT_APPLICABLE).append(ApiConstant.GRAPH_SEPARATER);
						}

					} else {
						ch1Builder
								.append(Optional.ofNullable(resultSet.getObject(ApiConstant.KONDUIT_CH_1))
										.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE))
								.append(ApiConstant.GRAPH_SEPARATER);
					}

				} else {
					if (ApiConstant.CHANNEL_2_TOTALIZER_TYPE == type) {

						if (resultSet.getObject(ApiConstant.KONDUIT_CH_2) != null) {
							ch2Builder.append(convertTotalizer_2(resultSet.getDouble(ApiConstant.KONDUIT_CH_2)))
									.append(ApiConstant.GRAPH_SEPARATER);
						} else {
							ch2Builder.append(ApiConstant.NOT_APPLICABLE).append(ApiConstant.GRAPH_SEPARATER);
						}

					} else {
						ch2Builder
								.append(Optional.ofNullable(resultSet.getObject(ApiConstant.KONDUIT_CH_2))
										.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE))
								.append(ApiConstant.GRAPH_SEPARATER);
					}

				}
			}

			categoryBuilder.deleteCharAt(categoryBuilder.length() - 1);

			if (ch1Builder.length() > 0) {
				ch1Builder.deleteCharAt(ch1Builder.length() - 1);
			}
			if (ch2Builder.length() > 0) {
				ch2Builder.deleteCharAt(ch2Builder.length() - 1);
			}

			if (ch1Builder.length() > 0) {
				jsonArray
						.put(new JSONObject()
								.put(ApiConstant.CATEGORIES,
										new JSONArray().put(
												new JSONObject().put(ApiConstant.CATEGORY, categoryBuilder.toString())))
								.put(ApiConstant.DATASET,
										new JSONArray().put(new JSONObject()
												.put(ApiConstant.SERIES_NAME, GraphConstants.KF_KONDUIT_CH_1)
												.put(ApiConstant.DATA, ch1Builder.toString()))));
			} else {
				jsonArray
						.put(new JSONObject()
								.put(ApiConstant.CATEGORIES,
										new JSONArray().put(
												new JSONObject().put(ApiConstant.CATEGORY, categoryBuilder.toString())))
								.put(ApiConstant.DATASET,
										new JSONArray().put(new JSONObject()
												.put(ApiConstant.SERIES_NAME, GraphConstants.KF_KONDUIT_CH_2)
												.put(ApiConstant.DATA, ch2Builder.toString()))));
			}

			if (gotType) {
				responseJsonArray.put(0, type);
				responseJsonArray.put(1, jsonArray);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void oldKCHTempTimePoints(ResultSet resultSet, JSONArray jsonArray, boolean isCh1,
			JSONArray responseJsonArray) {
		JSONArray valueJsonArray = null;
		boolean gotType = false;
		Integer type = null;

		try {
			valueJsonArray = new JSONArray();
			valueJsonArray.put(0,
					BasicUtility.getInstance().convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)));
			if (isCh1) {
				if (!gotType) {
					type = resultSet.getInt(ApiConstant.KONDUIT_CH_1_TYPE);
					gotType = true;
				}

				if (ApiConstant.CHANNEL_1_TOTALIZER_TYPE == type) {

					if (resultSet.getObject(ApiConstant.KONDUIT_CH_1) != null) {
						valueJsonArray.put(1,
								HistoricalConverter.convertTotalizer_1(resultSet.getDouble(ApiConstant.KONDUIT_CH_1)));
					} else {
						valueJsonArray.put(1, ApiConstant.NOT_APPLICABLE);
					}

				} else {
					valueJsonArray.put(1, Optional.ofNullable(resultSet.getObject(ApiConstant.KONDUIT_CH_1))
							.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE));
				}

			} else {
				if (!gotType) {
					type = resultSet.getInt(ApiConstant.KONDUIT_CH_2_TYPE);
					gotType = true;
				}

				if (ApiConstant.CHANNEL_2_TOTALIZER_TYPE == type) {

					if (resultSet.getObject(ApiConstant.KONDUIT_CH_2) != null) {
						valueJsonArray.put(1,
								HistoricalConverter.convertTotalizer_2(resultSet.getDouble(ApiConstant.KONDUIT_CH_2)));
					} else {
						valueJsonArray.put(1, ApiConstant.NOT_APPLICABLE);
					}

				} else {
					valueJsonArray.put(1, Optional.ofNullable(resultSet.getObject(ApiConstant.KONDUIT_CH_2))
							.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE));
				}

			}
			jsonArray.put(valueJsonArray);
			while (resultSet.next()) {
				valueJsonArray = new JSONArray();
				valueJsonArray.put(0,
						BasicUtility.getInstance().convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)));
				if (isCh1) {
					if (!gotType) {
						type = resultSet.getInt(ApiConstant.KONDUIT_CH_1_TYPE);
						gotType = true;
					}

					if (ApiConstant.CHANNEL_1_TOTALIZER_TYPE == type) {

						if (resultSet.getObject(ApiConstant.KONDUIT_CH_1) != null) {
							valueJsonArray.put(1, HistoricalConverter
									.convertTotalizer_1(resultSet.getDouble(ApiConstant.KONDUIT_CH_1)));
						} else {
							valueJsonArray.put(1, ApiConstant.NOT_APPLICABLE);
						}

					} else {
						valueJsonArray.put(1, Optional.ofNullable(resultSet.getObject(ApiConstant.KONDUIT_CH_1))
								.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE));
					}

				} else {
					if (!gotType) {
						type = resultSet.getInt(ApiConstant.KONDUIT_CH_2_TYPE);
						gotType = true;
					}

					if (ApiConstant.CHANNEL_2_TOTALIZER_TYPE == type) {

						if (resultSet.getObject(ApiConstant.KONDUIT_CH_2) != null) {
							valueJsonArray.put(1, HistoricalConverter
									.convertTotalizer_2(resultSet.getDouble(ApiConstant.KONDUIT_CH_2)));
						} else {
							valueJsonArray.put(1, ApiConstant.NOT_APPLICABLE);
						}

					} else {
						valueJsonArray.put(1, Optional.ofNullable(resultSet.getObject(ApiConstant.KONDUIT_CH_2))
								.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE));
					}

				}
				jsonArray.put(valueJsonArray);
			}
			if (gotType) {
				responseJsonArray.put(0, type);
				responseJsonArray.put(1, jsonArray);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private final JSONArray getDVCondTimeValues(final ResultSet resultSet, boolean isLive) {
		final JSONArray jsonArray = new JSONArray();
		try {
			if (resultSet != null && resultSet.next()) {
				if (isLive) {
					oldDVCondTimePoints(resultSet, jsonArray);
				} else {
					if (resultSet.getString(ApiConstant.DURATION) != null
							&& !ApiConstant.NOT_APPLICABLE.equals(resultSet.getString(ApiConstant.DURATION))) {
						newDVCondTimePoints(resultSet, jsonArray);
					} else {
						oldDVCondTimePoints(resultSet, jsonArray);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return jsonArray;
		}
		return jsonArray;
	}

	private void oldDVCondTimePoints(ResultSet resultSet, JSONArray jsonArray) {
		JSONArray valueJsonArray = null;
		try {
			valueJsonArray = new JSONArray();
			valueJsonArray.put(0,
					BasicUtility.getInstance().convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)));
			valueJsonArray.put(1, resultSet.getDouble(ApiConstant.DIAFILTRATION_VOL_1));
			valueJsonArray.put(2, resultSet.getDouble(ApiConstant.DIAFILTRATION_VOL_2));
			valueJsonArray.put(3, Optional.ofNullable(resultSet.getObject(ApiConstant.CONDUCTIVITY_1))
					.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE));
			valueJsonArray.put(4, Optional.ofNullable(resultSet.getObject(ApiConstant.CONDUCTIVITY_2))
					.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE));
			jsonArray.put(valueJsonArray);
			while (resultSet.next()) {
				valueJsonArray = new JSONArray();
				valueJsonArray.put(0,
						BasicUtility.getInstance().convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)));
				valueJsonArray.put(1, resultSet.getDouble(ApiConstant.DIAFILTRATION_VOL_1));
				valueJsonArray.put(2, resultSet.getDouble(ApiConstant.DIAFILTRATION_VOL_2));
				valueJsonArray.put(3, Optional.ofNullable(resultSet.getObject(ApiConstant.CONDUCTIVITY_1))
						.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE));
				valueJsonArray.put(4, Optional.ofNullable(resultSet.getObject(ApiConstant.CONDUCTIVITY_2))
						.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE));
				jsonArray.put(valueJsonArray);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void newDVCondTimePoints(ResultSet resultSet, JSONArray jsonArray) {
		StringBuilder categoryBuilder = new StringBuilder();
		StringBuilder dia1Builder = new StringBuilder();
		StringBuilder dia2Builder = new StringBuilder();
		StringBuilder cond1Builder = new StringBuilder();
		StringBuilder cond2Builder = new StringBuilder();

		try {
			categoryBuilder.append(resultSet.getString(ApiConstant.DURATION)).append(ApiConstant.GRAPH_SEPARATER);
			dia1Builder.append(resultSet.getDouble(ApiConstant.DIAFILTRATION_VOL_1))
					.append(ApiConstant.GRAPH_SEPARATER);
			dia2Builder.append(resultSet.getDouble(ApiConstant.DIAFILTRATION_VOL_2))
					.append(ApiConstant.GRAPH_SEPARATER);
			cond1Builder.append(Optional.ofNullable(resultSet.getObject(ApiConstant.CONDUCTIVITY_1))
					.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE)).append(ApiConstant.GRAPH_SEPARATER);
			cond2Builder.append(Optional.ofNullable(resultSet.getObject(ApiConstant.CONDUCTIVITY_2))
					.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE)).append(ApiConstant.GRAPH_SEPARATER);

			while (resultSet.next()) {
				categoryBuilder.append(resultSet.getString(ApiConstant.DURATION)).append(ApiConstant.GRAPH_SEPARATER);
				dia1Builder.append(resultSet.getDouble(ApiConstant.DIAFILTRATION_VOL_1))
						.append(ApiConstant.GRAPH_SEPARATER);
				dia2Builder.append(resultSet.getDouble(ApiConstant.DIAFILTRATION_VOL_2))
						.append(ApiConstant.GRAPH_SEPARATER);
				cond1Builder
						.append(Optional.ofNullable(resultSet.getObject(ApiConstant.CONDUCTIVITY_1))
								.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE))
						.append(ApiConstant.GRAPH_SEPARATER);
				cond2Builder
						.append(Optional.ofNullable(resultSet.getObject(ApiConstant.CONDUCTIVITY_2))
								.map(Object::toString).orElse(ApiConstant.NOT_APPLICABLE))
						.append(ApiConstant.GRAPH_SEPARATER);
			}
			categoryBuilder.deleteCharAt(categoryBuilder.length() - 1);
			if (dia1Builder.length() > 0) {
				dia1Builder.deleteCharAt(dia1Builder.length() - 1);
			}
			if (dia2Builder.length() > 0) {
				dia2Builder.deleteCharAt(dia2Builder.length() - 1);
			}
			if (cond1Builder.length() > 0) {
				cond1Builder.deleteCharAt(cond1Builder.length() - 1);
			}
			if (cond2Builder.length() > 0) {
				cond2Builder.deleteCharAt(cond2Builder.length() - 1);
			}

			jsonArray.put(new JSONObject()
					.put(ApiConstant.CATEGORIES,
							new JSONArray().put(new JSONObject().put(ApiConstant.CATEGORY, categoryBuilder.toString())))
					.put(ApiConstant.DATASET, new JSONArray()
							.put(new JSONObject().put(ApiConstant.SERIES_NAME, GraphConstants.DIAFILTRATION_VOLUME_1)
									.put(ApiConstant.DATA, dia1Builder.toString()))
							.put(new JSONObject().put(ApiConstant.SERIES_NAME, GraphConstants.DIAFILTRATION_VOLUME_2)
									.put(ApiConstant.DATA, dia2Builder.toString()))
							.put(new JSONObject().put(ApiConstant.SERIES_NAME, GraphConstants.CONDUCTIVITY_1)
									.put(ApiConstant.DATA, cond1Builder.toString()))
							.put(new JSONObject().put(ApiConstant.SERIES_NAME, GraphConstants.CONDUCTIVITY_2)
									.put(ApiConstant.DATA, cond2Builder.toString()))));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private final JSONArray getPressureCFVTValues(final ResultSet resultSet) {
		final JSONArray jsonArray = new JSONArray();
		StringBuilder categoryBuilder = null;
		StringBuilder feedBuilder = null;
		StringBuilder permBuilder = null;
		StringBuilder retBuilder = null;
		StringBuilder tmpBuilder = null;
		StringBuilder deltaPBuilder = null;
		StringBuilder cfBuilder = null;

		try {
			if (resultSet != null) {
				categoryBuilder = new StringBuilder();
				feedBuilder = new StringBuilder();
				permBuilder = new StringBuilder();
				retBuilder = new StringBuilder();
				tmpBuilder = new StringBuilder();
				deltaPBuilder = new StringBuilder();
				cfBuilder = new StringBuilder();

				while (resultSet.next()) {
					categoryBuilder.append(resultSet.getDouble(ApiConstant.VT)).append(ApiConstant.GRAPH_SEPARATER);
					feedBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.FEED_PRESSURE)))
							.append(ApiConstant.GRAPH_SEPARATER);
					permBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.PERMEATE_PRESSURE)))
							.append(ApiConstant.GRAPH_SEPARATER);
					retBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.RETENTATE_PRESSURE)))
							.append(ApiConstant.GRAPH_SEPARATER);
					tmpBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.TMP)))
							.append(ApiConstant.GRAPH_SEPARATER);
					deltaPBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.DELTA_P)))
							.append(ApiConstant.GRAPH_SEPARATER);
					cfBuilder.append(resultSet.getDouble(ApiConstant.COLUMN_CONCENTRATION_FACTOR))
							.append(ApiConstant.GRAPH_SEPARATER);
				}
				categoryBuilder.deleteCharAt(categoryBuilder.length() - 1);
				feedBuilder.deleteCharAt(feedBuilder.length() - 1);
				permBuilder.deleteCharAt(permBuilder.length() - 1);
				retBuilder.deleteCharAt(retBuilder.length() - 1);
				tmpBuilder.deleteCharAt(tmpBuilder.length() - 1);
				deltaPBuilder.deleteCharAt(deltaPBuilder.length() - 1);
				cfBuilder.deleteCharAt(cfBuilder.length() - 1);

				jsonArray.put(new JSONObject()
						.put(ApiConstant.CATEGORIES,
								new JSONArray()
										.put(new JSONObject().put(ApiConstant.CATEGORY, categoryBuilder.toString())))
						.put(ApiConstant.DATASET, new JSONArray()
								.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.FEED)
										.put(ApiConstant.DATA, feedBuilder.toString()))
								.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.PERMEATE)
										.put(ApiConstant.DATA, permBuilder.toString()))
								.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.RETENTATE)
										.put(ApiConstant.DATA, retBuilder.toString()))
								.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.TMP)
										.put(ApiConstant.DATA, tmpBuilder.toString()))
								.put(new JSONObject().put(ApiConstant.SERIES_NAME, GraphConstants.DELTA_P)
										.put(ApiConstant.DATA, deltaPBuilder.toString()))
								.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.CONCENTRATION_FACTOR)
										.put(ApiConstant.DATA, cfBuilder.toString()))));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return jsonArray;
		}
		return jsonArray;
	}

	private final JSONArray getFluxTMPValues(final ResultSet resultSet, final int trialRunSettingId) {
		final JSONArray jsonArray = new JSONArray();
		final JSONArray flowRate1 = new JSONArray();
		final JSONArray flowRate2 = new JSONArray();
		final JSONArray flowRate3 = new JSONArray();
		try {
			if (resultSet != null) {

				String modeName = BasicUtility.getInstance().getModeName(trialRunSettingId);
				if (modeName != null) {
					while (resultSet.next()) {
						if (resultSet.getInt(ApiConstant.FLOW_RATE_COUNT) == 1) {
							JSONObject jsonObject = new JSONObject();
							jsonObject.put(ApiConstant.X_AXIS, convertPressure(resultSet.getDouble(ApiConstant.TMP)));
							if (ApiConstant.NWP.equalsIgnoreCase(modeName)) {
								jsonObject.put(ApiConstant.Y_AXIS,
										resultSet.getString(ApiConstant.WATER_FLUX_20_DEGREE));
							} else {
								jsonObject.put(ApiConstant.Y_AXIS, resultSet.getDouble(ApiConstant.FLUX));
							}
							flowRate1.put(jsonObject);
						} else if (resultSet.getInt(ApiConstant.FLOW_RATE_COUNT) == 2) {
							flowRate2.put(
									new JSONObject().put(ApiConstant.Y_AXIS, resultSet.getDouble(ApiConstant.FLUX)).put(
											ApiConstant.X_AXIS, convertPressure(resultSet.getDouble(ApiConstant.TMP))));
						} else if (resultSet.getInt(ApiConstant.FLOW_RATE_COUNT) == 3) {
							flowRate3.put(
									new JSONObject().put(ApiConstant.Y_AXIS, resultSet.getDouble(ApiConstant.FLUX)).put(
											ApiConstant.X_AXIS, convertPressure(resultSet.getDouble(ApiConstant.TMP))));
						}
					}
					jsonArray.put(flowRate1).put(flowRate2).put(flowRate3);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return jsonArray;
		}
		return jsonArray;
	}

	private final JSONArray getFluxCFValues(final ResultSet resultSet) {
		final JSONArray jsonArray = new JSONArray();
		StringBuilder categoryBuilder = null;
		StringBuilder dataBuilder = null;
		try {
			if (resultSet != null) {
				categoryBuilder = new StringBuilder();
				dataBuilder = new StringBuilder();
				while (resultSet.next()) {
					categoryBuilder.append(resultSet.getDouble(ApiConstant.COLUMN_CONCENTRATION_FACTOR))
							.append(ApiConstant.GRAPH_SEPARATER);
					dataBuilder.append(resultSet.getDouble(ApiConstant.FLUX)).append(ApiConstant.GRAPH_SEPARATER);
				}
				categoryBuilder.deleteCharAt(categoryBuilder.length() - 1);
				dataBuilder.deleteCharAt(dataBuilder.length() - 1);

				jsonArray.put(new JSONObject()
						.put(ApiConstant.CATEGORIES,
								new JSONArray()
										.put(new JSONObject().put(ApiConstant.CATEGORY, categoryBuilder.toString())))
						.put(ApiConstant.DATASET,
								new JSONArray().put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.FLUX)
										.put(ApiConstant.DATA, dataBuilder.toString()))));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return jsonArray;
		}
		return jsonArray;
	}

	private final JSONArray getPressureCFTimeValues(final ResultSet resultSet, boolean isLive) {
		final JSONArray jsonArray = new JSONArray();
		JSONArray valueJsonArray = null;
		try {
			if (resultSet != null && resultSet.next()) {
				if (isLive) {
					oldPressureCFTimePoints(resultSet, valueJsonArray, jsonArray);
				} else {
					if (resultSet.getString(ApiConstant.DURATION) != null
							&& !ApiConstant.NOT_APPLICABLE.equals(resultSet.getString(ApiConstant.DURATION))) {
						newPressureCFTimePoints(resultSet, jsonArray);
					} else {
						oldPressureCFTimePoints(resultSet, valueJsonArray, jsonArray);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return jsonArray;
		}
		return jsonArray;
	}

	private void newPressureCFTimePoints(ResultSet resultSet, JSONArray jsonArray) {
		StringBuilder categoryBuilder = new StringBuilder();
		StringBuilder feedBuilder = new StringBuilder();
		StringBuilder permBuilder = new StringBuilder();
		StringBuilder retBuilder = new StringBuilder();
		StringBuilder tmpBuilder = new StringBuilder();
		StringBuilder deltaPBuilder = new StringBuilder();
		StringBuilder cfBuilder = new StringBuilder();

		try {
			categoryBuilder.append(resultSet.getString(ApiConstant.DURATION)).append(ApiConstant.GRAPH_SEPARATER);
			feedBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.FEED_PRESSURE)))
					.append(ApiConstant.GRAPH_SEPARATER);
			permBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.PERMEATE_PRESSURE)))
					.append(ApiConstant.GRAPH_SEPARATER);
			retBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.RETENTATE_PRESSURE)))
					.append(ApiConstant.GRAPH_SEPARATER);
			tmpBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.TMP)))
					.append(ApiConstant.GRAPH_SEPARATER);
			deltaPBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.DELTA_P)))
					.append(ApiConstant.GRAPH_SEPARATER);
			cfBuilder.append(resultSet.getDouble(ApiConstant.COLUMN_CONCENTRATION_FACTOR))
					.append(ApiConstant.GRAPH_SEPARATER);

			while (resultSet.next()) {
				categoryBuilder.append(resultSet.getString(ApiConstant.DURATION)).append(ApiConstant.GRAPH_SEPARATER);
				feedBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.FEED_PRESSURE)))
						.append(ApiConstant.GRAPH_SEPARATER);
				permBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.PERMEATE_PRESSURE)))
						.append(ApiConstant.GRAPH_SEPARATER);
				retBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.RETENTATE_PRESSURE)))
						.append(ApiConstant.GRAPH_SEPARATER);
				tmpBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.TMP)))
						.append(ApiConstant.GRAPH_SEPARATER);
				deltaPBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.DELTA_P)))
						.append(ApiConstant.GRAPH_SEPARATER);
				cfBuilder.append(resultSet.getDouble(ApiConstant.COLUMN_CONCENTRATION_FACTOR))
						.append(ApiConstant.GRAPH_SEPARATER);
			}
			categoryBuilder.deleteCharAt(categoryBuilder.length() - 1);
			feedBuilder.deleteCharAt(feedBuilder.length() - 1);
			permBuilder.deleteCharAt(permBuilder.length() - 1);
			retBuilder.deleteCharAt(retBuilder.length() - 1);
			tmpBuilder.deleteCharAt(tmpBuilder.length() - 1);
			deltaPBuilder.deleteCharAt(deltaPBuilder.length() - 1);
			cfBuilder.deleteCharAt(cfBuilder.length() - 1);

			jsonArray
					.put(new JSONObject()
							.put(ApiConstant.CATEGORIES,
									new JSONArray().put(
											new JSONObject().put(ApiConstant.CATEGORY, categoryBuilder.toString())))
							.put(ApiConstant.DATASET, new JSONArray()
									.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.FEED)
											.put(ApiConstant.DATA, feedBuilder.toString()))
									.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.PERMEATE)
											.put(ApiConstant.DATA, permBuilder.toString()))
									.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.RETENTATE)
											.put(ApiConstant.DATA, retBuilder.toString()))
									.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.TMP)
											.put(ApiConstant.DATA, tmpBuilder.toString()))
									.put(new JSONObject().put(ApiConstant.SERIES_NAME, GraphConstants.DELTA_P)
											.put(ApiConstant.DATA, deltaPBuilder.toString()))
									.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.CONCENTRATION_FACTOR)
											.put(ApiConstant.DATA, cfBuilder.toString()))));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void oldPressureCFTimePoints(ResultSet resultSet, JSONArray valueJsonArray, JSONArray jsonArray) {
		try {
			valueJsonArray = new JSONArray();
			valueJsonArray.put(0,
					BasicUtility.getInstance().convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)));
			valueJsonArray.put(1, convertPressure(resultSet.getDouble(ApiConstant.FEED_PRESSURE)));
			valueJsonArray.put(2, convertPressure(resultSet.getDouble(ApiConstant.PERMEATE_PRESSURE)));
			valueJsonArray.put(3, convertPressure(resultSet.getDouble(ApiConstant.RETENTATE_PRESSURE)));
			valueJsonArray.put(4, convertPressure(resultSet.getDouble(ApiConstant.TMP)));
			valueJsonArray.put(5, convertPressure(resultSet.getDouble(ApiConstant.DELTA_P)));
			valueJsonArray.put(6, resultSet.getDouble(ApiConstant.COLUMN_CONCENTRATION_FACTOR));
			jsonArray.put(valueJsonArray);
			while (resultSet.next()) {
				valueJsonArray = new JSONArray();
				valueJsonArray.put(0,
						BasicUtility.getInstance().convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)));
				valueJsonArray.put(1, convertPressure(resultSet.getDouble(ApiConstant.FEED_PRESSURE)));
				valueJsonArray.put(2, convertPressure(resultSet.getDouble(ApiConstant.PERMEATE_PRESSURE)));
				valueJsonArray.put(3, convertPressure(resultSet.getDouble(ApiConstant.RETENTATE_PRESSURE)));
				valueJsonArray.put(4, convertPressure(resultSet.getDouble(ApiConstant.TMP)));
				valueJsonArray.put(5, convertPressure(resultSet.getDouble(ApiConstant.DELTA_P)));
				valueJsonArray.put(6, resultSet.getDouble(ApiConstant.COLUMN_CONCENTRATION_FACTOR));
				jsonArray.put(valueJsonArray);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private final JSONArray getFluxTimeValues(final ResultSet resultSet, boolean isLive) {
		final JSONArray jsonArray = new JSONArray();
		JSONArray valueJsonArray = null;
		try {
			if (resultSet != null && resultSet.next()) {
				if (isLive) {
					oldFluxTimePoints(resultSet, valueJsonArray, jsonArray);
				} else {
					if (resultSet.getString(ApiConstant.DURATION) != null
							&& !ApiConstant.NOT_APPLICABLE.equals(resultSet.getString(ApiConstant.DURATION))) {
						newFluxTimePoints(resultSet, jsonArray);
					} else {
						oldFluxTimePoints(resultSet, valueJsonArray, jsonArray);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return jsonArray;
		}
		return jsonArray;
	}

	private void newFluxTimePoints(ResultSet resultSet, JSONArray jsonArray) {
		StringBuilder categoryBuilder = new StringBuilder();
		StringBuilder fluxBuilder = new StringBuilder();
		try {
			categoryBuilder.append(resultSet.getString(ApiConstant.DURATION)).append(ApiConstant.GRAPH_SEPARATER);
			fluxBuilder.append(resultSet.getDouble(ApiConstant.FLUX)).append(ApiConstant.GRAPH_SEPARATER);

			while (resultSet.next()) {
				categoryBuilder.append(resultSet.getString(ApiConstant.DURATION)).append(ApiConstant.GRAPH_SEPARATER);
				fluxBuilder.append(resultSet.getDouble(ApiConstant.FLUX)).append(ApiConstant.GRAPH_SEPARATER);
			}
			categoryBuilder.deleteCharAt(categoryBuilder.length() - 1);
			fluxBuilder.deleteCharAt(fluxBuilder.length() - 1);

			jsonArray
					.put(new JSONObject()
							.put(ApiConstant.CATEGORIES,
									new JSONArray().put(
											new JSONObject().put(ApiConstant.CATEGORY, categoryBuilder.toString())))
							.put(ApiConstant.DATASET,
									new JSONArray().put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.FLUX)
											.put(ApiConstant.DATA, fluxBuilder.toString()))));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void oldFluxTimePoints(ResultSet resultSet, JSONArray valueJsonArray, JSONArray jsonArray) {
		try {
			valueJsonArray = new JSONArray();
			valueJsonArray.put(0,
					BasicUtility.getInstance().convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)));
			valueJsonArray.put(1, resultSet.getDouble(ApiConstant.FLUX));
			jsonArray.put(valueJsonArray);
			while (resultSet.next()) {
				valueJsonArray = new JSONArray();
				valueJsonArray.put(0,
						BasicUtility.getInstance().convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)));
				valueJsonArray.put(1, resultSet.getDouble(ApiConstant.FLUX));
				jsonArray.put(valueJsonArray);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private final JSONArray getPressureTimeValues(final ResultSet resultSet, boolean isLive) {
		final JSONArray responseArray = new JSONArray();
		try {

			if (resultSet != null && resultSet.next()) {
				if (isLive) {
					oldPressureTimePoints(resultSet, responseArray);
				} else {
					if (resultSet.getString(ApiConstant.DURATION) != null
							&& !ApiConstant.NOT_APPLICABLE.equals(resultSet.getString(ApiConstant.DURATION))) {
						newPressureTimePoints(resultSet, responseArray);
					} else {
						oldPressureTimePoints(resultSet, responseArray);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return responseArray;
		}
		return responseArray;
	}

	private void oldPressureTimePoints(ResultSet resultSet, JSONArray responseArray) {
		try {
			responseArray
					.put(new JSONArray()
							.put(0, BasicUtility.getInstance()
									.convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)))
							.put(1, GraphConstants.FEED)
							.put(2, convertPressure(resultSet.getDouble(ApiConstant.FEED_PRESSURE))))
					.put(new JSONArray()
							.put(0, BasicUtility.getInstance()
									.convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)))
							.put(1, GraphConstants.PERMEATE)
							.put(2, convertPressure(resultSet.getDouble(ApiConstant.PERMEATE_PRESSURE))))
					.put(new JSONArray()
							.put(0, BasicUtility.getInstance()
									.convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)))
							.put(1, GraphConstants.RETENTATE)
							.put(2, convertPressure(resultSet.getDouble(ApiConstant.RETENTATE_PRESSURE))))
					.put(new JSONArray().put(0,
							BasicUtility.getInstance().convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)))
							.put(1, GraphConstants.TMP).put(2, convertPressure(resultSet.getDouble(ApiConstant.TMP))))
					.put(new JSONArray()
							.put(0, BasicUtility.getInstance()
									.convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)))
							.put(1, GraphConstants.DELTA_P)
							.put(2, convertPressure(resultSet.getDouble(ApiConstant.DELTA_P))));
			while (resultSet.next()) {
				responseArray
						.put(new JSONArray().put(0,
								BasicUtility.getInstance().convertUnixToDateGraph(
										resultSet.getInt(ApiConstant.TIMESTAMP)))
								.put(1, GraphConstants.FEED)
								.put(2, convertPressure(resultSet.getDouble(ApiConstant.FEED_PRESSURE))))
						.put(new JSONArray()
								.put(0, BasicUtility.getInstance()
										.convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)))
								.put(1, GraphConstants.PERMEATE)
								.put(2, convertPressure(resultSet.getDouble(ApiConstant.PERMEATE_PRESSURE))))
						.put(new JSONArray()
								.put(0, BasicUtility.getInstance()
										.convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)))
								.put(1, GraphConstants.RETENTATE)
								.put(2, convertPressure(resultSet.getDouble(ApiConstant.RETENTATE_PRESSURE))))
						.put(new JSONArray().put(0,
								BasicUtility.getInstance().convertUnixToDateGraph(
										resultSet.getInt(ApiConstant.TIMESTAMP)))
								.put(1, GraphConstants.TMP)
								.put(2, convertPressure(resultSet.getDouble(ApiConstant.TMP))))
						.put(new JSONArray()
								.put(0, BasicUtility.getInstance()
										.convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)))
								.put(1, GraphConstants.DELTA_P)
								.put(2, convertPressure(resultSet.getDouble(ApiConstant.DELTA_P))));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void newPressureTimePoints(ResultSet resultSet, JSONArray responseArray) {
		StringBuilder categoryBuilder = new StringBuilder();
		StringBuilder feedBuilder = new StringBuilder();
		StringBuilder permBuilder = new StringBuilder();
		StringBuilder retBuilder = new StringBuilder();
		StringBuilder tmpBuilder = new StringBuilder();
		StringBuilder deltaPBuilder = new StringBuilder();

		try {
			categoryBuilder.append(resultSet.getString(ApiConstant.DURATION)).append(ApiConstant.GRAPH_SEPARATER);
			feedBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.FEED_PRESSURE)))
					.append(ApiConstant.GRAPH_SEPARATER);
			permBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.PERMEATE_PRESSURE)))
					.append(ApiConstant.GRAPH_SEPARATER);
			retBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.RETENTATE_PRESSURE)))
					.append(ApiConstant.GRAPH_SEPARATER);
			tmpBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.TMP)))
					.append(ApiConstant.GRAPH_SEPARATER);
			deltaPBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.DELTA_P)))
					.append(ApiConstant.GRAPH_SEPARATER);

			while (resultSet.next()) {
				categoryBuilder.append(resultSet.getString(ApiConstant.DURATION)).append(ApiConstant.GRAPH_SEPARATER);
				feedBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.FEED_PRESSURE)))
						.append(ApiConstant.GRAPH_SEPARATER);
				permBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.PERMEATE_PRESSURE)))
						.append(ApiConstant.GRAPH_SEPARATER);
				retBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.RETENTATE_PRESSURE)))
						.append(ApiConstant.GRAPH_SEPARATER);
				tmpBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.TMP)))
						.append(ApiConstant.GRAPH_SEPARATER);
				deltaPBuilder.append(convertPressure(resultSet.getDouble(ApiConstant.DELTA_P)))
						.append(ApiConstant.GRAPH_SEPARATER);
			}
			categoryBuilder.deleteCharAt(categoryBuilder.length() - 1);
			feedBuilder.deleteCharAt(feedBuilder.length() - 1);
			permBuilder.deleteCharAt(permBuilder.length() - 1);
			retBuilder.deleteCharAt(retBuilder.length() - 1);
			tmpBuilder.deleteCharAt(tmpBuilder.length() - 1);
			deltaPBuilder.deleteCharAt(deltaPBuilder.length() - 1);

			responseArray
					.put(new JSONObject()
							.put(ApiConstant.CATEGORIES,
									new JSONArray().put(
											new JSONObject().put(ApiConstant.CATEGORY, categoryBuilder.toString())))
							.put(ApiConstant.DATASET,
									new JSONArray()
											.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.FEED)
													.put(ApiConstant.DATA, feedBuilder.toString()))
											.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.PERMEATE)
													.put(ApiConstant.DATA, permBuilder.toString()))
											.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.RETENTATE)
													.put(ApiConstant.DATA, retBuilder.toString()))
											.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.TMP)
													.put(ApiConstant.DATA, tmpBuilder.toString()))
											.put(new JSONObject().put(ApiConstant.SERIES_NAME, GraphConstants.DELTA_P)
													.put(ApiConstant.DATA, deltaPBuilder.toString()))));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private final JSONArray getFlowRateTimeValues(final ResultSet resultSet, final int trialRunSettingId,
			boolean isLive) {
		final JSONArray responseArray = new JSONArray();
		try {
			if (resultSet != null && resultSet.next()) {
				if (isLive) {
					oldFlowRateTimePoints(resultSet, responseArray);
				} else {
					if (resultSet.getString(ApiConstant.DURATION) != null
							&& !ApiConstant.NOT_APPLICABLE.equals(resultSet.getString(ApiConstant.DURATION))) {
						newFlowRateTimePoints(resultSet, responseArray);
					} else {
						oldFlowRateTimePoints(resultSet, responseArray);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return responseArray;
		}
		return responseArray;
	}

	private void newFlowRateTimePoints(ResultSet resultSet, JSONArray responseArray) {
		StringBuilder categoryBuilder = new StringBuilder();
		StringBuilder feedBuilder = new StringBuilder();
		StringBuilder permBuilder = new StringBuilder();
		StringBuilder retBuilder = new StringBuilder();

		try {
			categoryBuilder.append(resultSet.getString(ApiConstant.DURATION)).append(ApiConstant.GRAPH_SEPARATER);
			feedBuilder.append(convertFlowRate(resultSet.getDouble(ApiConstant.FEED_FLOW_RATE)))
					.append(ApiConstant.GRAPH_SEPARATER);
			permBuilder.append(convertFlowRate(resultSet.getDouble(ApiConstant.PERMEATE_FLOW_RATE)))
					.append(ApiConstant.GRAPH_SEPARATER);
			retBuilder.append(convertFlowRate(resultSet.getDouble(ApiConstant.RETENTATE_FLOW_RATE)))
					.append(ApiConstant.GRAPH_SEPARATER);

			while (resultSet.next()) {
				categoryBuilder.append(resultSet.getString(ApiConstant.DURATION)).append(ApiConstant.GRAPH_SEPARATER);
				feedBuilder.append(convertFlowRate(resultSet.getDouble(ApiConstant.FEED_FLOW_RATE)))
						.append(ApiConstant.GRAPH_SEPARATER);
				permBuilder.append(convertFlowRate(resultSet.getDouble(ApiConstant.PERMEATE_FLOW_RATE)))
						.append(ApiConstant.GRAPH_SEPARATER);
				retBuilder.append(convertFlowRate(resultSet.getDouble(ApiConstant.RETENTATE_FLOW_RATE)))
						.append(ApiConstant.GRAPH_SEPARATER);
			}
			categoryBuilder.deleteCharAt(categoryBuilder.length() - 1);
			feedBuilder.deleteCharAt(feedBuilder.length() - 1);
			permBuilder.deleteCharAt(permBuilder.length() - 1);
			retBuilder.deleteCharAt(retBuilder.length() - 1);

			responseArray
					.put(new JSONObject()
							.put(ApiConstant.CATEGORIES,
									new JSONArray().put(
											new JSONObject().put(ApiConstant.CATEGORY, categoryBuilder.toString())))
							.put(ApiConstant.DATASET,
									new JSONArray()
											.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.FEED)
													.put(ApiConstant.DATA, feedBuilder.toString()))
											.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.PERMEATE)
													.put(ApiConstant.DATA, permBuilder.toString()))
											.put(new JSONObject().put(ApiConstant.SERIES_NAME, ApiConstant.RETENTATE)
													.put(ApiConstant.DATA, retBuilder.toString()))));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void oldFlowRateTimePoints(ResultSet resultSet, JSONArray responseArray) {
		try {
			responseArray.put(new JSONArray()
					.put(0, BasicUtility.getInstance().convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)))
					.put(1, GraphConstants.FEED).put(2,
							convertFlowRate(resultSet.getDouble(ApiConstant.FEED_FLOW_RATE))))
					.put(new JSONArray()
							.put(0, BasicUtility.getInstance()
									.convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)))
							.put(1, GraphConstants.PERMEATE)
							.put(2, convertFlowRate(resultSet.getDouble(ApiConstant.PERMEATE_FLOW_RATE))))
					.put(new JSONArray()
							.put(0, BasicUtility.getInstance()
									.convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)))
							.put(1, GraphConstants.RETENTATE)
							.put(2, convertFlowRate(resultSet.getDouble(ApiConstant.RETENTATE_FLOW_RATE))));
			while (resultSet.next()) {
				responseArray
						.put(new JSONArray().put(0,
								BasicUtility.getInstance().convertUnixToDateGraph(
										resultSet.getInt(ApiConstant.TIMESTAMP)))
								.put(1, GraphConstants.FEED)
								.put(2, convertFlowRate(resultSet.getDouble(ApiConstant.FEED_FLOW_RATE))))
						.put(new JSONArray()
								.put(0, BasicUtility.getInstance()
										.convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)))
								.put(1, GraphConstants.PERMEATE)
								.put(2, convertFlowRate(resultSet.getDouble(ApiConstant.PERMEATE_FLOW_RATE))))
						.put(new JSONArray()
								.put(0, BasicUtility.getInstance()
										.convertUnixToDateGraph(resultSet.getInt(ApiConstant.TIMESTAMP)))
								.put(1, GraphConstants.RETENTATE)
								.put(2, convertFlowRate(resultSet.getDouble(ApiConstant.RETENTATE_FLOW_RATE))));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public final JSONArray fetchHistoricalData(final int trialRunSettingId, final long currentTimestamp)
			throws Exception {
		String query = GraphUtil.getInstance().getHistoricalDataQuery(trialRunSettingId, currentTimestamp);
		if (query == null) {
			return new JSONArray();
		}
		try (final Connection con = DbConnectionManager.getInstance().getConnection();
				final PreparedStatement fetchGraphDataStmt = con.prepareStatement(query);) {

			fetchGraphDataStmt.setInt(1, trialRunSettingId);

			try (final ResultSet resultSet = fetchGraphDataStmt.executeQuery()) {
				return getJSONArrayForHistorical(resultSet);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	private final JSONArray getJSONArrayForHistorical(final ResultSet resultSet) throws SQLException {
		final JSONArray jsonArray = new JSONArray();

		if (resultSet != null) {
			while (resultSet.next()) {
				JSONObject liveDataJson = GraphUtil.getInstance().resultSetToJSON(resultSet);
				if (!liveDataJson.isEmpty()) {
					jsonArray.put(GraphMapper.getGraphData(liveDataJson));
				}
			}
		}
		return jsonArray;
	}

}