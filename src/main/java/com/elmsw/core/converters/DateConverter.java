package com.elmsw.core.converters;

import com.elmsw.Converter;
import com.elmsw.XppIO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter extends NoOpConverter<Date> implements Converter<Date> {

	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.S z";

	public final String pattern;

	public DateConverter(String pattern) {
		this.pattern = pattern;
	}

	public DateConverter() {
		pattern = DEFAULT_DATE_FORMAT;
	}

	@Override
	public String asText(Date object, XppIO xppIO) {
		return new SimpleDateFormat(pattern).format(object);
	}

	@Override
	public Date fromString(String value) {

		try {
			return new SimpleDateFormat(pattern).parse(value);
		} catch (ParseException e) {
			throw new RuntimeException(e.toString(), e);
		}

	}

}
