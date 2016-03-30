package com.sciamlab.common.exception;

public abstract class SciamlabVocabularyException extends SciamlabException {

	private static final long serialVersionUID = -2213429135494785501L;

	public SciamlabVocabularyException(String msg){
		super(msg);
	}

	public SciamlabVocabularyException(Throwable t){
		super(t);
	}
}
