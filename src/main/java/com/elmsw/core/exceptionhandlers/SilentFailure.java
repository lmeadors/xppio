package com.elmsw.core.exceptionhandlers;

import com.elmsw.ExceptionHandler;

public class SilentFailure implements ExceptionHandler {
	public void handle(Throwable throwable) {
		// The class name is a hint here.
	}
}
