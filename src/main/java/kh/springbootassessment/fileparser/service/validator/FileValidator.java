package kh.springbootassessment.fileparser.service.validator;

import jakarta.servlet.http.HttpServletRequest;
import kh.springbootassessment.fileparser.data.RequestSourceValidationResult;

public interface FileValidator {

	public RequestSourceValidationResult validateSourceIP(HttpServletRequest request);
	
	/**
	 * Validates content of the file is not null or empty
	 * @param fileContent
	 * @return true if valid
	 */
	public boolean validateFile(String fileContent);
	public boolean validateLineContent(String[] fields);
}
