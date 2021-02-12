package org.lattice.spectrum_backend_final.services.handler;


import com.lattice.spectrum.ComLibrary.comHandler.PortNotFoundException;
import org.json.JSONObject;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.*;
import org.lattice.spectrum_backend_final.dao.util.BasicUtility;
import org.lattice.spectrum_backend_final.dao.util.UserAuthorization;
import org.lattice.spectrum_backend_final.exception.HardwareValidationException;
import org.lattice.spectrum_backend_final.exception.TrialRunException;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path(ApiConstant.FIRMWARE_UPDATE_PATH)
public class FirmwareHandler {

    FirmwareManager firmwareManager = new FirmwareManager();
    UserAuthorization authorization = new UserAuthorization();


    /**
     * Api to check compatibility of firmware.
     */
    @GET
    @Path(ApiConstant.CHECK_FIRMWARE_VERSION_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response isCompatibleFirmwareVersionApi(@Context HttpHeaders headers, String digitalSignatureValues) {

        JSONObject responseJson = new JSONObject();
        try {

            if (authorization.isUserRoleEqualsManagerTechnician(headers)) {

                String pumpName = DeviceManager.getInstance().autoConnect();

                if(pumpName == null){
                    throw new PortNotFoundException(ApiConstant.PORT_NOT_FOUND);
                }

                if (!firmwareManager.isCompatibleFirmwareVersion()) {
                    responseJson.put(ApiConstant.SUCCESS, ApiConstant.FIRMWARE_MISMATCH_ERROR_MESSAGE);
                    responseJson.put(ApiConstant.IS_UPDATED, false);
                } else {
                    responseJson.put(ApiConstant.SUCCESS, ApiConstant.FIRMWARE_UP_TO_DATE);
                    responseJson.put(ApiConstant.IS_UPDATED, true);
                }


                return Response.ok()
                        .entity(responseJson.toString())
                        .build();
            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }
        } catch (HardwareValidationException hvEx) {

            responseJson.put(ApiConstant.ERROR, hvEx.getMessage());
            hvEx.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();

        }catch (PortNotFoundException pnfEx) {

            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, pnfEx);
            responseJson.put(ApiConstant.ERROR, pnfEx.getMessage());

            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();

        }catch (Exception ex) {

            responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();

        }


    }

    /**
     * Api to update latest firmware.
     */
    @POST
    @Path(ApiConstant.UPDATE_FIRMWARE_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFirmwareVersionApi(@Context HttpHeaders headers, String digitalSignatureValues) {

        JSONObject responseJson = new JSONObject();

        JSONObject digitalSignatureJson = new JSONObject(digitalSignatureValues);
        String notes = ApiConstant.BLANK_QUOTE;
        try {


            if (
                    authorization.isUserRoleEqualsManagerTechnician(headers) &&
                            DatabaseManager.getInstance().isDigitalSignatureMTVerified(digitalSignatureJson, false)
            ) {

                if(digitalSignatureJson.has(ApiConstant.DIGITAL_NOTES)){
                    notes = digitalSignatureJson.getString(ApiConstant.DIGITAL_NOTES);
                }
//                if (DeviceManager.getInstance().getModeType() != 2) {
//                    throw new TrialRunException(ApiConstant.OPERATION_FAILED_MESSAGE);
//                }


                LogManager.getInstance().insertSystemLog(
                        BasicUtility.getInstance().getUserId(digitalSignatureJson),
                        0,
                        ApiConstant.UPDATE_FIRMWARE_ACTION,
                        notes
                );

                firmwareManager.updateFirmwareOnDevice();
                responseJson.put(ApiConstant.SUCCESS, ApiConstant.UPDATING_FIRMWARE);


                return Response.ok()
                        .entity(responseJson.toString())
                        .build();
            } else {
                responseJson.put(ApiConstant.ERROR, ApiConstant.UNAUTHORIZED_ACCESS_MESSAGE);
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(responseJson.toString())
                        .build();
            }


        } catch (TrialRunException trEx) {

            responseJson.put(ApiConstant.ERROR, trEx.getMessage());
            trEx.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();

        } catch (Exception ex) {

            responseJson.put(ApiConstant.ERROR, ApiConstant.SOMETHING_WENT_WRONG_PLEASE_RESTART_APPLICATION);
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(responseJson.toString())
                    .build();

        }


    }
}
