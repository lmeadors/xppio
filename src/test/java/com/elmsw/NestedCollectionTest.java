package com.elmsw;

import com.elmsw.beans.Customer;
import com.elmsw.beans.LineItem;
import com.elmsw.beans.Order;
import com.elmsw.beans.OrderWithCustomer;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NestedCollectionTest extends AbstractTestBase {

	private static final Logger log = LoggerFactory.getLogger(NestedCollectionTest.class);
	public static final String EXPECTED_XML = "<order><id>1</id><lineItemList><lineItem><id>1</id><orderId>1</orderId><productId>3</productId><quantity>4</quantity></lineItem><lineItem><id>2</id><orderId>1</orderId><productId>4</productId><quantity>5</quantity></lineItem><lineItem><id>3</id><orderId>1</orderId><productId>5</productId><quantity>6</quantity></lineItem></lineItemList></order>";

	@Test
	public void shouldReadBeanWithCollectionOfObjects() throws Exception {

		// setup test
		Order expectedOrder = new Order(1);
		expectedOrder.addLineItem(new LineItem(1, 2, 3, 4));
		expectedOrder.addLineItem(new LineItem(2, 3, 4, 5));
		expectedOrder.addLineItem(new LineItem(3, 4, 5, 6));
		System.out.println(expectedOrder.toString());

		// run test
		final String xml = xppIO.toXml(expectedOrder);
		System.out.println(xml);

		// verify behavior
		assertEquals(EXPECTED_XML, xml);

		// now read it and see if it comes out the same. It better.
		// need some alias names
		xppIO.addAlias("order", Order.class);
		xppIO.addAlias("lineItem", LineItem.class);
		xppIO.addAlias("lineItemList", ArrayList.class);
		final Order actualOrder = xppIO.toObject(xml);

		assertNotNull(actualOrder);
		assertPropertiesAreEqual(expectedOrder, actualOrder, "lineItemList");
		List<LineItem> lineItemList = expectedOrder.getLineItemList();
		for (int i = 0; i < lineItemList.size(); i++) {
			assertPropertiesAreEqual(expectedOrder.getLineItemList().get(i), actualOrder.getLineItemList().get(i));
		}

	}

	@Test
	public void shouldMapEmbeddedOrderWithCustomer() throws Exception {

		// setup test
		// this is the customer we expect
		final Customer expectedCustomer = new Customer();
		expectedCustomer.setId(123);
		expectedCustomer.setName("blah");

		// this is the order we expect
		final LinkedList<LineItem> lineItemList = new LinkedList<LineItem>();
		lineItemList.add(new LineItem(1, 1, 3, 4));
		lineItemList.add(new LineItem(2, 1, 4, 5));
		lineItemList.add(new LineItem(3, 1, 5, 6));
		final OrderWithCustomer expectedOrder = new OrderWithCustomer();
		expectedOrder.setId(1);
		expectedOrder.setLineItemList(lineItemList);

		final Customer actualCustomer = new Customer();
		final OrderWithCustomer actualOrder = new OrderWithCustomer();
		actualOrder.setCustomer(actualCustomer);

		final String xml = resourceAsString("samples/embedded_order.xml");

		// run test
		// we need these hints for the order line mapping
		// todo: I wonder if we could determine the second from the com.elmsw.beans.Order#lineItemList field?
		xppIO.addAlias("lineItemList", ArrayList.class);
		xppIO.addAlias("lineItem", LineItem.class);

		xppIO.populate(actualOrder, xml, "/envelope/body/order");
		xppIO.populate(actualCustomer, xml, "/envelope/headers");

		// verify behavior
		log.debug("order: \n{}", actualOrder);
		assertPropertiesAreEqual(expectedCustomer, actualCustomer);
		assertPropertiesAreEqual(expectedCustomer, actualOrder.getCustomer());
		assertPropertiesAreEqual(expectedOrder, actualOrder, "customer", "lineItemList");
		assertEquals(expectedOrder.getLineItemList().size(), actualOrder.getLineItemList().size());
		for (int i = 0; i < lineItemList.size(); i++) {
			assertPropertiesAreEqual(lineItemList.get(i), actualOrder.getLineItemList().get(i));
		}

	}

}
