package kh.springbootassessment.fileparser.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class EmptyValidator implements FileValidator {

	private static final Logger logger = LoggerFactory.getLogger(EmptyValidator.class);
	
	public EmptyValidator() {
		logger.info("EmptyValidator initialized...");
	}
	
	@Override
	public boolean validateFile(String fileContent) {
		return true;
	}

	@Override
	public boolean validateLineContent(String[] fields) {
		return true;
	}

}
