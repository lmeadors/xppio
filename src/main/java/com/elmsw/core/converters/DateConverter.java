package com.elmsw.core.converters;

import com.elmsw.Converter;
import com.elmsw.XppIO;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

public class DateConverter extends NoOpConverter<Date> implements Converter<Date> {

	public static int[] dateFormats = { DateFormat.SHORT, DateFormat.MEDIUM, DateFormat.LONG, DateFormat.FULL };

	@Override
	public String asText(Date object, XppIO xppIO) {
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
		return format.format(object);
	}

	@Override
	public Date fromString(String value) {
		Date date = null;
		for (int dateFormat : dateFormats) {
			DateFormat format = DateFormat.getDateInstance(dateFormat);

			try {
				date = format.parse(value);
				break;
			} catch (ParseException e) {
				// continue to the next formatter
			}
		}

		return date;
	}

}
