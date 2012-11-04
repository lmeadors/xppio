package com.elmsw;

import com.elmsw.beans.Account;
import com.elmsw.beans.CustomerWithAccount;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class NestedObjectTest extends AbstractTestBase {

	private static final Logger log = LoggerFactory.getLogger(NestedObjectTest.class);

	public static final String ACCOUNT_XML = "<account><id>123</id><name>Blah Sales</name></account>";
	public static final String CUSTOMER_XML = "<customer><id>123</id><name>Blah Inc.</name></customer>";
	public static final String CUSTOMER_ACCOUNT_XML = "<customer><id>123</id><name>Blah Inc.</name>" + ACCOUNT_XML + "</customer>";
	public static final String CUSTOMER_ACCOUNT_EXTRA_XML = "<customer><number>123-123-1234</number><id>123</id><name>Blah Inc.</name>" + ACCOUNT_XML + "</customer>";

	@Test
	public void shouldReadObjectWithoutChildObject() throws IOException, XmlPullParserException, IllegalAccessException, InstantiationException {
		// setup test
		final String input = CUSTOMER_XML;
		xppIO.addAlias("customer", CustomerWithAccount.class);

		// run test
		final CustomerWithAccount customerWithAccount = xppIO.populate(new CustomerWithAccount(), input);
//		final CustomerWithAccount customerWithAccount = xppIO.toObject(input);

		// verify behavior
		assertNotNull(customerWithAccount);

	}

	@Test
	public void shouldReadChildObject() throws Exception {

		// setup test
		Account expected = new Account();
		expected.setId(123);
		expected.setName("Blah Sales");

		// run test
		xppIO.addAlias("account", Account.class);
		final Account actual = xppIO.populate(new Account(), ACCOUNT_XML);

		// verify behavior
		assertPropertiesAreEqual(expected, actual);

	}

	@Test
	public void shouldReadObjectWithChildObject() throws Exception {

		// setup test
		final Account expectedAccount = new Account();
		expectedAccount.setId(123);
		expectedAccount.setName("Blah Sales");
		final CustomerWithAccount expected = new CustomerWithAccount();
		expected.setAccount(expectedAccount);
		expected.setId(123);
		expected.setName("Blah Inc.");

		// run test
		xppIO.addAlias("customer", CustomerWithAccount.class);
		xppIO.addAlias("account", Account.class);
		final CustomerWithAccount actual = xppIO.populate(new CustomerWithAccount(), CUSTOMER_ACCOUNT_XML);
//		final CustomerWithAccount actual = xppIO.toObject(CUSTOMER_ACCOUNT_XML);

		// verify behavior
		assertPropertiesAreEqual(expected, actual, "account");
		assertPropertiesAreEqual(expectedAccount, actual.getAccount());

	}

	@Test
	public void shouldReadObjectWithChildObjectAndIgnoreJunk() throws IOException, XmlPullParserException, IllegalAccessException, InstantiationException {

		// setup test
		final String input = CUSTOMER_ACCOUNT_EXTRA_XML;
		xppIO.addAlias("customer", CustomerWithAccount.class);
		xppIO.addAlias("account", Account.class);

		// run test
		log.debug(input);
		final CustomerWithAccount customerWithAccount = xppIO.populate(new CustomerWithAccount(), input);
//		final CustomerWithAccount customerWithAccount = xppIO.toObject(input);

		// verify behavior
		assertNotNull("Customer should have been created", customerWithAccount);
		assertNotNull("Account should be populated", customerWithAccount.getAccount());

	}

}
