package com.elmsw;

import com.elmsw.beans.Customer;
import com.elmsw.beans.StringCustomer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleXppIOTest extends XppIOTestBase {

	@Test
	public void shouldImportSimpleBeanWithStrings() throws Exception {

		// setup test
		final String input = "<customer><id>123</id><name>Joe's Garage</name></customer>";
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
		final String input = "<customer><id>123</id><name>Joe's Garage</name></customer>";
		Customer expectedCustomer = new Customer(123, "Joe's Garage");
		xppIO.addAlias("customer", Customer.class);

		// run test
		final Customer actualCustomer = xppIO.toObject(input);

		// verify behavior
		assertNotNull(actualCustomer);
		assertEquals(expectedCustomer.getId(), actualCustomer.getId());
		assertEquals(expectedCustomer.getName(), actualCustomer.getName());

	}

}
