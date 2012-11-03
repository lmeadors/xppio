package com.elmsw;

import com.elmsw.beans.Customer;
import com.elmsw.beans.MyCustomerIsCoolerThanYours;
import com.elmsw.beans.StringCustomer;
import com.elmsw.core.converters.DateConverter;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleObjectTest extends AbstractTestBase {

	final private String input = "<customer><id>123</id><name>Joe's Garage</name><created>2012-12-31 00:00:00.0 UTC</created></customer>";
	final private String inputWithLongDate = "<customer><id>123</id><name>Joe's Garage</name><created>2012-12-31 00:00:00.0 UTC</created></customer>";

	@Test
	public void shouldImportSimpleBeanWithStrings() throws Exception {

		// setup test
		xppIO.addAlias("customer", StringCustomer.class);
		StringCustomer expectedCustomer = new StringCustomer("123", "Joe's Garage");

		// run test
		final StringCustomer actualCustomer = xppIO.toObject(input);

		// verify behavior
		assertNotNull(actualCustomer);
		assertEquals(expectedCustomer.getId(), actualCustomer.getId());
		assertEquals(expectedCustomer.getName(), actualCustomer.getName());

	}

	@Test
	public void shouldImportSimpleBeanWithMixedTypes() throws Exception {

		// setup test
		DateFormat format = new SimpleDateFormat(DateConverter.PATTERN);
		Date date = format.parse("2012-12-31 00:00:00.0 GMT");
		Customer expectedCustomer = new Customer(123, "Joe's Garage", date);
		xppIO.addAlias("customer", Customer.class);

		// run test
		final Customer actualCustomer = xppIO.toObject(input);

		// verify behavior
		assertNotNull(actualCustomer);
		assertEquals(expectedCustomer.getId(), actualCustomer.getId());
		assertEquals(expectedCustomer.getName(), actualCustomer.getName());
		assertEquals(expectedCustomer.getCreated(), actualCustomer.getCreated());
	}

	@Test
	public void shouldImportSimpleBeanWithMixedTypesWithFullDateFormat() throws Exception {

		DateFormat format = new SimpleDateFormat(DateConverter.PATTERN);
		Date date = format.parse("2012-12-31 00:00:00.0 GMT");

		// setup test
		Customer expectedCustomer = new Customer(123, "Joe's Garage", date);
		xppIO.addAlias("customer", Customer.class);

		// run test
		final Customer actualCustomer = xppIO.toObject(inputWithLongDate);

		// verify behavior
		assertNotNull(actualCustomer);
		assertEquals(expectedCustomer.getId(), actualCustomer.getId());
		assertEquals(expectedCustomer.getName(), actualCustomer.getName());
		assertEquals(expectedCustomer.getCreated(), actualCustomer.getCreated());
	}

	@Test
	public void shouldMapCustomerToExistingObjectWithoutAlias() throws Exception {

		// setup test
		MyCustomerIsCoolerThanYours expectedCustomer = new MyCustomerIsCoolerThanYours(123, "Joe's Garage");

		// run test
		final MyCustomerIsCoolerThanYours actualCustomer = new MyCustomerIsCoolerThanYours();
		xppIO.populate(actualCustomer, input, "/customer");

		// verify behavior
		assertPropertiesAreEqual(expectedCustomer, actualCustomer);

	}

	@Test
	public void shouldPopulateCustomer() throws Exception {

		// setup test
		final String input = "<customer><id>123</id><name>Joe's Garage</name><description>yay me</description><enabled>true</enabled></customer>";
		final Customer expected = new Customer(123, "Joe's Garage");
		expected.setDescription("yay me");
		expected.setEnabled(true);

		// run test
		final Customer actual = xppIO.populate(new Customer(), input);

		// verify behavior
		System.out.println(actual);

		assertPropertiesAreEqual(expected, actual);

	}


}
