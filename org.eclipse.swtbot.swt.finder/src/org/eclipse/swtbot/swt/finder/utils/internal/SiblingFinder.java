/*******************************************************************************
 * Copyright (c) 2008, 2015 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Patrick Tasse - support tool item and menu item
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.utils.internal;


import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.results.ArrayResult;

/**
 * Finds the siblings of a widget.
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 */
public final class SiblingFinder implements ArrayResult<Widget> {
	/**
	 * The widget to use.
	 */
	private final Widget	w;

	/**
	 * Constructs the sibling finder with the given widget.
	 *
	 * @param w the widget
	 */
	public SiblingFinder(Widget w) {
		this.w = w;
	}

	/**
	 * Runs the process of finding the siblings.
	 *
	 * @see org.eclipse.swtbot.swt.finder.results.ArrayResult#run()
	 * @return The object found.
	 */
	@Override
	public Widget[] run() {
		Widget[] siblings = new Widget[] {};
		if (isControl(w))
			siblings = children(((Control) w).getParent());
		else if (isTabItem(w))
			siblings = ((TabItem) w).getParent().getItems();
		else if (isToolItem(w))
			siblings = ((ToolItem) w).getParent().getItems();
		else if (isMenuItem(w))
			siblings = ((MenuItem) w).getParent().getItems();
		return siblings;
	}

	/**
	 * Gets the children widgets starting with the given composite.
	 *
	 * @param parent The parent composite.
	 * @return The list of child widgets or an empty list if none.
	 */
	private Widget[] children(Composite parent) {
		if (parent == null)
			return new Widget[] {};
		Control[] children = parent.getChildren();
		return (children == null) ? new Widget[] {} : children;
	}

	/**
	 * Gets if this passed in widget is a control.
	 *
	 * @param w The widget.
	 * @return <code>true</code> if it is a control. Otherwise <code>false</code>.
	 */
	private boolean isControl(Widget w) {
		return w instanceof Control;
	}

	/**
	 * Gets if this is a tab item widget.
	 *
	 * @param w The widget.
	 * @return <code>true</code> if it is a tab item. Otherwise <code>false</code>.
	 */
	private boolean isTabItem(Widget w) {
		return w instanceof TabItem;
	}

	/**
	 * Gets if this is a tool item widget.
	 *
	 * @param w The widget.
	 * @return <code>true</code> if it is a tool item. Otherwise <code>false</code>.
	 */
	private boolean isToolItem(Widget w) {
		return w instanceof ToolItem;
	}

	/**
	 * Gets if this is a menu item widget.
	 *
	 * @param w The widget.
	 * @return <code>true</code> if it is a menu item. Otherwise <code>false</code>.
	 */
	private boolean isMenuItem(Widget w) {
		return w instanceof MenuItem;
	}
}
