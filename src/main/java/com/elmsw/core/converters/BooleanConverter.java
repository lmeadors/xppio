package com.elmsw.core.converters;

import com.elmsw.Converter;
import com.elmsw.XppIO;

public class BooleanConverter extends NoOpConverter<Boolean> implements Converter<Boolean> {

	@Override
	public String asText(Boolean object, XppIO xppIO) {
		return "" + object;
	}

	@Override
	public Boolean fromString(String value) {
		return Boolean.valueOf(value);
	}

}
