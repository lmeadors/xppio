package com.elmsw;

import com.elmsw.beans.Response;
import com.elmsw.beans.ResponseError;
import org.junit.Test;
import org.w3c.dom.Document;

import java.util.LinkedList;

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

}
