package com.sciamlab.common.exception;

public class DAOException extends SciamlabRuntimeException {

	private static final long serialVersionUID = -2213429135494785501L;

	public DAOException(String msg){
		super(msg);
	}

	public DAOException(Throwable t){
		super(t);
	}
}
