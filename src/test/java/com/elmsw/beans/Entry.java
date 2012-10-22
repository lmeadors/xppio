package com.elmsw.beans;

import java.util.List;

public class Entry {

	private String string;
	private List<TitleListItem> list;

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public List<TitleListItem> getList() {
		return list;
	}

	public void setList(List<TitleListItem> list) {
		this.list = list;
	}

}
