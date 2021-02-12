package org.lattice.spectrum_backend_final.dao.manager;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.Notes;
import org.lattice.spectrum_backend_final.beans.PumpLookUp;
import org.lattice.spectrum_backend_final.beans.TrialLog;
import org.lattice.spectrum_backend_final.beans.TubingDetails;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.constants.DbConstant;
import org.lattice.spectrum_backend_final.dao.constants.QueryConstant;
import org.lattice.spectrum_backend_final.dao.manager.nonrun.NonRunManager;
import org.lattice.spectrum_backend_final.dao.manager.nonrun.NonRunTrialManager;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.EndPointUtil;
import org.lattice.spectrum_backend_final.dao.util.HistoricalConverter;
import org.lattice.spectrum_backend_final.dao.util.JsonMapper;
import org.lattice.spectrum_backend_final.dao.util.Logger;
import org.lattice.spectrum_backend_final.dao.util.PasswordUtil;
import org.lattice.spectrum_backend_final.dao.util.RecipeMapper;
import org.lattice.spectrum_backend_final.dao.util.TubingDetailsMapper;
import org.lattice.spectrum_backend_final.exception.HardwareValidationException;
import org.lattice.spectrum_backend_final.exception.InvalidCredentialException;
import org.lattice.spectrum_backend_final.exception.InvalidRecipeException;
import org.lattice.spectrum_backend_final.exception.OldPasswordException;
import org.lattice.spectrum_backend_final.exception.TrialRunException;
import org.lattice.spectrum_backend_final.exception.UserDoesNotExistException;

/**
 * @author RAHUL KUMAR MAURYA
 */

import com.lattice.spectrum.ComLibrary.utility.sLog;

import javassist.NotFoundException;

/**
 * This class holds all the necessary functions for the different operations.
 */
public class DatabaseManager {

    private static DatabaseManager databaseManager;

    public static DatabaseManager getInstance() {

        synchronized (DatabaseManager.class) {
            if (databaseManager == null) {
                databaseManager = new DatabaseManager();
            }
        }

        return databaseManager;
    }

    private DatabaseManager() {
    }

