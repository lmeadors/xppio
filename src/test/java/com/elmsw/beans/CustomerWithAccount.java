package com.elmsw.beans;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CustomerWithAccount extends Customer {

	private Account account;

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
