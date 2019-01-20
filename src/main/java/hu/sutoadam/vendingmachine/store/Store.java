package hu.sutoadam.vendingmachine.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Store<T> {
	private Map<T,Integer> items = new HashMap();
	
	public boolean hasItem(T item) {
		Integer quantity = items.get(item);
		return (quantity == null || quantity == 0) ? false : true;
	}
	
	public void addOneQuantity(T item) {
		int quantity = items.get(item);
		items.put(item, quantity + 1);
	}
	
	public void removeOneQuantity(T item) {
		if(hasItem(item)) {
			int quantity = items.get(item);
			items.put(item, quantity - 1);
		}
	}
	
	public void fill(T item,int quantity) {
		items.put(item, quantity);
	}
	
	public void clear() {
		items.clear();
	}
	
	public List<T> getTypes(){
		return items.entrySet()
				.stream()
					.map(entry -> entry.getKey())
				.collect(Collectors.toList());
	}
	
	public T getItem(T item) {
		return items.entrySet()
				.stream()
				.filter(coins -> 
					coins.getKey().equals(item)
				).findFirst().get().getKey();
		
	}
}
