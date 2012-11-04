package com.elmsw.core.converters;

import com.elmsw.Converter;
import com.elmsw.XppIO;
import com.elmsw.core.converters.NoOpConverter;

public class StringConverter extends NoOpConverter<String> {

	@Override
	public String fromString(String value) {
		return value;
	}

	@Override
	public String asText(String object, XppIO xppIO) {
		return object;
	}

}
