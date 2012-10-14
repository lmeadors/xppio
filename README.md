xppio
=====
XML Pull Parser based utility for mapping xml to objects

This project was created because all of the existing libraries to do it suck.

This one probably sucks, too...just in a way that's more palatable for me.

If you want a small library that doesn't explode when you throw extra xml that you don't have in your objects at it,
this might work for you.

For example, say this is your class:

public class Customer {

	private Integer id;
	private String name;

	// extra java noise here

}

And you want to create an object of this type from this xml:

<customer>
	<id>123</id>
	<name>Blah Inc.</name>
	<number>123-123-1234</number>
</customer>

You do this:

	xppIO.addAlias("customer", Customer.class);
	Customer actualCustomer = xppIO.toObject(input);

Using something like XStream, you're going to have to add a number field to your class. Dumb.

Using xppio, it just ignores missing fields. If you want to to blow up in this case, you CAN make it happen by adding an
exception handler that explodes. You choose, not me.

Currently, you can create objects from xml and xml from objects.

Nested objects are also supported, including lists such as this:

public class Order {
	Integer id;
	List<LineItem> lineItemList = new LinkedList<LineItem>();
}

That would create this xml:

<order>
	<id>1</id>
	<lineItemList>
		<lineItem>
			<id>1</id>
			<orderId>1</orderId>
			<productId>3</productId>
			<quantity>4</quantity>
		</lineItem>
		<lineItem>
			<id>2</id>
			<orderId>1</orderId>
			<productId>4</productId>
			<quantity>5</quantity>
		</lineItem>
		<lineItem>
			<id>3</id>
			<orderId>1</orderId>
			<productId>5</productId>
			<quantity>6</quantity>
		</lineItem>
	</lineItemList>
</order>

