package com.github.fengtan.solrgui.tables;

import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.selection.config.RowOnlySelectionBindings;
import org.eclipse.nebula.widgets.nattable.ui.menu.HeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;
import org.eclipse.swt.widgets.Composite;

import com.github.fengtan.solrgui.solr.SolrGUIServer;

// TODO ability to freeze panes
public class SolrGUITable extends NatTable {

	public SolrGUITable(Composite parent, SolrGUIServer server) {
		super(parent, new SolrGUIGridLayer(server), false);

		// Default styles.
		addConfiguration(new DefaultNatTableStyleConfiguration());
		
		// Allow to show/hide columns.
		addConfiguration(new HeaderMenuConfiguration(this) {
			@Override
			protected PopupMenuBuilder createColumnHeaderMenu(NatTable natTable) {
				// TODO right click + select 'choose columns' seems to do nothing
				return super.createColumnHeaderMenu(natTable).withCategoriesBasedColumnChooser("Choose columns");
			}
		});
		
		// Select full row.
		addConfiguration(new RowOnlySelectionBindings());
		
		configure();
	}

}
