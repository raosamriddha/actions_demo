package org.lattice.spectrum_backend_final.dao.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.LicenseStatus;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.DbConstant;
import org.lattice.spectrum_backend_final.dao.constants.QueryConstant;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.PasswordUtil;

import com.lattice.spectrum.ComLibrary.utility.sLog;

public class LicenseActivationManager {

	public final LicenseStatus fetchLicenseStatus() {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchLicenseStatus = null;
		ResultSet resultSet = null;
		try {
			fetchLicenseStatus = con.prepareStatement(QueryConstant.FETCH_LICENSE_STATUS);
			sLog.d(QueryConstant.FETCH_LICENSE_STATUS);
			resultSet = fetchLicenseStatus.executeQuery();
			if (resultSet.next()) {
				final LicenseStatus licenseStatus = new LicenseStatus();
				licenseStatus.setStatusId(resultSet.getInt(ApiConstant.STATUS_ID));
				licenseStatus
						.setStatus(PasswordUtil.decrypt(resultSet.getString(ApiConstant.STATUS), ApiConstant.SECRET));
				return licenseStatus;
			} else {
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchLicenseStatus, con);
		}
	}

	public final int updateLicenseActivation(String status) {
		Connection con = null;
		PreparedStatement licenseStatusStmt = null;
		try {
			con = DbConnectionManager.getInstance().getConnection();
			con.setAutoCommit(false);
			LicenseStatus checkStatusExistance = fetchLicenseStatus();
			if (checkStatusExistance != null) {
				licenseStatusStmt = con.prepareStatement(QueryConstant.UPDATE_LICENSE_STATUS,
						Statement.RETURN_GENERATED_KEYS);
				licenseStatusStmt.setString(1, status);
				licenseStatusStmt.setInt(2, 1);
				sLog.d(QueryConstant.UPDATE_LICENSE_STATUS);
			} else {
				return 0;
			}
			int affectedRows = licenseStatusStmt.executeUpdate();
			if (affectedRows == 0) {
				return 0;
			}
			con.commit();
			return affectedRows;
		} catch (Exception ex) {
			try {
				con.rollback();
			} catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
			}
			ex.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, licenseStatusStmt, con);
		}
	}

	public final int createSuperAdmin(final JSONObject userJSON) throws Exception {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement createUserMasterPS = null;
		PreparedStatement createUserDetailPS = null;
		PreparedStatement addAdminRolePS = null;
		PreparedStatement addUserCredentialPS = null;
		int userId = 0;
		try {
			con.setAutoCommit(false);

			createUserMasterPS = con.prepareStatement(DbConstant.CREATE_NEW_USER_MASTER,
					Statement.RETURN_GENERATED_KEYS);
			createUserMasterPS.setString(1, userJSON.getString(ApiConstant.USERNAME));
			createUserMasterPS.setString(2, ApiConstant.FLAG_INACTIVE);
			createUserMasterPS.setString(3, ApiConstant.BLANK_SPACE);
			createUserMasterPS.setString(4, BasicUtility.getInstance().getCurrentTimestamp());
			createUserMasterPS.setString(5, ApiConstant.BLANK_SPACE);
			createUserMasterPS.executeUpdate();

			try (ResultSet generatedKeys = createUserMasterPS.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					userId = generatedKeys.getInt(1);
				} else {
					throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
				}
			}

			createUserDetailPS = con.prepareStatement(DbConstant.CREATE_NEW_USER_DETAILS);
			createUserDetailPS.setInt(1, userId);
			createUserDetailPS.setString(2, userJSON.getString(ApiConstant.PREFIX));
			createUserDetailPS.setString(3, userJSON.getString(ApiConstant.FIRST_NAME));
			createUserDetailPS.setString(4, userJSON.getString(ApiConstant.MIDDLE_NAME));
			createUserDetailPS.setString(5, userJSON.getString(ApiConstant.LAST_NAME));
			createUserDetailPS.setString(6, userJSON.getString(ApiConstant.EMAIL));
			createUserDetailPS.setString(7, userJSON.getString(ApiConstant.CONTACT_NUMBER));
			createUserDetailPS.setString(8, BasicUtility.getInstance().getFullName(userJSON));

			int addedUserDetails = createUserDetailPS.executeUpdate();
			if (addedUserDetails <= 0) {
				return 0;
			}

			addAdminRolePS = con.prepareStatement(DbConstant.ADD_USER_ROLE_MAP);
			addAdminRolePS.setInt(1, userId);
			addAdminRolePS.setInt(2, 5);
			int savedRole = addAdminRolePS.executeUpdate();
			if (savedRole <= 0) {
				return 0;
			}

			addUserCredentialPS = con.prepareStatement(DbConstant.ADD_NEW_USER_CREDENTIALS);
			addUserCredentialPS.setInt(1, userId);
			addUserCredentialPS.setString(2, userJSON.getString(ApiConstant.PASSWORD));
			addUserCredentialPS.setString(3, userJSON.getString(ApiConstant.MASTER_PASSWORD));
			int addedCredentials = addUserCredentialPS.executeUpdate();
			if (addedCredentials <= 0) {
				return 0;
			}

			con.commit();
			return userId;

		} catch (Exception ex) {
			try {
				con.rollback();
			} catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
				throw sqlEx;
			}
			ex.printStackTrace();
			if (userId > 0) {
				deleteSuperAdmin(userId);
			}
			throw ex;
		} finally {
			DbConnectionManager.closeDBConnection(null, addUserCredentialPS, null);
			DbConnectionManager.closeDBConnection(null, addAdminRolePS, null);
			DbConnectionManager.closeDBConnection(null, createUserDetailPS, null);
			DbConnectionManager.closeDBConnection(null, createUserMasterPS, con);
		}
	}

	public final boolean deleteSuperAdmin(final int userId) throws Exception {
		Connection con = null;

		PreparedStatement deleteUserCredentials = null;
		PreparedStatement deleteUserRole = null;
		PreparedStatement deleteUserDetails = null;
		PreparedStatement deleteUserMaster = null;

		try {
			con = DbConnectionManager.getInstance().getConnection();

			// delete user credentials
			deleteUserCredentials = con.prepareStatement(QueryConstant.DELETE_USER_CREDENTIALS);
			deleteUserCredentials.setInt(1, userId);
			sLog.d(QueryConstant.DELETE_USER_CREDENTIALS);
			int deleteUserCredentialsResult = deleteUserCredentials.executeUpdate();
			if (deleteUserCredentialsResult <= 0) {
				// error
				return false;
			}

			// delete user role map
			deleteUserRole = con.prepareStatement(QueryConstant.DELETE_USER_ROLE_MAP);
			deleteUserRole.setInt(1, userId);
			sLog.d(QueryConstant.DELETE_USER_ROLE_MAP);
			int deleteUserRoleResult = deleteUserRole.executeUpdate();
			if (deleteUserRoleResult <= 0) {
				// error
				return false;
			}

			// delete user details
			deleteUserDetails = con.prepareStatement(QueryConstant.DELETE_USER_DETAILS);
			deleteUserDetails.setInt(1, userId);
			sLog.d(QueryConstant.DELETE_USER_DETAILS);
			int deleteUserDetailsResult = deleteUserDetails.executeUpdate();
			if (deleteUserDetailsResult <= 0) {
				// error
				return false;
			}

			// delete user master
			deleteUserMaster = con.prepareStatement(QueryConstant.DELETE_USER_MASTER);
			deleteUserMaster.setInt(1, userId);
			sLog.d(QueryConstant.DELETE_USER_MASTER);
			int deleteUserMasterResult = deleteUserMaster.executeUpdate();
			if (deleteUserMasterResult <= 0) {
				// error
				return false;
			}

			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			DbConnectionManager.closeDBConnection(null, deleteUserCredentials, null);
			DbConnectionManager.closeDBConnection(null, deleteUserRole, null);
			DbConnectionManager.closeDBConnection(null, deleteUserDetails, null);
			DbConnectionManager.closeDBConnection(null, deleteUserMaster, con);
		}

	}

	public final boolean activateSuperAdminAccount(final int userId) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement activateAccountPS = null;
		try {
			activateAccountPS = con.prepareStatement(QueryConstant.ACTIVATE_SUPER_ADMIN_ACCOUNT);
			activateAccountPS.setInt(1, userId);
			sLog.d(QueryConstant.ACTIVATE_SUPER_ADMIN_ACCOUNT);
			int result = activateAccountPS.executeUpdate();
			if (result <= 0) {
				return false;
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, activateAccountPS, con);
		}
	}

	public static final JSONObject getUserDetailsByEmail(final String email) {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement userDetailsPS = null;
		ResultSet resultSet = null;
		final JSONObject jsonObject = new JSONObject();
		try {
			userDetailsPS = con.prepareStatement(QueryConstant.FETCH_DUPLICATE_EMAIL_USER_DETAILS);
			userDetailsPS.setString(1, email);
			sLog.d(QueryConstant.FETCH_DUPLICATE_EMAIL_USER_DETAILS);
			resultSet = userDetailsPS.executeQuery();
			if (resultSet.next()) {
				jsonObject.put(ApiConstant.JSON_USER_ID, resultSet.getInt(ApiConstant.JSON_USER_ID));
				jsonObject.put(ApiConstant.USERNAME, resultSet.getString(ApiConstant.USERNAME));
				jsonObject.put(ApiConstant.FULL_NAME, resultSet.getString(ApiConstant.FULL_NAME));
				jsonObject.put(ApiConstant.EMAIL, email);
			}
			return jsonObject;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, userDetailsPS, con);
		}
	}

	public final void inactiveOldSuperAdmin(final int userId) {
		Connection con = null;
		PreparedStatement inactiveSuperAdmins = null;
		PreparedStatement inactiveSuperAdminUserMaster = null;
		try {
			con = DbConnectionManager.getInstance().getConnection();
			con.setAutoCommit(false);
			inactiveSuperAdmins = con.prepareStatement(QueryConstant.INACTIVE_PREVIOUS_SUPER_ADMINS);
			inactiveSuperAdmins.setInt(1, userId);
			sLog.d(QueryConstant.INACTIVE_PREVIOUS_SUPER_ADMINS);
			final int result = inactiveSuperAdmins.executeUpdate();
			if (result > 0) {
				inactiveSuperAdminUserMaster = con
						.prepareStatement(QueryConstant.INACTIVE_PREVIOUS_SUPER_ADMINS_USER_MASTER);
				sLog.d(QueryConstant.INACTIVE_PREVIOUS_SUPER_ADMINS_USER_MASTER);
				inactiveSuperAdminUserMaster.executeUpdate();
				con.commit();
			}
		} catch (Exception ex) {
			try {
				con.rollback();
			} catch (SQLException sqlEx) {
				sqlEx.printStackTrace();
			}
			ex.printStackTrace();
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(null, inactiveSuperAdminUserMaster, null);
			DbConnectionManager.closeDBConnection(null, inactiveSuperAdmins, con);
		}
	}

}
