package com.elmsw.core;

import com.elmsw.NamingStrategy;

public class ClassNameStrategy implements NamingStrategy {
	public String getElementName(Object object) {
		return object.getClass().getSimpleName();
	}
}
