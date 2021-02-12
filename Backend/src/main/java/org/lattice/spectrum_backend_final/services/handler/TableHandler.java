package org.lattice.spectrum_backend_final.services.handler;

import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.DatabaseManager;
import org.lattice.spectrum_backend_final.dao.manager.TrialManager;
import org.lattice.spectrum_backend_final.dao.util.UserAuthorization;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;


@Path(ApiConstant.REST_TABLE_PATH)
public class TableHandler {

    UserAuthorization authorization = new UserAuthorization();

    @GET
    @Path(ApiConstant.REST_TABLE_HEADER_PATH)
    public Response getTableHeaderApi(@Context HttpHeaders headers) {

        JSONObject responseJson = new JSONObject();
        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
                responseJson = TrialManager.getInstance().getTableHeader();
            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, e);
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();
        }

        return Response.status(Response.Status.OK)
                .entity(responseJson.toString())
                .build();
    }


    @POST
    @Path(ApiConstant.REST_TABLE_HEADER_PATH)
    public Response setTableHeaderApi(String values, @Context HttpHeaders headers) {

        JSONObject tableHeader = new JSONObject(values);
        JSONObject responseJson = new JSONObject();

        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {
                TrialManager.getInstance().setTableHeader(tableHeader);
            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, e);
            responseJson.put(ApiConstant.ERROR, e.getMessage());
            return Response.status(Response.Status.NOT_ACCEPTABLE)
                    .entity(responseJson.toString())
                    .build();
        }

        return Response.status(Response.Status.OK)
                .build();
    }

}
