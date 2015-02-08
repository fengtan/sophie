package com.github.fengtan.solrgui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Maintain 2 lists: 1 for column titles + 1 for display status (displayed/hidden).
 */
public class SolrGUIColumnList implements List<String> {

	private List<String> titles = new ArrayList<String>();
	private List<Boolean> displayed = new ArrayList<Boolean>();
	
	@Override
	public int size() {
		return titles.size();
	}
	@Override
	public boolean isEmpty() {
		return titles.isEmpty();
	}
	@Override
	public boolean contains(Object o) {
		return titles.contains(o);
	}
	@Override
	public Iterator<String> iterator() {
		return titles.iterator();
	}
	@Override
	public Object[] toArray() {
		return titles.toArray();
	}
	@Override
	public boolean remove(Object o) {
		return titles.remove(o);
	}
	@Override
	public void clear() {
		titles.clear();
		displayed.clear();
		
	}
	@Override
	public <T> T[] toArray(T[] a) {
		return titles.toArray(a);
	}
	@Override
	public boolean add(String e) {
		displayed.add(true);
		return titles.add(e);
	}
	@Override
	public boolean containsAll(Collection<?> c) {
		return titles.containsAll(c);
	}
	@Override
	public boolean addAll(Collection<? extends String> c) {
		return titles.addAll(c);
	}
	@Override
	public boolean addAll(int index, Collection<? extends String> c) {
		return titles.addAll(index, c);
	}
	@Override
	public boolean removeAll(Collection<?> c) {
		return titles.removeAll(c);
	}
	@Override
	public boolean retainAll(Collection<?> c) {
		return titles.retainAll(c);
	}
	@Override
	public String get(int index) {
		return titles.get(index);
	}
	@Override
	public String set(int index, String element) {
		return titles.set(index, element);
	}
	@Override
	public void add(int index, String element) {
		titles.add(index, element);
	}
	@Override
	public String remove(int index) {
		return titles.remove(index);
	}
	@Override
	public int indexOf(Object o) {
		return titles.indexOf(o);
	}
	@Override
	public int lastIndexOf(Object o) {
		return titles.lastIndexOf(o);
	}
	@Override
	public ListIterator<String> listIterator() {
		return titles.listIterator();
	}
	@Override
	public ListIterator<String> listIterator(int index) {
		return titles.listIterator(index);
	}
	@Override
	public List<String> subList(int fromIndex, int toIndex) {
		return titles.subList(fromIndex, toIndex);
	}
	
	public List<String> getItemsDisplayed() {
		List<String> items = new ArrayList<String>();
		for (int index=0; index<titles.size(); index++) {
			if (displayed.get(index)) {
				items.add(titles.get(index));
			}
		}
		return items;
	}
	
	public boolean isItemDisplayed(String title) {
		int index = titles.indexOf(title);
		if (index == -1) {
			return false;
		}
		return displayed.get(index);
	}
	
	public void setItemDisplayed(String title, boolean isDisplayed) {
		int index = titles.indexOf(title);
		if (index != -1) {
			displayed.set(index, isDisplayed);
		}
	}
	
	public int getIndex(String title) {
		return titles.indexOf(title);
	}
	
	public int getIndexDisplayed(String title) {
		return getItemsDisplayed().indexOf(title);
	}
	
}
