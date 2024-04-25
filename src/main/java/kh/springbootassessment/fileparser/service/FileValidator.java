package kh.springbootassessment.fileparser.service;

public interface FileValidator {

	/**
	 * Validates content of the file is not null or empty
	 * @param fileContent
	 * @return true if valid
	 */
	public boolean validateFile(String fileContent);
	public boolean validateLineContent(String[] fields);
}
