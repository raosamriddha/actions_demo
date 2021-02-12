package org.lattice.spectrum_backend_final.dao.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.AlarmHistory;
import org.lattice.spectrum_backend_final.beans.TrialHistory;
import org.lattice.spectrum_backend_final.beans.TrialTableLogs;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.QueryConstant;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.HistoricalMapper;

import com.lattice.spectrum.ComLibrary.utility.sLog;

public final class HistoricalManager {

	public final JSONObject fetchTrialTableLogs(final int trialRunSettingId, final int direction, int pageSize,
			final int interval, final int pageCount, final long timeStamp, long offset) {
		Connection con = null;
		PreparedStatement fetchBeforePauseTrialStmt = null;
		ResultSet beforePauseResultSet = null;
		final List<TrialTableLogs> trialLogsTableList = new ArrayList<>(pageSize);
		final JSONObject jsonObject = new JSONObject();
		long startTime = 0;
		long endTime = 0;
		final List<Long> queryTimeStamps = new ArrayList<>(pageSize);
		try {

			if (direction == 1) {
				// get trial start timestamp.
				startTime = getTrialTimestamp(trialRunSettingId, offset, true);
			} else {
				// get trial end timestamp.
				endTime = getTrialTimestamp(trialRunSettingId, offset, false);
			}

			if (offset <= 0) {
				// fetch first result and add to trial logs list and assign timestamp to offset
				offset = addFirstResultToList(trialRunSettingId, trialLogsTableList, timeStamp);
				queryTimeStamps.add(offset);
				pageSize -= 1;
			}

			// get remaining trial logs
			final StringBuilder stringBuilder = new StringBuilder();
			int index = 0;
			if ((direction == 1) && ((offset - interval) >= startTime)) {
				stringBuilder.append(QueryConstant.GET_TRIAL_DETAILS).append(trialRunSettingId)
						.append(QueryConstant.AND_TIMESTAMP_IN);
				while ((index < pageSize) && ((offset - interval) >= startTime)) {

					offset = BasicUtility.getInstance().manipulateTimeStamp(offset, interval, false);
					queryTimeStamps.add(offset);
					stringBuilder.append(offset).append(ApiConstant.COMMA);
					index++;
				}
			} else if ((direction == 0) && ((offset + interval) <= endTime)) {
				stringBuilder.append(QueryConstant.GET_TRIAL_DETAILS).append(trialRunSettingId)
						.append(QueryConstant.AND_TIMESTAMP_IN);
				while ((index < pageSize) && ((offset + interval) <= endTime)) {

					offset = BasicUtility.getInstance().manipulateTimeStamp(offset, interval, true);
					queryTimeStamps.add(offset);
					stringBuilder.append(offset).append(ApiConstant.COMMA);
					index++;
				}
			}
			if (index > 0) {
				stringBuilder.deleteCharAt(stringBuilder.length() - 1);
				stringBuilder.append(QueryConstant.CLOSING_BRACKET)
						.append(QueryConstant.ORDER_BY_TRAIL_LIVE_DATA_ID_DESC);
				addRemainingTrialTableLogs(stringBuilder, trialLogsTableList);
			}

			// dummy object creation if timestamps in queryTimeStamps is not equals to
			// lastTimeStamp in trialLogsTableList

			con = DbConnectionManager.getInstance().getConnection();

			Map<Integer, Long> pauseTrialLiveIdTimeMap = null;
			Map<Long, Long> pauseIntervalMap = null;

			fetchBeforePauseTrialStmt = con.prepareStatement(QueryConstant.FETCH_TIMESTAMP_BEFORE_PAUSE);
			fetchBeforePauseTrialStmt.setInt(1, trialRunSettingId);

			beforePauseResultSet = fetchBeforePauseTrialStmt.executeQuery();
			if (beforePauseResultSet.next()) {
				pauseTrialLiveIdTimeMap = new HashMap<Integer, Long>();
				pauseTrialLiveIdTimeMap.put(beforePauseResultSet.getInt(ApiConstant.TRIAL_LIVE_DATA_ID),
						beforePauseResultSet.getLong(ApiConstant.TIMESTAMP));
				while (beforePauseResultSet.next()) {
					pauseTrialLiveIdTimeMap.put(beforePauseResultSet.getInt(ApiConstant.TRIAL_LIVE_DATA_ID),
							beforePauseResultSet.getLong(ApiConstant.TIMESTAMP));
				}
			}

			beforePauseResultSet.close();
			fetchBeforePauseTrialStmt.close();

			if (pauseTrialLiveIdTimeMap != null) {

				pauseIntervalMap = new HashMap<Long, Long>();

				for (Entry<Integer, Long> entry : pauseTrialLiveIdTimeMap.entrySet()) {

					fetchBeforePauseTrialStmt = con.prepareStatement(QueryConstant.GET_TIMESTAMP_WHERE_PAUSE_EQUALS_0);
					fetchBeforePauseTrialStmt.setInt(1, trialRunSettingId);
					fetchBeforePauseTrialStmt.setLong(2, entry.getKey());

					beforePauseResultSet = fetchBeforePauseTrialStmt.executeQuery();
					if (beforePauseResultSet.next()) {
						pauseIntervalMap.put(entry.getValue(), beforePauseResultSet.getLong(1));
					}

					beforePauseResultSet.close();
					fetchBeforePauseTrialStmt.close();
				}
			}

			long queryStartTime = 0;

			if (!trialLogsTableList.isEmpty() && !queryTimeStamps.isEmpty()) {
				queryStartTime = queryTimeStamps.get(queryTimeStamps.size() - 1);
				for (TrialTableLogs currentTableLog : trialLogsTableList) {
					if (currentTableLog.getIsPaused() == 1) {
						currentTableLog.setIsPaused(0);
					}
					queryTimeStamps.remove(
							BasicUtility.getInstance().convertDateToUnix(currentTableLog.getTimestamp()) / 1000);
				}
				fillEmptyData(trialLogsTableList, queryTimeStamps, pauseIntervalMap, queryStartTime);
			} else {
				fillEmptyData(trialLogsTableList, queryTimeStamps, pauseIntervalMap, queryStartTime);
			}

			// get total page count if required
			if (pageCount == 1) {
				jsonObject.put(ApiConstant.TOTAL_PAGES, getTrialTableCount(interval, trialRunSettingId, direction));
			} else {
				jsonObject.put(ApiConstant.TOTAL_PAGES, 0);
			}

			// remove duplicates if list size exceeds 60
			if (trialLogsTableList.size() > 60) {
				jsonObject.put(ApiConstant.TRIAL_TABLE_LOGS, getDistinctTableLogs(trialLogsTableList));
			} else {
				jsonObject.put(ApiConstant.TRIAL_TABLE_LOGS, trialLogsTableList);
			}

			return jsonObject;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(beforePauseResultSet, fetchBeforePauseTrialStmt, con);
		}
	}

