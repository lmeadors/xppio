package com.elmsw.core;

import com.elmsw.Converter;
import com.elmsw.XppIO;

import java.lang.reflect.Field;

public class StringConverter extends NoOpConverter<String> implements Converter<String> {

	@Override
	public String fromString(String value) {
		return value;
	}

//	@Override
//	public Field[] getFields(String object) {
//		return NO_FIELDS;
//	}

	@Override
	public String asText(String object, XppIO xppIO) {
		return object;
	}

//	public String asXml(String object, XppIO xppIO) {
//		return NOT_XML;
//	}

}
