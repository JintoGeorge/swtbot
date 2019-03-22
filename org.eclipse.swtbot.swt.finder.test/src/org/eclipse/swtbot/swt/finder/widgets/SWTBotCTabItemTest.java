/*******************************************************************************
 * Copyright (c) 2009, 2019 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotAssert.pass;
import static org.eclipse.swtbot.swt.finder.utils.SWTUtils.getTextPath;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.eclipse.swtbot.swt.finder.test.AbstractCustomControlExampleTest;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotCTabItemTest extends AbstractCustomControlExampleTest {

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("CTabFolder").activate();
		bot.checkBox("SWT.CLOSE").deselect();
		bot.checkBox("SWT.CLOSE").select();
		bot.checkBox("Popup Menu").select();
		bot.checkBox("Listen").select();
		bot.button("Clear").click();
	}

	@Test
	public void canCloseCTabItems() throws Exception {
		SWTBotCTabItem cTabItem = bot.cTabItem("CTabItem 0");
		cTabItem.close();

		SWTBotText listeners = bot.textInGroup("Listeners");
		assertEventMatches(listeners, "CTabFolderEvent: CTabFolderEvent{CTabFolder {} time=185015066 data=null item=CTabItem {CTabItem 0} doit=true x=0 y=0 width=0 height=0}");
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{CTabFolder {} time=185324514 data=null item=CTabItem {CTabItem 1} detail=0 x=0 y=0 width=0 height=0 stateMask=" + AbstractControlExampleTest.toStateMask(0, cTabItem.widget) + " text=null doit=true}");
		assertEventMatches(listeners, "Dispose [12]: DisposeEvent{CTabItem {CTabItem 0} time=185739697 data=null}");

		try{
			bot.cTabItem("CTabItem 0");
			fail("Did not expect to find the ctab item that was just closed");
		}catch (Exception e) {
			pass();
		}
	}

	@Test
	public void canClickContextMenuOnCTabItem() throws Exception {
		SWTBotCTabItem cTabItem = bot.cTabItem("CTabItem 0");
		SWTBotMenu menuItem = cTabItem.contextMenu("Sample popup menu item").hide();
		assertArrayEquals(new String[] { "POP_UP", "Sample popup menu item" }, getTextPath(menuItem.widget));
	}

}
