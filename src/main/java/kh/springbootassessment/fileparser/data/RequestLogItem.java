package kh.springbootassessment.fileparser.data;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Represents a request log entry, stored on completion of each request.
 */
@Entity
public class RequestLogItem {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String requestId;
	private String uri;
	private LocalDateTime requestTimestamp;
	private int responseCode;
	private String ipAddress;
	private String requestIpProvider;
	private long timeLapsed;
	private String requestISP;

	public RequestLogItem() {
		
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public LocalDateTime getRequestTimestamp() {
		return requestTimestamp;
	}

	public void setRequestTimestamp(LocalDateTime requestTimestamp) {
		this.requestTimestamp = requestTimestamp;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getRequestIpProvider() {
		return requestIpProvider;
	}

	public void setRequestIpProvider(String requestIpProvider) {
		this.requestIpProvider = requestIpProvider;
	}

	public long getTimeLapsed() {
		return timeLapsed;
	}

	public void setTimeLapsed(long timeLapsed) {
		this.timeLapsed = timeLapsed;
	}
	
	public String getRequestISP() {
		return requestISP;
	}

	public void setRequestISP(String requestISP) {
		this.requestISP = requestISP;
	}
}
