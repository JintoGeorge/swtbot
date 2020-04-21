/*******************************************************************************
 * Copyright (c) 2008,2010 Ketan Padegaonkar and others.
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
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.pass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotRadioTest extends AbstractControlExampleTest {

	private long	oldTimeout;
	private SWTBotText	listeners;

	@Before
	public void lowerTimeout() {
		this.oldTimeout = SWTBotPreferences.TIMEOUT;
		SWTBotPreferences.TIMEOUT = 2000;
	}

	@After
	public void resetTimeout() {
		SWTBotPreferences.TIMEOUT = oldTimeout;
	}

	@Test
	public void clicksCheckBox() throws Exception {
		bot.radio("Three").click();
		listeners.setText("");

		SWTBotRadio radio = bot.radio("One");
		radio.click();
		assertEventMatches(listeners, "Activate [26]: ShellEvent{Button {One} time=557343275 data=null doit=true}");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Button {One} time=557343275 data=null button=1 stateMask=" + toStateMask(0, radio.widget) + " x=31 y=13 count=1}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Button {One} time=557343275 data=null button=1 stateMask=" + toStateMask(524288, radio.widget) + " x=31 y=12 count=1}");
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Button {One} time=557343275 data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(0, radio.widget) + " text=null doit=true}");
	}

	@Test
	public void clickingOneRadioDeselectsOthers() throws Exception {
		bot.radio("One").click();
		listeners.setText("");
		SWTBotRadio radio = bot.radio("Two");
		radio.click();
		assertEventMatches(listeners, "Deactivate [27]: ShellEvent{Button {One} time=557347515 data=null doit=true}");
		assertEventMatches(listeners, "Activate [26]: ShellEvent{Button {Two} time=557347515 data=null doit=true}");
		assertEventMatches(listeners, "MouseDown [3]: MouseEvent{Button {Two} time=557347515 data=null button=1 stateMask=" + toStateMask(0, radio.widget) + " x=16 y=13 count=1}");
		assertEventMatches(listeners, "MouseUp [4]: MouseEvent{Button {Two} time=557347515 data=null button=1 stateMask=" + toStateMask(524288, radio.widget) + " x=16 y=13 count=1}");
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Button {One} time=557347515 data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(0, radio.widget) + " text=null doit=true}");
		assertEventMatches(listeners, "Selection [13]: SelectionEvent{Button {Two} time=557347515 data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(0, radio.widget) + " text=null doit=true}");
	}

	@Test
	public void doesNotMatchCheckboxButtons() throws Exception {
		try {
			assertNull(bot.radio("SWT.FLAT").widget);
			fail("Expecting WidgetNotFoundException");
		} catch (WidgetNotFoundException e) {
			pass();
		}
		try {
			assertNull(bot.radio("Horizontal Fill").widget);
			fail("Expecting WidgetNotFoundException");
		} catch (WidgetNotFoundException e) {
			pass();
		}
	}

	@Test
	public void doesNotMatchRegularButtons() throws Exception {
		try {
			assertNull(bot.radio("Clear").widget);
			fail("Expecting WidgetNotFoundException");
		} catch (WidgetNotFoundException e) {
			pass();
		}
		try {
			assertNull(bot.radio("Select Listeners").widget);
			fail("Expecting WidgetNotFoundException");
		} catch (WidgetNotFoundException e) {
			pass();
		}
	}

	@Test
	public void isSelectedWorks() throws Exception {
		bot.radio("SWT.PUSH").click();
		assertTrue(bot.radio("SWT.PUSH").isSelected());
		assertFalse(bot.radio("SWT.ARROW").isSelected());
	}

	@Test
	public void clicksRadioButtons() throws Exception {
		bot.radio("SWT.CHECK").click();
		assertFalse(bot.radio("SWT.PUSH").isSelected());
		assertFalse(bot.radio("SWT.TOGGLE").isSelected());
		assertFalse(bot.radio("SWT.ARROW").isSelected());

		bot.radio("SWT.PUSH").click();
		assertFalse(bot.radio("SWT.CHECK").isSelected());
	}

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("Button").activate();
		bot.radio("SWT.RADIO").click();
		listeners = bot.textInGroup("Listeners");
		bot.checkBox("Listen").select();
		bot.button("Clear").click();
	}

}
