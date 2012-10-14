xppio
=====

XML Pull Parser based utility for mapping xml to objects

This project was created because all of the existing libraries to do it suck.

This one probably sucks, too...just in a way that's more palatable for me.

What's cool?
------
* it's tiny: less than 50k including the dependency (singular - slf-api).
* it's extensible: the main class is 106 lines of executable code, everything else can be replaced
* it doesn't explode if your xml has extra elements


How do I use it to create xml?
------

Say this is your class:

	public class Customer {
		private Integer id;
		private String name;
		// extra java noise here - getters and setters, etc...
	}

Here is what you do to create XML from that:

	XppIO xppIO = new XppIO();
	Customer customer = new Customer();
	customer.setId(123);
	customer.setName("Blah Industries");
	String xml = xppIO.toXml(customer));

Yep. That simple.

You want to create an object from that xml? Easy.

	String input = "<customer><id>123</id><name>Joe's Garage</name></customer>";
	XppIO xppIO = new XppIO();
	xppIO.addAlias("customer", Customer.class);
	Customer customer = xppIO.toObject(input);

See the trick there? You do have to tell xppio that when it sees the <customer> element that it needs to create an
instance of the Customer class. Acceptable for me.

Now, what happens if you get the xml from someone and it has extra stuff in it? Like this:

	<customer>
		<id>123</id>
		<name>Blah Inc.</name>
		<number>123-123-1234</number>
	</customer>

You do this:

	XppIO xppIO = new XppIO();
	xppIO.addAlias("customer", Customer.class);
	Customer customer = xppIO.toObject(input);

See how it just ignores missing fields? What happened there?

By default, xppio ignores missing fields. If you want to to blow up in this case, you CAN make it happen by adding an
exception handler that explodes. Your choice, not mine. It's open source, and it's extensible.

Nesting?
------

Nested objects are also supported, including lists such as this:

	public class Order {
		Integer id;
		List<LineItem> lineItemList = new LinkedList<LineItem>();
	}

That would create xml something like this:

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

Not surprisingly, this XML can be used to create an order object with a list of lineItem objects (contained in a field
named lineItemList).

The code for creating this object looks something like this:

	xppIO.addAlias("order", Order.class);
	xppIO.addAlias("lineItem", LineItem.class);
	xppIO.addAlias("lineItemList", ArrayList.class);
	final Order actualOrder = xppIO.toObject(xml);

What's missing?
------
It might be nice to add annotations for aliases. That's a possible addition...but I'm not convinced that this is
something the framework should do, because it's more of a configuration thing.

Other collection types like Maps and Sets are not yet supported. I don't need them yet, so I didn't build the support.

