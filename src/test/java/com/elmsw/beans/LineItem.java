package com.elmsw.beans;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class LineItem {
	Integer id;
	Integer orderId;
	Integer productId;
	Integer quantity;

	public LineItem() {
	}

	public LineItem(Integer id, Integer orderId, Integer productId, Integer quantity) {
		this.id = id;
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
