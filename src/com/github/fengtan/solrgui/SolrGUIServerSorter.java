package com.github.fengtan.solrgui;
import org.apache.solr.common.SolrDocument;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class SolrGUIServerSorter extends ViewerSorter {

	/**
	 * Constructor argument values that indicate to sort items by 
	 * description, owner or percent complete.
	 */
	/* TODO drop
	public final static int DESCRIPTION 		= 1;
	public final static int OWNER 				= 2;
	public final static int PERCENT_COMPLETE 	= 3;
	*/

	// Criteria that the instance uses 
	private int criteria;

	/**
	 * Creates a resource sorter that will use the given sort criteria.
	 *
	 * @param criteria the sort criterion to use: one of <code>NAME</code> or 
	 *   <code>TYPE</code>
	 */
	public SolrGUIServerSorter(int criteria) {
		super();
		this.criteria = criteria;
	}

	public int compare(Viewer viewer, Object o1, Object o2) {
		SolrDocument document1 = (SolrDocument) o1;
		SolrDocument document2 = (SolrDocument) o2;

		/* TODO drop
		switch (criteria) {
			case DESCRIPTION :
				return compareDescriptions(document1, document2);
			case OWNER :
				return compareOwners(document1, document2);
			case PERCENT_COMPLETE :
				return comparePercentComplete(document1, document2);
			default:
				return 0;
		}
		*/
		return 0;
	}

	/**
	 * Returns the sort criteria of this this sorter.
	 *
	 * @return the sort criterion
	 */
	public int getCriteria() {
		return criteria;
	}
}