package org.lattice.spectrum_backend_final.dao.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author RAHUL KUMAR MAURYA
 */

import org.json.JSONArray;
import org.lattice.spectrum_backend_final.beans.CfrStatus;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.sqlite.SQLiteConfig;

/**
 * This class provides the single connection instance
 */
public class DbConnectionManager {

	private TokenManager tokenManager = new TokenManager();
	private TimerManager timerManager = new TimerManager();
	private CfrStatus cfrStatus = new CfrStatus();

	public boolean isPiracyControlMode = true;
	public boolean isDebuggingMode = false;
	private JSONArray kfCommVersion = new JSONArray();

	private static String DB_URL = null;

	private static final String DRIVER = "org.sqlite.JDBC";

	private static DbConnectionManager connectionManager;

	private DbConnectionManager() {
		
		try {
			
			Class.forName(DRIVER);
			
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
			
		}

	}

	// Getters and Setters

	public TokenManager getTokenManager() {
		return tokenManager;
	}

	public CfrStatus getCfrStatus() {
		return cfrStatus;
	}

	public TimerManager getTimerManager() {
		return timerManager;
	}

	public String getDB_URL() {
		return DB_URL;
	}
	
	public void setDB_URL(String url) {
		DB_URL = url;
	}

	public JSONArray getKfCommVersion() {
		return kfCommVersion;
	}

	public void setKfCommVersion(JSONArray kfCommVersion) {
		this.kfCommVersion = kfCommVersion;
	}

	public static DbConnectionManager getInstance() {

		synchronized (DbConnectionManager.class) {
			if (connectionManager == null) {
				connectionManager = new DbConnectionManager();
			}
		}

		return connectionManager;
	}

	
	public Connection getConnection() {
		try {

			Connection conn = DriverManager.getConnection(DB_URL, getSqliteConfig().toProperties());
			conn.setAutoCommit(true);
			return conn;
		} catch (Exception e) {
			System.out.println(" Get Connection Error: " + e.getMessage());
		}
		return null;
	}

	private SQLiteConfig getSqliteConfig(){

		int cache_size = -50000;

		SQLiteConfig config = new SQLiteConfig();
		config.setCacheSize(cache_size);
		config.setSynchronous(SQLiteConfig.SynchronousMode.NORMAL);
		config.setTempStore(SQLiteConfig.TempStore.MEMORY);
		config.setJournalMode(SQLiteConfig.JournalMode.WAL);

		return config;

	}

	public static void closeDBConnection(ResultSet rs, Statement stmt, Connection con) {
		try {
			if (rs != null && con != null && stmt != null) {
				rs.close();
			}

			if (stmt != null && con != null) {
				stmt.close();
			}

			if (con != null) {
				con.close();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

	public static void executePrepStatementBatch(PreparedStatement preparedStatement1, PreparedStatement preparedStatement2,
			PreparedStatement preparedStatement3, PreparedStatement preparedStatement4) throws SQLException {

		try {
			if (preparedStatement1 != null) {
				preparedStatement1.executeUpdate();
				preparedStatement1.close();
			}

			if (preparedStatement2 != null) {
				preparedStatement2.executeUpdate();
				preparedStatement2.close();
			}

			if (preparedStatement3 != null) {
				preparedStatement3.executeUpdate();
				preparedStatement3.close();
			}

			if (preparedStatement4 != null) {
				preparedStatement4.executeUpdate();
				preparedStatement4.close();
			}

		} catch (SQLException e) {
			System.out.println("----exp---" + e.getMessage());
			throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void executePrepStatementBatch(PreparedStatement preparedStatement1, PreparedStatement preparedStatement2,
			PreparedStatement preparedStatement3, PreparedStatement preparedStatement4, PreparedStatement preparedStatement5,
			PreparedStatement preparedStatement6, PreparedStatement preparedStatement7, PreparedStatement preparedStatement8)
			throws SQLException {

		try {
			if (preparedStatement1 != null) {
				preparedStatement1.executeUpdate();
				preparedStatement1.close();
			}

			if (preparedStatement2 != null) {
				preparedStatement2.executeUpdate();
				preparedStatement2.close();
			}

			if (preparedStatement3 != null) {
				preparedStatement3.executeUpdate();
				preparedStatement3.close();
			}

			if (preparedStatement4 != null) {
				preparedStatement4.executeUpdate();
				preparedStatement4.close();
			}

			if (preparedStatement5 != null) {
				preparedStatement5.executeUpdate();
				preparedStatement5.close();
			}
			if (preparedStatement6 != null) {
				preparedStatement6.executeUpdate();
				preparedStatement6.close();
			}
			if (preparedStatement7 != null) {
				preparedStatement7.executeUpdate();
				preparedStatement7.close();
			}

			if (preparedStatement8 != null) {
				preparedStatement8.executeUpdate();
				preparedStatement8.close();
			}

		} catch (SQLException e) {
			throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
