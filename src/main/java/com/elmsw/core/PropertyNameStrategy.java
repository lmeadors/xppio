package com.elmsw.core;

import com.elmsw.NamingStrategy;

public class PropertyNameStrategy implements NamingStrategy {

	public String getElementName(Object object) {
		final String simpleName = object.getClass().getSimpleName();
		return simpleName.substring(0, 1).toLowerCase() + simpleName.substring(1);
	}

}
