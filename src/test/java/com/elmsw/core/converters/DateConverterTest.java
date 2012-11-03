package com.elmsw.core.converters;

import org.junit.Test;

import static org.junit.Assert.fail;

public class DateConverterTest {

	private final DateConverter converter = new DateConverter();

	@Test(expected = RuntimeException.class)
	public void shouldBarfOnBadDateFormat() {

		// no setup for test

		// run test
		converter.fromString("what?");

		// verify behavior
		fail("we should never get here");

	}

}
