package kh.springbootassessment.fileparser.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kh.springbootassessment.fileparser.data.ResponseFileItem;
import kh.springbootassessment.fileparser.service.exception.FileContentEmptyException;
import kh.springbootassessment.fileparser.service.exception.FileContentMissingFieldsException;
import kh.springbootassessment.fileparser.service.exception.InvalidFieldValueException;

@SpringBootTest
public class FileParserServiceTest {
	
	private static final String LINE1 = "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1";
	
	/**
	 * Missing 1 field
	 */
	private static final String INVALID_LINE1 = "18148426-89e1-11ee-b9d1-0242ac120002|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2";
	
	/**
	 * Invalid uuid
	 */
	private static final String INVALID_UUID = "18148426|1X1D14|John Smith|Likes Apricots|Rides A Bike|6.2|12.1";
	
	@Autowired
	private FileParserService service;
	
	/**
	 * Tests calling parseFileContent() with null throws a FileContentEmptyException
	 * as a validation error
	 */
	@Test()
	void testParseFileContent_null() {
		assertThrows(FileContentEmptyException.class, () -> {
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
			List<ResponseFileItem> result = service.parseFileContent("");
			
		});
	}
	
	/**
	 * Tests parsing with a single valid line from the requirements scenario.
	 */
	@Test()
	void testParseFileContent_1Line() {
		List<ResponseFileItem> result = service.parseFileContent(LINE1);
		assertEquals(1, result.size());
		ResponseFileItem item = result.get(0);
		assertEquals("John Smith", item.getName());
		assertEquals("Rides A Bike", item.getTransport());
		assertEquals(new BigDecimal("12.1"), item.getTopSpeed());
	}
	
	/**
	 * Tests parsing invalid single line
	 */
	@Test()
	void testParseFileContent_1InvalidLine() {
		assertThrows(FileContentMissingFieldsException.class, () -> {
			List<ResponseFileItem> result = service.parseFileContent(INVALID_LINE1);
		});
	}

	/**
	 * Tests parsing invalid single line
	 */
	@Test()
	void testParseFileContent_invalidUUID() {
		assertThrows(InvalidFieldValueException.class, () -> {
			List<ResponseFileItem> result = service.parseFileContent(INVALID_UUID);
		});
	}
	
	
}

