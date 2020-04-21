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

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertText;
import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.pass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swtbot.swt.finder.test.AbstractMenuExampleTest;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class MenuFinderTest extends AbstractMenuExampleTest {

	@Test
	public void clicksMenuItem() throws Exception {
		List<MenuItem> findControls = menuFinder.findMenus(anyMenuItem);
		MenuItem menuItem = findControls.get(1);
		try {
			menuItem.notifyListeners(SWT.Selection, null);
			fail("Expecting an SWTException");
		} catch (SWTException e) {
			pass();
		}
		assertText("&New Contact...	Ctrl+N", menuItem);
	}

	@Test
	public void findsAllVisibleMenus() throws Exception {
		List<MenuItem> findControls = menuFinder.findMenus(anyMenuItem);
		assertEquals(26, findControls.size());
		assertText("&Find...\tCtrl+F", findControls.get(21));
	}

}
