package com.sciamlab.common.exception;

public class OpenDataFormatException extends SciamlabVocabularyException {

	private static final long serialVersionUID = -2213429135494785501L;

	public OpenDataFormatException(String msg){
		super(msg);
	}

	public OpenDataFormatException(Throwable t){
		super(t);
	}
}
