package com.elmsw;

import com.elmsw.beans.Customer;
import com.elmsw.core.namingstrategies.ClassNameStrategy;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class ClassNameStrategyTest {

	private final ClassNameStrategy strategy = new ClassNameStrategy();

	@Test
	public void shouldMatchClassNameExactly() {

		// no setup for test

		// run tests and verify behavior
		assertEquals("Integer", strategy.getElementName(1));
		assertEquals("String", strategy.getElementName("test"));
		assertEquals("Date", strategy.getElementName(new Date()));
		assertEquals("Customer", strategy.getElementName(new Customer()));

	}

}
