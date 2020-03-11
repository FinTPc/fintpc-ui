package ro.allevo.fintpui.utils;

import java.util.ArrayList;
import java.util.List;

public class Filters {
	
	public static List<String> getFiltersParams(String... value) {
		List<String> filterParams = new ArrayList<String>();
		for (String val : value) {
			filterParams.add(val);
		}
		return filterParams;
	}

}
