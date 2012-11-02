package com.elmsw;

import com.elmsw.beans.Customer;
import com.elmsw.beans.MyCustomerIsCoolerThanYours;
import com.elmsw.beans.StringCustomer;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleObjectTest extends AbstractTestBase {

	final private String input = "<customer><id>123</id><name>Joe's Garage</name><created>12/31/12</created></customer>";
	final private String inputWithLongDate = "<customer><id>123</id><name>Joe's Garage</name><created>Tuesday, January 16, 2007</created></customer>";

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
		DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
		Date date = format.parse("12/31/12");
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

		DateFormat format = DateFormat.getDateInstance(DateFormat.FULL);
		Date date = format.parse("Tuesday, January 16, 2007");

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
