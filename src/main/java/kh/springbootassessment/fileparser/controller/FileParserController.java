package kh.springbootassessment.fileparser.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import kh.springbootassessment.fileparser.data.RequestSourceValidationResult;
import kh.springbootassessment.fileparser.data.ResponseFileItem;
import kh.springbootassessment.fileparser.service.FileParserService;
import kh.springbootassessment.fileparser.service.FileValidator;
import kh.springbootassessment.fileparser.service.SourceRequestBlockedCountryCodeException;

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
}
