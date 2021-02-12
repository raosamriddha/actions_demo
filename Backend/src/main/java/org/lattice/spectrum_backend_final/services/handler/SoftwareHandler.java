package org.lattice.spectrum_backend_final.services.handler;

import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;
import org.lattice.spectrum_backend_final.dao.manager.LogManager;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.UserAuthorization;
import org.lattice.spectrum_backend_final.exception.InvalidCredentialException;
import org.lattice.spectrum_backend_final.exception.TrialRunException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path(ApiConstant.SPECTRUM_SOFTWARE_PATH)
public class SoftwareHandler {

    UserAuthorization authorization = new UserAuthorization();

    /**
     * Api for quit operation.
     */
    @GET
    @Path(ApiConstant.SPECTRUM_QUIT_PATH)
    public Response closeFrontEnd() {

        JSONObject responseJson = new JSONObject();
        try {

            String currentDir = System.getProperty(ApiConstant.USER_DIRECTORY);

            Runtime.getRuntime().exec(
                    currentDir + ApiConstant.CLOSE_FRONT_END_FILE_PATH);

        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        } catch (Exception ex) {
            ex.printStackTrace();
            responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();

    }

    /**
     * Api to shutdown whole software.
     */
    @GET
    @Path(ApiConstant.SPECTRUM_SOFTWARE_SHUTDOWN_PATH)
    public Response shutdownSoftware(@Context HttpHeaders headers) {
        JSONObject responseJson = new JSONObject();
        try {

            if (
                    authorization.isUserRoleEqualsManagerTechnician(headers) ||
                            authorization.isUserRoleEqualsAdminSuperAdminAuditor(headers)
            ) {

                // check wheather a trial is running
//                if(){
//
//                }

                LogManager.getInstance().insertTrialLog(
                        DbConnectionManager.getInstance().getTokenManager().getUserId(),
                        0,
                        ApiConstant.SHUTDOWN_SOFTWARE_ACTION,
                        ApiConstant.LOG_TYPE_SYSTEM
                );

                String currentDir = System.getProperty(ApiConstant.USER_DIRECTORY);

                Runtime.getRuntime().exec(
                        currentDir + ApiConstant.CLOSE_FRONT_END_FILE_PATH);

                Thread.sleep(1000);

                Runtime.getRuntime().exec(
                        currentDir + ApiConstant.CLOSE_BACKEND_FILE_PATH);

            } else {
                throw new InvalidCredentialException(ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
            }

        } catch (IOException ioEx) {
            ioEx.printStackTrace();
            responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();

        } catch (TrialRunException trEx) {

            responseJson.put(ApiConstant.ERROR, trEx.getMessage());
            trEx.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();

        } catch (InvalidCredentialException icEx) {

            responseJson.put(ApiConstant.ERROR, icEx.getMessage());
            icEx.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();

        } catch (Exception ex) {
            ex.printStackTrace();
            responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();


    }
}
