/*******************************************************************************
 * Copyright (c) 2014 SWTBot Committers and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Matt Biggs - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.e4.finder.finders;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.utils.internal.Assert;

/**
 * @deprecated Use org.eclipse.swtbot.swt.finder.finders.ContextMenuFinder instead
 */
@Deprecated
public class ContextMenuFinder extends MenuFinder {
	/**
	 * The control to find context menus.
	 */
	private final Control	control;

	/**
	 * Constructs the context menu finder for the given control to be searched.
	 *
	 * @param control the control that has a context menu.
	 */
	public ContextMenuFinder(Control control) {
		super();
		Assert.isNotNull(control, "The control cannot be null"); //$NON-NLS-1$
		this.control = control;
	}

	/**
	 * Gets the menubar for the given shell.
	 *
	 * @see org.eclipse.swtbot.swt.finder.finders.MenuFinder#menuBar(org.eclipse.swt.widgets.Shell)
	 * @param shell The shell to find the menu bar for.
	 * @return The menu bar found.
	 */
	@Override
	protected Menu menuBar(final Shell shell) {
		return UIThreadRunnable.syncExec(display, new WidgetResult<Menu>() {
			@Override
			public Menu run() {
				return control.getMenu();
			}
		});
	}
}
