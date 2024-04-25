package kh.springbootassessment.fileparser.service;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import kh.springbootassessment.fileparser.data.ResponseFileItem;

class FileParserServiceTest {

	/**
	 * Tests calling parseFileContent() with null throws a FileContentEmptyException
	 * as a validation error
	 */
	@Test()
	void testParseFileContent_null() {
		assertThrows(FileContentEmptyException.class, () -> {
			FileParserService service = new FileParserService();
			List<ResponseFileItem> result = service.parseFileContent(null);
			
		});
	}

	/**
	 * Tests calling parseFileContent() with "" throws a FileContentEmptyException
	 * as a validation error
	 */
	@Test()
	void testParseFileContent_emptyString() {
		assertThrows(FileContentEmptyException.class, () -> {
			FileParserService service = new FileParserService();
			List<ResponseFileItem> result = service.parseFileContent("");
			
		});
	}

}
