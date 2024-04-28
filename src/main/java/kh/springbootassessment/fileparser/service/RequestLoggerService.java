package kh.springbootassessment.fileparser.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kh.springbootassessment.fileparser.data.RequestLogItem;
import kh.springbootassessment.fileparser.repository.RequestLogRepository;

/**
 * Logs incoming requests to the relational database.
 */
@Service
public class RequestLoggerService {

	@Autowired
	private RequestLogRepository logRepository;

	public void logIncomingRequest(int httpStatus, String ipAddress, String uri, long elapsedMillis) {
		// store request log to db table
		RequestLogItem requestLog = new RequestLogItem();
		requestLog.setResponseCode(httpStatus);
		requestLog.setIpAddress(ipAddress);
		requestLog.setUri(uri);
		requestLog.setRequestTimestamp(LocalDateTime.now());		
		requestLog.setTimeLapsed(elapsedMillis);
		
		requestLog.setRequestIpProvider(null);

		this.logRepository.save(requestLog);
	}

}
