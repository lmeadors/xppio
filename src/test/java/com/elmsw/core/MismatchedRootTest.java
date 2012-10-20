package com.elmsw.core;

import com.elmsw.AbstractTestBase;
import com.elmsw.beans.Account;
import com.elmsw.beans.CustomerWithAccount;
import org.junit.Test;

public class MismatchedRootTest extends AbstractTestBase{

	@Test
	public void shouldPopulateExistingObjectWithFragmentContents() throws Exception {

		// setup test - this is the xml we have to deal with
		final String xml = "<some><envelope><x><id>2</id><name>foo</name></x></envelope></some>";

		// this is what we want to get out of it
		final Account expected = new Account();
		expected.setId(2);
		expected.setName("foo");

		// this is where our object mapping will start
		final String partToOurData = "/some/envelope/x";

		// OK, go get it...
		final Account actual = new Account();
		xppIO.populate(actual, xml, partToOurData);

		// verify behavior
		assertPropertiesAreEqual(expected, actual);

	}

	@Test
	public void shouldPopulateComplexNestedObject() throws Exception {

		// this is the xml we are dealing with
		final String xml = "<envelope><blah><lol>wtf?</lol><id>2</id><name>customer</name><account><id>1</id><name>test acct</name></account></blah></envelope>";

		// this is what we expect to extract from the xml
		final Account expectedAccount = new Account();
		expectedAccount.setId(1);
		expectedAccount.setName("test acct");
		final CustomerWithAccount expectedCustomer = new CustomerWithAccount();
		expectedCustomer.setAccount(expectedAccount);
		expectedCustomer.setId(2);
		expectedCustomer.setName("customer");

		// extract the data we want
		final CustomerWithAccount actualCustomer = new CustomerWithAccount();
		xppIO.populate(actualCustomer, xml, "/envelope/blah");

		// verify behavior
		assertPropertiesAreEqual(expectedCustomer, actualCustomer, "account");
		assertPropertiesAreEqual(expectedAccount, actualCustomer.getAccount());

	}

}
