package com.elmsw;

import com.elmsw.beans.Account;
import com.elmsw.beans.Customer;
import com.elmsw.core.converters.DateConverter;
import org.junit.Test;
import org.w3c.dom.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class SimpleOutputTest extends AbstractTestBase {

	public static final String EXPECTED_CUSTOMER_XML = "<customer><id>123</id><name>Blah Industries</name><enabled>false</enabled><created>2012-12-31 12:00:00.0 UTC</created></customer>";
	public static final String EXPECTED_CUSTOMER_XML_WITH_ALIAS = "<myCustomer><id>123</id><name>Blah Industries</name><enabled>false</enabled></myCustomer>";
	private static final String EXPECTED_ACCOUNT_XML_WITH_ALIAS = "<myAccount><id>234</id><name>Woot Account</name></myAccount>";

	@Test
	public void shouldCreateCustomerXml() throws Exception, ParseException {

		// setup test
		XppIO xppIO = new XppIO();
		xppIO.addAlias("customer", Customer.class);

		final SimpleDateFormat format = new SimpleDateFormat(DateConverter.PATTERN);
		Date date = format.parse("2012-12-31 12:00:00.0 UTC");

		Customer customer = new Customer();
		customer.setId(123);
		customer.setName("Blah Industries");
		customer.setCreated(date);

		// run test and verify behavior
		final Document document = xmlTestHelper.stringToDocument(xppIO.toXml(customer));
		assertEquals(customer.getId().toString(), xmlTestHelper.xPathQuery(document, "/customer/id"));
		assertEquals(customer.getName(), xmlTestHelper.xPathQuery(document, "/customer/name"));
		assertEquals(customer.isEnabled() + "", xmlTestHelper.xPathQuery(document, "/customer/enabled"));
		assertEquals(customer.getCreated(), format.parse(xmlTestHelper.xPathQuery(document, "/customer/created")));

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
