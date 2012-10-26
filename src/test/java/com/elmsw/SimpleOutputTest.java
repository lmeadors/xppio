package com.elmsw;

import com.elmsw.beans.Account;
import com.elmsw.beans.Customer;
import org.junit.Test;
import org.xmlpull.v1.XmlPullParserException;

import static org.junit.Assert.assertEquals;

public class SimpleOutputTest extends AbstractTestBase {

	public static final String EXPECTED_CUSTOMER_XML = "<customer><id>123</id><name>Blah Industries</name><enabled>false</enabled></customer>";
	public static final String EXPECTED_CUSTOMER_XML_WITH_ALIAS = "<myCustomer><id>123</id><name>Blah Industries</name><enabled>false</enabled></myCustomer>";
	private static final String EXPECTED_ACCOUNT_XML_WITH_ALIAS = "<myAccount><id>234</id><name>Woot Account</name></myAccount>";

	@Test
	public void shouldCreateCustomerXml() throws XmlPullParserException {

		// setup test
		XppIO xppIO = new XppIO();
		xppIO.addLocalAlias("customer", Customer.class);

		Customer customer = new Customer();
		customer.setId(123);
		customer.setName("Blah Industries");

		// run test and verify behavior
		assertEquals(EXPECTED_CUSTOMER_XML, xppIO.toXml(customer));

	}

	@Test
	public void shouldCreateXmlUsingAlias() throws Exception {

		xppIO.addAlias("myCustomer", Customer.class);
		xppIO.addAlias("myAccount", Account.class);

		// setup test
		Customer expectedCustomer = new Customer();
		expectedCustomer.setId(123);
		expectedCustomer.setName("Blah Industries");

		Account expectedAccount = new Account();
		expectedAccount.setId(234);
		expectedAccount.setName("Woot Account");

		// run test and verify behavior
		final String actualCustomerXml = xppIO.toXml(expectedCustomer);
		assertEquals(EXPECTED_CUSTOMER_XML_WITH_ALIAS, actualCustomerXml);
		final String actualAccountXml = xppIO.toXml(expectedAccount);
		assertEquals(EXPECTED_ACCOUNT_XML_WITH_ALIAS, actualAccountXml);

		// just for fun, we'll round-trip these, too
		assertPropertiesAreEqual(expectedAccount, xppIO.<Account>toObject(actualAccountXml));
		assertPropertiesAreEqual(expectedCustomer, xppIO.<Customer>toObject(actualCustomerXml));

	}

}
