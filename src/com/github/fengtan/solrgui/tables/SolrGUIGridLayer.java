package com.github.fengtan.solrgui.tables;

import org.apache.solr.common.SolrDocument;
import org.eclipse.nebula.widgets.nattable.data.IColumnAccessor;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.extension.glazedlists.GlazedListsDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultGridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultRowHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;

import ca.odell.glazedlists.EventList;

import com.github.fengtan.solrgui.solr.SolrGUIServer;

public class SolrGUIGridLayer extends DefaultGridLayer {

	public SolrGUIGridLayer(SolrGUIServer server) {
        super(true);
        
        EventList<SolrDocument> list = null;
        IColumnAccessor<SolrDocument> columnAccessor = null;
        IDataProvider bodyDataProvider = new GlazedListsDataProvider<>(list, columnAccessor);
        IDataProvider columnHeaderDataProvider = new SolrGUIColumnHeaderDataProvider(server);
        IDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyDataProvider);
        IDataProvider cornerDataProvider = new DefaultCornerDataProvider(columnHeaderDataProvider, rowHeaderDataProvider);
        
        DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
        DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
        DataLayer rowHeaderDataLayer = new DefaultRowHeaderDataLayer(rowHeaderDataProvider);
        DataLayer cornerDataLayer = new DataLayer(cornerDataProvider);
        
        // TODO sorting should leverage Solr features  
        init(bodyDataLayer, columnHeaderDataLayer, rowHeaderDataLayer, cornerDataLayer);
    }
	
}
