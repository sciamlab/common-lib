package com.sciamlab.common.exception;

public class SciamlabException extends Exception {

	private static final long serialVersionUID = -2213429135494785501L;

	public SciamlabException(String msg){
		super(msg);
	}

	public SciamlabException(Throwable t){
		super(t);
	}
	
	public SciamlabException(){
		super();
	}
}
