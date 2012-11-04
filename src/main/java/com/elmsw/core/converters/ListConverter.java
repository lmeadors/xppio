package com.elmsw.core.converters;

import com.elmsw.Converter;
import com.elmsw.XppIO;

import java.util.List;

public class ListConverter extends NoOpConverter<List> {

	public String asXml(List list, XppIO xppIO) {
		StringBuilder returnValue = new StringBuilder("");
		for (Object object : list.toArray()) {
			returnValue.append(xppIO.toXml(object));
		}
		return returnValue.toString();
	}

}
