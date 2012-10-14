package com.elmsw.core;

import com.elmsw.core.exceptionhandlers.SilentFailure;
import org.junit.Test;

public class SilentFailureTest {

	@Test
	public void thisIsReallyDifficult() {
		// setup test
		// run test
		// verify behavior
		new SilentFailure().handle(new RuntimeException("nothing happens here."));
	}

}
