package com.github.fengtan.solrgui.tables;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.LukeRequest;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultColumnHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultCornerDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.data.DefaultRowHeaderDataProvider;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.CornerLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultRowHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.GridLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.RowHeaderLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.stack.DefaultBodyLayerStack;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.sort.SortHeaderLayer;
import org.eclipse.nebula.widgets.nattable.sort.config.SingleClickSortConfiguration;

public class SolrGUIGridLayer extends GridLayer {
	
	public SolrGUIGridLayer(SolrServer server) {
        super(true);
        
		// TODO what if user adds a new field after the SolrGUIServer object gets created ?
		List<FieldInfo> fields = extractFields(server);
        SolrGUIDataProvider bodyDataProvider = new SolrGUIDataProvider(server, fields);
        String[] columnLabels = new String[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
        	columnLabels[i] = fields.get(i).getName();
        }
        IDataProvider columnHeaderDataProvider = new DefaultColumnHeaderDataProvider(columnLabels);
        IDataProvider rowHeaderDataProvider = new DefaultRowHeaderDataProvider(bodyDataProvider);
        IDataProvider cornerDataProvider = new DefaultCornerDataProvider(columnHeaderDataProvider, rowHeaderDataProvider);
      
        DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);
        DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
        DataLayer rowHeaderDataLayer = new DefaultRowHeaderDataLayer(rowHeaderDataProvider);
        DataLayer cornerDataLayer = new DataLayer(cornerDataProvider);
        
        SortHeaderLayer sortHeaderLayer = new SortHeaderLayer<>(columnHeaderDataLayer, new SolrGUISortModel(bodyDataProvider));
        sortHeaderLayer.addConfiguration(new SingleClickSortConfiguration());

        // TODO sorting should leverage Solr features
		DefaultBodyLayerStack bodyLayer = new DefaultBodyLayerStack(bodyDataLayer);
		SelectionLayer selectionLayer = bodyLayer.getSelectionLayer();
		ILayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, bodyLayer, selectionLayer);
		ILayer rowHeaderLayer = new RowHeaderLayer(rowHeaderDataLayer, bodyLayer, selectionLayer);
		ILayer cornerLayer = new CornerLayer(cornerDataLayer, rowHeaderLayer, columnHeaderLayer);
		
		setBodyLayer(bodyLayer);
		setColumnHeaderLayer(sortHeaderLayer);
		setRowHeaderLayer(rowHeaderLayer);
		setCornerLayer(cornerLayer);
        
    }
	

	// Map field name => field info
	private static List<FieldInfo> extractFields(SolrServer server) {
		try {
			return new ArrayList<FieldInfo>(new LukeRequest().process(server).getFieldInfo().values());
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Collections.EMPTY_LIST;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Collections.EMPTY_LIST;
		}
		/* TODO provide option to use this in case Luke handler is not available? requires at least 1 document in the server
		Collection<String> fields = getAllDocuments().get(0).getFieldNames();
		return fields.toArray(new String[fields.size()]);
		*/
	}
	
}
