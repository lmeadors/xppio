package com.elmsw.core.converters;

import com.elmsw.XppIO;

public class LongConverter extends NoOpConverter<Long> {
	@Override
	public Long fromString(String value) {
		return Long.valueOf(value);
	}

	@Override
	public String asText(Long number, XppIO xppIO) {
		return number.toString();
	}

}
