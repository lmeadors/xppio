package com.elmsw.core;

import com.elmsw.Converter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NoOpConverterTest {

	@Test
	public void shouldReallyDoNothing() {
		// setup test
		final NoOpConverter<Object> noOpConverter = new NoOpConverter<Object>(){};

		// run test and verify behavior
		assertEquals(Converter.NO_FIELDS, noOpConverter.getFields(null));
		assertNull(noOpConverter.fromString(null));
		assertEquals(Converter.NOT_TEXT, noOpConverter.asText(null, null));
		assertEquals(Converter.NOT_XML, noOpConverter.asXml(null, null));

	}

}
