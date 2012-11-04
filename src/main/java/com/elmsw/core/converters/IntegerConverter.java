package com.elmsw.core.converters;

import com.elmsw.Converter;
import com.elmsw.XppIO;

public class IntegerConverter extends NoOpConverter<Integer> {

	@Override
	public Integer fromString(String value) {
		return Integer.valueOf(value);
	}

	@Override
	public String asText(Integer object, XppIO xppIO) {
		return object.toString();
	}

}
