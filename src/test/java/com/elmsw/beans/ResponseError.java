package com.elmsw.beans;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ResponseError {

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	private String field;
	private String value;
	private String text;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
