package kh.springbootassessment.fileparser.data;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Represents a single item in the generated response file output.
 * 
 * Uses @JsonProperty to set the property name of each item
 * to match the names in the Scenario requirements (Pascal Case instead
 * of Camel Case typically used for Java property names).
 * 
 * @author kevinhooke
 */
public class ResponseFileItem {

	public ResponseFileItem() {
		
	}
	
	private String name;
	private String transport;
	private BigDecimal topSpeed;
	
	@JsonProperty("Name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@JsonProperty("Transport")
	public String getTransport() {
		return transport;
	}
	public void setTransport(String transport) {
		this.transport = transport;
	}
	
	@JsonProperty("TopSpeed")
	public BigDecimal getTopSpeed() {
		return topSpeed;
	}
	public void setTopSpeed(BigDecimal topSpeed) {
		this.topSpeed = topSpeed;
	}
}
