package kh.springbootassessment.fileparser.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kh.springbootassessment.fileparser.data.ResponseFileItem;
import kh.springbootassessment.fileparser.service.FileParserService;

@RestController
public class FileParserController {

	@Autowired
	private FileParserService fileParserService;
	
	@PostMapping (path = "/entryfile", consumes = MediaType.TEXT_PLAIN_VALUE
			, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ResponseFileItem> parseEntryFile(@RequestBody String entryFileContent) {
		List<ResponseFileItem> result = this.fileParserService.parseFileContent(entryFileContent);
		return result;
	}
	
}
