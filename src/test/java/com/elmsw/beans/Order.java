package com.elmsw.beans;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.LinkedList;
import java.util.List;

public class Order {

	Integer id;
	List<LineItem> lineItemList = new LinkedList<LineItem>();

	public Order(Integer id) {
		this.id = id;
	}

	public Order() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<LineItem> getLineItemList() {
		return lineItemList;
	}

	public void setLineItemList(List<LineItem> lineItemList) {
		this.lineItemList = lineItemList;
	}

	public Order addLineItem(LineItem lineItem) {
		lineItemList.add(lineItem);
		lineItem.setOrderId(id);
		return this;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
