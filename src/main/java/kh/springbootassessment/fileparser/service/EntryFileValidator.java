package kh.springbootassessment.fileparser.service;

import java.math.BigDecimal;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import kh.springbootassessment.fileparser.data.EntryFileContent;
import kh.springbootassessment.fileparser.data.RequestSourceValidationResult;

public class EntryFileValidator implements FileValidator {

	private static final int EXPECTED_FIELDS_PER_LINE = 7;
	private static final String IP_VALIDATION_URL = "/json/{query}?fields=status,message,country,isp,org,hosting,query";
	private static final Logger logger = LoggerFactory.getLogger(EntryFileValidator.class);
	
	@Value("${ip-api.root.url}")
	private String ipValidationRootUrl;
	
	public EntryFileValidator() {
		logger.info("EntryFileValidator instantiated...");
	}
	
	/**
	 * Calls ip-addr.com to validate source ip for the request.
	 */
	@Override
	public RequestSourceValidationResult validateSourceIP(String ip) {
		RestTemplate restTemplate = new RestTemplate();
		RequestSourceValidationResult result = restTemplate
				  .getForObject(ipValidationRootUrl + IP_VALIDATION_URL, RequestSourceValidationResult.class, ip);
		if(result.getStatus().equals("success")) {
			
			//TODO add other checks here
			
			result.setValid(true);
		}
		else {
			result = new RequestSourceValidationResult();
			result.setValid(false);
		}
		return result;
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

