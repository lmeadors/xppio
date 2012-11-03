package com.elmsw;

import com.elmsw.beans.Customer;
import com.elmsw.beans.MyCustomerIsCoolerThanYours;
import com.elmsw.beans.StringCustomer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

public class SimpleObjectTest extends AbstractTestBase {

	private String input;

	@Before
	public void beforeSimpleObjectTest() throws IOException {
		input = resourceAsString("samples/customer.xml");
	}

	@Test
	public void shouldImportSimpleBeanWithStrings() throws Exception {

		// setup test
		StringCustomer expected = new StringCustomer("123", "Joe's Garage");
		xppIO.addAlias("customer", StringCustomer.class);

		// run test
		final StringCustomer actual = xppIO.toObject(input);

		// verify behavior
		assertPropertiesAreEqual(expected, actual);

	}

	@Test
	public void shouldImportSimpleBeanWithMixedTypes() throws Exception {

		// setup test
		Date date = getDateUsingDefaultFormat("2012-12-31 00:00:00.0 GMT");
		Customer expected = new Customer(123, "Joe's Garage", date);
		xppIO.addAlias("customer", Customer.class);

		// run test
		final Customer actual = xppIO.toObject(input);

		// verify behavior
		assertPropertiesAreEqual(expected, actual);

	}

	@Test
	public void shouldMapCustomerToExistingObjectWithoutAlias() throws Exception {

		// setup test
		MyCustomerIsCoolerThanYours expected = new MyCustomerIsCoolerThanYours(123, "Joe's Garage");

		// run test
		final MyCustomerIsCoolerThanYours actual = xppIO.populate(new MyCustomerIsCoolerThanYours(), input, "/customer");

		// verify behavior
		assertPropertiesAreEqual(expected, actual);

	}

	@Test
	public void shouldPopulateCustomerEvenWithExtraJunk() throws Exception {

		// setup test
		final String input = resourceAsString("samples/customer_with_account.xml");
		final Customer expected = new Customer(123, "Blah Inc.");
		expected.setDescription("yay me");
		expected.setEnabled(true);

		// run test
		final Customer actual = xppIO.populate(new Customer(), input);

		// verify behavior
		assertPropertiesAreEqual(expected, actual);

	}


}
