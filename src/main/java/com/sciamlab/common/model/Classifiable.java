package com.sciamlab.common.model;

import com.sciamlab.common.model.datacatalog.OpenDataCategory;
import com.sciamlab.common.nlp.EurovocConcept;
import com.sciamlab.common.nlp.EurovocField;
import com.sciamlab.common.nlp.EurovocMicroThesaurus;

public interface Classifiable {
	public String getText();
	public EurovocConcept getConcept();
	public OpenDataCategory getCategory();
	public void setCategory(OpenDataCategory category);
	public EurovocMicroThesaurus getMicroThesaurus();
	public EurovocField getField();
	public void setConcept(EurovocConcept concept);
	public void setMicroThesaurus(EurovocMicroThesaurus microThesaurus);
	public void setField(EurovocField field);
}
