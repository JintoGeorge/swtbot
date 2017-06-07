/*******************************************************************************
 * Copyright (c) 2008, 2016 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Mickael Istria (Red Hat Inc.) - Note about bug 437915
 *     Patrick Tasse - SWTBotView does not support dynamic view menus (Bug 489325)
 *******************************************************************************/
package org.eclipse.swtbot.eclipse.finder.finders;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.SubContributionItem;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotViewMenu;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.ListResult;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotRootMenu;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.menus.CommandContributionItem;
import org.hamcrest.Matcher;

/**
 * Finds the menu items within a view.
 *
 * @author @author Stephen Paulin &lt;paulin [at] spextreme [dot] com&gt;
 * @version $Id$
 * @since 1.2
 * @deprecated Use {@link SWTBotView#viewMenu()} and
 *             {@link SWTBotRootMenu#menu(String...)} to get a
 *             {@link SWTBotMenu} instead.
 */
@Deprecated
public class ViewMenuFinder {
	/**
	 * The logging instance for this class.
	 */
	private static final Logger	log	= Logger.getLogger(ViewMenuFinder.class);

	/**
	 * Creates a MenuFinder.
	 */
	public ViewMenuFinder() {
		// Do nothing.
	}

	/**
	 * Gets a list of all menus within the view.
	 *
	 * @param view the view to probe for menus.
	 * @param matcher the matcher that can match menus and menu items.
	 * @param recursive if set to <code>true</code>, will find sub-menus as well.
	 * @return The list of menus (IContributionItems) that match the matcher.
	 * @since 2.0
	 */
	public List<SWTBotViewMenu> findMenus(final IViewReference view, final Matcher<?> matcher, final boolean recursive) {
		return UIThreadRunnable.syncExec(new ListResult<SWTBotViewMenu>() {

			@Override
			public List<SWTBotViewMenu> run() {
				List<SWTBotViewMenu> l = new ArrayList<SWTBotViewMenu>();
                IViewPart viewPart = view.getView(false);
                if (viewPart == null) {
                   return l;
                }
               IMenuManager viewMenuManager = viewPart.getViewSite().getActionBars().getMenuManager();
               l.addAll(getMenuItemsInternal(viewMenuManager.getItems(), matcher, recursive));
               return l;
			}
		});
	}

	// This is expected to be called from within the UI thread. If not it will throw
	// exceptions based on invalid thread access.
	private List<SWTBotViewMenu> getMenuItemsInternal(IContributionItem[] items, Matcher<?> matcher, boolean recursive) {
		List<SWTBotViewMenu> l = new ArrayList<SWTBotViewMenu>();

		for (int i = 0; i < items.length; i++) {
			IContributionItem item = items[i];

			try {
				if ((item instanceof MenuManager) && recursive) {
					// Sub menus
					MenuManager menuManager = (MenuManager) item;

					l.addAll(getMenuItemsInternal(menuManager.getItems(), matcher, recursive));
				} else {
					SWTBotViewMenu menu = getMenuItem(item, matcher);
					if (menu != null)
						l.add(menu);
				}
			} catch (WidgetNotFoundException e) {
				log.warn(e);
			}
		}

		return l;
	}
	
	private SWTBotViewMenu  getMenuItem(IContributionItem item, Matcher<?> matcher) {
		SWTBotViewMenu menu = null;
		if (item instanceof ActionContributionItem) {
			ActionContributionItem actionContribution = (ActionContributionItem) item;
			if (matcher.matches(actionContribution.getAction()))
				menu = new SWTBotViewMenu(actionContribution);
		} else if (item instanceof SubContributionItem ) {
			    menu = getMenuItem(((SubContributionItem) item).getInnerItem(), matcher);
		} else if (item instanceof CommandContributionItem) {
			CommandContributionItem cmdContribution = (CommandContributionItem) item;
			if (matcher.matches(new CommandItemWithTextMatcherWrapper(cmdContribution)))
				menu = new SWTBotViewMenu(cmdContribution.getCommand());
		} /* else if (item instanceof HandledContributionItem) {
			Bug 437915:
			The HandledContributionItem is used instead of other ones on e4 when using
			a ParameterizedCommand. It's part of e4 renderer.
			However, we prefer this bundle to not depend on e4 for that part yet.
			We should either find a good way to retrive contribution info (commandId)
			from a simple IContributionItem, or just accept the idea of dropping pure-e3
			support
		} */
		return menu;
	}
	
	/* This class should be public at it will be accessed outside of its parent class*/
	public static class CommandItemWithTextMatcherWrapper {
		
		private CommandContributionItem wrappedCommandItem;
		
		public CommandItemWithTextMatcherWrapper(CommandContributionItem item) {
			this.wrappedCommandItem = item;
		}
		
		/*
		 * This method will be called reflectively by a matcher.
		 */
		public String getText() throws Exception {
				/* label attribute of command contribution item is not available */
				String label = (String) SWTUtils.getAttribute(wrappedCommandItem, "label"); ////$NON-NLS-1$
				return label != null ? label:""; ////$NON-NLS-1$
		}
	}
}
