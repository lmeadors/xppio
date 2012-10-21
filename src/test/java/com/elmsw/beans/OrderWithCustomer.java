package com.elmsw.beans;

public class OrderWithCustomer extends Order {
	private Customer customer;

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

}
