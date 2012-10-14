package com.elmsw;

import java.lang.reflect.Field;

public interface Converter<T> {

	public static final Field[] NO_FIELDS = new Field[0];
	public static final String NOT_TEXT = "";
	public static final String NOT_XML = "";

	T fromString(String value);

	Field[] getFields(T object);

	String asText(T object, XppIO xppIO);

	String asXml(T object, XppIO xppIO);

}
