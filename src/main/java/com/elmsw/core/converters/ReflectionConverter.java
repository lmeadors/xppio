package com.elmsw.core.converters;

import com.elmsw.Converter;

import java.lang.reflect.Field;

public class ReflectionConverter extends NoOpConverter<Object> implements Converter<Object> {

	@Override
	public Field[] getFields(Object object) {
		return object.getClass().getDeclaredFields();
	}

}
