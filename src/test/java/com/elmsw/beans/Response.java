package com.elmsw.beans;

import java.util.LinkedList;
import java.util.List;

public class Response {

	private String status;
	private List<ResponseError> errors = new LinkedList<ResponseError>();

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ResponseError> getErrors() {
		return errors;
	}

	public void setErrors(List<ResponseError> errors) {
		this.errors = errors;
	}

}
