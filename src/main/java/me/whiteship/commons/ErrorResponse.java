package me.whiteship.commons;

import java.util.List;

import lombok.Data;

@Data
public class ErrorResponse {
	private String message;
	
	private String code;
	
	private List<FieldError> errors;
	
	// TODO
	private static class FieldError {
		private String field;
		private String value;
		private String reason;
	}
}
