package ro.allevo.fintpui.utils.datatables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class DataTableRequest {

	private final NavigableMap<String, String[]> allRequestParams;
	private final List<DataTableRequestColumn> columns;
	
	private static final String API_FILTER_PREFIX = "filter_";
	private static final String API_FILTER_PAGE = "page";
	private static final String API_FILTER_PAGE_SIZE = "page_size";
	private static final String API_SORT_PREFIX = "sort_";
	private static final String COLUMNS = "columns[%s][%s]";
	private static final String COLUMNS_SEARCH = "columns[%s][%s][%s]";
	private static final String ORDER = "order[%s][%s]";
	
	public DataTableRequest(HttpServletRequest request) {
		allRequestParams = new TreeMap<>(request.getParameterMap());
		
		columns = getTableColumns();
	}

	public Map<String, List<String>> getApiParameters() {
		Map<String, List<String>> apiParams = new LinkedHashMap<String, List<String>>();
		
		if (!allRequestParams.containsKey("columns[0][data]"))
			return apiParams;
		
		apiParams.putAll(getFilterParameter());
		apiParams.putAll(getSortParameter());
		apiParams.putAll(getPaginationParameter());
		return apiParams;
	}

	public Map<String, List<String>> getFilterParameter() {
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		String value = null;
		String filter = null;
		
		//add column filters
		for (DataTableRequestColumn column : columns) {
			value = column.getSearch().getValue();
			String filterName = column.getFilterName();
			if(value.length() > 0 && filterName != null && filterName.equals("dateInterval")) {
				String[] dateTime = value.split(" - ");
				String startDateTime = dateTime[0].replace(' ', 'T');
				String endDateTime = dateTime[1].replace(' ', 'T');

				filter = API_FILTER_PREFIX + column.getName();	
				params.put(filter + "_udate", Arrays.asList(startDateTime));
				params.put(filter + "_ldate", Arrays.asList(endDateTime));
			}
			else {	
				filter = API_FILTER_PREFIX + column.getName() + column.getFilterType();	
								
				if (value.length() > 0) {
					params.put(filter, Arrays.asList(StringUtils.stripEnd(value,".")));
				}
			}
		}
		
		//add injected filters
		SortedMap<String, String[]> filters = allRequestParams.subMap(API_FILTER_PREFIX, API_FILTER_PREFIX + Character.MAX_VALUE);
		for (String key : filters.keySet())
			params.put(key, Arrays.asList(getRequestValue(key)));
		
		return params;
	}

	public Map<String, List<String>> getSortParameter() {
		Map<String, List<String>> params = new LinkedHashMap<String, List<String>>();
		
		int index = 0;
		
		while (allRequestParams.containsKey(String.format(ORDER, index, "column"))) {
			String sortColIndex = getRequestValue(String.format(ORDER, index, "column"));
			String colName = columns.get(Integer.parseInt(sortColIndex)).getName();
			String sortDirection = getRequestValue(String.format(ORDER, index, "dir"));
			
			String orderBy = API_SORT_PREFIX + colName; 
			params.put(orderBy, Arrays.asList(sortDirection));
			
			index++;
		}
		
		return params;
	}

	public Map<String, List<String>> getPaginationParameter() {
		Map<String, List<String>> params = new HashMap<String, List<String>>();
		if (getRequestValue("start") != null && getRequestValue("length") != null) {
			int start = Integer.parseInt(getRequestValue("start"));
			int length = Integer.parseInt(getRequestValue("length"));
			params.put(API_FILTER_PAGE, Arrays.asList(String.valueOf(start / length + 1)));
			params.put(API_FILTER_PAGE_SIZE, Arrays.asList(String.valueOf(length)));
		}
		params.put("total", Arrays.asList(""));
		return params;
	}

	private List<DataTableRequestColumn> getTableColumns() {
		List<DataTableRequestColumn> columns = new ArrayList<DataTableRequestColumn>();
		int index = 0;
		DataTableRequestColumn data = null;
		Search search = null;
		while (allRequestParams.containsKey(String.format(COLUMNS, index, "data"))) {
			data = new DataTableRequestColumn();
			data.setName(getRequestValue(String.format(COLUMNS, index, "data")));
			data.setType(getRequestValue(String.format(COLUMNS, index, "type")));
			data.setFilterName(getRequestValue(String.format(COLUMNS_SEARCH, index, "type", "filterName")));
			data.setFilterType(getRequestValue(String.format(COLUMNS_SEARCH, index, "type", "filterType")));
			data.setOrderable(Boolean.valueOf(getRequestValue(String.format(COLUMNS, index, "orderable"))));
			data.setSearchable(Boolean.valueOf(getRequestValue(String.format(COLUMNS, index, "searchable"))));
			search = new Search();
			search.setValue(getRequestValue(String.format(COLUMNS_SEARCH, index, "search", "value")));
			search.setRegex(Boolean.valueOf(getRequestValue(String.format(COLUMNS_SEARCH, index, "search", "regex"))));
			data.setSearch(search);
			columns.add(data);
			index++;
		}
		return columns;
	}

	private String getRequestValue(String key) {
		if (allRequestParams.containsKey(key)) {
			return allRequestParams.get(key)[0];
		}
		return null;
	}

}
