package com.elmsw;

import com.elmsw.beans.LineItem;
import com.elmsw.beans.Order;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NestedCollectionTest extends XppIOTestBase {

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

}
