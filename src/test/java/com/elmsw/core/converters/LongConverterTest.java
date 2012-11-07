package com.elmsw.core.converters;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LongConverterTest {
	private final LongConverter longConverter = new LongConverter();

	@Test
	public void shouldMakeLongFromString() {
		// setup test

		// run test
		final Long actual = longConverter.fromString("1234567890");

		// verify behavior
		assertEquals(1234567890l, actual.longValue());

	}

	@Test
	public void shouldMakeStringFromLong() {
		// setup test

		// run test
		final String actual = longConverter.asText(123456789012345l, null);

		// verify behavior
		assertEquals("123456789012345", actual);

	}

}
