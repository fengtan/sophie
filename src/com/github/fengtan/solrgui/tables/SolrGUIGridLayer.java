package com.github.fengtan.solrgui.tables;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultGridLayer;

import com.github.fengtan.solrgui.solr.SolrGUIServer;

public class SolrGUIGridLayer extends DefaultGridLayer {

	public SolrGUIGridLayer(SolrGUIServer server) {
        super(true);
        
        IDataProvider bodyDataProvider = new SolrGUIDataProvider(server);
        IDataProvider columnHeaderDataProvider = new SolrGUIColumnHeaderDataProvider(server);
        IDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyDataProvider);
        IDataProvider cornerDataProvider = new DefaultCornerDataProvider(columnHeaderDataProvider, rowHeaderDataProvider);
        
        /* TODO drop (unless we want to customize layers)
        IUniqueIndexLayer bodyDataLayer = new DataLayer(bodyDataProvider);
        IUniqueIndexLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
        IUniqueIndexLayer rowHeaderDataLayer = new DefaultRowHeaderDataLayer(rowHeaderDataProvider);
        IUniqueIndexLayer cornerDataLayer = new DataLayer(cornerDataProvider);
        
        init(bodyDataLayer, columnHeaderDataLayer, rowHeaderDataLayer, cornerDataLayer);
        */
        
        init(bodyDataProvider, columnHeaderDataProvider, rowHeaderDataProvider, cornerDataProvider);
    }
	
}
