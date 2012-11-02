package com.elmsw;

import com.elmsw.core.converters.BooleanConverter;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;

public class XppIOTest {

	@Test
	public void shouldAddedConverted() throws XmlPullParserException {
		XppIO xppIO = new XppIO();

		xppIO.addConverter(TestClass.class, new BooleanConverter());

		final Converter converterForClass = xppIO.getConverterForClass(BigDecimal.class);

		assertNotNull(converterForClass);
	}

	public class TestClass {

	}
}
