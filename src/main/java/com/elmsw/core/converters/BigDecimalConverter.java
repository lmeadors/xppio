package com.elmsw.core.converters;

import com.elmsw.XppIO;

import java.math.BigDecimal;

public class BigDecimalConverter extends NoOpConverter<BigDecimal> {

	@Override
	public BigDecimal fromString(String value) {
		return new BigDecimal(value);
	}

	@Override
	public String asText(BigDecimal object, XppIO xppIO) {
		return object.toString();
	}

}
