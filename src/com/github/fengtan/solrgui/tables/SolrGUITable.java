package com.github.fengtan.solrgui.tables;

import org.apache.solr.client.solrj.SolrServer;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.DefaultNatTableStyleConfiguration;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.config.IEditableRule;
import org.eclipse.nebula.widgets.nattable.edit.EditConfigAttributes;
import org.eclipse.nebula.widgets.nattable.selection.config.RowOnlySelectionBindings;
import org.eclipse.nebula.widgets.nattable.ui.menu.HeaderMenuConfiguration;
import org.eclipse.nebula.widgets.nattable.ui.menu.PopupMenuBuilder;
import org.eclipse.swt.widgets.Composite;

// TODO ability to freeze panes
public class SolrGUITable extends NatTable {

	public SolrGUITable(Composite parent, SolrServer server) {
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
		
		// Make cells editable.
		addConfiguration(new EditConfiguration());

		configure();
	}

}


/*
 * TODO move into SolrGUITable.class ?
 * TODO create drop down lists for booleans ?
 */
class EditConfiguration extends AbstractRegistryConfiguration  {

	public void configureRegistry(IConfigRegistry configRegistry) {
		// TODO should all cells actually be editable ?
		configRegistry.registerConfigAttribute(EditConfigAttributes.CELL_EDITABLE_RULE, IEditableRule.ALWAYS_EDITABLE);

	}
}

