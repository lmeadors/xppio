package com.elmsw.core;

import com.elmsw.Converter;
import com.elmsw.XppIO;

import java.lang.reflect.Field;

public abstract class NoOpConverter<T> implements Converter<T> {

	@Override
	public T fromString(String value) {
		return null;
	}

	@Override
	public Field[] getFields(Object object) {
		return NO_FIELDS;
	}

	@Override
	public String asText(Object object, XppIO xppIO) {
		return NOT_TEXT;
	}

	@Override
	public String asXml(Object object, XppIO xppIO) {
		return NOT_XML;
	}

}
