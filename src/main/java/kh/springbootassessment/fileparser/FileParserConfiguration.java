package kh.springbootassessment.fileparser;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kh.springbootassessment.fileparser.interceptor.RequestLoggerInterceptor;
import kh.springbootassessment.fileparser.service.validator.EmptyValidator;
import kh.springbootassessment.fileparser.service.validator.EntryFileValidator;
import kh.springbootassessment.fileparser.service.validator.FileValidator;

@Configuration
public class FileParserConfiguration implements WebMvcConfigurer {

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

	@Bean
	public RequestLoggerInterceptor getRequestLoggerInterceptor() {
		return new RequestLoggerInterceptor();
	}
	
	
	/**
	 * Register the RequestLoggerInterceptor to log the incoming requests to the relational database.
	 * 
	 */
	@Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        registry.addInterceptor(this.getRequestLoggerInterceptor());
    }
}