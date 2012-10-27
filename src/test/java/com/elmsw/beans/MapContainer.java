package com.elmsw.beans;

import java.util.List;
import java.util.Map;

public class MapContainer {

	private Map<String, List<TitleListItem>> itemMap;

	public Map<String, List<TitleListItem>> getMap() {
		return itemMap;
	}

	public void setMap(Map<String, List<TitleListItem>> itemMap) {
		this.itemMap = itemMap;
	}

}
