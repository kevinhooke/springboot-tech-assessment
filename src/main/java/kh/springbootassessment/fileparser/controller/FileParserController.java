package kh.springbootassessment.fileparser.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kh.springbootassessment.fileparser.data.RequestSourceValidationResult;
import kh.springbootassessment.fileparser.data.ResponseFileItem;
import kh.springbootassessment.fileparser.data.ResponseWrapper;
import kh.springbootassessment.fileparser.service.FileParserService;
import kh.springbootassessment.fileparser.service.exception.FileContentMissingFieldsException;
import kh.springbootassessment.fileparser.service.exception.InvalidFieldValueException;
import kh.springbootassessment.fileparser.service.exception.SourceRequestBlockedCountryCodeException;
import kh.springbootassessment.fileparser.service.exception.SourceRequestBlockedISPExcpetion;
import kh.springbootassessment.fileparser.service.validator.FileValidator;

@RestController
public class FileParserController {

	/**
	 * The validator implementation is conditionally injected depending on the value
	 * feature flag property: features.entryfile.validation
	 * 
	 * If true, validation is enabled, otherwise validation is skipped
	 */
	@Autowired
	private FileValidator validator;
	
	@Autowired
	private FileParserService fileParserService;
	
	@PostMapping (path = "/entryfile", consumes = MediaType.TEXT_PLAIN_VALUE
			, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ResponseFileItem> parseEntryFile(@RequestBody String entryFileContent, HttpServletRequest request) {
		
	List<ResponseFileItem> result = null;
	
		String sourceIPAddress = request.getRemoteAddr();
		RequestSourceValidationResult ipValidationResult = this.validator.validateSourceIP(sourceIPAddress);
		
		if(ipValidationResult.isValid()) {
			result = this.fileParserService.parseFileContent(entryFileContent);
		}
		else
		{
			//TODO build error response based on exceptions and contained error message
		}
		
		return result;
	}
	
	
	
	@ExceptionHandler(value = InvalidFieldValueException.class)
	public ResponseEntity<Object> handleInvalidFieldValueException(InvalidFieldValueException e) {
		ResponseWrapper response = new ResponseWrapper("Error: invalid values in submitted EntryFile data", HttpStatus.INTERNAL_SERVER_ERROR.value());
	    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	
	@ExceptionHandler(value = FileContentMissingFieldsException.class)
	public ResponseEntity<Object> handleFileContentMissingFieldsException(FileContentMissingFieldsException e) {
		ResponseWrapper response = new ResponseWrapper("Error: expected field(s) missing from lines in submitted EntryFile data", HttpStatus.INTERNAL_SERVER_ERROR.value());
	    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(value = SourceRequestBlockedCountryCodeException.class)
	public ResponseEntity<Object> handleSourceRequestBlockedCountryCodeException(SourceRequestBlockedCountryCodeException e) {
		ResponseWrapper response = new ResponseWrapper("Error: request rejected. IP address from blocked country code", HttpStatus.FORBIDDEN.value());
	    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}
	
	@ExceptionHandler(value = SourceRequestBlockedISPExcpetion.class)
	public ResponseEntity<Object> handleSourceRequestBlockedISPExcpetion(SourceRequestBlockedISPExcpetion e) {
		ResponseWrapper response = new ResponseWrapper("Error: request rejected, IP address from blocked ISP/Data Center IP range", HttpStatus.FORBIDDEN.value());
	    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
	}
}
