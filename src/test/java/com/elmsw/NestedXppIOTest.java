package com.elmsw;

import com.elmsw.beans.Account;
import com.elmsw.beans.CustomerWithAccount;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

public class NestedXppIOTest extends XppIOTestBase {

	private static final Logger log = LoggerFactory.getLogger(NestedXppIOTest.class);

	public static final String ACCOUNT_XML = "<account><id>123</id><name>Blah Sales</name></account>";
	public static final String CUSTOMER_XML = "<customer><id>123</id><name>Blah Inc.</name></customer>";
	public static final String CUSTOMER_ACCOUNT_XML = "<customer><id>123</id><name>Blah Inc.</name>" + ACCOUNT_XML + "</customer>";
	public static final String CUSTOMER_ACCOUNT_EXTRA_XML = "<customer><foo>bar</foo><id>123</id><name>Blah Inc.</name>" + ACCOUNT_XML + "</customer>";

	@Test
	public void shouldReadObjectWithoutChildObject() throws IOException, XmlPullParserException, IllegalAccessException, InstantiationException {
		// setup test
		final String input = CUSTOMER_XML;
		xppIO.addAlias("customer", CustomerWithAccount.class);

		// run test
		final CustomerWithAccount customerWithAccount = xppIO.toObject(input);

		// verify behavior
		assertNotNull(customerWithAccount);

	}

	@Test
	public void shouldReadChildObject() throws IOException, XmlPullParserException, IllegalAccessException, InstantiationException {

		// setup test
		final String input = ACCOUNT_XML;
		xppIO.addAlias("account", Account.class);

		// run test
		final Account account = xppIO.toObject(input);

		// verify behavior
		assertNotNull(account);

	}

	@Test
	public void shouldReadObjectWithChildObject() throws IOException, XmlPullParserException, IllegalAccessException, InstantiationException {

		// setup test
		final String input = CUSTOMER_ACCOUNT_XML;
		xppIO.addAlias("customer", CustomerWithAccount.class);
		xppIO.addAlias("account", Account.class);

		// run test
		log.debug(input);
		final CustomerWithAccount customerWithAccount = xppIO.toObject(input);

		// verify behavior
		assertNotNull(customerWithAccount);
		assertNotNull(customerWithAccount.getAccount());

	}

	@Test
	public void shouldReadObjectWithChildObjectAndIgnoreJunk() throws IOException, XmlPullParserException, IllegalAccessException, InstantiationException {

		// setup test
		final String input = CUSTOMER_ACCOUNT_EXTRA_XML;
		xppIO.addAlias("customer", CustomerWithAccount.class);
		xppIO.addAlias("account", Account.class);

		// run test
		log.debug(input);
		final CustomerWithAccount customerWithAccount = xppIO.toObject(input);

		// verify behavior
		assertNotNull("Customer should have been created", customerWithAccount);
		assertNotNull("Account should be populated", customerWithAccount.getAccount());

	}

}
