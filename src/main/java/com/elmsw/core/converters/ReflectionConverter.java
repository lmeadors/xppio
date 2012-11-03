package com.elmsw.core.converters;

import com.elmsw.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ReflectionConverter extends NoOpConverter<Object> implements Converter<Object> {

	private static final Logger log = LoggerFactory.getLogger(ReflectionConverter.class);

	@Override
	public Field[] getFields(Object object) {
		final List<Field> fieldsInClass = getFieldsInClass(object.getClass());
		log.debug("fields in {}: \n {}", new Object[]{object.getClass(), fieldsInClass});
		return fieldsInClass.toArray(new Field[fieldsInClass.size()]);
	}

	private List<Field> getFieldsInClass(Class targetClass) {
		List<Field> fieldList = new LinkedList<Field>();
		fieldList.addAll(Arrays.asList(targetClass.getDeclaredFields()));
		if(!Object.class.equals(targetClass)){
			fieldList.addAll(getFieldsInClass(targetClass.getSuperclass()));
		}
		return fieldList;
	}

}
