/*******************************************************************************
 * Copyright (c) 2008 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Lorenzo Bettini - https://bugs.eclipse.org/bugs/show_bug.cgi?id=464687
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.waits;

import java.util.List;

import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.swt.finder.finders.MenuFinder;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.hamcrest.Matcher;

/**
 * Condition that waits for a shell with the specified text to appear.
 *
 * @see Conditions
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 * @since 2.3
 */
public class WaitForMenu extends WaitForObjectCondition<MenuItem> {

	private final SWTBotShell	shell;
	private final boolean 		recursive;

	/**
	 * @param shell the shell to search for the menu.
	 * @param matcher the matcher used for matching the menu items.
	 */
	public WaitForMenu(SWTBotShell shell, Matcher<MenuItem> matcher) {
		this(shell, matcher, true);
	}

	/**
	 * @param shell the shell to search for the menu.
	 * @param matcher the matcher used for matching the menu items.
	 * @param recursive if set to true, will find submenus as well
	 */
	public WaitForMenu(SWTBotShell shell, Matcher<MenuItem> matcher, boolean recursive) {
		super(matcher);
		this.shell = shell;
		this.recursive = recursive;
	}

	@Override
	public String getFailureMessage() {
		return "Could not find a menu within the shell '" + shell + "' matching '" + matcher + "'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	@Override
	protected List<MenuItem> findMatches() {
		return new MenuFinder().findMenus(shell.widget, matcher, recursive);
	}

}
