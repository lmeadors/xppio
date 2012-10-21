package com.elmsw;

import com.elmsw.core.namingstrategies.ClassNameStrategy;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ClassNameStrategyTest {
	private final ClassNameStrategy strategy = new ClassNameStrategy();

	@Test
	public void shouldMatchClassNameExactly() {
		// setup test
		// run test
		// verify behavior
		assertEquals("Integer", strategy.getElementName(1));
	}

}