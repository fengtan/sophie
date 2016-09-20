package com.github.fengtan.sophie.toolbars;

import org.eclipse.swt.internal.SWTEventListener;

public interface ChangeListener extends SWTEventListener {

	void changed();

	void unchanged();
	
}
