package kh.springbootassessment.fileparser.service.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import kh.springbootassessment.fileparser.data.RequestSourceValidationResult;


public class EmptyValidator implements FileValidator {

	private static final Logger logger = LoggerFactory.getLogger(EmptyValidator.class);
	
	public EmptyValidator() {
		logger.info("EmptyValidator initialized...");
	}
	
	@Override
	public RequestSourceValidationResult validateSourceIP(HttpServletRequest request) {
		RequestSourceValidationResult result = new RequestSourceValidationResult();
		result.setValid(true);
		return result;
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
