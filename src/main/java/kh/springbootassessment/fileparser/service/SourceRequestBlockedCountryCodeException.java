package kh.springbootassessment.fileparser.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Request from blocked country code")
public class SourceRequestBlockedCountryCodeException extends RuntimeException {

}
