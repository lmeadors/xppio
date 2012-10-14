package com.elmsw;

import com.elmsw.core.namingstrategies.PropertyNameStrategy;
import com.elmsw.core.statefactory.SimpleStateFactory;
import org.apache.commons.beanutils.PropertyUtils;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.beans.PropertyDescriptor;
import java.text.MessageFormat;

import static org.junit.Assert.assertEquals;

public abstract class AbstractTestBase {

	private static final Logger log = LoggerFactory.getLogger(AbstractTestBase.class);

	protected XppIO xppIO;
	protected final XmlPullParserFactory factory;

	public AbstractTestBase() {
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
		xppIO = new XppIO(factory, exceptionHandler, namingStrategy, new SimpleStateFactory());
	}

	public <T, S extends T> void assertPropertiesAreEqual(T expected, S actual, String... excludes) throws Exception {
		PropertyDescriptor[] pd = PropertyUtils.getPropertyDescriptors(expected);
		for (PropertyDescriptor p : pd) {
			Boolean excluded = Boolean.FALSE;
			for (String s : excludes) {
				if (s.equalsIgnoreCase(p.getName())) {
					excluded = Boolean.TRUE;
				}
			}
			if (!excluded) {
				Object expectedValue = PropertyUtils.getProperty(expected, p.getName());
				Object actualValue = PropertyUtils.getProperty(actual, p.getName());

				if ((expectedValue == null) && (actualValue == null)) {
					// We will also consider two nulls as equal, even though technically they aren't.
					// In practice, though, this method is called to test "sameness", not equality.
				} else {
					assertEquals(MessageFormat.format("Assert failed for property ''{0}''", p.getName()), expectedValue, actualValue);
				}
			}
		}
	}

}
