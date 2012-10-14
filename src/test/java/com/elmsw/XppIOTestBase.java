package com.elmsw;

import com.elmsw.core.PropertyNameStrategy;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public abstract class XppIOTestBase {

	private static final Logger log = LoggerFactory.getLogger(XppIOTestBase.class);

	protected XppIO xppIO;
	protected final XmlPullParserFactory factory;

	public XppIOTestBase() {
		try {
			factory = XmlPullParserFactory.newInstance();
		} catch (XmlPullParserException e) {
			throw new RuntimeException(e.toString(), e);
		}
	}

	@Before
	public void beforeXppIOTestBase() {
		final ExceptionHandler exceptionHandler = new ExceptionHandler() {
			public void handle(Throwable throwable) {
				log.error(throwable.toString(), throwable);
			}
		};
		final NamingStrategy namingStrategy = new PropertyNameStrategy();
		xppIO = new XppIO(factory, exceptionHandler, namingStrategy);
	}

}
