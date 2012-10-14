package com.elmsw.core.converters;

import com.elmsw.Converter;
import com.elmsw.core.converters.NoOpConverter;

import java.lang.reflect.Field;

public class ReflectionConverter extends NoOpConverter<Object> implements Converter<Object> {

//	public Object fromString(String value) {
//		todo: this should maybe do something?
//		return null;
//	}

	@Override
	public Field[] getFields(Object object) {
		return object.getClass().getDeclaredFields();
	}

//	public String asText(Object object, XppIO xppIO) {
//		return NOT_TEXT;
//	}

//	public String asXml(Object object, XppIO xppIO) {
//		return NOT_XML;
//	}

}
