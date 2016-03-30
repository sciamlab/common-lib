package com.sciamlab.common.model.mdr;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;
import org.junit.Before;
import org.junit.Test;

import com.sciamlab.common.exception.SciamlabException;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityCountry;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityFrequency;
import com.sciamlab.common.model.mdr.vocabulary.EUNamedAuthorityLanguage;

public class EUNamedAuthorityTest {
	
	@Before
	public void setup(){
		ConsoleAppender console = new ConsoleAppender(new PatternLayout("%m%n"));
		console.setThreshold(Level.INFO);
		BasicConfigurator.configure(console);
	}

	@Test
	public <E extends EUNamedAuthorityEntry> void loadAllVocabularies() throws SciamlabException {
		for (EUNamedAuthorityVocabulary vocabulary : EUNamedAuthorityVocabulary.values()) {
			System.out.println("Vocabulary : " + vocabulary.toString());
			for (E entry : vocabulary.<E>load().values()) {
				org.junit.Assert.assertNotNull(entry);
				System.out.println(entry);
			}
		}
	}
	

	@Test
	public void loadCountriesVocabularyCached() throws SciamlabException {
		for (EUNamedAuthorityCountry entry : EUNamedAuthorityVocabulary.COUNTRY.<EUNamedAuthorityCountry>load().values()) {
			org.junit.Assert.assertNotNull(entry);
			System.out.println(entry);
		}
	}
	
	@Test
	public void loadLanguagesVocabularyNOTCached() throws SciamlabException {
		for (EUNamedAuthorityLanguage entry : EUNamedAuthorityVocabulary.LANGUAGE.<EUNamedAuthorityLanguage>load().values()) {
			org.junit.Assert.assertNotNull(entry);
			System.out.println(entry);
		}
	}
	
	@Test
	public void loadFrequencyVocabularyWithAlias() throws SciamlabException {
		for (EUNamedAuthorityFrequency entry : EUNamedAuthorityVocabulary.FREQUENCY.<EUNamedAuthorityFrequency>load().values()) {
			org.junit.Assert.assertNotNull(entry);
			System.out.println(entry);
		}
	}
	
	@Test
	public <E extends EUNamedAuthorityEntry> void loadGenericVocabulary() throws SciamlabException {
		for (E entry : EUNamedAuthorityVocabulary.FREQUENCY.<E>load().values()) {
			org.junit.Assert.assertNotNull(entry);
			System.out.println(entry);
		}
	}
	
	
}
