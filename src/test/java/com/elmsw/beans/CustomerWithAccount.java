package com.elmsw.beans;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CustomerWithAccount {
	private Integer id;
	private String name;
	private Account account;

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

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
