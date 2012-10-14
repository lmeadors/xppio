package com.elmsw;

import com.elmsw.core.State;

public interface StateFactory {
	public State get(ExceptionHandler exceptionHandler);
}
