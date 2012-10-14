package com.elmsw.core;

import com.elmsw.Converter;
import com.elmsw.XppIO;

import java.lang.reflect.Field;

public class IntegerConverter extends NoOpConverter<Integer> implements Converter<Integer> {

	@Override
	public Integer fromString(String value) {
		return Integer.valueOf(value);
	}

	@Override
	public String asText(Integer object, XppIO xppIO) {
		return object.toString();
	}

//	public String asXml(Integer object, XppIO xppIO) {
//		return NOT_XML;
//	}

//	@Override
//	public Field[] getFields(Integer object) {
//		return NO_FIELDS;
//	}

}
