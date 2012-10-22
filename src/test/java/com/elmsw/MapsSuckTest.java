package com.elmsw;

import com.elmsw.beans.Entry;
import com.elmsw.beans.TitleListItem;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertNotNull;

public class MapsSuckTest extends AbstractTestBase {

	@Test
	public void shouldBuildMapWithSomeWork() throws IOException {

		// setup test
		final String xml = resourceAsString("samples/maps_suck.xml");

		// run test
		List<Entry> entryList = new LinkedList<Entry>();
		xppIO.addLocalAlias("entry", Entry.class);
		xppIO.addLocalAlias("list", LinkedList.class);
		xppIO.addLocalAlias("titleListItem", TitleListItem.class);
		xppIO.populate(entryList, xml, "/message/body/results");

		// verify behavior
		System.out.println(entryList);

		Map<String, List<TitleListItem>> map = new HashMap<String, List<TitleListItem>>();

		for (Entry entry : entryList) {
			map.put(entry.getString(), (List<TitleListItem>) entry.getList());
		}

		assertNotNull(map);

	}

}
