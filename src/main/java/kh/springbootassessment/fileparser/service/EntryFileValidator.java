package kh.springbootassessment.fileparser.service;

import java.math.BigDecimal;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import kh.springbootassessment.fileparser.data.EntryFileContent;

public class EntryFileValidator implements FileValidator {

	private static final int EXPECTED_FIELDS_PER_LINE = 7;
	private static final Logger logger = LoggerFactory.getLogger(EntryFileValidator.class);
	
	public EntryFileValidator() {
		logger.info("EntryFileValidator instantiated...");
	}
	
	@Override
	public boolean validateFile(String fileContent) {
		boolean result = false;
		if(fileContent == null || fileContent.trim().length() == 0) {
			throw new FileContentEmptyException();
		}
		else {
			result = true;
		}
		return result;
	}

	/**
	 * Validates input file line matches expected format, for example:
	 * 
	 * 18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1
	 */
	@Override
	public boolean validateLineContent(String[] fields) {
		boolean result = false;
		Set<ConstraintViolation<EntryFileContent>> violations = null;
		
		if(fields.length < EXPECTED_FIELDS_PER_LINE) {
			throw new FileContentMissingFieldsException();
		}
		else {
			EntryFileContent lineItem = new EntryFileContent();
			lineItem.setUuid(fields[0]);
			lineItem.setId(fields[1]);
			lineItem.setName(fields[2]);
			lineItem.setLikes(fields[3]);
			lineItem.setTransport(fields[4]);
			lineItem.setAvgSpeed(new BigDecimal(fields[5]));
			lineItem.setTopSpeed(new BigDecimal(fields[6]));
			
			//validate with bean validation validator
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Validator validator = factory.getValidator();
			violations = validator.validate(lineItem);
		}

		if(violations.size() == 0) {
			result = true;
		}
		else {
			//TODO copy validation messages to exception
			throw new InvalidFieldValueException();
		}

		return result;
	}

}

