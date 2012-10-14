package com.elmsw;

import com.elmsw.beans.LineItem;
import com.elmsw.beans.Order;
import org.junit.Test;

public class NestedCollectionTest extends XppIOTestBase {

	@Test
	public void shouldReadBeanWithCollectionOfObjects() {
		// setup test
		Order order = new Order(1);
		order.addLineItem(new LineItem(1, 2, 3, 4));
		order.addLineItem(new LineItem(2, 3, 4, 5));
		order.addLineItem(new LineItem(3, 4, 5, 6));
		System.out.println(order.toString());

		// run test
		System.out.println(xppIO.toXml(order));

		// verify behavior

	}

}
