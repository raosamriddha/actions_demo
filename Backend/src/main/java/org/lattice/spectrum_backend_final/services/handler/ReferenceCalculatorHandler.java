package org.lattice.spectrum_backend_final.services.handler;

import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.ReferenceCalculatorManager;
import org.lattice.spectrum_backend_final.dao.util.Logger;
import org.lattice.spectrum_backend_final.dao.util.UserAuthorization;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@Path(ApiConstant.REST_REFERENCE_CALCULATOR_ENTRY_POINT)
public class ReferenceCalculatorHandler {

    UserAuthorization authorization = new UserAuthorization();

    @POST
//    @Path(value = ApiConstant.REST_ABV_PRESSURE_CONTROL_PATH)
    public Response getCalculatedResultApi(String values, @Context HttpHeaders headers) {
        JSONObject responseJson = new JSONObject();
        JSONObject parametersJson;
        Logger.info(this, "getCalculatedResultApi : Calculating..");
        try {
            parametersJson = new JSONObject(values);
            Logger.debug(this, "getCalculatedResultApi : parametersJson", parametersJson);
//            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
            responseJson = new ReferenceCalculatorManager().getCalculatedResult(parametersJson);
            Logger.debug(this, "getCalculatedResultApi : calculated Results", responseJson, HttpStatus.OK_200);
            return Response.ok()
                        .entity(responseJson.toString())
                        .build();

//            } else {
//                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
//                return Response.status(Response.Status.UNAUTHORIZED)
//                        .entity(responseJson.toString())
//                        .build();
//            }

        } catch (Exception e) {
            Logger.error(this, "getCalculatedResultApi",HttpStatus.INTERNAL_SERVER_ERROR_500, e);
            responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();
        }

    }

}