	private final long addFirstResultToList(final int trialRunSettingId, final List<TrialTableLogs> trialLogsTableList,
			final long timeStamp) throws Exception {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			con = DbConnectionManager.getInstance().getConnection();
			preparedStatement = con.prepareStatement(QueryConstant.FETCH_TABLE_LOGS_FIRST_ROW_TIMESTAMP);
			preparedStatement.setInt(1, trialRunSettingId);
			preparedStatement.setLong(2, timeStamp);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				trialLogsTableList.add(HistoricalMapper.trialTableLogsMapper(resultSet));
				return resultSet.getLong(ApiConstant.TIMESTAMP);
			}
			return 0;
		} catch (Exception e) {
			throw e;
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, preparedStatement, con);
		}
	}

	private void addRemainingTrialTableLogs(final StringBuilder stringBuilder,
			final List<TrialTableLogs> trialLogsTableList) throws Exception {
		try {
			Connection con = null;
			PreparedStatement preparedStatement = null;
			ResultSet resultSet = null;
			try {
				con = DbConnectionManager.getInstance().getConnection();
				preparedStatement = con.prepareStatement(stringBuilder.toString());
				resultSet = preparedStatement.executeQuery();
				while (resultSet.next()) {
					trialLogsTableList.add(HistoricalMapper.trialTableLogsMapper(resultSet));
				}
			} catch (Exception e) {
				throw e;
			} finally {
				DbConnectionManager.closeDBConnection(resultSet, preparedStatement, con);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final long getTrialTimestamp(final int trialRunSettingId, final long offset, boolean isStartTimestamp)
			throws Exception {
		PreparedStatement fetchTrialLogsTableStmt = null;
		ResultSet resultSet = null;
		Connection con = null;
		try {
			con = DbConnectionManager.getInstance().getConnection();
			if (isStartTimestamp) {
				fetchTrialLogsTableStmt = con.prepareStatement(QueryConstant.GET_TRIAL_START_TIME);
			} else {
				fetchTrialLogsTableStmt = con.prepareStatement(QueryConstant.GET_TRIAL_END_TIME);
			}
			fetchTrialLogsTableStmt.setInt(1, trialRunSettingId);
			resultSet = fetchTrialLogsTableStmt.executeQuery();
			if (resultSet.next()) {
				return resultSet.getLong(ApiConstant.TIMESTAMP);
			} else {
				return offset;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchTrialLogsTableStmt, con);
		}
	}

	private final ArrayList<TrialTableLogs> getDistinctTableLogs(final List<TrialTableLogs> trialLogsTableList)
			throws ParseException {
		final ArrayList<TrialTableLogs> distinctTrialLogsList = new ArrayList<TrialTableLogs>();

		// Always add first value
		distinctTrialLogsList.add(trialLogsTableList.get(0));

		// Iterate the remaining values
		for (int i = 1; i < trialLogsTableList.size(); i++) {

			// Compare current value to previous
			if (BasicUtility.getInstance()
					.convertDateToUnix(trialLogsTableList.get(i - 1).getTimestamp()) != BasicUtility.getInstance()
							.convertDateToUnix(trialLogsTableList.get(i).getTimestamp())) {
				distinctTrialLogsList.add(trialLogsTableList.get(i));
			}
		}
		return distinctTrialLogsList;
	}

	private void fillEmptyData(List<TrialTableLogs> trialLogsTableList, List<Long> queryTimeStamps,
			final Map<Long, Long> pauseIntervalMap, final long queryStartTime) throws ParseException {

		List<Long> pauseTimeStamps = null;
		if (!queryTimeStamps.isEmpty()) {
			if (pauseIntervalMap != null && !pauseIntervalMap.isEmpty()) {
				pauseTimeStamps = new ArrayList<Long>();
				Iterator<Long> itr = queryTimeStamps.iterator();
				while (itr.hasNext()) {
					long queryTime = itr.next();
					for (Entry<Long, Long> pause : pauseIntervalMap.entrySet()) {
						if (queryTime >= pause.getKey() && queryTime <= pause.getValue()) {
							pauseTimeStamps.add(queryTime);
							itr.remove();
							break;
						}
					}
				}
			}

			if (!queryTimeStamps.isEmpty()) {
				for (long remainingTime : queryTimeStamps) {
					TrialTableLogs dummyTableLogs = new TrialTableLogs();
					dummyTableLogs.setTimestamp(BasicUtility.getInstance().convertUnixToDate(remainingTime));
					dummyTableLogs.setIsPaused(-1);
					trialLogsTableList.add(dummyTableLogs);
				}
			}

			if (pauseTimeStamps != null && !pauseTimeStamps.isEmpty()) {
				for (long pauseTime : pauseTimeStamps) {
					TrialTableLogs dummyTableLogs = new TrialTableLogs();
					dummyTableLogs.setTimestamp(BasicUtility.getInstance().convertUnixToDate(pauseTime));
					dummyTableLogs.setIsPaused(1);
					trialLogsTableList.add(dummyTableLogs);
				}
			}
		}
	}

	private int getTrialTableCount(final int interval, final int trialRunSettingId, final int direction) {
		Connection con = null;
		PreparedStatement fetchTotalPagesStmt = null;
		ResultSet totalPagesResultSet = null;
		int totalPages = 0;
		try {
			con = DbConnectionManager.getInstance().getConnection();
			long startTime = 0;
			long endTime = 0;
			fetchTotalPagesStmt = con.prepareStatement(QueryConstant.GET_TRIAL_START_TIME);
			fetchTotalPagesStmt.setInt(1, trialRunSettingId);
			totalPagesResultSet = fetchTotalPagesStmt.executeQuery();
			if (totalPagesResultSet.next()) {
				startTime = totalPagesResultSet.getLong(ApiConstant.TIMESTAMP);
			}

			totalPagesResultSet.close();
			fetchTotalPagesStmt.close();

			fetchTotalPagesStmt = con.prepareStatement(QueryConstant.GET_TRIAL_END_TIME);
			fetchTotalPagesStmt.setInt(1, trialRunSettingId);
			totalPagesResultSet = fetchTotalPagesStmt.executeQuery();
			if (totalPagesResultSet.next()) {
				endTime = totalPagesResultSet.getLong(ApiConstant.TIMESTAMP);
			}

			totalPagesResultSet.close();
			fetchTotalPagesStmt.close();
			while (endTime >= startTime) {
				totalPages++;
				endTime = BasicUtility.getInstance().manipulateTimeStamp(endTime, interval, false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(totalPagesResultSet, fetchTotalPagesStmt, con);
		}
		return totalPages;
	}

	public JSONObject fetchAlarmsLogs(int trialRunSettingId, int direction, int pageSize, int pageCount, int offset,
			String type, String nature) {
		Connection con = null;
		Connection con2 = null;
		PreparedStatement fetchAlarmLogStmt = null;
		PreparedStatement fetchTotalPagesStmt = null;
		ResultSet resultSet = null;
		ResultSet totalPagesResultSet = null;
		final List<AlarmHistory> alarmHistoryList = new ArrayList<>(pageSize);
		final JSONObject jsonObject = new JSONObject();
		int totalPages = -1;
		try {
			con = DbConnectionManager.getInstance().getConnection();

			if ((type.isEmpty() || type == null) && (nature.isEmpty() || nature == null)) {
				if (offset == 0) {
					fetchAlarmLogStmt = con.prepareStatement(QueryConstant.FETCH_ALARM_LOGS_WITHOUT_OFFSET);
					fetchAlarmLogStmt.setInt(1, trialRunSettingId);
					fetchAlarmLogStmt.setInt(2, pageSize);
					sLog.d(this, QueryConstant.FETCH_ALARM_LOGS_WITHOUT_OFFSET);
				} else {
					if (direction == 1) {
						fetchAlarmLogStmt = con.prepareStatement(QueryConstant.FETCH_ALARM_LOGS_WITH_OFFSET_BACKWARD);
						sLog.d(this, QueryConstant.FETCH_ALARM_LOGS_WITH_OFFSET_BACKWARD);
					} else {
						fetchAlarmLogStmt = con.prepareStatement(QueryConstant.FETCH_ALARM_LOGS_WITH_OFFSET_FORWARD);
						sLog.d(this, QueryConstant.FETCH_ALARM_LOGS_WITH_OFFSET_FORWARD);
					}
					fetchAlarmLogStmt.setInt(1, trialRunSettingId);
					fetchAlarmLogStmt.setInt(2, offset);
					fetchAlarmLogStmt.setInt(3, pageSize);
				}
			} else {
				// search
				StringBuilder stringBuilder = new StringBuilder()
						.append("select * from alarm_history where trial_run_setting_id = ").append(trialRunSettingId);
				if (!type.isEmpty() && type != null) {
					stringBuilder.append(" and alarm_stop_desc = ").append("'").append(type).append("'");
				}
				if (!nature.isEmpty() && nature != null) {
					stringBuilder.append(" and nature IN (");
					Arrays.asList(nature.split(","))
							.forEach(natureObj -> stringBuilder.append("'").append(natureObj.toString()).append("',"));
					stringBuilder.setLength(stringBuilder.length() - 1);
					stringBuilder.append(")");
				}
				if (offset == 0) {
					stringBuilder.append(" order by alarm_history_id desc limit ").append(pageSize);
					sLog.d(this, stringBuilder.toString());
					fetchAlarmLogStmt = con.prepareStatement(stringBuilder.toString());
				} else {
					if (direction == 1) {
						// backward
						stringBuilder.append(" and alarm_history_id < ").append(offset)
								.append(" order by alarm_history_id desc limit ").append(pageSize);
						sLog.d(this, stringBuilder.toString());
						fetchAlarmLogStmt = con.prepareStatement(stringBuilder.toString());
					} else {
						// forward
						stringBuilder.append(" and alarm_history_id > ").append(offset).append(" limit ")
								.append(pageSize);
						sLog.d(this, stringBuilder.toString());
						fetchAlarmLogStmt = con.prepareStatement(stringBuilder.toString());
					}
				}
			}

			resultSet = fetchAlarmLogStmt.executeQuery();
			while (resultSet.next()) {
				alarmHistoryList.add(HistoricalMapper.alarmsHistoryMapper(resultSet));
			}

			if (pageCount == 1) {
				// get total page count
				con2 = DbConnectionManager.getInstance().getConnection();
				if ((type.isEmpty() || type == null) && (nature.isEmpty() || nature == null)) {
					fetchTotalPagesStmt = con2.prepareStatement(QueryConstant.FETCH_ALARM_TOTAL_PAGES);
					fetchTotalPagesStmt.setInt(1, trialRunSettingId);
					sLog.d(this, QueryConstant.FETCH_ALARM_TOTAL_PAGES);
				} else {
					StringBuilder stringBuilder = new StringBuilder()
							.append("select count(*) from alarm_history where trial_run_setting_id = ")
							.append(trialRunSettingId);
					if (!type.isEmpty() && type != null) {
						stringBuilder.append(" and alarm_stop_desc = ").append("'").append(type).append("'");
					}
					if (!nature.isEmpty() && nature != null) {
						stringBuilder.append(" and nature IN (");
						Arrays.asList(nature.split(",")).forEach(
								natureObj -> stringBuilder.append("'").append(natureObj.toString()).append("',"));
						stringBuilder.setLength(stringBuilder.length() - 1);
						stringBuilder.append(")");
					}
					sLog.d(this, stringBuilder.toString());
					fetchTotalPagesStmt = con2.prepareStatement(stringBuilder.toString());
				}
				totalPagesResultSet = fetchTotalPagesStmt.executeQuery();
				if (totalPagesResultSet.next()) {
					totalPages = totalPagesResultSet.getInt(1);
				}
			}
			jsonObject.put(ApiConstant.TOTAL_PAGES, totalPages);
			jsonObject.put(ApiConstant.ALARM_LOGS, alarmHistoryList);
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchAlarmLogStmt, con);
			DbConnectionManager.closeDBConnection(totalPagesResultSet, fetchTotalPagesStmt, con2);
		}
	}

	public JSONObject fetchTrialLogs(int trialRunSettingId, int direction, int pageSize, int pageCount, String search,
			int offset) {
		Connection con = null;
		Connection con2 = null;
		PreparedStatement fetchTrialLogStmt = null;
		PreparedStatement fetchTotalPagesStmt = null;
		ResultSet resultSet = null;
		ResultSet totalPagesResultSet = null;
		final List<TrialHistory> trialLogsList = new ArrayList<>(pageSize);
		final JSONObject jsonObject = new JSONObject();
		int totalPages = -1;
		String searchText = null;
		try {
			con = DbConnectionManager.getInstance().getConnection();
			if (search.isEmpty() || search == null) {
				if (offset == 0) {
					fetchTrialLogStmt = con.prepareStatement(QueryConstant.FETCH_TRIAL_LOGS_WITHOUT_OFFSET);
					fetchTrialLogStmt.setInt(1, trialRunSettingId);
					fetchTrialLogStmt.setInt(2, pageSize);
					sLog.d(this, QueryConstant.FETCH_TRIAL_LOGS_WITHOUT_OFFSET);
				} else {
					if (direction == 1) {
						fetchTrialLogStmt = con.prepareStatement(QueryConstant.FETCH_TRIAL_LOGS_WITH_OFFSET_BACKWARD);
						sLog.d(this, QueryConstant.FETCH_TRIAL_LOGS_WITH_OFFSET_BACKWARD);
					} else {
						fetchTrialLogStmt = con.prepareStatement(QueryConstant.FETCH_TRIAL_LOGS_WITH_OFFSET_FORWARD);
						sLog.d(this, QueryConstant.FETCH_TRIAL_LOGS_WITH_OFFSET_FORWARD);
					}
					fetchTrialLogStmt.setInt(1, trialRunSettingId);
					fetchTrialLogStmt.setInt(2, offset);
					fetchTrialLogStmt.setInt(3, pageSize);
				}
			} else {
				// search
				searchText = MessageFormat.format(ApiConstant.SEARCH_PARAM, search);
				if (offset == 0) {
					fetchTrialLogStmt = con.prepareStatement(QueryConstant.SEARCH_TRIAL_LOGS_WITHOUT_OFFSET);
					fetchTrialLogStmt.setInt(1, trialRunSettingId);
					fetchTrialLogStmt.setString(2, searchText);
					fetchTrialLogStmt.setString(3, searchText);
					fetchTrialLogStmt.setString(4, searchText);
					fetchTrialLogStmt.setString(5, searchText);
					fetchTrialLogStmt.setString(6, searchText);
					fetchTrialLogStmt.setString(7, searchText);
					fetchTrialLogStmt.setInt(8, pageSize);
					sLog.d(this, QueryConstant.SEARCH_TRIAL_LOGS_WITHOUT_OFFSET);
				} else {
					if (direction == 1) {
						fetchTrialLogStmt = con.prepareStatement(QueryConstant.SEARCH_TRIAL_LOGS_WITH_OFFSET_BACKWARD);
						sLog.d(this, QueryConstant.SEARCH_TRIAL_LOGS_WITH_OFFSET_BACKWARD);
					} else {
						fetchTrialLogStmt = con.prepareStatement(QueryConstant.SEARCH_TRIAL_LOGS_WITH_OFFSET_FORWARD);
						sLog.d(this, QueryConstant.SEARCH_TRIAL_LOGS_WITH_OFFSET_FORWARD);
					}
					fetchTrialLogStmt.setInt(1, trialRunSettingId);
					fetchTrialLogStmt.setInt(2, offset);
					fetchTrialLogStmt.setString(3, searchText);
					fetchTrialLogStmt.setString(4, searchText);
					fetchTrialLogStmt.setString(5, searchText);
					fetchTrialLogStmt.setString(6, searchText);
					fetchTrialLogStmt.setString(7, searchText);
					fetchTrialLogStmt.setString(8, searchText);
					fetchTrialLogStmt.setInt(9, pageSize);
				}
			}
			resultSet = fetchTrialLogStmt.executeQuery();
			while (resultSet.next()) {
				trialLogsList.add(HistoricalMapper.trialHistoryMapper(resultSet));
			}
			if (pageCount == 1) {
				// get total page count
				con2 = DbConnectionManager.getInstance().getConnection();
				if (search.isEmpty() || search == null) {
					fetchTotalPagesStmt = con2.prepareStatement(QueryConstant.FETCH_TRIAL_LOGS_TOTAL_PAGES);
					fetchTotalPagesStmt.setInt(1, trialRunSettingId);
					sLog.d(this, QueryConstant.FETCH_TRIAL_LOGS_TOTAL_PAGES);
				} else {
					fetchTotalPagesStmt = con2.prepareStatement(QueryConstant.FETCH_TRIAL_LOGS_TOTAL_PAGES_SEARCH);
					fetchTotalPagesStmt.setInt(1, trialRunSettingId);
					fetchTotalPagesStmt.setString(2, searchText);
					fetchTotalPagesStmt.setString(3, searchText);
					fetchTotalPagesStmt.setString(4, searchText);
					fetchTotalPagesStmt.setString(5, searchText);
					fetchTotalPagesStmt.setString(6, searchText);
					fetchTotalPagesStmt.setString(7, searchText);
					sLog.d(this, QueryConstant.FETCH_TRIAL_LOGS_TOTAL_PAGES_SEARCH);
				}
				totalPagesResultSet = fetchTotalPagesStmt.executeQuery();
				if (totalPagesResultSet.next()) {
					totalPages = totalPagesResultSet.getInt(1);
				}
			}
			jsonObject.put(ApiConstant.TOTAL_PAGES, totalPages);
			jsonObject.put(ApiConstant.TRIAL_LOGS, trialLogsList);
			return jsonObject;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchTrialLogStmt, con);
			DbConnectionManager.closeDBConnection(totalPagesResultSet, fetchTotalPagesStmt, con2);
		}
	}

	public List<TrialHistory> fetchAllTrialLogs(final int trialRunSettingId) {
		Connection con = null;
		PreparedStatement fetchTrialLogStmt = null;
		ResultSet resultSet = null;
		final List<TrialHistory> trialLogsList = new ArrayList<>();
		try {
			con = DbConnectionManager.getInstance().getConnection();
			fetchTrialLogStmt = con.prepareStatement(QueryConstant.FETCH_TRIAL_LOGS_WITHOUT_OFFSET);
			fetchTrialLogStmt.setInt(1, trialRunSettingId);
			sLog.d(this, QueryConstant.FETCH_TRIAL_LOGS_WITHOUT_OFFSET);
			resultSet = fetchTrialLogStmt.executeQuery();
			while (resultSet.next()) {
				trialLogsList.add(HistoricalMapper.trialHistoryMapper(resultSet));
			}
			return trialLogsList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchTrialLogStmt, con);
		}
	}

}
