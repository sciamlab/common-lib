package com.sciamlab.common.exception;

public class DatasetBuilderException extends SciamlabModelBuilderException {

	private static final long serialVersionUID = 2388065250231179447L;
	
	public DatasetBuilderException(String msg){
		super(msg);
	}

	public DatasetBuilderException(Throwable t){
		super(t);
	}
	
	public DatasetBuilderException() {
		super();
	}

}
