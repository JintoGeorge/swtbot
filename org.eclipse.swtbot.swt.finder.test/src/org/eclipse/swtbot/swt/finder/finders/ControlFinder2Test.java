/*******************************************************************************
 * Copyright (c) 2008 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.finders;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.test.AbstractCustomControlExampleTest;
import org.eclipse.swtbot.swt.finder.utils.TreePath;
import org.hamcrest.Matcher;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class ControlFinder2Test extends AbstractCustomControlExampleTest {

	@Test
	public void findsAllTabItem() throws Exception {
		selectCTabFolder();
		List<CTabItem> tabItems = controlFinder.findControls(widgetOfType(CTabItem.class));
		assertEquals(3, tabItems.size());
	}

	@Test
	public void getsControlPath() throws Exception {
		selectCTabFolder();
		Matcher<CTabItem> withText = withText("CTabItem 1");
		List<CTabItem> labels = controlFinder.findControls(allOf(widgetOfType(CTabItem.class), withText));
		Widget w = labels.get(0);
		TreePath path = controlFinder.getPath(w);
		assertEquals(8, path.getSegmentCount());
	}

	private void selectCTabFolder() {
		UIThreadRunnable.syncExec(display, new VoidResult() {
			@Override
			public void run() {
				List<TabFolder> findControls = controlFinder.findControls(widgetOfType(TabFolder.class));
				TabFolder folder = findControls.get(0);
				folder.setSelection(2);
			}
		});
	}
}
