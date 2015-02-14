package tableviewer;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.swt.widgets.TableItem;

public class SolrGUICellModifier implements ICellModifier {
	private SolrGUI solrGUI;
	
	public SolrGUICellModifier(SolrGUI solrGUI) {
		super();
		this.solrGUI = solrGUI;
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object, java.lang.String)
	 */
	public boolean canModify(Object element, String property) {
		return true;
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object, java.lang.String)
	 */
	public Object getValue(Object element, String property) {

		// Find the index of the column
		int columnIndex = solrGUI.getColumnNames().indexOf(property);

		Object result = null;
		SolrGUIDocument document = (SolrGUIDocument) element;

		switch (columnIndex) {
			case 0 :
				result = new Boolean(document.isModified());
				break;
			case 1 : // DESCRIPTION_COLUMN 
				result = document.getDescription();
				break;
			case 2 : // OWNER_COLUMN 
				String stringValue = document.getOwner();
				String[] choices = solrGUI.getChoices(property);
				int i = choices.length - 1;
				while (!stringValue.equals(choices[i]) && i > 0)
					--i;
				result = new Integer(i);					
				break;
			case 3 : // PERCENT_COLUMN 
				result = document.getPercentComplete() + "";
				break;
			default :
				result = "";
		}
		return result;	
	}

	/**
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	public void modify(Object element, String property, Object value) {	

		// Find the index of the column 
		int columnIndex	= solrGUI.getColumnNames().indexOf(property);
			
		TableItem item = (TableItem) element;
		SolrGUIDocument document = (SolrGUIDocument) item.getData();
		String valueString;

		switch (columnIndex) {
			case 0 : 
			    document.setModified(((Boolean) value).booleanValue());
				break;
			case 1 : // DESCRIPTION_COLUMN 
				valueString = ((String) value).trim();
				document.setDescription(valueString);
				break;
			case 2 : // OWNER_COLUMN 
				valueString = solrGUI.getChoices(property)[((Integer) value).intValue()].trim();
				if (!document.getOwner().equals(valueString)) {
					document.setOwner(valueString);
				}
				break;
			case 3 : // PERCENT_COLUMN
				valueString = ((String) value).trim();
				if (valueString.length() == 0)
					valueString = "0";
				document.setPercentComplete(Integer.parseInt(valueString));
				break;
			default :
			}
		solrGUI.getTaskList().documentChanged(document);
	}
}