package kh.springbootassessment.fileparser.service;

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

	@Autowired
	private RequestLogRepository logRepository;
	
	public List<ResponseFileItem> parseFileContent(String fileContent) {
		
		if(fileContent == null || fileContent.trim().length() == 0) {
			throw new FileContentEmptyException();
		}
		
		List<ResponseFileItem> responseFileContent = new ArrayList<>();
		
		//split string on newlines
		String[] lines = fileContent.split("\\n");
		responseFileContent = Arrays.stream(lines).map(line -> parseLine(line))
			.collect(Collectors.toList());
		
		
		return responseFileContent;
	}
	
	/**
	 * Parses a single line of text from the submitted EntryFile, and
	 * extracts the requested fields from each line: 
	 * Name, Transport, Top Speed
	 * 
	 * @param line
	 * @return a single line item for the response OutComeFile
	 */
	ResponseFileItem parseLine(String line) {
		ResponseFileItem result = new ResponseFileItem();
		
		//split the input file line into fields
		String[] fields = line.split("\\|");
		if(fields != null && fields.length > 0) {
			result.setName(fields[2]);
			result.setTransport(fields[4]);
			result.setTopSpeed(fields[6]);
			
		}
		
		
		return result;
	}
}
