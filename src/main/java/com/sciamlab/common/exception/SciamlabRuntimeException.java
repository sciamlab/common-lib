package com.sciamlab.common.exception;

public class SciamlabRuntimeException extends RuntimeException {

	private static final long serialVersionUID = -2213429135494785501L;

	public SciamlabRuntimeException(String msg){
		super(msg);
	}

	public SciamlabRuntimeException(Throwable t){
		super(t);
	}
}
