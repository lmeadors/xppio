package com.elmsw;

import com.elmsw.beans.Customer;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.Reader;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class XppIOExceptionTest {

	@Test
	public void shouldIgnoreUnknownEvents() throws XmlPullParserException, IOException {

		// setup test
		final XmlPullParserFactory factory = mock(XmlPullParserFactory.class);
		final XmlPullParser xmlPullParser = mock(XmlPullParser.class);
		when(factory.newPullParser()).thenReturn(xmlPullParser);
		when(xmlPullParser.getEventType()).thenReturn(XmlPullParser.COMMENT);
		when(xmlPullParser.next()).thenReturn(XmlPullParser.END_DOCUMENT);

		final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);
		final NamingStrategy namingStrategy = mock(NamingStrategy.class);
		XppIO xppIO = new XppIO(factory, exceptionHandler, namingStrategy);

		// run test
		assertNull(xppIO.toObject("<bomb/>"));

		// verify behavior
	}

	@Test(expected = RuntimeException.class)
	public void shouldExplodeViolentlyIfUnableToGetParser() throws XmlPullParserException {

		// setup test
		final XmlPullParserFactory factory = mock(XmlPullParserFactory.class);
		when(factory.newPullParser()).thenThrow(new XmlPullParserException("test"));
		final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);
		final NamingStrategy namingStrategy = mock(NamingStrategy.class);
		XppIO xppIO = new XppIO(factory, exceptionHandler, namingStrategy);

		// run test
		xppIO.toObject("<bomb/>");

		// verify behavior
		fail("this should have exploded.");
	}


	@Test
	public void shouldReturnNullAndCallExceptionHandlerOnMostFailures() throws Exception {

		// setup test
		final XmlPullParserFactory factory = mock(XmlPullParserFactory.class);

		final XmlPullParser xmlPullParser = mock(XmlPullParser.class);
		when(factory.newPullParser()).thenReturn(xmlPullParser);
		doThrow(new XmlPullParserException("test")).when(xmlPullParser).setInput(any(Reader.class));

		final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);

		final NamingStrategy namingStrategy = mock(NamingStrategy.class);

		XppIO xppIO = new XppIO(factory, exceptionHandler, namingStrategy);

		// run test
		assertNull(xppIO.toObject("<bomb/>"));

		// verify behavior
		verify(exceptionHandler).handle(any(Throwable.class));

	}

	@Test
	public void shouldReturnNullAndCallExceptionHandlerWhenTryingToCreateXmlFails() throws Exception {

		// setup test
		final XmlPullParserFactory factory = mock(XmlPullParserFactory.class);
		final XmlPullParser xmlPullParser = mock(XmlPullParser.class);

		final ExceptionHandler exceptionHandler = mock(ExceptionHandler.class);

		final NamingStrategy namingStrategy = mock(NamingStrategy.class);
		doThrow(new RuntimeException("test")).when(namingStrategy).getElementName(any(Object.class));

		final XppIO xppIO = new XppIO(factory, exceptionHandler, namingStrategy);

		// run test
		assertNull(xppIO.toXml(new Customer()));

		// verify behavior
		verify(exceptionHandler).handle(any(Throwable.class));

	}

}
