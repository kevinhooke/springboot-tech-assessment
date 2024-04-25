package kh.springbootassessment.fileparser.data;

import java.math.BigDecimal;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Single line of the EntryFile, using Bean Validation to specify validation rules for each individual field.
 * The rules are assumed based on the example lines in the Scenario since no specific rules were specified, but the
 * rules can easily be modified per requirements.
 */
public class EntryFileContent {

	public EntryFileContent() {
		
	}
	
	@NotNull
	@Size(min = 36, max = 36)
	private String uuid;
	
	@NotNull
	@Size(min=1, max=10)
	private String id;
	
	@NotNull
	@Size(min=1, max=60)
	private String name;
	
	@NotNull
	@Size(max=60)
	private String likes;
	
	@NotNull
	@Size(max=60)
	private String transport;
	
	@NotNull
	@Max(150)
	private BigDecimal avgSpeed;
	
	@NotNull
	@Max(260)
	private BigDecimal topSpeed;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLikes() {
		return likes;
	}

	public void setLikes(String likes) {
		this.likes = likes;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public BigDecimal getAvgSpeed() {
		return avgSpeed;
	}

	public void setAvgSpeed(BigDecimal avgSpeed) {
		this.avgSpeed = avgSpeed;
	}

	public BigDecimal getTopSpeed() {
		return topSpeed;
	}

	public void setTopSpeed(BigDecimal topSpeed) {
		this.topSpeed = topSpeed;
	}
}
