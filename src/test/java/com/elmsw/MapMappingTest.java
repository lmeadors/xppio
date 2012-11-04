package com.elmsw;

import com.elmsw.beans.StringCustomer;
import com.elmsw.beans.TitleListItem;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MapMappingTest extends AbstractTestBase {

	@Test
	public void shouldBuildMap() throws IOException {

		// setup test
		final String xml = resourceAsString("samples/map_sample.xml");

		// run test
		final Map<String, List<TitleListItem>> mapOfLists = new HashMap<String, List<TitleListItem>>();
		xppIO.addAlias("string", String.class);
		xppIO.addAlias("list", LinkedList.class);
		xppIO.addAlias("titleListItem", TitleListItem.class);
		xppIO.addAlias("results", HashMap.class);

		// the assumption we make is that the Map when represented as xml will have a list of nodes. Each of those
		// nodes will have 2 children - the first child will be used as the key, and the second as the value
		final Map<String, List<TitleListItem>> populate = xppIO.populate(mapOfLists, xml, "/message/body/results");

		// verify behavior - we don't create a new Map, we use the one provided...but return it, too.
		assertTrue(populate == mapOfLists);

		assertEquals(4, mapOfLists.size());

		final List<TitleListItem> audiobooks = mapOfLists.get("Audiobooks");
		final List<TitleListItem> movies = mapOfLists.get("Movies");
		final List<TitleListItem> music = mapOfLists.get("Music");
		final List<TitleListItem> television = mapOfLists.get("Television");

		assertNotNull(audiobooks);
		assertEquals("should be 1 audiobook", 1, audiobooks.size());

		assertNotNull(movies);
		assertEquals("should be 25 movies", 25, movies.size());

		assertNotNull(music);
		assertEquals("should be 25 music items", 25, music.size());

		assertNotNull(television);
		assertEquals("should be 4 tv shows", 4, television.size());

	}

	@Test
	public void shouldMapSingleElementToString() {

		// setup test
		String input = "<string>blah</string>";
		xppIO.addAlias("string", String.class);

		// run test
//		final String output = xppIO.populate(, input);
		final String output = xppIO.toObject(input);

		// verify behavior
		assertNotNull(output);
		assertEquals("blah", output);

	}

}
