package ro.allevo.fintpui.dao;

import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.UriBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import ro.allevo.fintpui.utils.PagedCollection;
import ro.allevo.fintpui.utils.datatables.DataTableRequest;

public abstract class RestApiDao<T> {

	private final Class<T> clazz;

	@Autowired
	private RestOperations client;

	@Autowired
	// used for datatables params
	HttpServletRequest request;

	public RestApiDao(Class<T> clazz) {
		this.clazz = clazz;
	}

	public abstract URI getBaseUrl();

	public T get(URI uri) {
		return client.getForObject(uri, clazz);
	}

	public T get(String id) {
		URI url = UriBuilder.fromUri(getBaseUrl()).path(id).build();
		return client.getForObject(url, clazz);
	}

	public PagedCollection<T> getPage() {
		return getPage(new LinkedHashMap<String, List<String>>());
	}

//	public PagedCollection<T> getPage(int page) {
//		return getPage(page, DataTables.getDatatableParameters(request));
//	}

//	public PagedCollection<T> getPage(int page, Map<String, List<String>> params) {
//		return getPage(getBaseUrl(), page, params);
//	}

//	public PagedCollection<T> getPage(URI uri, int page) {
//		return getPage(uri, page, DataTables.getDatatableParameters(request));
//	}

	private PagedCollection<T> getPage(URI uri, int page, LinkedHashMap<String, List<String>> params) {
		return getPage(uri, null, page, params);
	}

//	public PagedCollection<T> getPage(Integer pageSize, int page) {
//		return getPage(getBaseUrl(), pageSize, page);
//	}

//	public PagedCollection<T> getPage(URI uri, Integer pageSize, int page) {
//		return getPage(uri, pageSize, page, DataTables.getDatatableParameters(request));
//	}

	private PagedCollection<T> getPage(URI uri, Integer pageSize, int page,
			LinkedHashMap<String, List<String>> params) {
		params.put("page", new ArrayList<String>() {
			{
				add(page + "");
			}
		});
		if (null != pageSize)
			params.put("page_size", new ArrayList<String>() {
				{
					add(pageSize.toString());
				}
			});

		return getPage(uri, params);
	}

	public PagedCollection<T> getPage(LinkedHashMap<String, List<String>> params) {
		return getPage(getBaseUrl(), params);
	}

	public PagedCollection<T> getPage(URI uri) {
		return getPage(uri, new LinkedHashMap<String, List<String>>());
	}

	public PagedCollection<T> getPage(URI uri, LinkedHashMap<String, List<String>> params) {
		UriBuilder builder = UriBuilder.fromUri(uri);

		if (null == params)
			params = new LinkedHashMap<String, List<String>>();

		// add datatable params
		params.putAll(new DataTableRequest(request).getApiParameters());

		// set params
		for (String key : params.keySet())
			builder = builder.queryParam(key, params.get(key).toArray());

		URI url = builder.build();

		ResponseEntity<String> response = client.getForEntity(url, String.class);

		ObjectMapper mapper = new ObjectMapper();
		PagedCollection<T> page = null;

		try {
			page = mapper.readValue(response.getBody(),
					mapper.getTypeFactory().constructParametricType(PagedCollection.class, clazz));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return page;
	}

	public Long getCount(URI uri, LinkedHashMap<String, List<String>> params) {
		UriBuilder builder = UriBuilder.fromUri(uri);

		if (null == params) {
			params = new LinkedHashMap<String, List<String>>();
		}

		for (String key : params.keySet()) {
			builder = builder.queryParam(key, params.get(key).toArray());
		}

		URI url = builder.build();

		ResponseEntity<Long> response = client.getForEntity(url, Long.class);

		return response.getBody();
	}

	public T[] getAll(URI uri, LinkedHashMap<String, List<String>> params) {
		int i = 0;
		PagedCollection<T> page = getPage(uri, ++i, params);

		List<T> items = new ArrayList<T>();
		if (null != page) {
			T[] pageItems = page.getItems();
			if (null != pageItems)
				items.addAll(Arrays.asList(pageItems));

			while (null != page && page.hasMore()) {
				page = getPage(uri, ++i, params);
				if (null != page) {
					pageItems = page.getItems();
					if (null != pageItems)
						items.addAll(Arrays.asList(pageItems));
				}
			}
		}
		return (T[]) items.toArray((T[]) Array.newInstance(clazz, 0));
	}

	public T[] getAll(LinkedHashMap<String, List<String>> params) {
		return getAll(getBaseUrl(), params);
	}

	public T[] getAll(URI uri) {
		return getAll(uri, new LinkedHashMap<String, List<String>>());
	}

	// gets all from paged list
	public T[] getAll() {
		return getAll(getBaseUrl());
	}

	public T[] getList() {
		return getList(getBaseUrl(), clazz);
	}

	public T[] getList(String id) {
		URI url = UriBuilder.fromUri(getBaseUrl()).path(id).build();
		return getList(url, clazz);
	}

	public T[] getList(URI uri) {
		return getList(uri, clazz);
	}

	// gets simple list
	public <U> U[] getList(URI uri, Class<U> clazz) {
		U[] array = (U[]) Array.newInstance(clazz, 0);
		return (U[]) getObject(uri, array.getClass());
		// return client.getForObject(uri, (Class<U[]>)((U[])Array.newInstance(clazz,
		// 0)).getClass());
	}

	public <U> U getObject(URI uri, Class<U> clazz) {
		return client.getForObject(uri, clazz);
	}

	public ResponseEntity<String> post(T entity) {
		return post(getBaseUrl(), entity);
	}

	public ResponseEntity<String> post(URI uri, Object entity) {
		return client.postForEntity(uri, entity, String.class);
	}

	public ResponseEntity<String> post(T[] entities) {
		return post(getBaseUrl(), entities);
	}

	public ResponseEntity<String> post(URI uri, T[] entities) {
		return client.postForEntity(uri, entities, String.class);
	}

	public ResponseEntity<String> put(String id, T entity) {
		URI url = UriBuilder.fromUri(getBaseUrl()).path(id).build();
		return put(url, entity);
	}

	public ResponseEntity<String> put(URI uri, T entity) {
		HttpEntity<T> httpEntity = new HttpEntity<T>(entity);
		return client.exchange(uri, HttpMethod.PUT, httpEntity, String.class);
	}
	
	public ResponseEntity<String> patch(String id, T entity) {
		URI url = UriBuilder.fromUri(getBaseUrl()).path(id).build();
		return patch(url, entity);
	}

	public ResponseEntity<String> patch(URI uri, T entity) {
		HttpEntity<T> httpEntity = new HttpEntity<T>(entity);
		return client.exchange(uri, HttpMethod.PATCH, httpEntity, String.class);
	}

	public ResponseEntity<String> delete(String id) {
		URI url = UriBuilder.fromUri(getBaseUrl()).path(id).build();
		return delete(url);
	}

	public ResponseEntity<String> delete(URI uri) {
		return delete(uri, null);
	}

	public ResponseEntity<String> delete(URI uri, Object requestObject) {
		return delete(uri, new HttpEntity<Object>(requestObject));
	}

	private ResponseEntity<String> delete(URI uri, HttpEntity<?> requestObject) {
		return client.exchange(uri, HttpMethod.DELETE, requestObject, String.class);
	}
}
