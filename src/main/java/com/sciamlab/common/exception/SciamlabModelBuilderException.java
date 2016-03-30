package com.sciamlab.common.exception;

public abstract class SciamlabModelBuilderException extends SciamlabException {

	private static final long serialVersionUID = -2213429135494785501L;

	public SciamlabModelBuilderException(String msg){
		super(msg);
	}

	public SciamlabModelBuilderException(Throwable t){
		super(t);
	}
	
	public SciamlabModelBuilderException(){
		super();
	}
}
