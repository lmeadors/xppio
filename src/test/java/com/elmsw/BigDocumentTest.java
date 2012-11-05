package com.elmsw;

import com.elmsw.beans.Response;
import com.elmsw.beans.ResponseError;
import com.elmsw.core.converters.DateConverter;
import org.junit.Test;
import org.w3c.dom.Document;

import java.util.Date;
import java.util.LinkedList;

import static org.junit.Assert.assertNotNull;

public class BigDocumentTest extends AbstractTestBase {
	@Test
	public void shouldNotTakeForever() throws Exception {

		// setup test
		final String xml = resourceAsString("samples/holy-cow.xml");
		final Document document = xmlTestHelper.stringToDocument(xml);
		xppIO.addAlias("message", Response.class);
		xppIO.addAlias("status", String.class);
		xppIO.addAlias("errors", LinkedList.class);
		xppIO.addAlias("error", ResponseError.class);

		// run test
		final long start = System.currentTimeMillis();
		final Response response = xppIO.populate(new Response(), document, "/message");
//		final Response response = xppIO.populate(new Response(), document, "/message");
//		final Response response = xppIO.toObject(document);
		final long end = System.currentTimeMillis();
		System.out.println(end - start);

//		xppIO.populate(response.getErrors(), document, "/message/errors");

		// verify behavior
		System.out.println("status:" + response.getStatus());
		System.out.println("errors:" + response.getErrors().size());
		System.out.println(response.getErrors().get(0));

	}

	@Test
	public void shouldGetErrors() throws Exception {
		// setup test
		XppIO xppIO = new XppIO();
		xppIO.addAlias("message", Response.class);
		xppIO.addAlias("errors", LinkedList.class);
		xppIO.addAlias("error", ResponseError.class);
		xppIO.addConverter(Date.class, new DateConverter("yyyy/MM/dd hh:mm:ss zzzZ"));
		final String xml = resourceAsString("samples/errors.xml");

		// run test
		Response response = xppIO.populate(new Response(), xml, "/message");

		// verify behavior
		assertNotNull(response.getErrors());
	}

	@Test
	public void shouldMapErrors() throws Exception {
		final String errorsResponse = "<message><status>ERROR</status><errors><error><field>libraryId</field><text>Library Id is a required field</text></error></errors></message>";

		XppIO xppIO = new XppIO();
		xppIO.addAlias("message", Response.class);
		xppIO.addAlias("errors", LinkedList.class);
		xppIO.addAlias("error", ResponseError.class);
		xppIO.addConverter(Date.class, new DateConverter("yyyy/MM/dd hh:mm:ss zzzZ"));

		Response response = new Response();

		xppIO.populate(response, errorsResponse, "/message");

		assertNotNull(response.getErrors());
	}

}
