package com.elmsw.beans;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

public class Customer {

	private Integer id;
	private String name;
	private String description;
	private boolean enabled;
	private Date created;

	public Customer() {
	}

	public Customer(Integer id, String name) {
		this(id, name, null);
	}

	public Customer(Integer id, String name, Date created) {
		this.id = id;
		this.name = name;
		this.created = created;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
