package org.lattice.spectrum_backend_final.services.handler;


import static org.lattice.spectrum_backend_final.services.handler.ManualModeHandler.abvSettingsGlobal;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.lattice.spectrum.ModeLibrary.Managers.run.RunModeManager;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.beans.ABVSetting;
import org.lattice.spectrum_backend_final.beans.Notes;
import org.lattice.spectrum_backend_final.beans.PumpLookUp;
import org.lattice.spectrum_backend_final.beans.SystemSetting;
import org.lattice.spectrum_backend_final.beans.TrialLog;
import org.lattice.spectrum_backend_final.beans.TubingDetails;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.AlarmManager;
import org.lattice.spectrum_backend_final.dao.manager.CfrManager;
import org.lattice.spectrum_backend_final.dao.manager.DatabaseManager;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.dao.manager.DeviceManager;
import org.lattice.spectrum_backend_final.dao.manager.EndPointSettingManager;
import org.lattice.spectrum_backend_final.dao.manager.FirmwareManager;
import org.lattice.spectrum_backend_final.dao.manager.LogManager;
import org.lattice.spectrum_backend_final.dao.manager.ManualTrialManager;
import org.lattice.spectrum_backend_final.dao.manager.NotesManager;
import org.lattice.spectrum_backend_final.dao.manager.RunManager;
import org.lattice.spectrum_backend_final.dao.manager.SystemSettingsManager;
import org.lattice.spectrum_backend_final.dao.manager.TargetStepSettingManager;
import org.lattice.spectrum_backend_final.dao.manager.TrialManager;
import org.lattice.spectrum_backend_final.dao.manager.ValidationManager;
import org.lattice.spectrum_backend_final.dao.manager.device.AbvManager;
import org.lattice.spectrum_backend_final.dao.manager.device.AuxPumpManager;
import org.lattice.spectrum_backend_final.dao.manager.device.MainPumpManager;
import org.lattice.spectrum_backend_final.dao.manager.nonrun.NonRunTrialManager;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.HistoricalConverter;
import org.lattice.spectrum_backend_final.dao.util.Logger;
import org.lattice.spectrum_backend_final.dao.util.UserAuthorization;
import org.lattice.spectrum_backend_final.exception.FirmwareMismatchException;
import org.lattice.spectrum_backend_final.exception.InvalidCredentialException;
import org.lattice.spectrum_backend_final.exception.OldPasswordException;
import org.lattice.spectrum_backend_final.exception.TrialRunException;
import org.lattice.spectrum_backend_final.exception.UserDoesNotExistException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lattice.spectrum.ComLibrary.comHandler.PortNotFoundException;
import com.lattice.spectrum.ComLibrary.utility.sLog;

import javassist.NotFoundException;


@Path(ApiConstant.REST_ROOT_ENTRY_POINT)
public class RequestHandler {

    private NotesManager notesManager = new NotesManager();
    private DatabaseManager dbManager = DatabaseManager.getInstance();
    private TrialManager trialManager = TrialManager.getInstance();
    private UserAuthorization authorization = new UserAuthorization();
    private CfrManager cfrManager = new CfrManager();

    private static SystemSetting systemSetting;

    public static void setSystemSetting() throws SQLException {
        systemSetting = new SystemSettingsManager().fetchSystemSettings();
    }

    public static SystemSetting getStSystemSetting() throws SQLException {
        if (systemSetting == null) {
            setSystemSetting();
        }
        return systemSetting;
    }

    private static int trialRunSettingId = 0;

    public synchronized static int getTrialRunSettingId() {
        return trialRunSettingId;
    }

    public synchronized static void setTrialRunSettingId(int trialRunSettingId) {
        RequestHandler.trialRunSettingId = trialRunSettingId;
    }

