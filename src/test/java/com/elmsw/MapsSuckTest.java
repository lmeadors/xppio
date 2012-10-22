package com.elmsw;

import com.elmsw.beans.Entry;
import com.elmsw.beans.TitleListItem;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MapsSuckTest extends AbstractTestBase {

	@Test
	public void shouldBuildMapWithSomeWork() throws IOException {

		// setup test
		final String xml = resourceAsString("samples/maps_suck.xml");

		// run test
		List<Entry> keyNames = new LinkedList<Entry>();
		xppIO.addLocalAlias("entry", Entry.class);
		xppIO.addLocalAlias("list", LinkedList.class);
		xppIO.addLocalAlias("titleListItem", TitleListItem.class);
		xppIO.populate(keyNames, xml, "/message/body/results");

		// verify behavior
		System.out.println(keyNames);

	}

}
