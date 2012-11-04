package com.elmsw.core.converters;

import com.elmsw.Converter;
import com.elmsw.XppIO;

import java.lang.reflect.Field;

public abstract class NoOpConverter<T> implements Converter<T> {

	@Override
	public T fromString(String value) {
		return null;
	}

	@Override
	public Field[] getFields(T object) {
		return NO_FIELDS;
	}

	@Override
	public String asText(T object, XppIO xppIO) {
		return NOT_TEXT;
	}

	@Override
	public String asXml(T object, XppIO xppIO) {
		return NOT_XML;
	}

}
