package br.com.treinaweb.javajobs.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {

    private int status;

    private LocalDateTime timestamp;

    private String message;

    private String path;

    private List<String> errors;
    
    

	public ApiError() {	}

	public ApiError(int status, LocalDateTime timestamp, String message, String path, List<String> errors) {
		this.status = status;
		this.timestamp = timestamp;
		this.message = message;
		this.path = path;
		this.errors = errors;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}
    
}
