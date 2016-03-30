package com.sciamlab.common.exception;

public class DistributionBuilderException extends SciamlabModelBuilderException {

	private static final long serialVersionUID = -7568762086529571666L;

	public DistributionBuilderException(String msg){
		super(msg);
	}

	public DistributionBuilderException(Throwable t){
		super(t);
	}
}
