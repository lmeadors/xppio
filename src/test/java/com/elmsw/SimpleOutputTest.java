package com.elmsw;

import com.elmsw.beans.Customer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleOutputTest extends XppIOTestBase {

	public static final String EXPECTED_XML = "<customer><id>123</id><name>Blah Industries</name></customer>";

	@Test
	public void shouldCreateCustomerXml() {

		// setup test
		Customer customer = new Customer();
		customer.setId(123);
		customer.setName("Blah Industries");

		// run test and verify behavior
		assertEquals(EXPECTED_XML, xppIO.toXml(customer));

	}

}
