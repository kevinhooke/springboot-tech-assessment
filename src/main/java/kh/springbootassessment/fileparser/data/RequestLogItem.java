package kh.springbootassessment.fileparser.data;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
}
