package ro.allevo.fintpui.utils;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PagedCollection <T> {
	
	private boolean hasMore;
	
	private int total;
	
	private T[] items;

	@JsonGetter(value = "hasMore")
	public boolean hasMore() {
		return hasMore;
	}

	public int getTotal() {
		return total;
	}

	public T[] getItems() {
		return items;
	}
}
