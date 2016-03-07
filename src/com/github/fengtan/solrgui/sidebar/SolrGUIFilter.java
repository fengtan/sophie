package com.github.fengtan.solrgui.sidebar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.github.fengtan.solrgui.beans.SolrGUIServer;
import com.github.fengtan.solrgui.tabs.SolrGUITabItem;

// TODO refresh fields when hit button 'refresh' ?
public class SolrGUIFilter extends Composite {

	private static final String LABEL = "Filter by";
	
	private Combo combo;
	private Text text;
	
	public SolrGUIFilter(final Composite parent, SolrGUIServer server, final SolrGUITabItem tabItem) {
		super(parent, SWT.SHADOW_ETCHED_IN);
		setLayout(new FillLayout(SWT.HORIZONTAL));

		combo = new Combo(this, SWT.SIMPLE | SWT.BORDER);
		combo.setItems(server.getFields());
		combo.setText(LABEL);
		
	    text = new Text(this, SWT.BORDER);
	    
	    final SolrGUIFilter filter = this;
	    
	    Label labelMinus = new Label(this, SWT.NULL);
	    Image imgMinus = new Image(getDisplay(), "img/filters-minus.png"); // TODO use another image ?
	    labelMinus.setImage(imgMinus);
	    labelMinus.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseDown(MouseEvent e) {
	    		if (tabItem.getFilters().size() > 1) {
		    		// If there are multiple filters, then remove this filter.
	    			tabItem.removeFilter(filter);
	    		} else {
	    			// If this is the last filter remaining, then reset its value.
	    			filter.reset();
	    		}
	    		super.mouseDown(e);
	    	}
		});
	    
	    Label labelPlus = new Label(this, SWT.NULL);
	    Image imgPlus = new Image(getDisplay(), "img/filters-plus.png"); // TODO use another image ?
	    labelPlus.setImage(imgPlus);
	    labelPlus.addMouseListener(new MouseAdapter() {
	    	@Override
	    	public void mouseDown(MouseEvent e) {
	    		tabItem.addFilter();
	    		super.mouseDown(e);
	    	}
		});
	}
	
	// TODO maximize window + click 'plus' => table gets minimized to original window size
	
	public String getField() {
		int index = combo.getSelectionIndex();
		return (index == -1) ? "" : combo.getItem(index);
	}
	
	public String getValue() {
		return text.getText();
	}
	
	public void reset() {
		combo.setText(LABEL);
		text.setText("");
	}

}
