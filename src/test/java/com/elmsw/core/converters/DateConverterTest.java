package com.elmsw.core.converters;

import com.elmsw.AbstractTestBase;
import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DateConverterTest extends AbstractTestBase {


	@Test(expected = RuntimeException.class)
	public void shouldBarfOnBadDateFormat() {

		// setup test
		final DateConverter converter = new DateConverter();

		// run test
		converter.fromString("what?");

		// verify behavior
		fail("we should never get here");

	}

	@Test
	public void shouldAllowDifferentFormats() throws ParseException {
		// setup test
		final Date expected = getDateUsingDefaultFormat("2012-11-03 00:00:00.0 UTC");
		final DateConverter converter = new DateConverter("yyyyMMdd z");

		// run test
		final Date actual = converter.fromString("20121103 UTC");

		// verify behavior
		assertEquals("Dates should be the same", expected, actual);
	}

}
