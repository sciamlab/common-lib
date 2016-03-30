package com.sciamlab.common.exception;

public class DistributionListEmptyException extends DatasetBuilderException {

	private static final long serialVersionUID = 2388065250231179447L;
	
	public DistributionListEmptyException(String msg){
		super(msg);
	}

	public DistributionListEmptyException(Throwable t){
		super(t);
	}

	public DistributionListEmptyException() {
		super();
	}

}
