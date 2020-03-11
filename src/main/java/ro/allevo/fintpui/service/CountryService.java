package ro.allevo.fintpui.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class CountryService {
	public Map<String, String> getAllCountries() {
		HashMap<String, String> countries = new LinkedHashMap<String, String>();
		String[] countryCodes = Locale.getISOCountries();
		Locale locale = null;
		for (String countryCode : countryCodes) {
			locale = new Locale("", countryCode);
			countries.put(locale.getCountry(), locale.getDisplayCountry());
		}
		return (HashMap<String, String>) sortByValue(countries);
	}
	
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
				return (e1.getValue()).compareTo(e2.getValue());
			}
		});
	 	Map<K, V> result = new LinkedHashMap<>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
}
