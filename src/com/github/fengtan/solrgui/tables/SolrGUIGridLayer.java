package com.github.fengtan.solrgui.tables;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultGridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultRowHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;

import com.github.fengtan.solrgui.solr.SolrGUIServer;

public class SolrGUIGridLayer extends DefaultGridLayer {

	public SolrGUIGridLayer(SolrGUIServer server) {
        super(true);
        
        IDataProvider bodyDataProvider = new SolrGUIDataProvider(server);
        IDataProvider columnHeaderDataProvider = new SolrGUIColumnHeaderDataProvider(server);
        IDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyDataProvider);
        IDataProvider cornerDataProvider = new DefaultCornerDataProvider(columnHeaderDataProvider, rowHeaderDataProvider);
        
        DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
        DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
        DataLayer rowHeaderDataLayer = new DefaultRowHeaderDataLayer(rowHeaderDataProvider);
        DataLayer cornerDataLayer = new DataLayer(cornerDataProvider);
        
        // TODO what if too many columns - horizontal scrollbar ?
        // Make table expand the full width of the window. TODO drop ?
        //bodyDataLayer.setColumnPercentageSizing(true);
        
        init(bodyDataLayer, columnHeaderDataLayer, rowHeaderDataLayer, cornerDataLayer);
    }
	
}