    /**
     * This api authenticate when a user tries to login.
     *
     * @param values Takes credentials int the form of JSON String.
     * @return 1) Success message.
     * 2) Invalid credential message if not a valid credential.
     * 3) Else return Server Error if any internal error.
     */
    @POST
    @Path(ApiConstant.REST_USER_PATH + ApiConstant.REST_USER_LOGIN)
    public Response userLoginApi(String values) {

        Response.ResponseBuilder responseBuilder = Response.ok();
        JSONObject responseJsonObject = new JSONObject();
        Response response;

        if (DbConnectionManager.getInstance().getTokenManager().getToken() != null) {
            Logger.error(this, "userLoginApi: "+ApiConstant.A_USER_ALREADY_LOGGED_INTO_THE_SYSTEM, HttpStatus.OK_200);
            responseJsonObject.put(ApiConstant.ERROR, ApiConstant.A_USER_ALREADY_LOGGED_INTO_THE_SYSTEM);

            return Response.status(Response.Status.OK)
                    .entity(responseJsonObject.toString())
                    .build();
        }

        try {

            String isNewUser = dbManager.login(values);

            if (isNewUser.equals(ApiConstant.YES)) {
                Logger.info(this, "userLoginApi: New user");
                response = responseBuilder.header(ApiConstant.IS_NEW_USER, ApiConstant.YES)
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.IS_NEW_USER)
                        .build();

            } else if (isNewUser.equals(ApiConstant.INVALID)) {
                Logger.error(this, "userLoginApi: "+ApiConstant.INVALID_LOGIN_MESSAGE);
                responseJsonObject.put(ApiConstant.ERROR, ApiConstant.INVALID_LOGIN_MESSAGE);
                response = responseBuilder.header(ApiConstant.AUTHORIZATION, DbConnectionManager.getInstance().getTokenManager().getToken())
                        .header(ApiConstant.USER_TYPE, DbConnectionManager.getInstance().getTokenManager().getUserType())
                        .header(ApiConstant.USER_ID, DbConnectionManager.getInstance().getTokenManager().getUserEmail())
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.AUTHORIZATION)
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.USER_TYPE)
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.USER_ID)
                        .entity(responseJsonObject.toString())
                        .build();
            } else if (isNewUser.equals(ApiConstant.INACTIVE)) {
                Logger.error(this, "userLoginApi: "+ApiConstant.USER_IS_INACTIVE_PLEASE_CONTACT_SUPERADMIN);
                responseJsonObject.put(ApiConstant.ERROR, ApiConstant.USER_IS_INACTIVE_PLEASE_CONTACT_SUPERADMIN);
                response = responseBuilder.header(ApiConstant.AUTHORIZATION, DbConnectionManager.getInstance().getTokenManager().getToken())
                        .header(ApiConstant.USER_TYPE, DbConnectionManager.getInstance().getTokenManager().getUserType())
                        .header(ApiConstant.USER_ID, DbConnectionManager.getInstance().getTokenManager().getUserEmail())
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.AUTHORIZATION)
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.USER_TYPE)
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.USER_ID)
                        .entity(responseJsonObject.toString())
                        .build();
            } else if (isNewUser.equals(ApiConstant.NO)) {
                Logger.info(this, "userLoginApi: Existing user");
                responseJsonObject = dbManager.getSingleUserProfile().getJSONObject(0);
                response = responseBuilder.header(ApiConstant.AUTHORIZATION, DbConnectionManager.getInstance().getTokenManager().getToken())
                        .header(ApiConstant.USER_TYPE, DbConnectionManager.getInstance().getTokenManager().getUserType())
                        .header(ApiConstant.USER_ID, DbConnectionManager.getInstance().getTokenManager().getUserEmail())
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.AUTHORIZATION)
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.USER_TYPE)
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.USER_ID)
                        .entity(responseJsonObject.toString())
                        .build();

                // starting thr timer when user login.
                DbConnectionManager.getInstance().getTimerManager().startTimer();

                // Now saving the logs in access-log
                dbManager.addUserAccessLog(
                        ApiConstant.LOGIN,
                        DbConnectionManager.getInstance().getTokenManager().getUsername(),
                        ApiConstant.BLANK_QUOTE
                );
            } else if (isNewUser.contains(ApiConstant.ATTEMPT_LEFT)) {
                Logger.error(this, "userLoginApi: "+ApiConstant.ATTEMPT_LEFT);
                responseJsonObject.put(ApiConstant.ERROR, isNewUser);
                response = responseBuilder.header(ApiConstant.AUTHORIZATION, DbConnectionManager.getInstance().getTokenManager().getToken())
                        .header(ApiConstant.USER_TYPE, DbConnectionManager.getInstance().getTokenManager().getUserType())
                        .header(ApiConstant.USER_ID, DbConnectionManager.getInstance().getTokenManager().getUserEmail())
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.AUTHORIZATION)
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.USER_TYPE)
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.USER_ID)
                        .entity(responseJsonObject.toString())
                        .build();
            } else if (isNewUser.equals(ApiConstant.YOUR_ACCOUNT_IS_BLOCKED_DUE_TO_THREE_INVALID_LOGIN_ATTEMPT)) {
                Logger.error(this, "userLoginApi: "+ApiConstant.YOUR_ACCOUNT_IS_BLOCKED_DUE_TO_THREE_INVALID_LOGIN_ATTEMPT);
                responseJsonObject.put(ApiConstant.ERROR, isNewUser);
                response = responseBuilder.header(ApiConstant.AUTHORIZATION, DbConnectionManager.getInstance().getTokenManager().getToken())
                        .header(ApiConstant.USER_TYPE, DbConnectionManager.getInstance().getTokenManager().getUserType())
                        .header(ApiConstant.USER_ID, DbConnectionManager.getInstance().getTokenManager().getUserEmail())
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.AUTHORIZATION)
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.USER_TYPE)
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.USER_ID)
                        .entity(responseJsonObject.toString())
                        .build();
            } else {
                responseJsonObject.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
                response = responseBuilder.header(ApiConstant.AUTHORIZATION, DbConnectionManager.getInstance().getTokenManager().getToken())
                        .header(ApiConstant.USER_TYPE, DbConnectionManager.getInstance().getTokenManager().getUserType())
                        .header(ApiConstant.USER_ID, DbConnectionManager.getInstance().getTokenManager().getUserEmail())
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.AUTHORIZATION)
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.USER_TYPE)
                        .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.USER_ID)
                        .entity(responseJsonObject.toString())
                        .build();
            }


            // checks for 21 CFR status
            Logger.info(this, "userLoginApi: Updating Status to cfr token");
            cfrManager.update21CfrStatus();


            if (DeviceManager.getInstance().getModeType() == 2) {
                RunManager.getInstance().updateTrialRunStatusOnUnexpectedClose();
            }


        } catch (Exception e) {
            e.printStackTrace();
            responseJsonObject.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            response = responseBuilder.header(ApiConstant.AUTHORIZATION, DbConnectionManager.getInstance().getTokenManager().getToken())
                    .header(ApiConstant.USER_TYPE, DbConnectionManager.getInstance().getTokenManager().getUserType())
                    .header(ApiConstant.USER_ID, DbConnectionManager.getInstance().getTokenManager().getUserEmail())
                    .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.AUTHORIZATION)
                    .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.USER_TYPE)
                    .header(ApiConstant.ACCESS_CONTROL_EXPOSE_HEADERS, ApiConstant.USER_ID)
                    .entity(responseJsonObject.toString())
                    .build();

        }


        return response;

    }


    /**
     * This api logout a user.
     *
     * @param token   Taken token to authenticate a user logout.
     * @param headers This is the HttpHeaders no need to provide.
     * @return
     */
    @POST
    @Path(ApiConstant.REST_USER_PATH + ApiConstant.REST_USER_LOGOUT)
    public Response logoutApi(@PathParam(ApiConstant.TOKEN) String token, @Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();


        if (token.equals(ApiConstant.BLANK_SPACE)) {
            Logger.error(this, "logoutApi: "+ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE, HttpStatus.UNAUTHORIZED_401);
            responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(responseJson.toString())
                    .build();
        } else if (token.equals(DbConnectionManager.getInstance().getTokenManager().getToken())
        ) {

            dbManager.logout();
            Logger.error(this, "logoutApi: "+ApiConstant.LOGOUT_SUCCESSFUL_MESSAGE, HttpStatus.OK_200);
            responseJson.put(ApiConstant.SUCCESS, ApiConstant.LOGOUT_SUCCESSFUL_MESSAGE);

            //stopping the timer
            DbConnectionManager.getInstance().getTimerManager().getTimer().cancel();


            return Response.status(Response.Status.OK)
                    .entity(responseJson.toString())
                    .build();

        } else {
            Logger.error(this, "logoutApi: "+ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE, HttpStatus.UNAUTHORIZED_401);
            responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(responseJson.toString())
                    .build();
        }


    }


    //forgot password

    /**
     * This api is used when user forget its password.
     *
     * @param values Takes credentials as JSON String.
     * @return Error or Success message.
     */
    @POST
    @Path(ApiConstant.REST_USER_PATH + ApiConstant.REST_FORGOT_PASSWORD)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response forgotPasswordApi(String values) {

        JSONObject responseJson = new JSONObject();
        boolean isSuperAdmin = false;
        try {
            Logger.info(this, "forgotPasswordApi");
            isSuperAdmin = dbManager.forgotPassword(values);

        } catch (UserDoesNotExistException e) {
            Logger.error(this, "forgotPasswordApi",HttpStatus.NOT_ACCEPTABLE_406, e);
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();

        } catch (Exception e) {
            Logger.error(this, "forgotPasswordApi",HttpStatus.INTERNAL_SERVER_ERROR_500, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
        responseJson.put(ApiConstant.IS_SUPER_ADMIN, isSuperAdmin);
        Logger.info(this, "forgotPasswordApi : IS_SUPER_ADMIN : " + isSuperAdmin,HttpStatus.OK_200);
        responseJson.put(ApiConstant.SUCCESS, ApiConstant.PASSWORD_SUCCESSFULLY_RESET_MESSAGE);
        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();
    }


    @POST
    @Path(ApiConstant.REST_VALIDATE_PASSWORD)
    public Response validateMasterPassword(String values) {
        int userId = 0;
        JSONObject responseJson = new JSONObject();
        try {
            userId = dbManager.validateMasterPassword(values);
            if (userId != 0) {
                responseJson.put(ApiConstant.SUCCESS, ApiConstant.PASSWORD_RESET_MESSAGE);
                responseJson.put(ApiConstant.JSON_USER_ID, userId);
                return Response.status(Response.Status.OK)
                        .entity(responseJson.toString())
                        .build();
            } else {
                responseJson.put(ApiConstant.ERROR, "Invalid password");
                responseJson.put(ApiConstant.JSON_USER_ID, userId);
                return Response.status(Response.Status.NOT_ACCEPTABLE)
                        .entity(responseJson.toString())
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();
        }

    }

    @PUT
    @Path(ApiConstant.REST_CHANGE_PASSWORD)
    public Response changeSuperAdminPassword(String values) {
        JSONObject responseJson = new JSONObject();
        try {
            boolean result = dbManager.changeSuperAdminPassword(values);
            if (result) {
                responseJson.put(ApiConstant.SUCCESS, ApiConstant.PASSWORD_CHANGED);
                return Response.status(Response.Status.OK)
                        .entity(responseJson.toString())
                        .build();
            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
                return Response.status(Response.Status.NOT_ACCEPTABLE)
                        .entity(responseJson.toString())
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();
        }

    }
    
    @PUT
    @Path(ApiConstant.REST_CHANGE_EMAIL)
    public Response changeUserEmail(String values) {
        final JSONObject responseJson = new JSONObject();
        try {
        	
            final boolean result = dbManager.changeUserEmail(values);
            if (result) {
                responseJson.put(ApiConstant.SUCCESS, ApiConstant.EMAIL_CHANGED);
                return Response.status(Response.Status.OK)
                        .entity(responseJson.toString())
                        .build();
            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
                return Response.status(Response.Status.NOT_ACCEPTABLE)
                        .entity(responseJson.toString())
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();
        }

    }

    //Set new password

    /**
     * Through this api user can sets a new password.
     *
     * @param values Takes credentials int the form of JSON String.
     * @return 1) Success Message.
     * 2) When the user enters the last three password then it'll return a message saying " You have entered an old password , Please enter a new password. "
     * 3) Else in case of any internal error returns internal server error.
     */
    @PUT
    @Path(ApiConstant.REST_USER_PATH + ApiConstant.REST_SET_PASSWORD)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response setNewPasswordApi(String values) {

        JSONObject responseJsonObject = new JSONObject();
        try {

            dbManager.setNewPassword(values);


        } catch (OldPasswordException e) {
            responseJsonObject.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJsonObject.toString())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            responseJsonObject.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJsonObject.toString())
                    .build();
        }

        responseJsonObject.put(ApiConstant.SUCCESS, ApiConstant.PASSWORD_SUCCESSFULLY_RESET_MESSAGE);
        return Response.status(Response.Status.OK)
                .entity(responseJsonObject.toString())
                .build();
    }


    /**
     * @param values
     * @param headers
     * @return
     */

    @PUT
    @Path(ApiConstant.REST_USER_PATH + ApiConstant.CHANGE_PASSWORD)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response changePasswordApi(String values, @Context HttpHeaders headers) {


        JSONObject responseJson = new JSONObject();
        try {

            if (headers.getRequestHeader(ApiConstant.AUTHORIZATION) == null) {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            } else if (headers.getRequestHeader(
                    ApiConstant.AUTHORIZATION).get(0).equals(DbConnectionManager.getInstance().getTokenManager().getToken())
            ) {

                dbManager.changePassword(values);

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }


        } catch (OldPasswordException e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();
        }

        responseJson.put(ApiConstant.SUCCESS, ApiConstant.PASSWORD_SUCCESSFULLY_RESET_MESSAGE);
        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();
    }


    /**
     * This api returns list of all the existing users.
     *
     * @param headers This is the HttpHeaders no need to provide.
     * @return 1) list of all the existing users.
     * 2) Not authorized message if the user haven't authority to access this api.
     * 3) Else in case of any internal error returns internal server error.
     */
    @GET
    @Path(ApiConstant.REST_USER_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsersApi(@Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();

        try {

            if (authorization.isUserRoleEqualsAdminSuperAdminAuditor(headers)) {

                JSONArray jsonArray = dbManager.showAllUser();
                return Response.ok(jsonArray.toString(), MediaType.TEXT_PLAIN).build();
            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }
    }


    /**
     * Gets detail of a single user.
     *
     * @param userId  Takes user id of the user.
     * @param headers This is the HttpHeaders no need to provide.
     * @return 1) returns a single user detail.
     * 2) Not authorized message if the user haven't authority to access this api.
     * 3) Else in case of any internal error returns internal server error.
     */
    @GET
    @Path(ApiConstant.REST_USER_PATH + ApiConstant.REST_USER_ID_PARAM)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSingleUserApi(@PathParam(ApiConstant.USER_ID) int userId, @Context HttpHeaders headers) {


        JSONObject responseJson = new JSONObject();
        String userType = ValidationManager.getUserTypeFromId(userId);

        try {

            if (authorization.isUserRoleEqualsAdminSuperAdminAuditor(headers)) {

                JSONArray jsonArray = dbManager.getSingleUserDetail(userId);


                if (userType == null) {
                    responseJson.put(ApiConstant.ERROR, ApiConstant.USER_NOT_FOUND_MESSAGE);
                    return Response.status(Response.Status.NOT_FOUND)
                            .entity(responseJson.toString())
                            .build();
                }


                if (userId == DbConnectionManager.getInstance().getTokenManager().getUserId()) {
                    responseJson.put(ApiConstant.IS_EDITABLE, ApiConstant.FALSE);
                    jsonArray.put(responseJson);
                } else if (userType.equals(ApiConstant.ADMIN) &&
                        DbConnectionManager.getInstance().getTokenManager().getUserType().equals(ApiConstant.ADMIN)
                ) {
                    responseJson.put(ApiConstant.IS_EDITABLE, ApiConstant.PARTIAL);
                    jsonArray.put(responseJson);
                } else {
                    responseJson.put(ApiConstant.IS_EDITABLE, ApiConstant.TRUE);
                    jsonArray.put(responseJson);
                }


                return Response.ok(jsonArray.toString(), MediaType.TEXT_PLAIN).build();


            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }


        } catch (SQLException e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }
    }


    /**
     * Updates the details of a particular user.
     *
     * @param values  Takes updated user details as JSON String.
     * @param headers This is the HttpHeaders no need to provide.
     * @return 1) Success message.
     * 2) Not authorized message if the user haven't authority to access this api.
     * 3) Else in case of any internal error returns internal server error.
     */
    @PUT
    @Path(ApiConstant.REST_USER_PATH + ApiConstant.REST_USER_PROFILE)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateMyProfile(String values, @Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();
        JSONObject userJson = new JSONObject(values);

        JSONArray errorJsonArray;
        try {

            if (headers.getRequestHeader(ApiConstant.AUTHORIZATION) == null) {

                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();

            } else if (headers.getRequestHeader(
                    ApiConstant.AUTHORIZATION).get(0).equals(DbConnectionManager.getInstance().getTokenManager().getToken()) &&
                    (
                            DbConnectionManager.getInstance().getTokenManager().getUserId() == userJson.getInt("user_id")

                    )
            ) {

                errorJsonArray = dbManager.updateSingleUserProfile(values);

                if (errorJsonArray != null) {

                    return Response.status(Response.Status.NOT_ACCEPTABLE)
                            .entity(errorJsonArray.toString())
                            .build();
                }

                responseJson.put(ApiConstant.SUCCESS, ApiConstant.USER_DETAILS_UPDATE_MESSAGE);
                return Response.status(Response.Status.OK)
                        .entity(responseJson.toString())
                        .build();
            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }
        } catch (SQLException e) {

            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();

        } catch (InvalidCredentialException e) {

            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();

        } catch (Exception e) {

            responseJson.put(ApiConstant.ERROR, e.getMessage());

            return Response.status(Response.Status.CONFLICT)
                    .entity(responseJson.toString())
                    .build();
        }


    }


    /**
     * Get Profile detail of a particular user.
     *
     * @param headers This is the HttpHeaders no need to provide.
     * @return 1) Profile detail of a particular user.
     * 2) Not authorized message if the user haven't authority to access this api.
     * 3) Else in case of any internal error returns internal server error.
     */
    @GET
    @Path(ApiConstant.REST_USER_PATH + ApiConstant.REST_USER_PROFILE)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMyProfileApi(@Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();

        try {

            if (headers.getRequestHeader(ApiConstant.AUTHORIZATION) == null) {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            } else if (headers.getRequestHeader(ApiConstant.AUTHORIZATION)
                    .get(0)
                    .equals(DbConnectionManager.getInstance().getTokenManager().getToken())
            ) {

                JSONArray jsonArray = dbManager.getSingleUserProfile();
                return Response.ok(jsonArray.toString(), MediaType.TEXT_PLAIN).build();
            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }
        } catch (SQLException e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }
    }


    /**
     * This will adds a new user.
     *
     * @param values  Takes json having all the valid details of new user.
     * @param headers This is the HttpHeaders no need to provide.
     * @return 1) Success message.
     * 2) Not authorized message if the user haven't authority to access this api.
     * 3) Else in case of any internal error returns internal server error.
     */
    @POST
    @Path(ApiConstant.REST_USER_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addUserApi(String values, @Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();

        try {

            if (authorization.isUserRoleEqualsAdminSuperAdmin(headers)) {

                JSONArray errorJsonArray = dbManager.saveUser(values);

                if (errorJsonArray != null) {

                    return Response.status(Response.Status.NOT_ACCEPTABLE)
                            .entity(errorJsonArray.toString())
                            .build();
                }

                responseJson.put(ApiConstant.SUCCESS, ApiConstant.USER_CREATED_MESSAGE);

                return Response.ok(responseJson.toString(), MediaType.TEXT_PLAIN).build();

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (SQLException e) {

            responseJson.put(ApiConstant.ERROR, e.getMessage());

            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();

        } catch (InvalidCredentialException e) {

            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();

        } catch (Exception e) {

            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.CONFLICT)
                    .entity(responseJson.toString())
                    .build();
        }

    }

    //update user detail


    /**
     * Updates the details of existing user.
     *
     * @param values  details of user in the form of json.
     * @param headers This is the HttpHeaders no need to provide automatically fetch.
     * @return 1) Success message.
     * 2) Not authorized message if the user haven't authority to access this api.
     * 3) Else in case of any internal error returns internal server error.
     */
    @PUT
    @Path(ApiConstant.REST_USER_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUserApi(String values, @Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();
        JSONArray errorJsonArray;

        try {

            if (authorization.isUserRoleEqualsAdminSuperAdmin(headers)) {

                errorJsonArray = dbManager.updateUser(values);

                if (errorJsonArray != null) {

                    return Response.status(Response.Status.NOT_ACCEPTABLE)
                            .entity(errorJsonArray.toString())
                            .build();
                }
                responseJson.put(ApiConstant.SUCCESS, ApiConstant.USER_DETAILS_UPDATE_MESSAGE);
                return Response.ok(responseJson.toString(), MediaType.TEXT_PLAIN).build();
            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }
        } catch (SQLException e) {

            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();

        } catch (InvalidCredentialException e) {

            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();

        } catch (Exception e) {

            responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            return Response.status(Response.Status.CONFLICT)
                    .entity(responseJson.toString())
                    .build();
        }


    }


    /**
     * Gets List of all the user access logs.
     *
     * @param headers This is the HttpHeaders no need to provide.
     * @return 1) List of all the user access logs.
     * 2) Not authorized message if the user haven't authority to access this api.
     * 3) Else in case of any internal error returns internal server error.
     */
    @GET
    @Path(ApiConstant.REST_USER_PATH + ApiConstant.REST_ACCESS_LOG)
    @Produces(MediaType.APPLICATION_JSON)
    public Response showUserAccessLogApi(@Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();
        JSONArray logJsonArray;
        try {
            if (authorization.isUserRoleEqualsAdminSuperAdminAuditor(headers)) {

                logJsonArray = dbManager.showUserAccessLog();
                return Response.status(Response.Status.OK)
                        .entity(logJsonArray.toString())
                        .build();
            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }
        } catch (SQLException e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

    }

    /**
     * Logout a user after time out.
     *
     * @param headers This is the HttpHeaders no need to provide.
     */
    @POST
    @Path(ApiConstant.REST_USER_PATH + ApiConstant.TIME_OUT)
    public Response timeOut(@Context HttpHeaders headers) {
        JSONObject responseJson = new JSONObject();
        try {

            if (headers.getRequestHeader(ApiConstant.AUTHORIZATION) == null) {
                System.out.println("Not authorized 1!!");
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();

            } else if (headers.getRequestHeader(ApiConstant.AUTHORIZATION)
                    .get(0)
                    .equals(DbConnectionManager.getInstance().getTokenManager().getToken())
            ) {
                //Terminates this timer, discarding any currently scheduled tasks.
                DbConnectionManager.getInstance().getTimerManager().getTimer().cancel();
                //Schedules the specified task for execution after the specified delay.
                DbConnectionManager.getInstance().getTimerManager().startTimer();
//                System.out.println("-------timer----hit2---------------");
            } else {
                System.out.println("Not authorized 2!!");
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                        .entity(responseJson.toString())
                        .build();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(responseJson.toString())
                    .build();
        }

        responseJson.put(ApiConstant.SESSION_TIMEOUT_KEY, DbConnectionManager.getInstance().getTokenManager().isSessionTimeout());
        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();

    }


    /***************************************************Manager/Technician********************************************/


    /**
     * @return All the available modes.
     */
    @GET
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_MODES)
    public Response getTrialModesApi(@Context HttpHeaders headers) {


        JSONArray responseJsonArray = null;
        JSONObject responseJson = new JSONObject();
        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {

                responseJsonArray = dbManager.getTrialModes();

                sLog.d(this, responseJsonArray);

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }


        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(responseJsonArray.toString())
                .build();

    }


    /**
     * @param modeStepName Takes mode step name like Concentration, Diafiltration etc as input.
     * @return All the endpoints related to particular mode name.
     */

    @GET
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_END_POINT)
    public Response getEndPointApi(@PathParam(ApiConstant.STEP_NAME) String modeStepName, @Context HttpHeaders headers) {
        System.out.println("---------api-------" + ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_GET_PUMP);

        JSONArray responseJsonArray = null;
        JSONObject responseJson = new JSONObject();
        try {

//            if (authorization.isUserRoleEqualsManager(headers)) {
            responseJsonArray = dbManager.getEndPoint(modeStepName);

//            } else {
//                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
//                return Response.status(Response.Status.UNAUTHORIZED)
//                        .entity(responseJson.toString())
//                        .build();
//            }

        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();

        }

        return Response.status(Response.Status.OK)
                .entity(responseJsonArray.toString())
                .build();
    }

    /**
     * @param modeId Takes the selected mode id.
     * @return All the pumps compatible with selected mode.
     */
    @GET
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_GET_PUMP)
    public Response getPumpApi(@PathParam(ApiConstant.MODE_ID) int modeId, @Context HttpHeaders headers) {


        JSONArray responseJsonArray = null;
        JSONObject responseJson = new JSONObject();
        try {

            if (authorization.isUserRoleEqualsManager(headers)) {

                responseJsonArray = dbManager.getPump(modeId);

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }
        } catch (Exception e) {

            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(responseJsonArray.toString())
                .build();
    }


    /**
     * @param pumpId Takes the selected pump id.
     * @return All the tubing specification compatible with the selected pump type.
     */
    @GET
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_GET_TUBING_SPEC)
    public Response getTubeSpecApi(@PathParam(ApiConstant.PUMP_ID) int pumpId, @Context HttpHeaders headers) {


        JSONArray responseJsonArray = null;
        JSONObject responseJson = new JSONObject();
        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
                responseJsonArray = dbManager.getTubeSpec(pumpId);
            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }
        } catch (Exception e) {

            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(responseJsonArray.toString())
                .build();
    }


    @GET
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_GET_AUX_TUBING_SPEC)
    public Response getAuxTubeSpecApi(@PathParam(ApiConstant.AUX_PUMP_NAME) String auxPumpName, @Context HttpHeaders headers) {


        JSONArray responseJsonArray = null;
        JSONObject responseJson = new JSONObject();
        try {

            if (authorization.isUserRoleEqualsManager(headers)) {

                responseJsonArray = dbManager.getAuxTubeSpec(auxPumpName);

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }
        } catch (Exception e) {

            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(responseJsonArray.toString())
                .build();
    }


    @GET
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_GET_TUBE_COMPATIBLE_AUX_PATH)
    public Response getTubeCompatibleAuxApi(@PathParam(ApiConstant.TUBING_LOOKUP_ID) int tubingLookupId) {


        JSONArray responseJsonArray = null;
        JSONObject responseJson = new JSONObject();
        try {
            responseJsonArray = dbManager.getTubeCompatibleAux(tubingLookupId);
        } catch (Exception e) {

            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(responseJsonArray.toString())
                .build();
    }


    /**
     * This Api handles mode than one dropdown which are similar in path.
     *
     * @param dropdownKey Drop down key is what we expect in return like hollow_fiber, recirc_flow_unit, ramp_up_time etc.
     * @return The value according to the dropdown key.
     */
    @GET
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_DROPDOWN_KEY)
    public Response getDropdownValuesApi(@PathParam(ApiConstant.DROPDOWN_KEY) String dropdownKey, @Context HttpHeaders headers) {

        JSONArray responseJsonArray = null;
        JSONObject responseJson = new JSONObject();
        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
                responseJsonArray = dbManager.getDropdownValues(dropdownKey);

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .build();
        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(responseJsonArray.toString())
                .build();
    }


    @GET
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_GET_MAIN_PUMP_COMPATIBLE_TUBE_PATH)
    public Response getAuxAndMainPumpCompatibleTubeApi(@PathParam(ApiConstant.PUMP_ID) int pumpId, @Context HttpHeaders headers) {

        JSONArray responseJsonArray = null;
        JSONObject responseJson = new JSONObject();
        try {


            if (authorization.isUserRoleEqualsManager(headers)) {
                responseJsonArray = dbManager.getAuxAndMainPumpCompatibleTube(pumpId);

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }
        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(responseJsonArray.toString())
                .build();
    }


    /**
     * @param values Takes new recipe details as JSON String.
     * @return JsonArray of errors if there is any error else success message.
     */
    @POST
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_RECIPE_PATH)
    public Response saveRecipeApi(String values, @Context HttpHeaders headers) {
        JSONArray errorJsonArray = null;
        JSONObject responseJson = new JSONObject();

        try {
            BasicUtility.systemPrint("inside", "save");

            JSONObject recipeJson = new JSONObject(values);

            if (authorization.isUserRoleEqualsManager(headers)) {

                if (DatabaseManager.getInstance().isDigitalSignatureManagerVerified(recipeJson)) {

                    errorJsonArray = dbManager.saveRecipe(recipeJson);

                } else {
                    throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
                }
            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

            if (errorJsonArray.isEmpty()) {
                responseJson.put(ApiConstant.SUCCESS, ApiConstant.RECIPE_CREATED_SUCCESS_MESSAGE);
                return Response.status(Response.Status.OK)
                        .entity(responseJson.toString())
                        .build();

            } else {

                return Response.status(Response.Status.OK)
                        .entity(errorJsonArray.toString())
                        .build();
            }
        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

    }


    @GET
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_GET_RECIPE)
    public Response getRecipeApi(@PathParam(ApiConstant.TRIAL_MASTER_ID) int trialMasterId, @Context HttpHeaders headers) {

        JSONArray responseJsonArray = null;
        JSONObject responseJson = new JSONObject();
        try {

            if (authorization.isUserRoleEqualsManagerTechnicianAuditor(headers)) {

                responseJsonArray = dbManager.getRecipe(trialMasterId);


            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(responseJsonArray.toString())
                .build();
    }

    @GET
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_RECIPE_PATH)
    public Response getAllRecipeApi(@Context HttpHeaders headers) {

        JSONArray responseJsonArray = null;
        JSONObject responseJson = new JSONObject();

        try {

            if (authorization.isUserRoleEqualsManagerTechnicianAuditor(headers)) {
                responseJsonArray = dbManager.getAllRecipe();

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(responseJsonArray.toString())
                .build();
    }


    @POST
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_TRIAL_START_PATH)
    public Response startTrial(String values, @Context HttpHeaders headers) {

        JSONArray responseJsonArray = new JSONArray();
        JSONObject responseJson = new JSONObject();
        BasicUtility.systemPrint(11, "hello");

        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
                dbManager.startTrial(values);

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            RunManager.getInstance().deleteFailedAutoTrialDetails(TrialManager.getInstance().getTrialRunSettingId());
            return Response.serverError()
                    .entity(responseJson.toString())
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(responseJsonArray.toString())
                .build();

    }


    @POST
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_TRIAL_STOP_PATH)
    public Response stopTrial(@Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();

        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
                trialManager.stopTrial();

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseJson.put(ApiConstant.ERROR, e.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();

    }

    @POST
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_TRIAL_PAUSE_PATH)
    public Response pauseTrial(@Context HttpHeaders headers) {

        JSONArray responseJsonArray = new JSONArray();
        JSONObject responseJson = new JSONObject();

        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
                trialManager.pauseTrial();

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

//        responseJson.put(ApiConstant.SUCCESS, ApiConstant.TRIAL_PAUSED_MESSAGE);
        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();

    }

    @POST
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_TRIAL_RESUME_PATH)
    public Response resumeTrial(String digitalSignatureValues, @Context HttpHeaders headers) {

        JSONArray responseJsonArray = new JSONArray();

        JSONObject responseJson = new JSONObject();

        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
                trialManager.resumeTrial(digitalSignatureValues);

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

//        responseJson.put(ApiConstant.SUCCESS, ApiConstant.RESUME_TRIAL_MESSAGE);
        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();

    }


    @GET
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_GET_ALL_PORT_PATH)
    public Response getAllPortApi(@Context HttpHeaders headers) {

        JSONArray responseJsonArray = null;
        JSONObject responseJson = new JSONObject();

        try {

            responseJsonArray = DeviceManager.getInstance().getAllComPort();

        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(responseJsonArray.toString())
                .build();

    }

    @POST
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_GET_ALL_PORT_PATH + ApiConstant.REST_CONNECT_PATH)
    public Response connectToPortApi(String values, @Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();
        JSONObject portJson = new JSONObject(values);
        String portName = null;

        try {


            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
                portName = DeviceManager.getInstance().connectToPort(portJson.getString(ApiConstant.PORT_NAME));

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        responseJson.put(ApiConstant.SUCCESS, ApiConstant.PORT_SUCCESSFULLY_CONNECTED_MESSAGE + portName);
        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();

    }


    @POST
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_AUTO_CONNECT_PORT_PATH)
    public Response autoConnectApi(@Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();
        FirmwareManager firmwareManager = new FirmwareManager();
        String pumpName;
        boolean isUpdated = true;


        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
//                org.lattice.spectrum_backend_final.dao.util.Logger.setAutoConnectLogToFile(true);

                pumpName = DeviceManager.getInstance().autoConnect();

                if (pumpName == null) {
                    throw new PortNotFoundException(ApiConstant.PORT_NOT_FOUND);
                }
                if (!DbConnectionManager.getInstance().isDebuggingMode && !firmwareManager.isCompatibleFirmwareVersion()) {

                    isUpdated = false;
                    throw new FirmwareMismatchException(ApiConstant.FIRMWARE_MISMATCH_ERROR_MESSAGE);
                }

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

            if (pumpName == null) {
                throw new Exception(ApiConstant.CONNECTION_FAILED_ERROR_MESSAGE);
            }

        } catch (FirmwareMismatchException fmEx) {
            fmEx.printStackTrace();
            responseJson.put(ApiConstant.SUCCESS, fmEx.getMessage());
            responseJson.put(ApiConstant.IS_UPDATED, isUpdated);

            return Response.status(Response.Status.OK)
                    .entity(responseJson.toString())
                    .build();

        } catch (PortNotFoundException pnfEx) {
            pnfEx.printStackTrace();
            responseJson.put(ApiConstant.ERROR, pnfEx.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        responseJson.put(ApiConstant.SUCCESS, ApiConstant.PORT_SUCCESSFULLY_CONNECTED_MESSAGE + pumpName);
        responseJson.put(ApiConstant.IS_UPDATED, isUpdated);
        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();

    }

    @POST
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_CHANGE_DIRECTION_ON_PAUSE_PATH)
    public Response changeDirectionOnPauseApi(String values, @Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();
        JSONObject jObj = new JSONObject(values);
        MainPumpManager mainPumpManager = new MainPumpManager();

        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
                String direction = jObj.getString(ApiConstant.DIRECTION);
                mainPumpManager.changeDirectionOnPause(direction);

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        responseJson.put(ApiConstant.SUCCESS, ApiConstant.DIRECTION_CHANGE_SUCCESS_MESSAGE);
        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();
    }


    @POST
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_CHANGE_MAIN_PUMP_FLOW_RATE_ON_RUN_PATH)
    public Response changePumpFlowRateOnRunApi(String values, @Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();
        double mainPumpFlowRate = 0;
        String flowRateUnit = ApiConstant.ML_MIN;
        JSONObject runSettingJson = new JSONObject(values);
        String pumpName = DeviceManager.getInstance().getRecipeJson().getString(ApiConstant.PUMP_NAME);
        MainPumpManager mainPumpManager = new MainPumpManager();
        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
                mainPumpFlowRate = runSettingJson.getDouble(ApiConstant.MAIN_PUMP_FLOW_RATE);
                mainPumpManager.changePumpFlowRateOnRun(runSettingJson);


                if (
                        DeviceManager.getInstance().getRecipeJson() != null &&
                                (
                                        ApiConstant.KMPI.equalsIgnoreCase(pumpName) ||
                                                ApiConstant.KROSFLOFS500.equalsIgnoreCase(pumpName)
                                )
                ) {

                    mainPumpFlowRate = Double.valueOf(DeviceManager.decimalFormat.format(mainPumpFlowRate / 1000));
                    flowRateUnit = ApiConstant.LITER_PER_MIN;
                }


            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        responseJson.put(ApiConstant.SUCCESS, ApiConstant.MAIN_PUMP_FLOW_RATE_CHANGE_SUCCESS_MESSAGE + mainPumpFlowRate + flowRateUnit);
        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();
    }

    /**
     * Api to change main pump flow rate on run.
     * @param values
     * @param headers
     * @return
     */

    @POST
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_CHANGE_AUX_PUMP_FLOW_RATE_ON_RUN_PATH)
    public Response changeAuxFlowRateOnRunApi(String values, @Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();
        JSONObject auxJson = new JSONObject(values);
        double permFlowRate = 0;
        String flowRateUnit = ApiConstant.ML_MIN;
        JSONObject recipeJson = DeviceManager.getInstance().getRecipeJson();
        String auxPumpName;
        AuxPumpManager auxPumpManager = new AuxPumpManager();

        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {

                if (!DatabaseManager.getInstance().isDigitalSignatureMTVerified(auxJson, false)) {
                    throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
                }

                permFlowRate = auxJson.getDouble(ApiConstant.PERMEATE_FLOW_RATE);

                auxPumpName = auxPumpManager.changeAuxFlowRateOnRun(permFlowRate);

                // saving trial log
                if (TrialManager.getInstance().getTrialRunSettingId() != 0) {

                    LogManager.getInstance().insertTrialLog(
                            BasicUtility.getInstance().getUserId(auxJson),
                            TrialManager.getInstance().getTrialRunSettingId(),
                            ApiConstant.AUX_PUMP_FLOW_RATE_CHANGE_ACTION,
                            ApiConstant.LOG_TYPE_TRIAL
                    );
                    //Adding digital signature notes
                    if(auxJson.has(ApiConstant.DIGITAL_NOTES)){
                        new NotesManager().saveNotes(new Notes(auxJson.getString(ApiConstant.DIGITAL_NOTES)));
                    }
                } else {
                    throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
                }


                if (
                        recipeJson != null &&
                                ApiConstant.IP.equalsIgnoreCase(auxPumpName)
                ) {

                    String permFlowRateString = DeviceManager.decimalFormat.format(permFlowRate / 1000);
                    permFlowRateString = permFlowRateString.replaceAll(ApiConstant.REGEX_TO_REMOVE_NEGATIVE_ZERO, ApiConstant.BLANK_QUOTE);
                    permFlowRate = Double.valueOf(permFlowRateString);
                    flowRateUnit = ApiConstant.LITER_PER_MIN;
                }

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        responseJson.put(ApiConstant.SUCCESS, ApiConstant.AUX_PUMP_FLOW_RATE_CHANGE_SUCCESS_MESSAGE + permFlowRate + flowRateUnit);
        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();
    }


    @POST
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_OPEN_VALVE_PATH)
    public Response emergencyValveOpenApi(@Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();

        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
                new AbvManager().emergencyValveOpen(true);

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        responseJson.put(ApiConstant.SUCCESS, ApiConstant.VALVE_FULLY_OPENED_MESSAGE);
        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();
    }


    @POST
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_RESUME_PRESSURE_PATH)
    public Response resumePressureControlApi(String digitalSignatureValues, @Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();

        JSONObject digitalSignatureJson = new JSONObject(digitalSignatureValues);

        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
                if (DeviceManager.getInstance().getModeType() == 0) {
                    new AbvManager().resumePressureControl(digitalSignatureJson, true);
                } else if (DeviceManager.getInstance().getModeType() == 1) {
                	sLog.d("resumePressureControlApi: ", abvSettingsGlobal);
                    if (abvSettingsGlobal.length > 0) {
                        new AbvManager().abvPressureControlAtRun(abvSettingsGlobal);
                    }
                    if(!DatabaseManager.getInstance().isDigitalSignatureMTVerified(digitalSignatureJson, false)){
                        throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
                    }
                    // saving trial log
                    if (TrialManager.getInstance().getTrialRunSettingId() != 0) {

                        LogManager.getInstance().insertTrialLog(
                                BasicUtility.getInstance().getUserId(digitalSignatureJson),
                                TrialManager.getInstance().getTrialRunSettingId(),
                                ApiConstant.RESUME_PRESSURE_CONTROL_ACTION,
                                ApiConstant.LOG_TYPE_TRIAL
                        );
                        //Adding digital signature notes
                        if(digitalSignatureJson.has(ApiConstant.DIGITAL_NOTES)){
                            new NotesManager().saveNotes(new Notes(digitalSignatureJson.getString(ApiConstant.DIGITAL_NOTES)));
                        }
                    } else {
                        throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
                    }
                }

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseJson.put(ApiConstant.ERROR, e.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        responseJson.put(ApiConstant.SUCCESS, ApiConstant.PRESSURE_CONTROL_RESUMED
        );
        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();
    }


    @POST
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_VALVE_PERCENT_CLOSED_PATH)
    public Response setPercentClosedApi(String values, @Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();
        JSONObject abvJson = new JSONObject(values);

        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
                new AbvManager().setPercentClosed(abvJson);

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        responseJson.put(ApiConstant.SUCCESS, ApiConstant.ABV_PLACED_ON + abvJson.getString(ApiConstant.CONT_BASED_ON) + ApiConstant.IS_SET_TO + abvJson.getString(ApiConstant.PERCENT_CLOSED) + ApiConstant.PERCENT);
        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();
    }


    @POST
    @Path(value = ApiConstant.REST_TRIAL_DETAIL_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveTrialDetailApi(String alarmValues, @Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();
        int trialRunSettingId;
        try {
            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
                trialRunSettingId = AlarmManager.getInstance(). saveTrialDetail(alarmValues);

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        responseJson.put(ApiConstant.SUCCESS, ApiConstant.TRIAL_DETAIL_SAVED_SUCCESSFULLY_MESSAGE);
        responseJson.put(ApiConstant.TRIAL_RUN_SETTING_ID, trialRunSettingId);
        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();
    }


    @PUT
    @Path(value = ApiConstant.REST_TRIAL_DETAIL_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response editTrialDetailApi(String alarmValues, @Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();
        try {
            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
                AlarmManager.getInstance().editTrialDetail(alarmValues);

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }
        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        responseJson.put(ApiConstant.SUCCESS, ApiConstant.TRIAL_DETAIL_UPDATED_SUCCESSFULLY_MESSAGE);
        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();
    }

    @PUT
    @Path(value = ApiConstant.REST_ALARM_SETTING_AT_RUN_ENTRY_POINT)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveAlarmSettingAtRunApi(String alarmValues, @Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();
        Connection conn = null;
        try {
            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {

                if (dbManager.isDigitalSignatureMTVerified(new JSONObject(alarmValues), false)) {

                    conn = DbConnectionManager.getInstance().getConnection();
                    conn.setAutoCommit(false);
                    AlarmManager.getInstance().saveAlarmAtRun(alarmValues, conn, true);
                    conn.commit();

                } else {
                    throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
                }

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }
        } catch (InvalidCredentialException e) {
            e.printStackTrace();
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(responseJson.toString())
                    .build();
        } catch (Exception e) {
            try {
                if (conn != null) {

                    conn.rollback();
                }
            } catch (SQLException sqlEx) {

                sqlEx.printStackTrace();
            }
            e.printStackTrace();
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        responseJson.put(ApiConstant.SUCCESS, ApiConstant.TRIAL_DETAIL_UPDATED_SUCCESSFULLY_MESSAGE);
        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();
    }

    /**
     *
     * @param notesValue
     * @param headers
     * @return
     */
    @POST
    @Path(value = ApiConstant.REST_TRIAL_SAVE_NOTES_ENTRY_POINT)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveNotesApi(String notesValue, @Context HttpHeaders headers) {
        JSONObject responseJson = new JSONObject();
        JSONObject notesJson;
        Notes notes;
        try {

            notesJson = new JSONObject(notesValue);
            if (authorization.isUserRoleEqualsManagerTechnicianAuditor(headers)) {
                if(notesJson.has(ApiConstant.DIGITAL_NOTES)){
                    if(!dbManager.isDigitalSignatureMTVerified(notesJson, false)){
                        throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
                    }
                    notes = new Notes(notesJson.getString(ApiConstant.DIGITAL_NOTES));
                }else{
                    notes = new Notes(notesJson);
                }
                sLog.d(this, notes);
                notesManager.saveNotes(notes);

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        responseJson.put(ApiConstant.SUCCESS, ApiConstant.NOTES_ADDED_SUCCESSFULLY_MESSAGE);
        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();
    }

    /**
     * API_DOC: Service to fetch trial logs list
     *
     * @throws SQLException
     */
    @GET
    @Path(value = ApiConstant.REST_TRIAL_LOGS_LIST_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrialLogsList(@Context HttpHeaders headers) throws SQLException {
        systemSetting = null;
        getStSystemSetting();
        HistoricalConverter.resetTotalizerUnits();
        JSONObject responseJson = new JSONObject();
        try {
            if (!authorization.isUserRoleEqualsManagerTechnician(headers)
                    && !authorization.isUserRoleEqualsAdminSuperAdminAuditor(headers)) {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
            }
            List<TrialLog> getTrialLogsList = dbManager.fetchTrialLogsList();
            if (getTrialLogsList.isEmpty() || getTrialLogsList == null) {
                responseJson.put(ApiConstant.ERROR, ApiConstant.TRIAL_LOGS_LIST_NOT_FOUND_ERROR);
                sLog.d(this, "getTrialLogsList, " + responseJson.toString() + ", STATUS: " + HttpStatus.NOT_FOUND_404);
                return Response.status(HttpStatus.NOT_FOUND_404).entity(responseJson.toString()).build();
            }
            sLog.d(this, "getTrialLogsList, SIZE: " + getTrialLogsList.size() + ", STATUS: " + HttpStatus.OK_200);
            GenericEntity<List<TrialLog>> TrialLogEntity = new GenericEntity<List<TrialLog>>(getTrialLogsList) {
            };
            return Response.ok(TrialLogEntity).build();
        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            sLog.d(this, "getTrialLogsList, " + responseJson.toString() + ", STATUS: " + HttpStatus.INTERNAL_SERVER_ERROR_500);
            return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
        }
    }

    /**
     * API to fetch all trial related details such as Alarms, Recipe, etc.
     * @param trialMasterId Unique id to fetch recipe.
     * @param trialRunSettingId Unique id to fetch trial details.
     * @return returns {@link JSONArray} of all trial details.
     * @throws Exception throws exception if any issue occur while performing this action.
     */
    public JSONArray getAllTrialDetails(int trialMasterId, int trialRunSettingId) throws Exception {
        JSONArray responseJsonArray = null;
        if (trialMasterId != -1) {
            responseJsonArray = dbManager.getRecipe(trialMasterId);
        } else {
            responseJsonArray = new JSONArray();
            responseJsonArray.put(new JSONObject());
            String mainPump = BasicUtility.getInstance().getMainPumpName(trialRunSettingId);
            if (mainPump != null && !mainPump.isEmpty()) {
                responseJsonArray.getJSONObject(0).put(ApiConstant.MAIN_PUMP_KEY, mainPump);
            } else {
                responseJsonArray.getJSONObject(0).put(ApiConstant.MAIN_PUMP_KEY, ApiConstant.BLANK_QUOTE);
            }
            final JSONObject auxPumpType = BasicUtility.getInstance().getAuxPumpType(trialRunSettingId);
            if (auxPumpType != null) {
                responseJsonArray.getJSONObject(0).put(ApiConstant.AUX_PUMP, auxPumpType);
            } else {
                responseJsonArray.getJSONObject(0).put(ApiConstant.AUX_PUMP, new JSONObject());
            }
            responseJsonArray.getJSONObject(0).put(ApiConstant.FILTER, ManualTrialManager.getInstance().getManualFilterDetails(trialRunSettingId));
        }
        responseJsonArray.getJSONObject(0).put(ApiConstant.RUN_KEY, dbManager.fetchTrialDetails(trialRunSettingId));
        responseJsonArray.getJSONObject(0).put(ApiConstant.ALARMS_KEY, dbManager.fetchAlarmsDetails(trialRunSettingId));

        return responseJsonArray;
    }

    @GET
    @Path(ApiConstant.REST_TRIAL_LOGS_DETAILS_PATH)
    public Response getTrialLogDetails(@PathParam(ApiConstant.PARAM_TRIAL_RUN_SETTING_ID) int trialRunSettingId,
                                       @PathParam(ApiConstant.PARAM_TRIAL_MASTER_ID) int trialMasterId,
                                       @Context HttpHeaders headers) throws SQLException {

        JSONArray responseJsonArray = null;
        JSONObject responseJson = new JSONObject();
        RequestHandler.trialRunSettingId = 0;
        HistoricalConverter.resetPumpNameValues();
        HistoricalConverter.assignPumpNameAndAuxType(trialRunSettingId);
        try {
//            if (!authorization.isUserRoleEqualsManagerTechnician(headers)
//                    && !authorization.isUserRoleEqualsAdminSuperAdminAuditor(headers)) {
//                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
//                return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
//            }
            responseJsonArray = getAllTrialDetails(trialMasterId, trialRunSettingId);
            responseJsonArray.getJSONObject(0).put(ApiConstant.END_POINT_SETTING_KEY, EndPointSettingManager.getInstance().getEndPointSettingHistorical(trialRunSettingId));
            responseJsonArray.getJSONObject(0).put(ApiConstant.TARGET_STEP_SETTING_KEY, TargetStepSettingManager.getInstance().getTargetStepSettingHistorical(trialRunSettingId));
        } catch (Exception e) {
            e.printStackTrace();
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(responseJsonArray.toString())
                .build();
    }


    /**
     * API_DOC: Service to fetch pump look up list
     */
    @GET
    @Path(value = ApiConstant.REST_PUMP_LOOK_UP_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPumpsLookUpList() {

        JSONObject responseJson = new JSONObject();
        try {
            List<PumpLookUp> fetchedPumpLookUp = dbManager.fetchPumpLookUpList();
            if (fetchedPumpLookUp.isEmpty() || fetchedPumpLookUp == null) {
                responseJson.put(ApiConstant.ERROR, ApiConstant.PUMP_LOOK_UP_LIST_NOT_FOUND_ERROR);
                sLog.d(this, "getPumpsLookUpList, " + responseJson.toString() + ", STATUS: " + HttpStatus.NOT_FOUND_404);
                return Response.status(HttpStatus.NOT_FOUND_404).entity(responseJson.toString()).build();
            }
            sLog.d(this, "getPumpsLookUpList, SIZE: " + fetchedPumpLookUp.size() + ", STATUS: " + HttpStatus.OK_200);
            GenericEntity<List<PumpLookUp>> pumpGenericEntity = new GenericEntity<List<PumpLookUp>>(fetchedPumpLookUp) {
            };
            return Response.ok(pumpGenericEntity).build();
        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            sLog.d(this, "getPumpsLookUpList, " + responseJson.toString() + ", STATUS: " + HttpStatus.INTERNAL_SERVER_ERROR_500);
            return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
        }
    }

    /**
     * API to get system log.
     * @param logType
     * @param headers headers need to manage session.
     * @return returns {@link Response} that includes ERROR/SUCCESS message
     * along with {@link HttpStatus} as per request.
     */
    @GET
    @Path(value = ApiConstant.REST_GET_SYSTEM_LOG_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSystemLogApi(@PathParam("logType") int logType, @Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();
        JSONArray systemLogJsonArray = null;
        try {

            if (
                    headers.getRequestHeader(ApiConstant.AUTHORIZATION)
                            .get(0)
                            .equals(DbConnectionManager.getInstance().getTokenManager().getToken())
            ) {

                systemLogJsonArray = LogManager.getInstance().getSystemLog(logType);

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

            return Response.ok(systemLogJsonArray.toString()).build();
        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            sLog.d(this, "getPumpsLookUpList, " + responseJson.toString() + ", STATUS: " + HttpStatus.INTERNAL_SERVER_ERROR_500);
            return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
        }
    }

    /**
     * API to switch Vacuum sub mode from Concentration Factor(CF) to Diafiltration Volume(DV) or vice versa
     * @param digitalSignatureValues Digital signature to validate user action.
     * @param headers headers need to manage session.
     * @return returns {@link Response} that includes ERROR/SUCCESS message
     * along with {@link HttpStatus} as per request.
     */
    @POST
    @Path(value = ApiConstant.REST_SWITCH_VACUME_SUB_MODE_API)
    @Produces(MediaType.APPLICATION_JSON)
    public Response switchVacuumSubModeApi(String digitalSignatureValues, @Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();
        int vacuumModeStep;
        String endPoint = ApiConstant.BLANK_QUOTE;
        String action = ApiConstant.BLANK_QUOTE;
        try {

            JSONObject digitalSignatureJson = new JSONObject(digitalSignatureValues);

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {

                if (dbManager.isDigitalSignatureMTVerified(digitalSignatureJson, false)) {

                    RunModeManager.get().changeVacuumModeTarget();
                    vacuumModeStep = DeviceManager.getInstance().getVacuumModeStep();
                    DeviceManager.getInstance().setVacuumModeStep((vacuumModeStep == 0) ? 1 : 0);

                    endPoint = (vacuumModeStep == 0)
                            ? ApiConstant.CONCENTRATION_FACTOR
                            : ApiConstant.DIAFILTRATION_VOLUME;

                    action = ApiConstant.CHANGE_VACUUM_SUB_MODE + endPoint;
                    // saving trial log
                    if (TrialManager.getInstance().getTrialRunSettingId() != 0) {

                        LogManager.getInstance().insertTrialLog(
                                BasicUtility.getInstance().getUserId(digitalSignatureJson),
                                TrialManager.getInstance().getTrialRunSettingId(),
                                action,
                                ApiConstant.LOG_TYPE_TRIAL
                        );

                        //Adding digital signature notes
                        if(digitalSignatureJson.has(ApiConstant.DIGITAL_NOTES)){
                            new NotesManager().saveNotes(new Notes(digitalSignatureJson.getString(ApiConstant.DIGITAL_NOTES)));
                        }
                    } else {
                        throw new TrialRunException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);

                    }
                } else {
                    throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
                }


            } else {
                Logger.error(this, "switchVacuumSubModeApi : "+ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE, HttpStatus.UNAUTHORIZED_401);
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

            responseJson.put(ApiConstant.SUCCESS, action);
            Logger.debug(this, "switchVacuumSubModeApi : "+action, HttpStatus.OK_200);
            return Response.ok(responseJson.toString()).build();
        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            Logger.error(this, "switchVacuumSubModeApi : "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR_500, e);
            return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
        }
    }

    /**
     * Switch user role at login time.
     * @param role role to switch.
     * @param headers headers need to manage session.
     * @return returns {@link Response} that includes ERROR/SUCCESS message
     * along with {@link HttpStatus} as per request.
     */
    @GET
    @Path(value = ApiConstant.REST_SWITCH_ROLE_API)
    public Response switchRoleApi(@PathParam(ApiConstant.ROLE) String role, @Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();
        try {

            if (authorization.isUserRoleEqualsAdminSuperAdminAuditor(headers) || authorization.isUserRoleEqualsManagerTechnician(headers)) {
                if (!role.equalsIgnoreCase(DbConnectionManager.getInstance().getTokenManager().getUserType())) {
                    Logger.info(this, "switchRoleApi");
                    DbConnectionManager.getInstance().getTokenManager().setUserType(role);
                    BasicUtility.getInstance().updateUserRoleInAccessLog(role);
                } else {
                    DbConnectionManager.getInstance().getTokenManager().setUserType(role);
                }
                Logger.info(this, "switchRoleApi : userRole : "+ DbConnectionManager.getInstance().getTokenManager().getUserType());
            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                Logger.error(this, "switchRoleApi", HttpStatus.UNAUTHORIZED_401);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

            return Response.ok().build();

        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            Logger.error(this, "switchRoleApi", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
            return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();
        }

    }

    /**
     * API used to maintain 15 minute lockout screen.
     * <p> Lockout screen appears when user is inactive for 15 minutes asking user to enter credential to proceed.</p>
     * @param isSessionTimeout true if session timeout occurs else false.
     * @param values required data to perform this action.
     * @param headers headers need to manage session.
     * @return returns {@link Response} that includes ERROR/SUCCESS message
     * along with {@link HttpStatus} as per request.
     */
    @POST
    @Path(value = ApiConstant.REST_CHECK_15_MIN_LOCKOUT_PATH)
    public Response check15MinLockoutApi(@PathParam(ApiConstant.SESSION_TIMEOUT_KEY) int isSessionTimeout, String values, @Context HttpHeaders headers) {
        JSONObject responseJson = new JSONObject();
        try {

            Logger.info(this, "hit 15 min : check15MinLockoutApi : isSessionTimeout "+isSessionTimeout);

//            if (authorization.isUserRoleEqualsManagerTechnician(headers) || authorization.isUserRoleEqualsAdminSuperAdminAuditor(headers)) {

                if(isSessionTimeout == 1){
                    DbConnectionManager.getInstance().getTokenManager().setSessionTimeout(true);
                    return Response.status(Response.Status.OK)
                            .build();
                }
            if (dbManager.isDigitalSignatureSAAVerified(new JSONObject(values), true)) {
                DbConnectionManager.getInstance().getTokenManager().setSessionTimeout(false);
                Logger.debug(this, "check15MinLockoutApi", HttpStatus.OK_200);
                return Response.status(Response.Status.OK)
                            .build();

                } else {
                    throw new InvalidCredentialException(ApiConstant.INVALID_LOGIN_MESSAGE);
                }

//            } else {
//                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
//                return Response.status(Response.Status.UNAUTHORIZED)
//                        .entity(responseJson.toString())
//                        .build();
//            }

        } catch (InvalidCredentialException e) {

            responseJson.put(ApiConstant.ERROR, ApiConstant.INVALID_LOGIN_MESSAGE);
            Logger.error(this, "check15MinLockoutApi", HttpStatus.UNAUTHORIZED_401, e);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(responseJson.toString())
                    .build();
        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            Logger.error(this, "check15MinLockoutApi", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }


    }


    /**
     * API used to maintain ABV pressure during trial run.
     * @param abvSetting ABV setting need to update.
     * @param headers headers need to manage session.
     * @return returns {@link Response} that includes ERROR/SUCCESS message
     * along with {@link HttpStatus} as per request.
     */
    @POST
    @Path(value = ApiConstant.REST_ABV_PRESSURE_CONTROL_PATH)
    public Response abvPressureControlAtRunApi(ABVSetting abvSetting, @Context HttpHeaders headers) {
        JSONObject responseJson = new JSONObject();
        ABVSetting[] abvSettings = new ABVSetting[1];
        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
                abvSettings[0] = abvSetting;

                new AbvManager().abvPressureControlAtRun(abvSettings);

                responseJson.put(ApiConstant.SUCCESS, ApiConstant.ABV_SETTINGS_UPDATE_MESSAGE);
                return Response.ok()
                        .entity(responseJson.toString())
                        .build();

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }


    }

    /**
     * API return status either the current software is KFComm 2 or 2C.
     * @return returns {@link Response} that includes ERROR/SUCCESS message
     * along with {@link HttpStatus} as per request.
     */
    @GET
    @Path(value = ApiConstant.GET_CFR_STATUS_API)
    public Response get21CfrStatusApi() {
        JSONObject responseJson = new JSONObject();

        try {

            responseJson.put(ApiConstant.CFR_STATUS, cfrManager.get21CfrStatus());
            return Response.ok()
                    .entity(responseJson.toString())
                    .build();

        } catch (Exception e) {
            responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }


    }

    /**
     * API provides all the necessary data that is required for frontend to maintain its current
     * state either it is in trial run state or anything else.
     * @param headers headers need to manage session.
     * @return returns {@link Response} that includes ERROR/SUCCESS message
     * along with {@link HttpStatus} as per request.
     */
    @GET
    @Path(value = ApiConstant.GET_RUNNING_STATUS)
    public Response getRunningStatusApi(@Context HttpHeaders headers) {
        JSONObject responseJson = new JSONObject();

        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers) || authorization.isUserRoleEqualsAdminSuperAdminAuditor(headers)) {

                responseJson = RunManager.getInstance().isTrialRunning();
                return Response.ok()
                        .entity(responseJson.toString())
                        .build();

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }


        } catch (Exception e) {
            e.printStackTrace();
            responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }


    }


    /**
     * API used to disable or enable an existing recipe.
     * @param trialMasterId unique recipe id.
     * @param status status need to set.
     * @param headers headers need to manage session.
     * @return returns {@link Response} that includes ERROR/SUCCESS message along with {@link HttpStatus} as per request.
     */
    @GET
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_SET_RECIPE_STATUS)
    public Response setRecipeStatusApi(@PathParam(ApiConstant.TRIAL_MASTER_ID) int trialMasterId, @PathParam(ApiConstant.STATUS) int status, @Context HttpHeaders headers) {

        int updateStatus = 0;
        JSONObject responseJson = new JSONObject();
        try {

            if (authorization.isUserRoleEqualsManager(headers)) {
                Logger.info(this, "setRecipeStatusApi : Setting recipe status...");
                updateStatus = dbManager.setRecipeStatus(trialMasterId, status);
                if(updateStatus == 0){
                    Logger.info(this, "setRecipeStatusApi : Recipe does not exist.");
                    throw new RuntimeException(ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
                }

            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();
        }

        return Response.status(Response.Status.OK)
                .build();
    }
    
	/**
	 * API_DOC: fetchAllTubingsList provides a list of all the available tubing
	 * sizes and their details - to be used in create/edit recipe
	 *
	 */
	@GET
	@Path(ApiConstant.REST_TUBING_LIST)
	public Response fetchAllTubingsList(@Context HttpHeaders headers) {

		JSONObject responseJson = new JSONObject();
		try {

			if (!authorization.isUserRoleEqualsManagerTechnician(headers)) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
				Logger.error(this, "fetchAllTubingsList", HttpStatus.UNAUTHORIZED_401, responseJson);
				return Response.status(Response.Status.UNAUTHORIZED).entity(responseJson.toString()).build();
			}

			List<TubingDetails> fetchedList = dbManager.fetchTubingsList();
			if (fetchedList.size() == 0 || fetchedList == null) {
				responseJson.put(ApiConstant.ERROR, ApiConstant.SYSTEM_SETTING_NOT_FOUND_ERROR);
				Logger.error(this, "fetchAllTubingsList", HttpStatus.NOT_FOUND_404, responseJson);
				return Response.status(HttpStatus.NOT_FOUND_404).entity(responseJson.toString()).build();
			}
			Logger.info(this, "fetchAllTubingsList", HttpStatus.OK_200);
			return Response.ok(new ObjectMapper().writeValueAsString(fetchedList)).build();

		} catch (Exception e) {

			responseJson.put(ApiConstant.ERROR, e.getMessage());
			Logger.error(this, "fetchAllTubingsList", HttpStatus.INTERNAL_SERVER_ERROR_500, e);
			return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(responseJson.toString()).build();

		}

	}
    
    /**
     * API service to check whether the trialId input by user is already exist or not.
     * @param trialId trial Id input by user.
     * @param headers headers need to manage session.
     * @return returns {@link Response} that includes ERROR/SUCCESS message
     * along with {@link HttpStatus} as per request.
     */
    @POST
    @Path(ApiConstant.REST_TRIAL_PATH + ApiConstant.REST_CHECK_TRIAL_ID_PATH)
    public Response isTrailIdExistApi(@PathParam(ApiConstant.TRIAL_ID_PATH_PARAM) String trialId, @Context HttpHeaders headers) {
        JSONObject responseJson = new JSONObject();
        boolean isExist;
        try {
            Logger.info(this, "isTrailIdExistApi : Checking trial ID : "+trialId);
            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
                isExist = ValidationManager.isTrialRunExist(trialId);
                if(isExist){
                    throw new TrialRunException(ApiConstant.TRIAL_RUN_EXIST_ERROR_MESSAGE);
                }
            } else {
                Logger.error(this, "isTrailIdExistApi : "+ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            Logger.error(this, "isTrailIdExistApi", HttpStatus.NOT_ACCEPTABLE_406, e);
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();
        }
        Logger.info(this, "isTrailIdExistApi", HttpStatus.OK_200);
        return Response.status(Response.Status.OK)
                .build();
    }

}