    /**
     * User login
     *
     * @param values takes JSON as String.
     * @return 1.) Yes, if credentials is valid and the user has assigned a default
     * password . 2.) No, if credentials is valid and user don't assigned
     * default password which means he/she is not a new user. 3.) Invalid ,
     * if a user enters wrong username or password.
     * @throws SQLException throws sql exception when there is database error.
     */
    public String login(String values) throws Exception {

        String username, password;
        PreparedStatement checkUserExistPS, userLoginPS, updateLastLoginPS, resetUserLoginLimitPS, setUserInactivePS,
                disableUserLoginLimitPS, checkUserActivePS, checkUserInactivePS;
        ResultSet resultSet = null;
        ResultSet isUserExist;

        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {

            JSONObject loginJson = new JSONObject(values);

            username = loginJson.getString(ApiConstant.USERNAME);
            password = loginJson.getString(ApiConstant.PASSWORD);
            Logger.debug(this, "login: IS_USER_EXIST");
            checkUserExistPS = conn.prepareStatement(DbConstant.IS_USER_EXIST);
            checkUserExistPS.setString(1, username);

            isUserExist = checkUserExistPS.executeQuery();

            // check if user exist
            if (isUserExist.next()) {
                Logger.debug(this, "login: IS_USER_INACTIVE");
                checkUserInactivePS = conn.prepareStatement(DbConstant.IS_USER_INACTIVE);
                checkUserInactivePS.setString(1, username);
                ResultSet isUserInactive = checkUserInactivePS.executeQuery();

                if (isUserInactive.next()) {
                    return ApiConstant.INACTIVE;
                }
                Logger.debug(this, "login: USER_LOGIN");
                userLoginPS = conn.prepareStatement(DbConstant.USER_LOGIN);

                userLoginPS.setString(1, username);
                userLoginPS.setString(2, PasswordUtil.encrypt(password, ApiConstant.SECRET));

                resultSet = userLoginPS.executeQuery();

            } else {
                DbConnectionManager.getInstance().getTokenManager().setToken(null);
                DbConnectionManager.getInstance().getTokenManager().setUserType(null);

                return ApiConstant.INVALID;
            }

            if (resultSet.next()) {

                if (resultSet.getString(ApiConstant.PASS_STATUS).equals(ApiConstant.DEFAULT_PASSWORD_STATUS)) {
                    Logger.debug(this, "login: DEFAULT_PASSWORD_STATUS");
                    return ApiConstant.YES;
                }

                String fullName;

                if (resultSet.getString(ApiConstant.MIDDLE_NAME) == null
                        || resultSet.getString(ApiConstant.MIDDLE_NAME).isEmpty()) {

                    fullName = resultSet.getString(ApiConstant.FIRST_NAME) + ApiConstant.BLANK_SPACE
                            + resultSet.getString(ApiConstant.LAST_NAME);
                } else {
                    fullName = resultSet.getString(ApiConstant.FIRST_NAME) + ApiConstant.BLANK_SPACE
                            + resultSet.getString(ApiConstant.MIDDLE_NAME) + ApiConstant.BLANK_SPACE
                            + resultSet.getString(ApiConstant.LAST_NAME);
                }

                Logger.debug(this, "login: UPDATE_LAST_LOGIN");
                updateLastLoginPS = conn.prepareStatement(DbConstant.UPDATE_LAST_LOGIN);

                updateLastLoginPS.setString(1, BasicUtility.getInstance().getCurrentTimestamp());
                updateLastLoginPS.setString(2, resultSet.getString(ApiConstant.EMAIL_ID));

                updateLastLoginPS.executeUpdate();

                DbConnectionManager.getInstance().getTokenManager().generateToken(ApiConstant.TOKEN_LENGTH);
                DbConnectionManager.getInstance().getTokenManager()
                        .setUserId(resultSet.getInt(ApiConstant.JSON_USER_ID));
                DbConnectionManager.getInstance().getTokenManager()
                        .setUserType(resultSet.getString(ApiConstant.ROLE_DESCRIPTION));
                DbConnectionManager.getInstance().getTokenManager()
                        .setUserEmail(resultSet.getString(ApiConstant.EMAIL_ID));
                DbConnectionManager.getInstance().getTokenManager().setUserFullName(fullName);
                DbConnectionManager.getInstance().getTokenManager()
                        .setUsername(resultSet.getString(ApiConstant.USERNAME));

                Logger.debug(this, "login: RESET_USER_LOGIN_LIMIT");
                resetUserLoginLimitPS = conn.prepareStatement(DbConstant.RESET_USER_LOGIN_LIMIT);
                resetUserLoginLimitPS.setString(1, username);
                resetUserLoginLimitPS.executeUpdate();
                resetUserLoginLimitPS.close();

                userLoginPS.close();
                updateLastLoginPS.close();
                resultSet.close();

                return ApiConstant.NO;

            } else {

                // Here put the 3 login attempts logic
                Logger.debug(this, "login: RESET_USER_LOGIN_LIMIT");
                checkUserActivePS = conn.prepareStatement(DbConstant.IS_USER_ACTIVE);
                checkUserActivePS.setString(1, username);

                ResultSet isUserActive = checkUserActivePS.executeQuery();

                if (isUserActive.next()) {

                    DbConnectionManager.getInstance().getTokenManager().setToken(null);
                    DbConnectionManager.getInstance().getTokenManager().setUserType(null);

                    Logger.debug(this, "login: IS_FIRST_LOGIN_ATTEMPT");
                    resetUserLoginLimitPS = conn.prepareStatement(DbConstant.IS_FIRST_LOGIN_ATTEMPT);
                    resetUserLoginLimitPS.setString(1, username);
                    ResultSet isFirstAttemptResultSet = resetUserLoginLimitPS.executeQuery();

                    if (isFirstAttemptResultSet.next()) {
                        int count = isFirstAttemptResultSet.getInt(ApiConstant.COUNT);

                        if (count == 2) {
                            Logger.debug(this, "login: SET_USER_INACTIVE");
                            setUserInactivePS = conn.prepareStatement(DbConstant.SET_USER_INACTIVE);
                            setUserInactivePS.setString(1, username);

                            Logger.debug(this, "login: DISABLE_USER_LOGIN_LIMIT");
                            disableUserLoginLimitPS = conn.prepareStatement(DbConstant.DISABLE_USER_LOGIN_LIMIT);
                            disableUserLoginLimitPS.setString(1, username);

                            Logger.debug(this, "login: DISABLE_USER_CREDENTIAL");
                            checkUserActivePS = conn.prepareStatement(DbConstant.DISABLE_USER_CREDENTIAL);
                            checkUserActivePS.setString(1, username);

                            Logger.debug(this, "login: ADD_USER_CREDENTIAL_WITH_DEFAULT_PASSWORD");
                            checkUserInactivePS = conn
                                    .prepareStatement(DbConstant.ADD_USER_CREDENTIAL_WITH_DEFAULT_PASSWORD);
                            checkUserInactivePS.setString(1, username);
                            checkUserInactivePS.setString(2,
                                    PasswordUtil.encrypt(PasswordUtil.generatePassword(), ApiConstant.SECRET));

                            DbConnectionManager.executePrepStatementBatch(setUserInactivePS, disableUserLoginLimitPS,
                                    checkUserActivePS, checkUserInactivePS);

                            Logger.info(this, "login: "+ApiConstant.YOUR_ACCOUNT_IS_BLOCKED_DUE_TO_THREE_INVALID_LOGIN_ATTEMPT);
                            return ApiConstant.YOUR_ACCOUNT_IS_BLOCKED_DUE_TO_THREE_INVALID_LOGIN_ATTEMPT;
                        } else {

                            Logger.debug(this, "login: UPDATE_USER_LOGIN_LIMIT_COUNT - "+(count + 1));
                            setUserInactivePS = conn.prepareStatement(DbConstant.UPDATE_USER_LOGIN_LIMIT_COUNT);
                            setUserInactivePS.setInt(1, count + 1);
                            setUserInactivePS.setString(2, username);
                            setUserInactivePS.executeUpdate();
                            setUserInactivePS.close();
                            return ApiConstant.INVALID_LOGIN_MESSAGE + " " + (3 - (count + 1)) + " "
                                    + ApiConstant.ATTEMPT_LEFT;

                        }
                    } else {
                        Logger.debug(this, "login: UPDATE_USER_LOGIN_LIMIT_COUNT");
                        disableUserLoginLimitPS = conn.prepareStatement(DbConstant.ADD_USER_LOGIN_LIMIT);
                        disableUserLoginLimitPS.setString(1, username);
                        disableUserLoginLimitPS.setString(2, BasicUtility.getInstance().getCurrentTimestamp());

                        disableUserLoginLimitPS.executeUpdate();
                        disableUserLoginLimitPS.close();
                        return ApiConstant.INVALID_LOGIN_MESSAGE + " " + 2 + " " + ApiConstant.ATTEMPT_LEFT;

                    }
                }

                return ApiConstant.INVALID;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }

    }

    /**
     * Logout user who is currently login.
     */

    public void logout() {

        try {
            Logger.setSAALogToFile(false);

            // Now saving the logs in access-log
            addUserAccessLog(
                    ApiConstant.LOGOUT,
                    DbConnectionManager.getInstance().getTokenManager().getUsername(),
                    ApiConstant.BLANK_QUOTE
            );

            DbConnectionManager.getInstance().getTokenManager().setToken(null);
            DbConnectionManager.getInstance().getTokenManager().setUserType(null);
            DbConnectionManager.getInstance().getTokenManager().setUserEmail(null);
            DbConnectionManager.getInstance().getTokenManager().setUserFullName(null);
            DbConnectionManager.getInstance().getTokenManager().setUsername(null);
            DbConnectionManager.getInstance().getTokenManager().setSessionTimeout(false);
            DbConnectionManager.getInstance().getTokenManager().setUserLoginLimitMap(new HashMap<>());

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * When user forget its password.
     *
     * @param values Takes credentials as JSON String.
     * @return
     * @throws UserDoesNotExistException When user doesn't exist.
     * @throws SQLException              When there is any Database error.
     */
    public boolean forgotPassword(String values) throws UserDoesNotExistException, SQLException {

        PreparedStatement preparedStatement = null, preparedStatement2;

        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {
            conn.setAutoCommit(false);

            JSONObject jsonObject = new JSONObject(values);
            Logger.debug(this, "forgotPassword: IS_SUPER_USER_ACTIVE");
            preparedStatement2 = conn.prepareStatement(DbConstant.IS_SUPER_USER_ACTIVE);
            preparedStatement2.setString(1, jsonObject.getString(ApiConstant.USERNAME));
            ResultSet isUserInactive = preparedStatement2.executeQuery();

            if (!isUserInactive.next()) {
                Logger.debug(this, "forgotPassword: USER_IS_INACTIVE_PLEASE_CONTACT_SUPERADMIN");
                throw new UserDoesNotExistException(ApiConstant.USER_IS_INACTIVE_PLEASE_CONTACT_SUPERADMIN);
            } else {
                if (isUserInactive.getInt("role_id") == 5) {
                    return true;
                } else {

                    Logger.debug(this, "forgotPassword: FORGOT_PASSWORD_QUERY");
                    preparedStatement = conn.prepareStatement(DbConstant.FORGOT_PASSWORD_QUERY);

                    preparedStatement.setString(1, jsonObject.getString(ApiConstant.USERNAME));

                    int rowCount = preparedStatement.executeUpdate();

                    if (rowCount == 0) {
                        Logger.debug(this, "forgotPassword: "+ApiConstant.USER_NOT_FOUND_MESSAGE);
                        throw new UserDoesNotExistException(ApiConstant.USER_NOT_FOUND_MESSAGE);
                    }

                    preparedStatement.close();
                    conn.commit();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } catch (UserDoesNotExistException e) {
            e.printStackTrace();
            throw new UserDoesNotExistException(e.getMessage());
        }
        return false;
    }

    public int validateMasterPassword(String values) throws Exception {
        final Connection con = DbConnectionManager.getInstance().getConnection();
        PreparedStatement checkIsMasterPasswordValid = null;
        PreparedStatement inactiveSuperAdmin = null;
        ResultSet resultSet = null;
        try {
            JSONObject jsonObject = new JSONObject(values);
            checkIsMasterPasswordValid = con.prepareStatement(QueryConstant.CHECK_IS_MASTER_PASSWORD_VALID);
            checkIsMasterPasswordValid.setString(1, jsonObject.getString(ApiConstant.USERNAME));
            sLog.d(this, QueryConstant.CHECK_IS_MASTER_PASSWORD_VALID);
            resultSet = checkIsMasterPasswordValid.executeQuery();
            if (resultSet.next()) {
                if (resultSet.getString(ApiConstant.PASS_STATUS) != null) {
                    if ("U".equalsIgnoreCase(resultSet.getString(ApiConstant.PASS_STATUS))
                            || "R".equalsIgnoreCase(resultSet.getString(ApiConstant.PASS_STATUS))) {
                        if (PasswordUtil.encrypt(jsonObject.getString(ApiConstant.MASTER_PASSWORD), ApiConstant.SECRET)
                                .equalsIgnoreCase(resultSet.getString(ApiConstant.MASTER_PASSWORD))) {
                            // update pass_status to 'R'
                            con.setAutoCommit(false);
                            inactiveSuperAdmin = con.prepareStatement(QueryConstant.INACTIVE_SUPER_ADMIN_CREDENTIALS);
                            inactiveSuperAdmin.setInt(1, resultSet.getInt(ApiConstant.JSON_USER_ID));
                            int result = inactiveSuperAdmin.executeUpdate();
                            if (result <= 0) {
                                throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
                            }
                            con.commit();
                            return resultSet.getInt(ApiConstant.JSON_USER_ID);
                        } else {
                            return 0;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            sLog.d(this, ex);
            try {
                con.rollback();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            throw ex;
        } finally {
            DbConnectionManager.closeDBConnection(resultSet, checkIsMasterPasswordValid, null);
            DbConnectionManager.closeDBConnection(null, inactiveSuperAdmin, con);
        }
        return 0;
    }

    public boolean changeSuperAdminPassword(String values) throws Exception {
        final Connection con = DbConnectionManager.getInstance().getConnection();
        PreparedStatement checkIsMasterPasswordValid = null;
        PreparedStatement changePasswordPS = null;
        PreparedStatement inactivePreviousAccount = null;
        ResultSet resultSet = null;
        try {
            JSONObject jsonObject = new JSONObject(values);
            checkIsMasterPasswordValid = con.prepareStatement(QueryConstant.CHECK_IS_SUPER_ADMIN);
            checkIsMasterPasswordValid.setInt(1, jsonObject.getInt(ApiConstant.JSON_USER_ID));
            sLog.d(this, QueryConstant.CHECK_IS_SUPER_ADMIN);
            resultSet = checkIsMasterPasswordValid.executeQuery();
            if (resultSet.next()) {
                con.setAutoCommit(false);
                // add new password
                inactivePreviousAccount = con.prepareStatement(DbConstant.DISABLE_USER_CREDENTIALS);
                inactivePreviousAccount.setInt(1, jsonObject.getInt(ApiConstant.JSON_USER_ID));
                int status = inactivePreviousAccount.executeUpdate();
                if (status > 0) {
                    changePasswordPS = con.prepareStatement(DbConstant.ADD_NEW_USER_CREDENTIALS);
                    changePasswordPS.setInt(1, jsonObject.getInt(ApiConstant.JSON_USER_ID));
                    changePasswordPS.setString(2,
                            PasswordUtil.encrypt(jsonObject.getString(ApiConstant.PASSWORD), ApiConstant.SECRET));
                    changePasswordPS.setString(3, resultSet.getString(ApiConstant.MASTER_PASSWORD));
                    int result = changePasswordPS.executeUpdate();
                    if (result <= 0) {
                        throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
                    }
                    con.commit();
                    return true;
                } else {
                    throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
                }
            }
        } catch (Exception ex) {
            sLog.d(this, ex);
            try {
                con.rollback();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            throw ex;
        } finally {
            DbConnectionManager.closeDBConnection(resultSet, checkIsMasterPasswordValid, null);
            DbConnectionManager.closeDBConnection(null, changePasswordPS, con);
        }
        return false;
    }

    public final boolean changeUserEmail(String values) throws Exception {
        final Connection con = DbConnectionManager.getInstance().getConnection();
        PreparedStatement changeEmailPS = null;
        try {
            JSONObject jsonObject = new JSONObject(values);
            
        	if (ValidationManager.isEmailExist(jsonObject.getString(ApiConstant.EMAIL), con)) {
        		throw new RuntimeException(ApiConstant.EMAIL_ALREADY_EXIST_MESSAGE);
			}
                con.setAutoCommit(false);
                // update email
                changeEmailPS = con.prepareStatement(QueryConstant.CHANGE_USER_EMAIL);
                changeEmailPS.setString(1, jsonObject.getString(ApiConstant.EMAIL));
                changeEmailPS.setInt(2, jsonObject.getInt(ApiConstant.JSON_USER_ID));
                final int status = changeEmailPS.executeUpdate();
                if (status > 0) {
                    con.commit();
                    return true;
                } else {
                    throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
                }
            
        } catch (Exception ex) {
            sLog.d(this, ex);
            try {
                con.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throw ex;
        } finally {
            DbConnectionManager.closeDBConnection(null, changeEmailPS, con);
        }
    }
    
    /**
     * Resets new password of existing user before login.
     *
     * @param values Takes credentials in form of JSON string.
     * @throws Exception When there is any exception occurs.
     */
    public void setNewPassword(String values) throws Exception {

        PreparedStatement passVerificationPS, addPasswordPS, resetLoginLimitPS;

        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {

            JSONObject jsonObject = new JSONObject(values);
            conn.setAutoCommit(false);

            passVerificationPS = conn
                    .prepareStatement(DbConstant.CHANGE_PASSWORD_VERIFICATION_FROM_LAST_THREE_PASSWORD_QUERY);

            passVerificationPS.setString(1, jsonObject.getString(ApiConstant.USERNAME));
            passVerificationPS.setString(2,
                    PasswordUtil.encrypt(jsonObject.getString(ApiConstant.NEW_PASSWORD), ApiConstant.SECRET));

            ResultSet resultSet = passVerificationPS.executeQuery();

            if (!resultSet.next()) {

                addPasswordPS = conn.prepareStatement(DbConstant.ADD_NEW_PASSWORD_QUERY);

                addPasswordPS.setString(1,
                        PasswordUtil.encrypt(jsonObject.getString(ApiConstant.NEW_PASSWORD), ApiConstant.SECRET));
                addPasswordPS.setString(2,
                        PasswordUtil.encrypt(jsonObject.getString(ApiConstant.OLD_PASSWORD), ApiConstant.SECRET));
                addPasswordPS.setString(3, jsonObject.getString(ApiConstant.USERNAME));

                addPasswordPS.executeUpdate();
                addPasswordPS.close();
                
                // Reset the login attempt limit if any exists - user_login_limit
                
                resetLoginLimitPS = conn.prepareStatement(DbConstant.RESET_USER_LOGIN_LIMIT);
                resetLoginLimitPS.setString(1, jsonObject.getString(ApiConstant.USERNAME));
                resetLoginLimitPS.executeUpdate();
                resetLoginLimitPS.close();
                
            } else {
                throw new OldPasswordException(ApiConstant.INVALID_OLD_PASSWORD_MESSAGE);

            }

            resultSet.close();
            passVerificationPS.close();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } catch (OldPasswordException e) {
            throw new OldPasswordException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }

    /**
     * Resets new password of existing user via user profile.
     *
     * @param values Takes credentials in form of JSON string.
     * @throws Exception When there is any exception occurs.
     */
    public void changePassword(String values) throws Exception {

        PreparedStatement passVerificationPS, validateUserPS, disableUserCredentialPS, addNewUserCredential;

        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {

            JSONObject jsonObject = new JSONObject(values);
            conn.setAutoCommit(false);

            passVerificationPS = conn
                    .prepareStatement(DbConstant.CHANGE_PASSWORD_VERIFICATION_FROM_LAST_THREE_PASSWORD_QUERY);

            passVerificationPS.setString(1, DbConnectionManager.getInstance().getTokenManager().getUsername());
            passVerificationPS.setString(2,
                    PasswordUtil.encrypt(jsonObject.getString(ApiConstant.NEW_PASSWORD), ApiConstant.SECRET));

            ResultSet resultSet = passVerificationPS.executeQuery();

            if (!resultSet.next()) {

                validateUserPS = conn.prepareStatement(DbConstant.VALIDATE_A_USER);
                validateUserPS.setInt(1, DbConnectionManager.getInstance().getTokenManager().getUserId());
                validateUserPS.setString(2,
                        PasswordUtil.encrypt(jsonObject.getString(ApiConstant.OLD_PASSWORD), ApiConstant.SECRET));
                ResultSet verifyResultSet = validateUserPS.executeQuery();

                if (verifyResultSet.next()) {

                    disableUserCredentialPS = conn.prepareStatement(DbConstant.DISABLE_USER_CREDENTIALS);
                    disableUserCredentialPS.setInt(1, DbConnectionManager.getInstance().getTokenManager().getUserId());

                    disableUserCredentialPS.executeUpdate();
                    disableUserCredentialPS.close();

                    addNewUserCredential = conn.prepareStatement(DbConstant.ADD_NEW_USER_CREDENTIALS);
                    addNewUserCredential.setInt(1, DbConnectionManager.getInstance().getTokenManager().getUserId());
                    addNewUserCredential.setString(2,
                            PasswordUtil.encrypt(jsonObject.getString(ApiConstant.NEW_PASSWORD), ApiConstant.SECRET));
                    addNewUserCredential.setString(3, verifyResultSet.getString(ApiConstant.MASTER_PASSWORD));
                    addNewUserCredential.executeUpdate();
                    addNewUserCredential.close();
                } else {
                    throw new OldPasswordException(ApiConstant.WRONG_OLD_PASSWORD_MESSAGE);

                }

            } else {
                throw new OldPasswordException(ApiConstant.INVALID_OLD_PASSWORD_MESSAGE);

            }

            resultSet.close();
            passVerificationPS.close();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } catch (OldPasswordException e) {
            e.printStackTrace();
            throw new OldPasswordException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }

        // Now saving the logs in access-log
        addUserAccessLog(ApiConstant.PASSWORD_CHANGED,
                DbConnectionManager.getInstance().getTokenManager().getUsername(),
                ApiConstant.BLANK_QUOTE);

    }

    /**
     * Verify digital signature
     *
     * @param userJson Takes Json Object of digital signature.
     * @return True if verified else false.
     * @throws Exception
     */
    public boolean isDigitalSignatureSAAVerified(JSONObject userJson, boolean islockOut) throws SQLException,
            InvalidCredentialException, JSONException, NoSuchPaddingException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {

        if (DbConnectionManager.getInstance().getCfrStatus().getStatus() == 0 && !islockOut) {

            return true;

        }

        String username = userJson.getString(ApiConstant.DIGITAL_USERNAME);
        String password = PasswordUtil.encrypt(userJson.getString(ApiConstant.DIGITAL_PASSWORD), ApiConstant.SECRET);
        Connection conn = null;
        PreparedStatement checkUserExistPS = null;
        ResultSet checkUserExistRS = null;

        try {

            conn = DbConnectionManager.getInstance().getConnection();
            Logger.debug(this, "isDigitalSignatureMTVerified : Verifying digital signature...");
            // To verify the current login user and digital signature are same
            if (!DbConnectionManager.getInstance().getTokenManager().getUsername()
                    .equals(userJson.getString(ApiConstant.DIGITAL_USERNAME))) {
                throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
            }

            checkUserExistPS = conn.prepareStatement(DbConstant.CHECK_USER_CREDENTIALS_EXISTS);

            checkUserExistPS.setString(1, username);
            checkUserExistPS.setString(2, password);

            checkUserExistRS = checkUserExistPS.executeQuery();

            return checkUserExistRS.next();

        } catch (InvalidCredentialException ex) {
            ex.printStackTrace();
            throw new InvalidCredentialException(ex.getMessage());

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);

        } finally {
            DbConnectionManager.closeDBConnection(null, checkUserExistPS, conn);
        }

    }

    public boolean isDigitalSignatureMTVerified(JSONObject userJson, boolean isShutdownCall) throws SQLException,
            InvalidCredentialException, JSONException, NoSuchPaddingException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {

        if (DbConnectionManager.getInstance().getCfrStatus().getStatus() == 0 && !isShutdownCall) {

            return true;

        }

        String username = userJson.getString(ApiConstant.DIGITAL_USERNAME);
        String password = PasswordUtil.encrypt(userJson.getString(ApiConstant.DIGITAL_PASSWORD), ApiConstant.SECRET);

        Connection conn = null;
        PreparedStatement checkUserExistPS = null;
        ResultSet checkUserExistRS = null;

        try {
            conn = DbConnectionManager.getInstance().getConnection();
            Logger.debug(this, "isDigitalSignatureMTVerified : Verifying digital signature...");
            ArrayList<String> dsUserRoles = BasicUtility.getUserRole(userJson.getString(ApiConstant.DIGITAL_USERNAME));
            String[] validRoles = {ApiConstant.MANAGER, ApiConstant.TECHNICIAN};

            Logger.debug("isDigitalSignatureMTVerified : Digital user roles ", dsUserRoles);

            // To verify the current digital signature role and login user role is same
            if (!(dsUserRoles.contains(validRoles[0]) || dsUserRoles.contains(validRoles[1]))) {
                throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
            }

            checkUserExistPS = conn.prepareStatement(DbConstant.CHECK_USER_CREDENTIALS_EXISTS);

            checkUserExistPS.setString(1, username);
            checkUserExistPS.setString(2, password);

            checkUserExistRS = checkUserExistPS.executeQuery();

            return checkUserExistRS.next();

        } catch (InvalidCredentialException ex) {
            ex.printStackTrace();
            throw new InvalidCredentialException(ex.getMessage());

        } catch (SQLException sqlEx) {

            sqlEx.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);

        } finally {
            DbConnectionManager.closeDBConnection(checkUserExistRS, checkUserExistPS, conn);
        }

    }

    public boolean isDigitalSignatureManagerVerified(JSONObject userJson) throws SQLException,
            InvalidCredentialException, JSONException, NoSuchPaddingException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {

        if (DbConnectionManager.getInstance().getCfrStatus().getStatus() == 0) {

            return true;

        }

        String username = userJson.getString(ApiConstant.DIGITAL_USERNAME);
        String password = PasswordUtil.encrypt(userJson.getString(ApiConstant.DIGITAL_PASSWORD), ApiConstant.SECRET);

        Connection conn = null;
        PreparedStatement checkUserExistPS = null;
        ResultSet checkUserExistRS = null;

        try {
            conn = DbConnectionManager.getInstance().getConnection();
            Logger.debug(this, "isDigitalSignatureMTVerified : Verifying digital signature...");
            ArrayList<String> dsUserRoles = BasicUtility.getUserRole(userJson.getString(ApiConstant.DIGITAL_USERNAME));

            Logger.debug("isDigitalSignatureMTVerified : Digital user roles ", dsUserRoles);

            // To verify the current digital signature role and login user role is same
            if (!dsUserRoles.contains(ApiConstant.MANAGER)) {
                throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
            }

            checkUserExistPS = conn.prepareStatement(DbConstant.CHECK_USER_CREDENTIALS_EXISTS);

            checkUserExistPS.setString(1, username);
            checkUserExistPS.setString(2, password);

            checkUserExistRS = checkUserExistPS.executeQuery();

            return checkUserExistRS.next();

        } catch (InvalidCredentialException ex) {
            ex.printStackTrace();
            throw new InvalidCredentialException(ex.getMessage());

        } catch (SQLException sqlEx) {

            sqlEx.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);

        } finally {
            DbConnectionManager.closeDBConnection(checkUserExistRS, checkUserExistPS, conn);
        }

    }

    /************************************************
     * USER MANAGEMENT
     *********************************************************/

    /**
     * Adds a new user.
     *
     * @param values Takes user details in the form of JSON String.
     * @return JSONArray of errors if error exist else null.
     * @throws Exception When there is any exception occurs.
     */
    public JSONArray saveUser(String values) throws Exception {

        int adminRole = 0, techRole = 0;
        PreparedStatement createUserMasterPS = null, createUserDetailPS = null, addAdminRolePS = null,
                addTechRolePS = null, addUserCredentialPS = null;
        String middleName = null;
        int flag = 0;
        String dsNotes = ApiConstant.BLANK_QUOTE;

        int userId;

        JSONArray errorJsonArray = new JSONArray();
        JSONObject userJson = new JSONObject(values);
        String emailId = userJson.getString(ApiConstant.EMAIL_ID).toLowerCase();

        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {

            if (ValidationManager.isEmailExist(emailId, conn)) {
                flag = 1;
                errorJsonArray
                        .put(new JSONObject().put(ApiConstant.EMAIL_ERROR, ApiConstant.EMAIL_ALREADY_EXIST_MESSAGE));
            }
            if (ValidationManager.isUsernameExist(userJson.getString(ApiConstant.USERNAME), conn)) {
                flag = 1;
                errorJsonArray.put(
                        new JSONObject().put(ApiConstant.USERNAME_ERROR, ApiConstant.USERNAME_ALREADY_EXIST_MESSAGE));
            }

            if (flag == 1) {
				return errorJsonArray;
			}

            conn.setAutoCommit(false);

            if (isDigitalSignatureSAAVerified(userJson, false)) {
                if(userJson.has(ApiConstant.DIGITAL_NOTES)){
                    dsNotes = userJson.getString(ApiConstant.DIGITAL_NOTES);
                }

                createUserMasterPS = conn.prepareStatement(DbConstant.CREATE_NEW_USER_MASTER,
                        Statement.RETURN_GENERATED_KEYS);
                createUserMasterPS.setString(1, userJson.getString(ApiConstant.USERNAME));
                createUserMasterPS.setString(2, userJson.getString(ApiConstant.IS_ACTIVE));
                createUserMasterPS.setString(3, DbConnectionManager.getInstance().getTokenManager().getUsername());
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

                try {

                    middleName = userJson.getString(ApiConstant.MIDDLE_NAME);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                createUserDetailPS = conn.prepareStatement(DbConstant.CREATE_NEW_USER_DETAILS);
                createUserDetailPS.setInt(1, userId);
                createUserDetailPS.setString(2, userJson.getString(ApiConstant.PREFIX));
                createUserDetailPS.setString(3, userJson.getString(ApiConstant.FIRST_NAME));
                createUserDetailPS.setString(4, middleName);
                createUserDetailPS.setString(5, userJson.getString(ApiConstant.LAST_NAME));
                createUserDetailPS.setString(6, emailId);
                createUserDetailPS.setString(7, userJson.getString(ApiConstant.CONTACT_NUMBER));
                createUserDetailPS.setString(8, BasicUtility.getInstance().getFullName(userJson));
                adminRole = userJson.getInt(ApiConstant.ADMIN_ROLE);

                if (adminRole == 1 || adminRole == 2) {
                    addAdminRolePS = conn.prepareStatement(DbConstant.ADD_USER_ROLE_MAP);
                    addAdminRolePS.setInt(1, userId);
                    addAdminRolePS.setInt(2, adminRole);
                }

                try {
                    techRole = userJson.getInt(ApiConstant.TECH_ROLE);

                    if (techRole == 4 || techRole == 5) {
                        addTechRolePS = conn.prepareStatement(DbConstant.ADD_USER_ROLE_MAP);
                        addTechRolePS.setInt(1, userId);
                        addTechRolePS.setInt(2, BasicUtility.getTechRoleId(techRole));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                addUserCredentialPS = conn.prepareStatement(DbConstant.ADD_USER_CREDENTIALS);
                addUserCredentialPS.setInt(1, userId);
                addUserCredentialPS.setString(2,
                        PasswordUtil.encrypt(userJson.getString(ApiConstant.DEFAULT_PASSWORD), ApiConstant.SECRET));

                DbConnectionManager.executePrepStatementBatch(createUserDetailPS, addAdminRolePS, addTechRolePS,
                        addUserCredentialPS);

                createUserMasterPS.close();
                conn.commit();

                // Now saving the logs in access-log
                addUserAccessLog(
                        ApiConstant.CREATE_USER,
                        userJson.getString(ApiConstant.USERNAME),
                        dsNotes
                );

            } else {
                throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
            }
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());

        } catch (InvalidCredentialException e) {
            throw new InvalidCredentialException(e.getMessage());

        } catch (Exception e) {
            throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);

        }



        return null;

    }

    /**
     * Shows list of all the users.
     *
     * @return JSONArray of all existing users.
     * @throws SQLException When there is any Database error.
     */
    public JSONArray showAllUser() throws Exception {
        ResultSet userResultSet;
        JSONArray jsonArray = null;

        try (Connection conn = DbConnectionManager.getInstance().getConnection();
             Statement statement = conn.createStatement()) {

            Logger.setSAALogToFile(true);

            BasicUtility.systemPrint("inside", "hello");

            conn.setAutoCommit(false);

            userResultSet = statement.executeQuery(DbConstant.GET_ALL_USERS_DETAIL);

            jsonArray = JsonMapper.getJSONFromMToMResultSet(userResultSet, ApiConstant.RESULT_SET_SIZE);

            userResultSet.close();

            statement.close();
            conn.commit();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }

        BasicUtility.systemPrint("inside2", jsonArray);
        return jsonArray;
    }

    /**
     * Get detail of a particular user.
     *
     * @param userId Takes id of the user.
     * @return JsonArray of particular user detail.
     * @throws SQLException When there is any database error.
     */
    public JSONArray getSingleUserDetail(int userId) throws SQLException {

        ResultSet resultSet = null;
        JSONArray jsonArray = null;
        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {

            conn.setAutoCommit(false);

//            This query will return default password value if there is default password else null.
            PreparedStatement preparedStatement = conn.prepareStatement(DbConstant.GET_SINGLE_USER_DETAIL);
            preparedStatement.setInt(1, userId);

            resultSet = preparedStatement.executeQuery();

            jsonArray = JsonMapper.getSingleJSONFromMToMResultSet(resultSet, ApiConstant.RESULT_SET_SIZE);

            preparedStatement.close();
            resultSet.close();
            conn.commit();

        } catch (SQLException ex) {
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }

        return jsonArray;

    }

    /**
     * Updates a user profile detail.
     *
     * @param values Takes updated user detail as JSON string.
     * @return JSONArray of errors if error exist else null.
     * @throws Exception When there is any exception error.
     */
    public JSONArray updateSingleUserProfile(String values) throws Exception {

        PreparedStatement updateUserMasterProfilePS, updateUserDetailProfilePS;
        JSONObject userJson = new JSONObject(values);
        JSONArray errorJsonArray = new JSONArray();
        String emailId = userJson.getString(ApiConstant.EMAIL_ID).toLowerCase();
        int flag = 0;
        String dsNotes = ApiConstant.BLANK_QUOTE;

        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {

            int userId = userJson.getInt(ApiConstant.JSON_USER_ID);
            String currentDateTime = BasicUtility.getInstance().getCurrentTimestamp();

            if (ValidationManager.isEmailExist(emailId, userJson.getInt(ApiConstant.JSON_USER_ID), conn)) {
                errorJsonArray
                        .put(new JSONObject().put(ApiConstant.EMAIL_ERROR, ApiConstant.EMAIL_ALREADY_EXIST_MESSAGE));
                flag = 1;
            }

            if (flag == 1) {

                return errorJsonArray;
            }

            conn.setAutoCommit(false);
            if (isDigitalSignatureSAAVerified(userJson, false)) {

                if(userJson.has(ApiConstant.DIGITAL_NOTES)){
                    dsNotes = userJson.getString(ApiConstant.DIGITAL_NOTES);
                }

                updateUserMasterProfilePS = conn.prepareStatement(DbConstant.UPDATE_USER_MASTER_PROFILE);

                updateUserMasterProfilePS.setString(1, currentDateTime);
                updateUserMasterProfilePS.setString(2,
                        DbConnectionManager.getInstance().getTokenManager().getUsername());
                updateUserMasterProfilePS.setInt(3, userId);

                updateUserDetailProfilePS = conn.prepareStatement(DbConstant.UPDATE_USER_DETAILS_PROFILE);
                updateUserDetailProfilePS.setString(1, userJson.getString(ApiConstant.PREFIX));
                updateUserDetailProfilePS.setString(2, userJson.getString(ApiConstant.FIRST_NAME));
                updateUserDetailProfilePS.setString(3, userJson.getString(ApiConstant.MIDDLE_NAME));
                updateUserDetailProfilePS.setString(4, userJson.getString(ApiConstant.LAST_NAME));
                updateUserDetailProfilePS.setString(5, userJson.getString(ApiConstant.CONTACT_NUMBER));
                updateUserDetailProfilePS.setInt(6, userId);

                DbConnectionManager.executePrepStatementBatch(updateUserMasterProfilePS, updateUserDetailProfilePS,
                        null, null);
                conn.commit();

                // Now saving the logs in access-log
                addUserAccessLog(
                        ApiConstant.EDIT_PROFILE,
                        DbConnectionManager.getInstance().getTokenManager().getUsername(),
                        dsNotes
                );

            } else {
				throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
			}

        } catch (SQLException ex) {

            throw new SQLException(ex.getMessage());

        } catch (InvalidCredentialException ex) {

            throw new InvalidCredentialException(ex.getMessage());

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }


        return null;

    }

    /**
     * Get Profile details of a user.
     *
     * @return JSONArray of errors if error exist else null.
     * @throws SQLException When there is any Database error.
     */
    public JSONArray getSingleUserProfile() throws SQLException {

        ResultSet resultSet;
        JSONArray jsonArray;
        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {

            conn.setAutoCommit(false);

            PreparedStatement getSingleProfilePS = conn.prepareStatement(DbConstant.GET_SINGLE_USER_PROFILE);
            getSingleProfilePS.setString(1, DbConnectionManager.getInstance().getTokenManager().getUsername());

            resultSet = getSingleProfilePS.executeQuery();

            jsonArray = JsonMapper.getProfileJSONFromMToMResultSet(resultSet, ApiConstant.RESULT_SET_SIZE);

            getSingleProfilePS.close();
            resultSet.close();
            conn.commit();

        } catch (SQLException ex) {
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }

        return jsonArray;

    }

    /**
     * Update a user detail.
     *
     * @param values Takes updated user details as JSON String.
     * @throws InvalidCredentialException JSONArray of errors if error exist else
     *                                    null.
     * @throws SQLException               When there is any Database error.
     */
    public JSONArray updateUser(String values) throws Exception {

        PreparedStatement updateUserMasterPS = null, updateUserDetailPS = null, adminUserRolePS = null,
                techUserRolePS = null, addAdminRolePS = null, addTechRolePS = null, disableCredentialPS = null,
                addCredentialPS = null;

        JSONObject userJson = new JSONObject(values);
        JSONArray errorJsonArray = new JSONArray();
        System.out.println("-----------update values----------" + userJson);
        String emailId = userJson.getString(ApiConstant.EMAIL_ID).toLowerCase();
        int flag = 0;
        String dsNotes = ApiConstant.BLANK_QUOTE;

        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {

            int userId = userJson.getInt(ApiConstant.JSON_USER_ID);
            String currentDateTime = BasicUtility.getInstance().getCurrentTimestamp();

            int oldAdminRoleId, oldTechRoleId, newAdminRoleId, newTechRoleId;

            if (ValidationManager.isEmailExist(emailId, userJson.getInt(ApiConstant.JSON_USER_ID), conn)) {
                errorJsonArray
                        .put(new JSONObject().put(ApiConstant.EMAIL_ERROR, ApiConstant.EMAIL_ALREADY_EXIST_MESSAGE));
                flag = 1;
            }

            if (flag == 1) {

                return errorJsonArray;
            }

            conn.setAutoCommit(false);

            if (isDigitalSignatureSAAVerified(userJson, false)) {
                if(userJson.has(ApiConstant.DIGITAL_NOTES)){
                    dsNotes = userJson.getString(ApiConstant.DIGITAL_NOTES);
                }

                updateUserMasterPS = conn.prepareStatement(DbConstant.UPDATE_USER_MASTER);
                updateUserMasterPS.setString(1, currentDateTime);
                updateUserMasterPS.setString(2, userJson.getString(ApiConstant.IS_ACTIVE));
                updateUserMasterPS.setString(3, DbConnectionManager.getInstance().getTokenManager().getUsername());
                updateUserMasterPS.setInt(4, userId);

                updateUserDetailPS = conn.prepareStatement(DbConstant.UPDATE_USER_DETAILS);
                updateUserDetailPS.setString(1, userJson.getString(ApiConstant.PREFIX));
                updateUserDetailPS.setString(2, userJson.getString(ApiConstant.FIRST_NAME));
                updateUserDetailPS.setString(3, userJson.getString(ApiConstant.MIDDLE_NAME));
                updateUserDetailPS.setString(4, userJson.getString(ApiConstant.LAST_NAME));
                updateUserDetailPS.setString(5, userJson.getString(ApiConstant.CONTACT_NUMBER));
                updateUserDetailPS.setString(6, emailId);
                updateUserDetailPS.setInt(7, userId);

                if (!userJson.getString(ApiConstant.DEFAULT_PASSWORD).equals("") && !userJson
                        .getString(ApiConstant.DEFAULT_PASSWORD).equals(userJson.getString(ApiConstant.OLD_PASSWORD))) {
                    disableCredentialPS = conn.prepareStatement(DbConstant.DISABLE_USER_CREDENTIALS);
                    disableCredentialPS.setInt(1, userId);

                    addCredentialPS = conn.prepareStatement(DbConstant.ADD_USER_CREDENTIALS);
                    addCredentialPS.setInt(1, userId);
                    addCredentialPS.setString(2,
                            PasswordUtil.encrypt(userJson.getString(ApiConstant.DEFAULT_PASSWORD), ApiConstant.SECRET));

                }

                if (BasicUtility.getRoleId(userJson.getString(ApiConstant.NEW_ADMIN_ROLE)) == BasicUtility
                        .getRoleId(userJson.getString(ApiConstant.NEW_TECH_ROLE))) {

                    throw new SQLException(ApiConstant.INVALID_ROLE_SELECTION_MESSAGE);

                } else if ((BasicUtility.getRoleId(userJson.getString(ApiConstant.NEW_ADMIN_ROLE)) == 2)
                        && (BasicUtility.getRoleId(userJson.getString(ApiConstant.NEW_TECH_ROLE)) != 0)) {

                    throw new SQLException(ApiConstant.INVALID_ROLE_SELECTION_MESSAGE);

                }

                if ((userJson.getString(ApiConstant.NEW_ADMIN_ROLE)).equals(ApiConstant.AUDITOR)) {
                    newAdminRoleId = BasicUtility.getRoleId(userJson.getString(ApiConstant.NEW_ADMIN_ROLE));

                    adminUserRolePS = conn.prepareStatement(DbConstant.DELETE_ALL_USER_ROLE_MAP);
                    adminUserRolePS.setInt(1, userId);

                    techUserRolePS = conn.prepareStatement(DbConstant.ADD_USER_ROLE_MAP);
                    techUserRolePS.setInt(1, userId);
                    techUserRolePS.setInt(2, newAdminRoleId);

                } else {

                    // assign role to user
                    if ((userJson.getString(ApiConstant.OLD_ADMIN_ROLE)).equals(ApiConstant.NEITHER)
                            && !(userJson.getString(ApiConstant.NEW_ADMIN_ROLE)).equals(ApiConstant.NEITHER)) {
                        newAdminRoleId = BasicUtility.getRoleId(userJson.getString(ApiConstant.NEW_ADMIN_ROLE));

                        adminUserRolePS = conn.prepareStatement(DbConstant.ADD_USER_ROLE_MAP);
                        adminUserRolePS.setInt(1, userId);
                        adminUserRolePS.setInt(2, newAdminRoleId);

                    }
                    // update user role by disabling the old role and inserting new updated role
                    else if (!(userJson.getString(ApiConstant.OLD_ADMIN_ROLE)).equals(ApiConstant.NEITHER)
                            && !(userJson.getString(ApiConstant.NEW_ADMIN_ROLE)).equals(ApiConstant.NEITHER)
                            && !(userJson.getString(ApiConstant.OLD_ADMIN_ROLE))
                            .equals(userJson.getString(ApiConstant.NEW_ADMIN_ROLE))) {
                        newAdminRoleId = BasicUtility.getRoleId(userJson.getString(ApiConstant.NEW_ADMIN_ROLE));
                        oldAdminRoleId = BasicUtility.getRoleId(userJson.getString(ApiConstant.OLD_ADMIN_ROLE));

                        adminUserRolePS = conn.prepareStatement(DbConstant.DISABLE_USER_ROLE_MAP);
                        adminUserRolePS.setInt(1, userId);
                        adminUserRolePS.setInt(2, oldAdminRoleId);

                        addAdminRolePS = conn.prepareStatement(DbConstant.ADD_USER_ROLE_MAP);
                        addAdminRolePS.setInt(1, userId);
                        addAdminRolePS.setInt(2, newAdminRoleId);

                    }
                    // remove role from user by disabling the role.
                    else if (!(userJson.getString(ApiConstant.OLD_ADMIN_ROLE)).equals(ApiConstant.NEITHER)
                            && (userJson.getString(ApiConstant.NEW_ADMIN_ROLE)).equals(ApiConstant.NEITHER)) {
                        oldAdminRoleId = BasicUtility.getRoleId(userJson.getString(ApiConstant.OLD_ADMIN_ROLE));

                        adminUserRolePS = conn.prepareStatement(DbConstant.DISABLE_USER_ROLE_MAP);
                        adminUserRolePS.setInt(1, userId);
                        adminUserRolePS.setInt(2, oldAdminRoleId);
                    }

                    // for techRole
                    if ((userJson.getString(ApiConstant.OLD_TECH_ROLE)).equals(ApiConstant.NEITHER)
                            && !(userJson.getString(ApiConstant.NEW_TECH_ROLE)).equals(ApiConstant.NEITHER)) {
                        newTechRoleId = BasicUtility.getRoleId(userJson.getString(ApiConstant.NEW_TECH_ROLE));

                        techUserRolePS = conn.prepareStatement(DbConstant.ADD_USER_ROLE_MAP);
                        techUserRolePS.setInt(1, userId);
                        techUserRolePS.setInt(2, newTechRoleId);

                    }

                    // update user role by disabling the old role and inserting new updated role
                    else if (!(userJson.getString(ApiConstant.OLD_TECH_ROLE)).equals(ApiConstant.NEITHER)
                            && !(userJson.getString(ApiConstant.NEW_TECH_ROLE)).equals(ApiConstant.NEITHER)
                            && !(userJson.getString(ApiConstant.OLD_TECH_ROLE))
                            .equals(userJson.getString(ApiConstant.NEW_TECH_ROLE))) {
                        newTechRoleId = BasicUtility.getRoleId(userJson.getString(ApiConstant.NEW_TECH_ROLE));
                        oldTechRoleId = BasicUtility.getRoleId(userJson.getString(ApiConstant.OLD_TECH_ROLE));

                        techUserRolePS = conn.prepareStatement(DbConstant.DISABLE_USER_ROLE_MAP);
                        techUserRolePS.setInt(1, userId);
                        techUserRolePS.setInt(2, oldTechRoleId);

                        addTechRolePS = conn.prepareStatement(DbConstant.ADD_USER_ROLE_MAP);
                        addTechRolePS.setInt(1, userId);
                        addTechRolePS.setInt(2, newTechRoleId);

                    }

                    // remove role from user by disabling the role.
                    else if (!(userJson.getString(ApiConstant.OLD_TECH_ROLE)).equals(ApiConstant.NEITHER)
                            && (userJson.getString(ApiConstant.NEW_TECH_ROLE)).equals(ApiConstant.NEITHER)) {
                        oldTechRoleId = BasicUtility.getRoleId(userJson.getString(ApiConstant.OLD_TECH_ROLE));

                        techUserRolePS = conn.prepareStatement(DbConstant.DISABLE_USER_ROLE_MAP);
                        techUserRolePS.setInt(1, userId);
                        techUserRolePS.setInt(2, oldTechRoleId);

                    }
                }

                DbConnectionManager.executePrepStatementBatch(updateUserMasterPS, updateUserDetailPS, adminUserRolePS,
                        techUserRolePS, addAdminRolePS, addTechRolePS, disableCredentialPS, addCredentialPS);

                conn.commit();

            } else {
				throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
			}

        } catch (SQLException ex) {

            throw new SQLException(ex.getMessage());

        } catch (InvalidCredentialException ex) {

            throw new InvalidCredentialException(ex.getMessage());

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new Exception(ex.getMessage());

        }

        // Now saving the logs in access-log
        if (userJson.getString(ApiConstant.IS_EDITED).equals(ApiConstant.TRUE)) {
            addUserAccessLog(ApiConstant.EDIT_USER, userJson.getString(ApiConstant.USERNAME), dsNotes);
        }

        if (!userJson.getString(ApiConstant.OLD_PASSWORD).equals(userJson.getString(ApiConstant.DEFAULT_PASSWORD))) {
            addUserAccessLog(ApiConstant.RESET_PASSWORD, userJson.getString(ApiConstant.USERNAME), dsNotes);
        }

        if (!userJson.getString(ApiConstant.OLD_ADMIN_ROLE).equals(userJson.getString(ApiConstant.NEW_ADMIN_ROLE))
                || !userJson.getString(ApiConstant.OLD_TECH_ROLE)
                .equals(userJson.getString(ApiConstant.NEW_TECH_ROLE))) {
            addUserAccessLog(ApiConstant.ROLE_CHANGED, userJson.getString(ApiConstant.USERNAME), dsNotes);
        }

        if (!userJson.getString(ApiConstant.IS_ACTIVE).equals(userJson.getString(ApiConstant.OLD_IS_ACTIVE))) {
            addUserAccessLog(ApiConstant.STATUS_CHANGED, userJson.getString(ApiConstant.USERNAME), dsNotes);
        }

        return null;
    }

    /****************************************************
     * Access Log
     ***********************************************************/

    /**
     * Adds user access log.
     *
     * @param action      Takes action which is performed.
     * @param performedOn Takes performedOn on which action is being performed.
     * @throws SQLException When there is any Database error.
     */
    public void addUserAccessLog(String action, String performedOn, String dsNote) throws SQLException {

        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {
            PreparedStatement addAccessLogPS = conn.prepareStatement(DbConstant.ADD_USER_ACCESS_LOG_QUERY);
            addAccessLogPS.setString(1, DbConnectionManager.getInstance().getTokenManager().getUsername());
            addAccessLogPS.setString(2, DbConnectionManager.getInstance().getTokenManager().getUserFullName());
            addAccessLogPS.setString(3, DbConnectionManager.getInstance().getTokenManager().getUserType());
            addAccessLogPS.setString(4, BasicUtility.getInstance().getCurrentTimestamp());
            addAccessLogPS.setString(5, action);
            addAccessLogPS.setString(6, performedOn);
            addAccessLogPS.setString(7, dsNote);
            addAccessLogPS.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }

    }


    /**
     * Shows all user access logs.
     *
     * @return JSONArray of all the user access log.
     * @throws SQLException When there is any Database error.
     */
    public JSONArray showUserAccessLog() throws SQLException {

        JSONArray logJsonArray;

        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {

            PreparedStatement getAccessLogPS = conn.prepareStatement(DbConstant.GET_ALL_USER_ACCESS_LOG);

            ResultSet resultSet = getAccessLogPS.executeQuery();

            logJsonArray = JsonMapper.getJSONFromResultSet(resultSet);

        } catch (SQLException e) {
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }

        return logJsonArray;

    }

    /***************************************************
     * Manager/Technician
     ********************************************/

    /**
     * @return
     * @throws SQLException
     */
    public JSONArray getTrialModes() throws SQLException {

        JSONArray modesJsonArray;

        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {

            PreparedStatement getModePS = conn.prepareStatement(DbConstant.GET_ALL_TRIAL_MODES);
            ResultSet getModeRS = getModePS.executeQuery();

            modesJsonArray = JsonMapper.getJSONFromResultSet(getModeRS);

            getModePS.close();
            getModeRS.close();

        } catch (SQLException e) {
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }
        System.out.println("-----------jarray---" + modesJsonArray);

        return modesJsonArray;

    }

    /**
     * @param modeStep
     * @return
     * @throws Exception
     */
    public JSONArray getEndPoint(String modeStep) throws Exception {

        JSONArray endPointJsonArray;

        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {

            PreparedStatement getEndPointPS = null;

            if (modeStep.equals(ApiConstant.CONCENTRATION)
                    || modeStep.equals(ApiConstant.CONSTANT_FEED_CONCENTRATION)) {

                getEndPointPS = conn.prepareStatement(DbConstant.GET_C_END_POINT);
            } else if (modeStep.equals(ApiConstant.DIAFILTRATION)) {

                getEndPointPS = conn.prepareStatement(DbConstant.GET_D_END_POINT);
            } else {
                throw new Exception(ApiConstant.INVALID_MODE_PLEASE_SELECT_VALID_MODE);
            }
            ResultSet resultSet = getEndPointPS.executeQuery();

            endPointJsonArray = JsonMapper.getJSONFromResultSet(resultSet);

            endPointJsonArray = EndPointUtil.getInstance().filterAuxEndPoints(endPointJsonArray);
//
            sLog.d(this, endPointJsonArray);

            getEndPointPS.close();
            resultSet.close();

        } catch (SQLException e) {
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return endPointJsonArray;
    }

    /**
     * @param modeId
     * @return
     * @throws SQLException
     */
    public JSONArray getPump(int modeId) throws SQLException {

        JSONArray pumpJsonArray;
        PreparedStatement getPumpPS = null;
        ResultSet getPumpRS = null;
        Connection conn = null;

        try {
            conn = DbConnectionManager.getInstance().getConnection();
            getPumpPS = conn.prepareStatement(DbConstant.GET_ALL_PUMP_COMPATIBLE_WITH_MODE_QUERY);
            getPumpPS.setInt(1, modeId);
            getPumpRS = getPumpPS.executeQuery();

            pumpJsonArray = JsonMapper.getJSONFromResultSet(getPumpRS);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);

        } finally {
            DbConnectionManager.closeDBConnection(getPumpRS, getPumpPS, conn);

        }
        System.out.println("-----------jarray-1-------------" + pumpJsonArray);

        return pumpJsonArray;

    }

    // todo tubing lookup changed.
    public JSONArray getTubeSpec(int pumpId) throws SQLException {

        JSONArray tubeJsonArray;
        PreparedStatement getTubingSpecPS = null;
        ResultSet getTubingSpecRS = null;
        Connection conn = null;

        try {

            conn = DbConnectionManager.getInstance().getConnection();
            getTubingSpecPS = conn.prepareStatement(DbConstant.GET_TUBING_SPEC_COMPATIBLE_WITH_PUMP_QUERY);
            getTubingSpecPS.setInt(1, pumpId);
            getTubingSpecRS = getTubingSpecPS.executeQuery();

            tubeJsonArray = JsonMapper.getTubingJSONFromResultSet(getTubingSpecRS);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } finally {
            DbConnectionManager.closeDBConnection(getTubingSpecRS, getTubingSpecPS, conn);
        }

        System.out.println("-----------jarray-1--" + tubeJsonArray);

        return tubeJsonArray;

    }

    public JSONArray getAuxTubeSpec(String auxPumpName) throws SQLException {

        JSONArray tubeJsonArray;
        PreparedStatement getTubingSpecPS;
        PreparedStatement getAuxPumpIdPS = null;
        ResultSet getAuxPumpIdRS = null;
        ResultSet getTubingSpecRS;

        int auxPumpId = 0;

        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {

            getAuxPumpIdPS = conn.prepareStatement(DbConstant.GET_PUMP_ID_QUERY);
            getAuxPumpIdPS.setString(1, auxPumpName);

            getAuxPumpIdRS = getAuxPumpIdPS.executeQuery();

            if (getAuxPumpIdRS.next()) {
                auxPumpId = getAuxPumpIdRS.getInt(ApiConstant.PUMP_LOOKUP_ID);
            }

            getTubingSpecPS = conn.prepareStatement(DbConstant.GET_TUBING_SPEC_COMPATIBLE_WITH_PUMP_QUERY);
            getAuxPumpIdPS.setInt(1, auxPumpId);

            getTubingSpecRS = getTubingSpecPS.executeQuery();

            tubeJsonArray = JsonMapper.getJSONFromResultSet(getTubingSpecRS);

            getTubingSpecPS.close();
            getTubingSpecRS.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }
        System.out.println("-----------jarray-1--" + tubeJsonArray);

        return tubeJsonArray;
    }

    public JSONArray getTubeCompatibleAux(int tubingLookupId) throws SQLException {

        JSONArray tubeJsonArray;
        PreparedStatement getTubingSpecPS;
        PreparedStatement getAuxPumpIdPS = null;
        ResultSet getTubingSpecRS;

        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {

            getTubingSpecPS = conn.prepareStatement(DbConstant.GET_TUBE_COMPATIBLE_AUX_PUMP_QUERY);
            getTubingSpecPS.setInt(1, tubingLookupId);

            getTubingSpecRS = getTubingSpecPS.executeQuery();

            tubeJsonArray = JsonMapper.getJSONFromResultSet(getTubingSpecRS);

            getTubingSpecPS.close();
            getTubingSpecRS.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }
        System.out.println("-----------jarray-1--" + tubeJsonArray);

        return tubeJsonArray;
    }

    public JSONArray getDropdownValues(String dropdownKey) throws SQLException, NotFoundException {

        JSONArray dropdownJsonArray;

        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;

            if (ApiConstant.RAMP_UP_TIME.equalsIgnoreCase(dropdownKey)) {

                preparedStatement = conn.prepareStatement(DbConstant.GET_ALL_RAMP_UP_TIME);
                resultSet = preparedStatement.executeQuery();
            } else if (ApiConstant.HOLLOW_FIBER.equalsIgnoreCase(dropdownKey)) {

                preparedStatement = conn.prepareStatement(DbConstant.GET_ALL_HOLLOW_FIBER);
                resultSet = preparedStatement.executeQuery();
            } else if (ApiConstant.RECIRC_PRESSURE_UNIT.equalsIgnoreCase(dropdownKey)) {

                preparedStatement = conn.prepareStatement(DbConstant.GET_RECIRC_FLOW_UNIT);
                resultSet = preparedStatement.executeQuery();
            } else if (ApiConstant.TUBING_SIZE.equalsIgnoreCase(dropdownKey)) {

                preparedStatement = conn.prepareStatement(DbConstant.GET_ALL_TUBING_DETAILS);
                resultSet = preparedStatement.executeQuery();

            } else if (ApiConstant.CASSETTE.equalsIgnoreCase(dropdownKey)) {

                preparedStatement = conn.prepareStatement(DbConstant.GET_ALL_CASSETTE_DETAILS);
                resultSet = preparedStatement.executeQuery();

            } else if (ApiConstant.FILTER_PLATE_INSERT.equalsIgnoreCase(dropdownKey)) {

                preparedStatement = conn.prepareStatement(DbConstant.GET_ALL_FILTER_PLATE_INSERT);
                resultSet = preparedStatement.executeQuery();

            } else {
                throw new NotFoundException(ApiConstant.NOT_FOUND);
            }

            dropdownJsonArray = JsonMapper.getJSONFromResultSet(resultSet);

            preparedStatement.close();
            resultSet.close();

        } catch (SQLException e) {

            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);

        } catch (NotFoundException e) {

            throw new NotFoundException(ApiConstant.NOT_FOUND);

        }

        return dropdownJsonArray;

    }

    // not in use but saved for future change
    public JSONArray getAuxAndMainPumpCompatibleTube(int pumpId) {

        PreparedStatement getMainPumpNamePS, getAuxPumpNamePS, getPumpCompatibleTubePS;
        ResultSet getPumpNameRS, getAuxPumpNameRS, getPumpCompatibleTubeRS;
        String pump_name = null;
        String getPumpCompatibleTubeQuery;
        JSONArray pumpCompatibleTubeJsonArray = null;

        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {

            getMainPumpNamePS = conn.prepareStatement(DbConstant.GET_PUMP_FROM_PUMP_ID);
            getMainPumpNamePS.setInt(1, pumpId);
            getPumpNameRS = getMainPumpNamePS.executeQuery();

            if (getPumpNameRS != null) {
                pump_name = getPumpNameRS.getString(ApiConstant.PUMP_NAME);

            }

            getAuxPumpNamePS = conn.prepareStatement(DbConstant.GET_AUX_PUMP_COMPATIBLE_WITH_MAIN_PUMP);
            getAuxPumpNamePS.setString(1, pump_name);
            getAuxPumpNameRS = getAuxPumpNamePS.executeQuery();

            getPumpCompatibleTubeQuery = DbConstant.GET_AUX_AND_MAIN_PUMP_COMPATIBLE_TUBING_SIZE_FIRST_HALF + pump_name
                    + DbConstant.GET_AUX_AND_MAIN_PUMP_COMPATIBLE_TUBING_SIZE_SECOND_HALF;

            while (getAuxPumpNameRS.next()) {

                getPumpCompatibleTubeQuery = getPumpCompatibleTubeQuery + " or \""
                        + getAuxPumpNameRS.getString(ApiConstant.AUX_PUMP) + "\" = 'TRUE'";

            }

            System.out.println("------------main query------------" + getPumpCompatibleTubeQuery);

            getPumpCompatibleTubePS = conn.prepareStatement(getPumpCompatibleTubeQuery);
            getPumpCompatibleTubeRS = getPumpCompatibleTubePS.executeQuery();

            pumpCompatibleTubeJsonArray = JsonMapper.getJSONFromResultSet(getPumpCompatibleTubeRS);

        } catch (Exception e) {

        }

        return pumpCompatibleTubeJsonArray;
    }

    public JSONArray saveRecipe(JSONObject recipeJson) throws Exception {

        Logger.setRecipeOutputLogToFile(true);
        RecipeMapper recipeMapper = new RecipeMapper();

        System.out.println("---------1------------");
        sLog.d(this, recipeJson);
        PreparedStatement trialMasterPS, modeMasterPS, filterPS = null, pumpMasterPS, endPointPS, auxPumpPS,
                abvMasterPS, kfConduitPS = null, trialDetailPS, disableTrialMasterPS;
        int trialMasterId;
        int loopCount = 1;
        Connection conn = null;

        JSONArray errorJsonArray = new JSONArray();
        try {

            conn = DbConnectionManager.getInstance().getConnection();

            conn.setAutoCommit(false);

            if (ValidationManager.isRecipeExist(recipeJson.getString(ApiConstant.RECIPE_NAME), conn)) {

                throw new InvalidRecipeException(ApiConstant.RECIPE_ERROR_MESSSAGE);
            }

            // Validate recipe before saving to db
            ValidationManager.isRecipeValidForSave(recipeJson, conn);

            // saving into trial master and get trial_master_id
            trialMasterPS = conn.prepareStatement(DbConstant.CREATE_NEW_TRIAL_MASTER, Statement.RETURN_GENERATED_KEYS);
            trialMasterPS = recipeMapper.insertTrialMasterSetting(trialMasterPS, recipeJson);
            trialMasterPS.executeUpdate();

            try (ResultSet generatedKeys = trialMasterPS.getGeneratedKeys()) {
                if (generatedKeys.next()) {

                    trialMasterId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
                }
            }

            // saving into mode_master
            modeMasterPS = conn.prepareStatement(DbConstant.CREATE_NEW_MODE_MASTER);
            modeMasterPS.setString(1, recipeJson.getString(ApiConstant.MODE_NAME));
            modeMasterPS.setInt(2, trialMasterId);
            modeMasterPS.executeUpdate();

            // saving into filter and perm table
            if (ApiConstant.FILTER_TYPE_IS_HOLLOW_FIBER
                    .equalsIgnoreCase(recipeJson.getString(ApiConstant.FILTER_TYPE))) {

                filterPS = conn.prepareStatement(DbConstant.ADD_NEW_FILTER_PERM_MASTER_FOR_HOLLOW_FIBER);
                recipeMapper.insertHollowFiberRecipeSetting(filterPS, recipeJson, trialMasterId).executeUpdate();

            } else if (ApiConstant.FILTER_TYPE_IS_CASSETTE
                    .equalsIgnoreCase(recipeJson.getString(ApiConstant.FILTER_TYPE))) {

                filterPS = conn.prepareStatement(DbConstant.ADD_NEW_FILTER_PERM_MASTER_FOR_CASSETTE);
                recipeMapper.insertCassetteRecipeSetting(filterPS, recipeJson, trialMasterId).executeUpdate();
            }

            // saving into pump_master
            pumpMasterPS = conn.prepareStatement(DbConstant.ADD_NEW_PUMP_MASTER);
            recipeMapper.insertPumpRecipeSetting(pumpMasterPS, recipeJson, trialMasterId).executeUpdate();

            if (BasicUtility.getInstance().isRunMode(recipeJson.getString(ApiConstant.MODE_NAME))) {

                // saving into end_point
                while (loopCount <= recipeJson.getInt(ApiConstant.TOTAL_STEPS)) {

                    endPointPS = conn.prepareStatement(DbConstant.ADD_NEW_END_POINT);
                    endPointPS = recipeMapper.insertEndPointRecipeSetting(endPointPS, recipeJson, trialMasterId,
                            loopCount);
                    endPointPS.executeUpdate();
                    endPointPS.close();
                    loopCount++;
                }

                loopCount = 1;

            } else {

                new NonRunManager().addTargetStep(recipeJson, trialMasterId, conn);

            }

            // saving into aux_pump_master
            while (loopCount <= recipeJson.getInt(ApiConstant.NO_OF_AUX_PUMP)) {

                auxPumpPS = conn.prepareStatement(DbConstant.ADD_NEW_AUX_PUMP_MASTER);
                auxPumpPS = recipeMapper.insertAuxPumpRecipeSetting(auxPumpPS, recipeJson, trialMasterId, loopCount);
                auxPumpPS.executeUpdate();
                auxPumpPS.close();

                loopCount++;
            }

            loopCount = 1;

            // saving into abv_master
            while (loopCount <= recipeJson.getInt(ApiConstant.TOTAL_ABV)) {

                abvMasterPS = conn.prepareStatement(DbConstant.ADD_NEW_ABV_MASTER);
                abvMasterPS = recipeMapper.insertAbvRecipeSetting(abvMasterPS, recipeJson, trialMasterId, loopCount);
                abvMasterPS.executeUpdate();
                abvMasterPS.close();

                loopCount++;

            }

            // saving into kf_conduit_setting

//            if (recipeJson.getInt(ApiConstant.TOTAL_ABV) < 2) {

            kfConduitPS = conn.prepareStatement(DbConstant.ADD_NEW_KF_KONDUIT_SETTING);
            kfConduitPS = recipeMapper.insertKfKonduitRecipeSetting(kfConduitPS, recipeJson, trialMasterId);
            kfConduitPS.executeUpdate();
            kfConduitPS.close();
//            }

            // saving into trial_detail
            trialDetailPS = conn.prepareStatement(DbConstant.ADD_NEW_TRIAL_DETAIL);
            trialDetailPS = recipeMapper.insertTrialDetailSetting(trialDetailPS, recipeJson, trialMasterId);
            trialDetailPS.executeUpdate();

            trialMasterPS.close();
            modeMasterPS.close();
            filterPS.close();
            pumpMasterPS.close();
            trialDetailPS.close();
            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                conn.rollback();
            }
            throw new SQLException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            if (conn != null) {
                conn.rollback();
            }
            throw new Exception(e.getMessage());
        } finally {
            DbConnectionManager.closeDBConnection(null, null, conn);

            // Redirect print stream to console.
//            Logger.setRecipeOutputLogToFile(false);
        }

        /* returns jsonArray of errors during backend validation */
        return errorJsonArray;

    }

    public JSONArray getRecipe(int trialMasterId) throws Exception {

        JSONArray recipeJsonArray = new JSONArray();
        JSONObject recipeJson = new JSONObject();
        JSONArray endPointJsonArray = null;
        JSONArray auxPumpJsonArray = null;
        JSONArray abvJsonArray = null;
        JSONObject pumpMinMaxFlowRateJson = null;
        JSONObject pumpMinMaxSpeedJson = null;
        JSONObject permMinMaxFlowRateJson = null;
        boolean isGeneric = false;

        PreparedStatement trialMasterPS, modeMasterPS, filterPS = null, pumpMasterPS, endPointPS, auxPumpPS,
                abvMasterPS, kfConduitPS = null, trialDetailPS;
        ResultSet trialMasterRS, modeMasterRS, filterRS = null, pumpMasterRS, endPointRS, auxPumpRS, abvMasterRS,
                kfConduitRS = null, trialDetailRS;

        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {

            // get trial_master details
            trialMasterPS = conn.prepareStatement(DbConstant.GET_TRIAL_MASTER_FROM_RECIPE);
            trialMasterPS.setInt(1, trialMasterId);
            trialMasterRS = trialMasterPS.executeQuery();

            if (trialMasterRS.next()) {

                trialMasterId = trialMasterRS.getInt(ApiConstant.TRIAL_MASTER_ID);
                recipeJson.put(ApiConstant.RECIPE_NAME, trialMasterRS.getString(ApiConstant.RECIPE_NAME));
                recipeJson.put(ApiConstant.TRIAL_TYPE, trialMasterRS.getString(ApiConstant.TRIAL_TYPE));
                recipeJson.put(ApiConstant.TRIAL_MASTER_ID, trialMasterRS.getString(ApiConstant.TRIAL_MASTER_ID));
                recipeJson.put(ApiConstant.CREATED_ON, trialMasterRS.getString(ApiConstant.CREATED_ON));
                recipeJson.put(ApiConstant.CREATED_BY, trialMasterRS.getString(ApiConstant.CREATED_BY));
                recipeJson.put(ApiConstant.IS_ACTIVE, trialMasterRS.getString(ApiConstant.IS_ACTIVE));

            } else {

                throw new InvalidRecipeException(ApiConstant.RECIPE_DOES_NOT_EXIST_ERROR_MESSAGE);
            }

            // get trial_details
            trialDetailPS = conn.prepareStatement(DbConstant.GET_TRIAL_DETAIL);
            trialDetailPS.setInt(1, trialMasterId);
            trialDetailRS = trialDetailPS.executeQuery();

            if (trialDetailRS.next()) {

                recipeJson.put(ApiConstant.NOTES, trialDetailRS.getString(ApiConstant.NOTES));
                recipeJson.put(ApiConstant.RUN_AS_SAFE, trialDetailRS.getString(ApiConstant.RUN_AS_SAFE));
                recipeJson.put(ApiConstant.FEED_TO_EMPTY, trialDetailRS.getString(ApiConstant.FEED_TO_EMPTY));
                recipeJson.put(ApiConstant.NO_OF_AUX_PUMP, trialDetailRS.getString(ApiConstant.NO_OF_AUX_PUMP));
                recipeJson.put(ApiConstant.PERMEATE_STOP_FIRST,
                        trialDetailRS.getString(ApiConstant.PERMEATE_STOP_FIRST));
                recipeJson.put(ApiConstant.RECIRCULATION_PRESSURE_CONTROL,
                        trialDetailRS.getString(ApiConstant.RECIRCULATION_PRESSURE_CONTROL));
                recipeJson.put(ApiConstant.RECIRCULATION_RATE_PER_UNIT,
                        trialDetailRS.getString(ApiConstant.RECIRCULATION_RATE_PER_UNIT));
                recipeJson.put(ApiConstant.RECIRCULATION_PRESSURE_UNIT_VALUE,
                        trialDetailRS.getString(ApiConstant.RECIRCULATION_PRESSURE_UNIT_VALUE));
                recipeJson.put(ApiConstant.RETENTATE_TUBE_SIZE,
                        trialDetailRS.getString(ApiConstant.RETENTATE_TUBE_SIZE));
                recipeJson.put(ApiConstant.END_POINT_CAL, trialDetailRS.getInt(ApiConstant.END_POINT_CAL));
                recipeJson.put(ApiConstant.DIGITAL_NOTES, trialDetailRS.getString(ApiConstant.DIGITAL_NOTES));

            }

            // get mode_master detail
            modeMasterPS = conn.prepareStatement(DbConstant.GET_MODE_MASTER);
            modeMasterPS.setInt(1, trialMasterId);
            modeMasterRS = modeMasterPS.executeQuery();

            if (modeMasterRS.next()) {

                recipeJson.put(ApiConstant.MODE_NAME, modeMasterRS.getString(ApiConstant.MODE_NAME));
                recipeJson.put(ApiConstant.MODE_FULL_NAME, modeMasterRS.getString(ApiConstant.MODE_FULL_NAME));
                recipeJson.put(ApiConstant.PCV, modeMasterRS.getString(ApiConstant.PCV));
                recipeJson.put(ApiConstant.EXCPECTED_YIELD_FC, modeMasterRS.getString(ApiConstant.EXCPECTED_YIELD_FC));
                recipeJson.put(ApiConstant.CELL_CULTURE_VOL_CF,
                        modeMasterRS.getString(ApiConstant.CELL_CULTURE_VOL_CF));
                recipeJson.put(ApiConstant.FILTER_RETENTION, modeMasterRS.getString(ApiConstant.FILTER_RETENTION));

            }

            // get end_point details
            endPointPS = conn.prepareStatement(DbConstant.GET_ALL_ENDPOINTS_RELATED_TO_TRIAL);
            endPointPS.setInt(1, trialMasterId);
            endPointRS = endPointPS.executeQuery();

            endPointJsonArray = JsonMapper.getJSONFromResultSet(endPointRS);
            recipeJson.put(ApiConstant.END_POINTS, endPointJsonArray);

            // add target step for non-run modes
            recipeJson.put(ApiConstant.TARGET_STEP, new NonRunManager().getTargetStep(trialMasterId));

            // get filter_perm_master details
            filterPS = conn.prepareStatement(DbConstant.GET_FILTER_PERM_MASTER_DETAILS);
            filterPS.setInt(1, trialMasterId);
            filterRS = filterPS.executeQuery();

            if (filterRS.next()) {

                if (ApiConstant.TRUE.equalsIgnoreCase(filterRS.getString(ApiConstant.IS_GENERIC))) {
                    isGeneric = true;
                }

                recipeJson.put(ApiConstant.FILTER_TYPE, filterRS.getString(ApiConstant.FILTER_TYPE));
                recipeJson.put(ApiConstant.PART_NO, filterRS.getString(ApiConstant.PART_NO));
                recipeJson.put(ApiConstant.FIBER_COUNT, filterRS.getString(ApiConstant.FIBER_COUNT));
                recipeJson.put(ApiConstant.FIBER_ID, filterRS.getString(ApiConstant.FIBER_ID));
                recipeJson.put(ApiConstant.SURF_AREA, filterRS.getString(ApiConstant.SURF_AREA));
                recipeJson.put(ApiConstant.ECS, filterRS.getString(ApiConstant.ECS));
                recipeJson.put(ApiConstant.PERMEATE_TUBE_SIZE, filterRS.getString(ApiConstant.PERMEATE_TUBE_SIZE));
                recipeJson.put(ApiConstant.TUBE_LENGTH, filterRS.getString(ApiConstant.TUBE_LENGTH));
                recipeJson.put(ApiConstant.PERM_TUBE_HOLDUP, filterRS.getString(ApiConstant.PERM_TUBE_HOLDUP));
                recipeJson.put(ApiConstant.TOTAL_PERM_TUBE_HOLDUP,
                        filterRS.getDouble(ApiConstant.TOTAL_PERM_TUBE_HOLDUP));
                recipeJson.put(ApiConstant.NO_OF_CASSETTE, filterRS.getString(ApiConstant.NO_OF_CASSETTE));
                recipeJson.put(ApiConstant.HEIGHT, filterRS.getString(ApiConstant.HEIGHT));
                recipeJson.put(ApiConstant.WIDTH, filterRS.getString(ApiConstant.WIDTH));
                recipeJson.put(ApiConstant.PERMEATE_HOLD_UP_VOLUME_CALCULATION,
                        filterRS.getString(ApiConstant.PERMEATE_HOLD_UP_VOLUME_CALCULATION));
                recipeJson.put(ApiConstant.NO_OF_CHANNEL, filterRS.getString(ApiConstant.NO_OF_CHANNEL));
                recipeJson.put(ApiConstant.FILTER_PLATE_INSERT_PART_NO,
                        filterRS.getString(ApiConstant.FILTER_PLATE_INSERT_PART_NO));
                recipeJson.put(ApiConstant.IS_GENERIC, isGeneric);
                recipeJson.put(ApiConstant.FILTER_MODEL_NAME, filterRS.getString(ApiConstant.FILTER_MODEL_NAME));
                recipeJson.put(ApiConstant.FILTER_MANUFACTURER_NAME, filterRS.getString(ApiConstant.FILTER_MANUFACTURER_NAME));
            }

            // get pump_master details
            pumpMasterPS = conn.prepareStatement(DbConstant.GET_PUMP_MASTER_DETAILS);
            pumpMasterPS.setInt(1, trialMasterId);
            pumpMasterRS = pumpMasterPS.executeQuery();

            if (pumpMasterRS.next()) {
                recipeJson.put(ApiConstant.PUMP_ID, pumpMasterRS.getString(ApiConstant.PUMP_ID));
                recipeJson.put(ApiConstant.HEAD_COUNT, pumpMasterRS.getString(ApiConstant.HEAD_COUNT));
                recipeJson.put(ApiConstant.PUMP_NAME, pumpMasterRS.getString(ApiConstant.PUMP_NAME));
                recipeJson.put(ApiConstant.PUMP_TYPE, pumpMasterRS.getString(ApiConstant.PUMP_TYPE));
                recipeJson.put(ApiConstant.DIRECTION, pumpMasterRS.getString(ApiConstant.DIRECTION));
                recipeJson.put(ApiConstant.FLOW_CONTROL, pumpMasterRS.getString(ApiConstant.FLOW_CONTROL));
                recipeJson.put(ApiConstant.PUMP_TUBING_SIZE, pumpMasterRS.getString(ApiConstant.PUMP_TUBING_SIZE));
                recipeJson.put(ApiConstant.PUMP_TUBING_LOOKUP_ID,
                        pumpMasterRS.getString(ApiConstant.PUMP_TUBING_LOOKUP_ID));
                recipeJson.put(ApiConstant.FLOW_RATE, pumpMasterRS.getString(ApiConstant.FLOW_RATE));
                recipeJson.put(ApiConstant.RAMP_UP_TIME, pumpMasterRS.getString(ApiConstant.RAMP_UP_TIME));
                recipeJson.put(ApiConstant.DELTA_P, pumpMasterRS.getString(ApiConstant.DELTA_P));
                recipeJson.put(ApiConstant.DELTA_P_RATE, pumpMasterRS.getString(ApiConstant.DELTA_P_RATE));
                recipeJson.put(ApiConstant.DELTA_P_DURATION, pumpMasterRS.getString(ApiConstant.DELTA_P_DURATION));
                recipeJson.put(ApiConstant.PUMP_RPM, pumpMasterRS.getString(ApiConstant.PUMP_RPM));
                recipeJson.put(ApiConstant.IS_SPEED, pumpMasterRS.getInt(ApiConstant.IS_SPEED));
            }

            // get aux_pump_master details
            auxPumpPS = conn.prepareStatement(DbConstant.GET_AUX_PUMP_DETAILS);
            auxPumpPS.setInt(1, trialMasterId);
            auxPumpRS = auxPumpPS.executeQuery();

            auxPumpJsonArray = JsonMapper.getJSONFromResultSet(auxPumpRS);
            recipeJson.put(ApiConstant.AUX_PUMP, auxPumpJsonArray);

            // get abv_master details
            abvMasterPS = conn.prepareStatement(DbConstant.GET_ABV_MASTER_DETAILS);
            abvMasterPS.setInt(1, trialMasterId);
            abvMasterRS = abvMasterPS.executeQuery();

            abvJsonArray = JsonMapper.getJSONFromResultSet(abvMasterRS);
            recipeJson.put(ApiConstant.ABV, abvJsonArray);

            // get kf_conduit details
            kfConduitPS = conn.prepareStatement(DbConstant.GET_KF_KONDUIT_SETTING_DETAIL);
            kfConduitPS.setInt(1, trialMasterId);
            kfConduitRS = kfConduitPS.executeQuery();

            if (kfConduitRS.next()) {
                recipeJson.put(ApiConstant.KF_KONDUIT_SETTING_ID,
                        kfConduitRS.getString(ApiConstant.KF_KONDUIT_SETTING_ID));
                recipeJson.put(ApiConstant.UV_CH_1, kfConduitRS.getString(ApiConstant.UV_CH_1));
                recipeJson.put(ApiConstant.UV_CH_2, kfConduitRS.getString(ApiConstant.UV_CH_2));
                recipeJson.put(ApiConstant.K_FACTOR_CH_1, kfConduitRS.getString(ApiConstant.K_FACTOR_CH_1));
                recipeJson.put(ApiConstant.K_FACTOR_CH_2, kfConduitRS.getString(ApiConstant.K_FACTOR_CH_2));
            }

            String pumpMinFlowRate = "";
            String pumpMaxFlowRate = "";
            String pumpName = recipeJson.getString(ApiConstant.PUMP_NAME);

            pumpMinMaxFlowRateJson = BasicUtility.getInstance()
                    .getMinMaxFlowRate(recipeJson.getString(ApiConstant.PUMP_TUBING_SIZE));
            pumpMinMaxSpeedJson = BasicUtility.getPumpMinMaxRpm(pumpName);


            if (ApiConstant.KR2I.equalsIgnoreCase(pumpName) || ApiConstant.KMPI.equalsIgnoreCase(pumpName)) {
                pumpMinFlowRate = pumpMinMaxFlowRateJson.getString(ApiConstant.MIN_FLOW_RATE);
                pumpMaxFlowRate = pumpMinMaxFlowRateJson.getString(ApiConstant.MAX_FLOW_RATE);

            } else if (ApiConstant.KROSFLOFS15.equalsIgnoreCase(pumpName)) {

                pumpMinMaxFlowRateJson = getTubeSpec(ApiConstant.KROSFLOFS15_PUMP_ID).getJSONObject(0);
                pumpMinFlowRate = pumpMinMaxFlowRateJson.getString(ApiConstant.MIN_FLOW_RATE);
                pumpMaxFlowRate = pumpMinMaxFlowRateJson.getString(ApiConstant.MAX_FLOW_RATE);
            } else if (ApiConstant.KROSFLOFS500.equalsIgnoreCase(pumpName)) {

                pumpMinMaxFlowRateJson = getTubeSpec(ApiConstant.KROSFLOFS500_PUMP_ID).getJSONObject(0);
                pumpMinFlowRate = pumpMinMaxFlowRateJson.getString(ApiConstant.MIN_FLOW_RATE);
                pumpMaxFlowRate = pumpMinMaxFlowRateJson.getString(ApiConstant.MAX_FLOW_RATE);
            }

            recipeJson.put(ApiConstant.PUMP_MIN_FLOW_RATE, pumpMinFlowRate);
            recipeJson.put(ApiConstant.PUMP_MAX_FLOW_RATE, pumpMaxFlowRate);
            recipeJson.put(ApiConstant.MIN_SPEED, pumpMinMaxSpeedJson.getString(ApiConstant.MIN_SPEED));
            recipeJson.put(ApiConstant.MAX_SPEED, pumpMinMaxSpeedJson.getString(ApiConstant.MAX_SPEED));

            int permAuxPumpIndex = BasicUtility.getPermAuxPumpIndex(recipeJson);
            String permMinFlowRate = "";
            String permMaxFlowRate = "";

            if (recipeJson.getJSONArray(ApiConstant.AUX_PUMP).length() > 0 && (permAuxPumpIndex != -1)) {

                permMinMaxFlowRateJson = BasicUtility.getInstance()
                        .getMinMaxFlowRate(recipeJson.getJSONArray(ApiConstant.AUX_PUMP).getJSONObject(permAuxPumpIndex)
                                .getString(ApiConstant.AUX_TUBING_SIZE));

                permMinFlowRate = permMinMaxFlowRateJson.getString(ApiConstant.MIN_FLOW_RATE);
                permMaxFlowRate = permMinMaxFlowRateJson.getString(ApiConstant.MAX_FLOW_RATE);

            }

            recipeJson.put(ApiConstant.PERM_MIN_FLOW_RATE, permMinFlowRate);
            recipeJson.put(ApiConstant.PERM_MAX_FLOW_RATE, permMaxFlowRate);

            recipeJsonArray.put(recipeJson);

        } catch (Exception e) {

            throw new Exception(e.getMessage());

        }

        return recipeJsonArray;
    }

    public JSONArray getAllRecipe() {

        JSONArray recipeJsonArray = null;
        PreparedStatement getAllRecipePS;
        ResultSet getAllRecipeRS;

        try (Connection conn = DbConnectionManager.getInstance().getConnection()) {

            getAllRecipePS = conn.prepareStatement(DbConstant.GET_ALL_RECIPE);

            getAllRecipeRS = getAllRecipePS.executeQuery();

            recipeJsonArray = JsonMapper.getJSONFromResultSet(getAllRecipeRS);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return recipeJsonArray;
    }

    public List<PumpLookUp> fetchPumpLookUpList() {
        final Connection con = DbConnectionManager.getInstance().getConnection();
        PreparedStatement fetchPumpLookUpStmt = null;
        ResultSet resultSet = null;
        try {
            List<PumpLookUp> pumpLookUpList = new ArrayList<>();
            fetchPumpLookUpStmt = con.prepareStatement(QueryConstant.FETCH_PUMP_LOOK_UP_LIST);
            sLog.d(this, QueryConstant.FETCH_PUMP_LOOK_UP_LIST);
            resultSet = fetchPumpLookUpStmt.executeQuery();
            while (resultSet.next()) {
                PumpLookUp pumpLookUp = new PumpLookUp();
                pumpLookUp.setPumpLookUpId(resultSet.getInt(ApiConstant.PUMP_LOOK_UP_ID));
                pumpLookUp.setPumpType(resultSet.getString(ApiConstant.PUMP_TYPE));
                pumpLookUp.setPumpName(resultSet.getString(ApiConstant.PUMP_NAME));
                pumpLookUp.setMinSpeed(resultSet.getDouble(ApiConstant.MIN_SPEED));
                pumpLookUp.setMaxSpeed(resultSet.getInt(ApiConstant.MAX_SPEED));
                pumpLookUp.setSpeedUnit(resultSet.getString(ApiConstant.SPEED_UNIT));
                pumpLookUpList.add(pumpLookUp);
            }
            return pumpLookUpList;
        } catch (Exception ex) {
            sLog.d(this, ApiConstant.EXCEPTION + ex);
            throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } finally {
            DbConnectionManager.closeDBConnection(resultSet, fetchPumpLookUpStmt, con);
        }
    }

    // start a trial
    public void startTrial(String values) throws Exception {
        JSONObject digitalSignatureJson;
        try {
            digitalSignatureJson = new JSONObject(values);
            if (DatabaseManager.getInstance().isDigitalSignatureMTVerified(digitalSignatureJson, false)) {
                if (!DbConnectionManager.getInstance().isDebuggingMode) {
                    // set alarm indicator
                    AlarmManager.getInstance().stopDeviceAlarmIndicator();
                }

                JSONObject runJson = new JSONObject(values);
                JSONObject recipeJson = DatabaseManager.getInstance()
                        .getRecipe(runJson.getInt(ApiConstant.TRIAL_MASTER_ID)).getJSONObject(0);
                DeviceManager.getInstance().setRecipeJson(recipeJson);
                String modeName = recipeJson.getString(ApiConstant.MODE_NAME);
                BasicUtility.systemPrint("modename", modeName);
                if (BasicUtility.getInstance().isRunMode(modeName)) {
                    TrialManager.getInstance().startAutoModeTrial(runJson, recipeJson);
                } else {
                    NonRunTrialManager.getInstance().startNonRunMode(runJson, recipeJson);
                }
                DeviceManager.pumpKeyListener.startRxListening();
            } else {
                throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
            }

            // saving trial log
            if (TrialManager.getInstance().getTrialRunSettingId() != 0) {

                LogManager.getInstance().insertTrialLog(BasicUtility.getInstance().getUserId(new JSONObject(values)),
                        TrialManager.getInstance().getTrialRunSettingId(), ApiConstant.TRIAL_START_ACTION,
                        ApiConstant.LOG_TYPE_TRIAL);
                //Adding digital signature notes
                if(digitalSignatureJson.has(ApiConstant.DIGITAL_NOTES)){
                    new NotesManager().saveNotes(new Notes(digitalSignatureJson.getString(ApiConstant.DIGITAL_NOTES)));
                }

            } else {
                throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            }

        } catch (HardwareValidationException ex) {
            throw new HardwareValidationException(ex.getMessage());
        } catch (InvalidCredentialException ex) {
            throw new InvalidCredentialException(ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        }

    }

    public List<TrialLog> fetchTrialLogsList() {
        final Connection con = DbConnectionManager.getInstance().getConnection();
        PreparedStatement fetchTrialLogStmt = null;
        ResultSet resultSet = null;
        try {
            List<TrialLog> trialLogs = new ArrayList<>();
            fetchTrialLogStmt = con.prepareStatement(QueryConstant.FETCH_TRIAL_LOGS_LIST);
            sLog.d(this, QueryConstant.FETCH_TRIAL_LOGS_LIST);
            resultSet = fetchTrialLogStmt.executeQuery();
            while (resultSet.next()) {
                TrialLog trialLog = new TrialLog();
                trialLog.setTrialId(resultSet.getString(ApiConstant.TRIAL_ID));
                trialLog.setTrialRunSettingId(resultSet.getInt(ApiConstant.TRIAL_RUN_SETTING_ID));
                trialLog.setTrialMasterId(resultSet.getInt(ApiConstant.TRIAL_MASTER_ID));
                trialLog.setTrialStartTime(Optional.ofNullable(resultSet.getString(ApiConstant.TRIAL_START_TIME))
                        .orElse(ApiConstant.BLANK_QUOTE));
                trialLog.setTrialEndTime(Optional.ofNullable(resultSet.getString(ApiConstant.TRIAL_END_TIME))
                        .orElse(ApiConstant.BLANK_QUOTE));
                trialLog.setRecipeName(resultSet.getString(ApiConstant.RECIPE_NAME));
                trialLog.setUserName(resultSet.getString(ApiConstant.USERNAME));
                trialLog.setTrialStatus(resultSet.getInt(ApiConstant.TRIAL_STATUS));
                trialLogs.add(trialLog);
            }
            return trialLogs;
        } catch (Exception ex) {
            sLog.d(this, ApiConstant.EXCEPTION + ex);
            throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } finally {
            DbConnectionManager.closeDBConnection(resultSet, fetchTrialLogStmt, con);
        }
    }

    public JSONArray fetchTrialDetails(int trialRunSettingId) {
        final Connection con = DbConnectionManager.getInstance().getConnection();
        PreparedStatement fetchTrialDetailsStmt = null;
        ResultSet resultSet = null;
        JSONArray jsonArray = new JSONArray();
        try {
            if (trialRunSettingId != -1) {
                fetchTrialDetailsStmt = con.prepareStatement(QueryConstant.FETCH_TRIAL_DETAILS);
                fetchTrialDetailsStmt.setInt(1, trialRunSettingId);
                sLog.d(this, QueryConstant.FETCH_TRIAL_DETAILS);
            } else {
                fetchTrialDetailsStmt = con.prepareStatement(QueryConstant.FETCH_LAST_TRIAL_DETAILS);
            }
            resultSet = fetchTrialDetailsStmt.executeQuery();
            jsonArray = JsonMapper.getJSONFromResultSet(resultSet);
            if (jsonArray.length() > 0) {
                String start = jsonArray.getJSONObject(0).getString(ApiConstant.TRIAL_START_TIME);
                String end = jsonArray.getJSONObject(0).getString(ApiConstant.TRIAL_END_TIME);
                jsonArray.getJSONObject(0).put(ApiConstant.TRIAL_DURATION_KEY, getTrialDuration(start, end));
                HistoricalConverter.setTotalizerUnits(
                        jsonArray.getJSONObject(0).getString(ApiConstant.COLUMN_TOTALIZER_UNIT),
                        jsonArray.getJSONObject(0).getString(ApiConstant.COLUMN_TOTALIZER_UNIT_2));
            }
            return jsonArray;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } finally {
            DbConnectionManager.closeDBConnection(resultSet, fetchTrialDetailsStmt, con);
        }
    }

    public String getTrialDuration(String startTime, String endTime) throws ParseException {
        if (startTime.isEmpty() || startTime == null || endTime.isEmpty() || endTime == null) {
            return null;
        }
        final java.util.Date startDate = BasicUtility.SIMPLE_DATE_FORMAT.parse(startTime);
        final java.util.Date endDate = BasicUtility.SIMPLE_DATE_FORMAT.parse(endTime);
        final long duration = endDate.getTime() - startDate.getTime();
        final long hours = TimeUnit.MILLISECONDS.toHours(duration);
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60;
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60;
        return MessageFormat.format(ApiConstant.DURATION_FORMAT, hours, minutes, seconds);
    }

    public JSONArray fetchAlarmsDetails(int trialRunSettingId) {
        final Connection con = DbConnectionManager.getInstance().getConnection();
        PreparedStatement fetchAlarmsStmt = null;
        ResultSet resultSet = null;
        try {
            fetchAlarmsStmt = con.prepareStatement(QueryConstant.FETCH_ALARM_DETAILS);
            fetchAlarmsStmt.setInt(1, trialRunSettingId);
            sLog.d(this, QueryConstant.FETCH_ALARM_DETAILS);
            resultSet = fetchAlarmsStmt.executeQuery();
            return JsonMapper.getJSONFromResultSet(resultSet);
        } catch (Exception ex) {
            throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } finally {
            DbConnectionManager.closeDBConnection(resultSet, fetchAlarmsStmt, con);
        }
    }

    public int setRecipeStatus(int trialMasterId, int status) throws SQLException {
        final Connection con = DbConnectionManager.getInstance().getConnection();
        PreparedStatement setRecipeStatusPS = null;
        String isActive = ApiConstant.RECIPE_ACTIVE_STATUS;
        try {
            con.setAutoCommit(false);
            if(status == 0){
                isActive = ApiConstant.RECIPE_INACTIVE_STATUS;
            }else if(status == 1){
                isActive = ApiConstant.RECIPE_ACTIVE_STATUS;
            }else{
                BasicUtility.systemPrint("wrong status",status);
                return 1;
            }
            setRecipeStatusPS = con.prepareStatement(DbConstant.UPDATE_RECIPE_IS_ACTIVE_STATUS);
            setRecipeStatusPS.setString(1, isActive);
            setRecipeStatusPS.setInt(2, trialMasterId);
            sLog.d(this, DbConstant.UPDATE_RECIPE_IS_ACTIVE_STATUS+ "; isActive ;"+isActive+"; trialMasterId ; "+trialMasterId);
            int rows = setRecipeStatusPS.executeUpdate();
            if(rows == 0){
                return 0;
            }
            con.commit();

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            throw new SQLException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
        } finally {
            DbConnectionManager.closeDBConnection(null, setRecipeStatusPS, con);
        }
        return 1;
    }

	public List<TubingDetails> fetchTubingsList() {
		final Connection con = DbConnectionManager.getInstance().getConnection();
		PreparedStatement fetchedList = null;
		ResultSet resultSet = null;
		try {
			List<TubingDetails> fetchedArrayList = new ArrayList<TubingDetails>();
			fetchedList = con.prepareStatement(QueryConstant.FETCH_TUBING_DETAILS_LIST);
			resultSet = fetchedList.executeQuery();
			if (resultSet != null) {
				while (resultSet.next()) {
					TubingDetails details = TubingDetailsMapper.fetchTubingDetailsMapper(resultSet);
					fetchedArrayList.add(details);
				}

				return fetchedArrayList;

			} else {

				return null;
			}

		} catch (Exception ex) {
			throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
		} finally {
			DbConnectionManager.closeDBConnection(resultSet, fetchedList, con);
		}
	}
}
