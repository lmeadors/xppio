package com.elmsw.core.exceptionhandlers;

import com.elmsw.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogFailure implements ExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(LogFailure.class);

	public void handle(Throwable throwable) {
		log.error(throwable.toString(), throwable);
	}

}
