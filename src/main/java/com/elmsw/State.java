package com.elmsw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Field;
import java.util.Stack;

class State {

	private static final Logger log = LoggerFactory.getLogger(State.class);

	private final ExceptionHandler exceptionHandler;
	private Object rootObject;
	private String nextPropertyName;
	private Stack<Object> stack = new Stack<Object>();

	State(ExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public void begin() {
		log.debug("begin ----");
		rootObject = null;
	}

	public void end() {
		log.debug("end ------");
	}

	public void processStartElement(XmlPullParser xpp, XppIO xppIO) throws IllegalAccessException, InstantiationException, NoSuchFieldException {

		log.debug("----------");
		nextPropertyName = xpp.getName();

		log.debug("started with       : {}", nextPropertyName);

		Class nextType = xppIO.typeForElement(nextPropertyName);

		log.debug("pushing object for {}", nextType);
		final Object parent;
		if (!stack.empty()) {
			parent = stack.peek();
		} else {
			parent = null;
		}

		final Object object = nextType.newInstance();
		if (null == rootObject) {
			rootObject = object;
		}
		stack.push(object);

		if (null != parent) {
			setField(nextPropertyName, stack.peek(), parent);
		}

		log.debug("next property name : {}", nextPropertyName);
		log.debug("next type          : {}", nextType);
		log.debug("parent object      : {}", parent);

	}

	public void processEndElement() {
		log.debug("popping from stack");
		log.debug("finished object    : {}", stack.pop());
		log.debug("finished with      : {}", nextPropertyName);
		log.debug("----------");
	}


	private void setField(String propertyName, Object value, Object target) throws NoSuchFieldException, IllegalAccessException {

		try {
			final Class aClass = target.getClass();
			final Field field = aClass.getDeclaredField(propertyName);
			if (field.getType().isAssignableFrom(value.getClass())) {
				field.setAccessible(true);
				field.set(target, value);
			}
		} catch (Exception e) {
			exceptionHandler.handle(e);
		}

	}

	public void processText(XmlPullParser xpp, XppIO xppIO) throws IllegalAccessException, NoSuchFieldException {
		final String text = xpp.getText();
		final Object pop = stack.pop();
		log.debug("popped {} from stack", pop);
		final Object currentObject = stack.peek();
		log.debug("setting {} to '{}' on {} ({})", new Object[]{nextPropertyName, text, currentObject, currentObject.getClass()});
		try {
			final Class aClass = currentObject.getClass();
			final Field field = aClass.getDeclaredField(nextPropertyName);
			field.setAccessible(true);
			setFieldValue(field, text, currentObject, xppIO);
		} catch (Exception e) {
			exceptionHandler.handle(e);
		}
		log.debug("pushing {} on to stack", pop);
		stack.push(pop);

	}

	private void setFieldValue(Field field, Object value, Object object, XppIO xppIO) throws IllegalAccessException {
		Converter converter = xppIO.getConverterForClass(field.getType());
		log.debug("using converter '{}' to set field '{}' to '{}'", new Object[]{converter, field, value});
		field.set(object, converter.fromString((String) value));
	}

	public Object getObject() {
		return rootObject;
	}

}
