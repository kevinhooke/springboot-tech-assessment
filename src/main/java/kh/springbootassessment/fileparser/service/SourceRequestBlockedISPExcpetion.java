package kh.springbootassessment.fileparser.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = "Request from blocked ISP")
public class SourceRequestBlockedISPExcpetion extends RuntimeException {

}
