package ro.allevo.fintpui.utils.datatables;

public class DatatableColumnEntity {
	private int data;
	private String name;
	private boolean searchable;
	private boolean orderable;
	private Search search;

	public int getData() {
		return data;
	}

	public void setData(int data) {
		this.data = data;
	}

	public String getName() {
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
	
}
