package org.lattice.spectrum_backend_final.dao.util;

/**
 * @author RAHUL KUMAR MAURYA
 */


import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;


/**
 * Handles CORS related issues.
 * Provides different types of access controls on methods , origin , headers and credentials.
 */
@Provider
public class CORSFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        responseContext.getHeaders().add(
                ApiConstant.ACCESS_CONTROL_ALLOW_ORIGIN, ApiConstant.ASTERISK);
        responseContext.getHeaders().add(
                ApiConstant.ACCESS_CONTROL_ALLOW_CREDENTIALS, ApiConstant.TRUE);
        responseContext.getHeaders().add(
                ApiConstant.ACCESS_CONTROL_ALLOW_HEADERS,
                ApiConstant.ORIGIN_CONTENT_TYPE_ACCEPT_AUTHORIZATION);
        responseContext.getHeaders().add(
                ApiConstant.ACCESS_CONTROL_ALLOW_METHODS,
                ApiConstant.GET_POST_PUT_DELETE_HEAD);
    }
}