/*===========================================================================================
 = Copyright (c) 2021 Lattice Innovations.
 = Created  :    18/01/21, 3:41 PM
 = Modified :    18/01/21, 3:41 PM
 = Author   :    Rahul Kumar Maurya
 = All right reserved.
 ==========================================================================================*/

package org.lattice.spectrum_backend_final.services.handler;

import com.lattice.spectrum.ComLibrary.utility.sLog;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.ReferenceCalculatorManager;
import org.lattice.spectrum_backend_final.dao.manager.device.AbvManager;
import org.lattice.spectrum_backend_final.dao.util.UserAuthorization;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@Path(ApiConstant.REST_DEVICE_SETTING_PATH)
public class DeviceHandler {

    private UserAuthorization authorization = new UserAuthorization();

    @POST
    @Path(ApiConstant.REST_VALVE_CONFIGURATION_PATH)
    public Response SetValve2ConnectorConfigApi(@PathParam(ApiConstant.TYPE) int typeId, @Context HttpHeaders headers) {
        JSONObject responseJson = new JSONObject();
        try {
            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
            new AbvManager().configureValve2Connector(typeId);
            return Response.ok()
                    .build();
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

    }


}
