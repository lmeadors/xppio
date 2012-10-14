package com.elmsw.core.statefactory;

import com.elmsw.ExceptionHandler;
import com.elmsw.StateFactory;
import com.elmsw.core.State;

public class SimpleStateFactory implements StateFactory {
	@Override
	public State get(ExceptionHandler exceptionHandler) {
		return new State(exceptionHandler);
	}
}
