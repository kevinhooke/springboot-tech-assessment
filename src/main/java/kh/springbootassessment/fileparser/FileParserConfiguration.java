package kh.springbootassessment.fileparser;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import kh.springbootassessment.fileparser.service.EmptyValidator;
import kh.springbootassessment.fileparser.service.EntryFileValidator;
import kh.springbootassessment.fileparser.service.FileValidator;

@Configuration
public class FileParserConfiguration {

	/**
	 * Provides the EntryFileValidator implementation of FileValidator if:
	 * 
	 * features.entryfile.validation = true
	 * or
	 * property is missing
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(prefix = "features.entryfile", name = "validation",
			havingValue = "true", matchIfMissing = true)
	public FileValidator getValidationEnabledBean() {
		return new EntryFileValidator();
	}

	/**
	 * Provides the EmptyValidator implementation of FileValidator if:
	 * 
	 * features.entryfile.validation = false
	 * 
	 * EmptyValidator bypasses the file validation, it is used as a passthrough
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(prefix = "features.entryfile", name = "validation",
			havingValue = "false")
	public FileValidator getValidationDisabledBean() {
		return new EmptyValidator();
	}

	
}