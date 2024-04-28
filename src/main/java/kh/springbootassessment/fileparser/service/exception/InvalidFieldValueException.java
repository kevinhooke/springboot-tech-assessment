package kh.springbootassessment.fileparser.service.exception;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import kh.springbootassessment.fileparser.data.EntryFileContent;

public class InvalidFieldValueException extends RuntimeException{

	public InvalidFieldValueException(Set<ConstraintViolation<EntryFileContent>> violations) {
		super();
		this.violations = violations;
	}

	private Set<ConstraintViolation<EntryFileContent>> violations;

	
	public Set<ConstraintViolation<EntryFileContent>> getViolations() {
		return violations;
	}

	public void setViolations(Set<ConstraintViolation<EntryFileContent>> violations) {
		this.violations = violations;
	}
	
}
