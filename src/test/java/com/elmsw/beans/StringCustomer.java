package com.elmsw.beans;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
* Created with IntelliJ IDEA.
* User: lmeadors
* Date: 10/12/12
* Time: 5:55 AM
* To change this template use File | Settings | File Templates.
*/
public class StringCustomer {
	private String id;
	private String name;

	public StringCustomer(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public StringCustomer() {
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
