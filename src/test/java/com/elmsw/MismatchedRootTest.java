package com.elmsw;

import com.elmsw.beans.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MismatchedRootTest extends AbstractTestBase {

	private static final Logger log = LoggerFactory.getLogger(MismatchedRootTest.class);

	@Test
	public void shouldPopulateExistingObjectWithFragmentContents() throws Exception {

		// setup test - this is the xml we have to deal with
		final String xml = resourceAsString("samples/simple_envelope.xml");

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
		final String xml = resourceAsString("samples/madness.xml");

//		xppIO.addAlias("account", Account.class);

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

	@Test
	public void shouldMapSuperclassProperties() throws Exception {

		// setup test

		final String xml = resourceAsString("samples/customer_with_account.xml");

		final Account expectedAccount = new Account();
		expectedAccount.setId(234);
		expectedAccount.setName("Blah Sales");

		final CustomerWithAccount expected = new CustomerWithAccount();
		expected.setId(123);
		expected.setName("Blah Inc.");
		expected.setAccount(expectedAccount);

		xppIO.addAlias("account", Account.class);
		xppIO.addAlias("customer", CustomerWithAccount.class);

		// run test
		final CustomerWithAccount actual = xppIO.toObject(xml);

		// verify behavior
		System.out.println("*" + xppIO.toXml(expected));
		System.out.println("*" + xppIO.toXml(actual));

		assertPropertiesAreEqual(expected, actual, "account");
		assertPropertiesAreEqual(expected.getAccount(), actual.getAccount());

	}


	@Test
	public void shouldBeAbleToDoWeirdFragmentMapping() throws Exception {

		// setup test
		// this is the xml we are dealing with
		final String xml = resourceAsString("samples/madness.xml");

		// this is what we expect to extract from the xml
		final Account expectedAccount = new Account();
		expectedAccount.setId(2);
		expectedAccount.setName("customer");

		final Customer expectedCustomer = new Customer();
		expectedCustomer.setId(1);
		expectedCustomer.setName("test acct");

		// run tests - ARE YOU NUTS?!
		// OK, here we're actually mapping the account fragment to the customer object - they have the similar properties, so why not?
		final Customer actualCustomer = new Customer();
		xppIO.populate(actualCustomer, xml, "/envelope/blah/account");

		// here we're going the other way - mapping the blah element (a customer before) to an account. :-|
		final Account actualAccount = new Account();
		xppIO.populate(actualAccount, xml, "/envelope/blah");

		// verify behavior
		assertPropertiesAreEqual(expectedCustomer, actualCustomer);
		assertPropertiesAreEqual(expectedAccount, actualAccount);

	}

	@Test
	public void shouldMapNodeListToCollection() throws IOException {

		// setup test
		final String xml = resourceAsString("samples/embedded_order.xml");

		// need some hints here to construct the list items
		xppIO.addAlias("lineItem", LineItem.class);
		xppIO.addAlias("lineItemList", ArrayList.class);

		// run test
		final Order order = new Order();
		xppIO.populate(order, xml, "/envelope/body/order");

		// verify behavior
		log.debug("order: {}", order);
		log.debug("order as xml:\n{}", xppIO.toXml(order));

	}

	@Test
	public void shouldExtractJustListFromDocument() throws IOException {
		// setup test
		final String xml = resourceAsString("samples/embedded_order.xml");
		List<LineItem> itemList = new LinkedList<LineItem>();
		xppIO.addAlias("lineItem", LineItem.class);

		// run test
		xppIO.populate(itemList, xml, "/envelope/body/order/lineItemList");

		// verify behavior
		System.out.println(itemList);
	}

}
