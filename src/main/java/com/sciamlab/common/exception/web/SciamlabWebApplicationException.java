package com.sciamlab.common.exception.web;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.sciamlab.common.util.SciamlabErrorResponse;

/**
 * 
 * @author SciamLab
 *
 */

public class SciamlabWebApplicationException extends WebApplicationException {

	private static final long serialVersionUID = 5522352446289929881L;
	private final int status;
    private final String errorMessage;
    private final int errorCode;
    private final String developerMessage;
	private static final Logger logger = Logger.getLogger(SciamlabWebApplicationException.class);

    public SciamlabWebApplicationException(int httpStatus, int errorCode, String errorMessage, String developerMessage) {
        this.status = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.developerMessage = developerMessage;
    }

    @Override
    public Response getResponse() {
    	return Response.status(status).type(MediaType.APPLICATION_JSON_TYPE).entity(getErrorResponse().toJSONString()).build();
    }

    public SciamlabErrorResponse getErrorResponse() {
        SciamlabErrorResponse response = new SciamlabErrorResponse();
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        response.setApplicationMessage(developerMessage);
        return response;
    }
    
    @Override
	public String getMessage(){
		return this.getErrorResponse().toJSONString();
	}

}
