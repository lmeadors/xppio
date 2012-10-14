package com.elmsw.core;

import com.elmsw.Converter;
import com.elmsw.core.converters.ReflectionConverter;
import org.junit.Test;

import static org.junit.Assert.assertNull;

public class ReflectionConverterTest {

	Converter converter = new ReflectionConverter();

	@Test
	public void shouldReturnNullForAnyText() {
		// setup test

		// run test
		// verify behavior
		assertNull(converter.fromString("whatever"));
	}

}
