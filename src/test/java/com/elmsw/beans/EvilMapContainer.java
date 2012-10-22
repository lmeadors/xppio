package com.elmsw.beans;

import java.util.List;
import java.util.Map;

public class EvilMapContainer {

	private Map<String, List<TitleListItem>> map;

	public Map<String, List<TitleListItem>> getMap() {
		return map;
	}

	public void setMap(Map<String, List<TitleListItem>> map) {
		this.map = map;
	}

}
