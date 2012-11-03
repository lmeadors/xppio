package com.elmsw;

import com.elmsw.beans.*;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MismatchedRootTest extends AbstractTestBase {

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
	public void shouldPopulateComplexNestedObjects() throws Exception {

		// this is the xml we are dealing with
		final String xml = resourceAsString("samples/madness.xml");

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
		expected.setDescription("yay me");
		expected.setEnabled(true);
		expected.setAccount(expectedAccount);

		xppIO.addAlias("account", Account.class);
		xppIO.addAlias("customer", CustomerWithAccount.class);

		// run test
		final CustomerWithAccount actual = xppIO.toObject(xml);

		// verify behavior
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
	public void shouldMapNodeListToCollection() throws Exception {

		// setup test
		final String xml = resourceAsString("samples/embedded_order.xml");
		final Order expected = new Order(1);
		expected.addLineItem(new LineItem(1, 1, 3, 4));
		expected.addLineItem(new LineItem(2, 1, 4, 5));
		expected.addLineItem(new LineItem(3, 1, 5, 6));

		// run test
		xppIO.addAlias("lineItem", LineItem.class);
		xppIO.addAlias("lineItemList", ArrayList.class);
		final Order actual = xppIO.populate(new Order(), xml, "/envelope/body/order");

		// verify behavior
		assertPropertiesAreEqual(expected, actual, "lineItemList");
		assertEquals("both orders should have the same number of line items", expected.getLineItemList().size(), actual.getLineItemList().size());
		for (int i = 0; i < expected.getLineItemList().size(); i++) {
			assertPropertiesAreEqual(expected.getLineItemList().get(i), actual.getLineItemList().get(i));
		}

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
		assertEquals("should have 3 line items", 3, itemList.size());
		assertEquals("first line is for product 3", 3, itemList.get(0).getProductId().intValue());
		assertEquals("second line is for product 4", 4, itemList.get(1).getProductId().intValue());
		assertEquals("third line is for product 5", 5, itemList.get(2).getProductId().intValue());

	}

	@Test
	public void shouldMapNodeToObject() throws Exception {

		// setup test
		final Document document = xmlTestHelper.stringToDocument(resourceAsString("samples/embedded_order.xml"));
		final Node node = xmlTestHelper.xPathQueryForNode(document, "/envelope/body/order/lineItemList/lineItem[1]");
		LineItem expected = new LineItem(1, 1, 3, 4);

		// run test
		xppIO.addAlias("lineItem", LineItem.class);
		LineItem actual = xppIO.toObject(node);

		// verify behavior
		assertPropertiesAreEqual(expected, actual);

	}

	@Test
	public void shouldPopulateObjectFromNode() throws Exception {

		// setup test
		final Document document = xmlTestHelper.stringToDocument(resourceAsString("samples/embedded_order.xml"));
		final Node node = xmlTestHelper.xPathQueryForNode(document, "/envelope/body/order/lineItemList/lineItem[1]");
		LineItem expected = new LineItem(1, 1, 3, 4);

		// run test
		xppIO.addAlias("lineItem", LineItem.class);
		LineItem actual = xppIO.populate(new LineItem(), node);

		// verify behavior
		assertPropertiesAreEqual(expected, actual);

	}

}
