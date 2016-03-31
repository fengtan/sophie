package com.github.fengtan.solrgui.tables;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.selection.config.RowOnlySelectionBindings;
import org.eclipse.nebula.widgets.nattable.ui.menu.HeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;
import org.eclipse.swt.widgets.Composite;

import com.github.fengtan.solrgui.solr.SolrGUIServer;

public class SolrGUITable extends NatTable {

	public SolrGUITable(Composite parent, SolrGUIServer server) {
		super(parent, new SolrGUIGridLayer(server), false);
		addConfiguration(new DefaultNatTableStyleConfiguration());
		addConfiguration(new HeaderMenuConfiguration(this) {
			@Override
			protected PopupMenuBuilder createColumnHeaderMenu(NatTable natTable) {
				return super.createColumnHeaderMenu(natTable).withCategoriesBasedColumnChooser("Choose columns");
			}
		});
		addConfiguration(new RowOnlySelectionBindings());
		// TODO right click + select 'choose columns' seems to do nothing
		configure();
	}

}
