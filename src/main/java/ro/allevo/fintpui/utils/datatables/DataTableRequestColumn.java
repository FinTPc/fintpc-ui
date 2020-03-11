package ro.allevo.fintpui.utils.datatables;

public class DataTableRequestColumn {
	private int data;
	private String name;
	private boolean searchable;
	private boolean orderable;
	private Search search;
	private String type;
	private String filterType;
	private String filterName;

	public int getData() {
		return data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setData(int data) {
		this.data = data;
	}

	public String getName() {
		if (null != filterName && !filterName.equals("dateInterval"))
			return filterName;
		
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	public boolean isOrderable() {
		return orderable;
	}

	public void setOrderable(boolean orderable) {
		this.orderable = orderable;
	}

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}
	
	public String getFilterType() {
		if (null != filterType)
			switch(filterType) {
			case "number":
				return "_exact";
			}
		
		return "";
	}

	public void setFilterType(String filterType) {
		this.filterType = filterType;
	}

	public String getFilterName() {
		return filterName;
	}

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}
	
}
