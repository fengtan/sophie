package com.github.fengtan.solrgui.tables;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.eclipse.nebula.widgets.nattable.sort.ISortModel;
import org.eclipse.nebula.widgets.nattable.sort.SortDirectionEnum;

/**
 * Empty sort model - sorting is handled on the Solr side.
 */
public class SolrGUISortModel implements ISortModel {
		
	// Index of sorted column ; -1 if unsorted
	// TODO support cumulative sort + README "default mouse bindings for triggering sorting (Alt + left click, Alt + Shift + left click for additive sort)"
	private int columnIndexSorted = -1;
	private SortDirectionEnum direction = SortDirectionEnum.NONE;
	private SolrGUIDataProvider dataProvider;
	
	// TODO should implement an observable pattern / lose coupling
	public SolrGUISortModel(SolrGUIDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}
	
	@Override
	public void clear() {
		this.columnIndexSorted = -1;
		this.direction = SortDirectionEnum.NONE;
	}

	@Override
	public Comparator<?> getColumnComparator(int columnIndex) {
		return null;
	}

	@Override
	public List<Comparator> getComparatorsForColumnIndex(int columnIndex) {
		return null;
	}

	@Override
	public SortDirectionEnum getSortDirection(int columnIndex) {
		if (columnIndex != columnIndexSorted) {
			return SortDirectionEnum.NONE;
		}
		return direction;
	}

	@Override
	public int getSortOrder(int columnIndex) {
		return (columnIndex != columnIndexSorted) ? -1 : 1;
	}

	@Override
	public List<Integer> getSortedColumnIndexes() {
		return Arrays.asList(columnIndexSorted);
	}

	@Override
	public boolean isColumnIndexSorted(int columnIndex) {
		return (columnIndex == columnIndexSorted);
	}

	@Override
	public void sort(int columnIndex, SortDirectionEnum direction, boolean accumulate) {
		this.columnIndexSorted = columnIndex;
		this.direction = direction;
		dataProvider.setSort("hash", direction); // TODO "hash"
	}

}
