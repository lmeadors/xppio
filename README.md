xppio
=====

XML Pull Parser based utility for mapping xml to objects

This project was created because all of the existing libraries to do it suck.

This one probably sucks, too...just in a way that's more palatable for me.


What's cool?
------
* it's tiny: less than 200k including the dependencies (xpp3 and slf-api).
* it's extensible: the main class is less than 200 lines of executable code, everything else can be replaced
* it doesn't explode if your xml has extra elements
* it will map xml fragments into existing objects


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
instance of the Customer class. Acceptable for me, but what if you don't want to do that? What if your class is called
MyCustomerIsCoolerThanYours?

Not a problem - using the same XML as the previous example, you can do this:

	final MyCustomerIsCoolerThanYours actualCustomer = new MyCustomerIsCoolerThanYours();
	xppIO.populate(actualCustomer, input, "/customer");

So, there, we're taking an existing object, and saying that starting at the /customer node of the XML data, it will map
to a MyCustomerIsCoolerThanYours object.

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
exception handler that explodes. Your choice, not mine. It's open source and it's extensible, too.

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

You should see the XML I have to deal with...
------
Yeah, I know - you don't always get XML that matches what you need exactly. That's fine, you can extract fragments, too.

Here's an example. Let's start with this XML:

	<envelope>
	  <blah>
		<lol>wth?</lol>
		<id>2</id>
		<name>customer</name>
		<account>
		  <id>1</id>
		  <name>test acct</name>
		</account>
	  </blah>
	</envelope>

Wait, that's a customer with an account in it! Sure it's got some extra noise in it, but we can still consume that:

	final CustomerWithAccount actualCustomer = new CustomerWithAccount();
	xppIO.populate(actualCustomer, xml, "/envelope/blah");

What the heck is that doing? We're creating the object to populate, then telling xppio to look at the node at
"/envelope/blah" and just pretend it's a CustomerWithAccount object. Think of this as casting in XML. :-P

What's more here is that you can map the "/envelope/blah/account" fragment to a customer just as easily (because they
have similar properties) like this:

	final Customer actualCustomer = new Customer();
	xppIO.populate(actualCustomer, xml, "/envelope/blah/account");

The same deal works for mapping "/envelope/blah" to an account:

	final Account actualAccount = new Account();
	xppIO.populate(actualAccount, xml, "/envelope/blah");


What's missing?
------
It might be nice to add annotations for aliases. That's a possible addition...but I'm not convinced that this is
something the framework should do, because it's more of a configuration thing.

Other collection types like Maps and Sets are not yet supported. I don't need them yet, so I didn't build the support.

The fragment mapping code needs to be optimized. I wanted to make it work and test the snot out of it first though. Once
it is better tested and proven to work, I'll revisit it and optimize it. It's currently O(m*n) but could be O(m+n) or
better.
