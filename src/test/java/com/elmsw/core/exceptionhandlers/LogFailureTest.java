package com.elmsw.core.exceptionhandlers;

import org.junit.Test;

public class LogFailureTest {
	private final LogFailure logFailure = new LogFailure();

	@Test
	public void notMuch() {

		// setup test? no.

		// run test
		logFailure.handle(new RuntimeException());

		// verify behavior?

	}


}
