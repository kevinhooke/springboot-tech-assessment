package kh.springbootassessment.fileparser.data;

public class RequestData {

	private long requestStartMillis;
	private long requestEndMillis;
	
	
	public long getRequestStartMillis() {
		return requestStartMillis;
	}
	
	public void setRequestStartMillis(long requestStartMillis) {
		this.requestStartMillis = requestStartMillis;
	}
	
	public long getRequestEndMillis() {
		return requestEndMillis;
	}
	
	public void setRequestEndMillis(long requestEndMillis) {
		this.requestEndMillis = requestEndMillis;
	}
	
}
