package kh.springbootassessment.fileparser.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kh.springbootassessment.fileparser.data.ResponseFileItem;
import kh.springbootassessment.fileparser.repository.RequestLogRepository;

@Service
public class FileParserService {

	private static final int INPUT_FILE_TOPSPEED_FIELD_POSITION = 6;

	private static final int INPUT_FILE_TRANSPORT_FIELD_POSITION = 4;

	private static final int INPUT_FILE_NAME_FIELD_POSITION = 2;

	private static final String INPUT_FILE_NEW_LINE = "\\n";

	private static final String INPUT_FILE_FIELD_DELIMITER = "\\|";

	/**
	 * The validator implementation is conditionally injected depending on the value
	 * feature flag property: features.entryfile.validation
	 * 
	 * If true, validation is enabled, otherwise validation is skipped
	 */
	@Autowired
	private FileValidator validator;
	
	@Autowired
	private RequestLogRepository logRepository;
	
	/**
	 * Parses and validates the submitted EntryFile.
	 * 
	 * @param fileContent
	 * @return
	 */
	public List<ResponseFileItem> parseFileContent(String fileContent) {
		
		this.validator.validateFile(fileContent);
		
		List<ResponseFileItem> responseFileContent = new ArrayList<>();
		
		//split string on newlines
		String[] lines = fileContent.split(INPUT_FILE_NEW_LINE);
		responseFileContent = Arrays.stream(lines).map(line -> parseLine(line))
			.collect(Collectors.toList());
		
		
		return responseFileContent;
	}
	
	/**
	 * Parses a single line of text from the submitted EntryFile, and
	 * extracts the requested fields from each line: 
	 * 
	 * Name, Transport, Top Speed
	 * 
	 * @param single line from input file
	 * @return a single line item for the response OutComeFile
	 */
	ResponseFileItem parseLine(String line) {
		ResponseFileItem result = new ResponseFileItem();
		
		//split the input file line into fields
		String[] fields = line.split(INPUT_FILE_FIELD_DELIMITER);
		if(this.validator.validateLineContent(fields)) {
			if(fields != null && fields.length > 0) {
				result.setName(fields[INPUT_FILE_NAME_FIELD_POSITION]);
				result.setTransport(fields[INPUT_FILE_TRANSPORT_FIELD_POSITION]);
				BigDecimal topSpeed = new BigDecimal(fields[INPUT_FILE_TOPSPEED_FIELD_POSITION]);
				result.setTopSpeed(topSpeed);
			}
		}
		
		return result;
	}
}
