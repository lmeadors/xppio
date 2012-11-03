package com.elmsw.core.converters;

import com.elmsw.Converter;
import com.elmsw.XppIO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter extends NoOpConverter<Date> implements Converter<Date> {

	public static final String PATTERN = "yyyy-MM-dd HH:mm:ss.S z";

	@Override
	public String asText(Date object, XppIO xppIO) {
		return new SimpleDateFormat(PATTERN).format(object);
	}

	@Override
	public Date fromString(String value) {

		try {
			return new SimpleDateFormat(PATTERN).parse(value);
		} catch (ParseException e) {
			throw new RuntimeException(e.toString(), e);
		}

	}

}
