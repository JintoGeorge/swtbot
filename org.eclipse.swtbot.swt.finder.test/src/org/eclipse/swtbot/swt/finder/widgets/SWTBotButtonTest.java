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
package org.eclipse.swtbot.swt.finder.widgets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotButtonTest extends AbstractControlExampleTest {

	@Test
	public void findsButtons() throws Exception {
		assertEquals("One", bot.button("One").getText());
		assertEquals("Select Listeners", bot.button("Select Listeners").getText());
	}

	@Test
	public void clicksButtons() throws Exception {
		SWTBotShell shell2 = null;
		try {
			SWTBotButton button = bot.button("Set/Get API");
			button.click();
			shell2 = bot.shell("Button Set/Get API");
			assertNotNull(shell2.widget);
			assertEquals("Button Set/Get API", shell2.getText());
		} finally {
			if ((shell2 != null) && (shell2.widget != null))
				shell2.close();
		}
	}

	@Test
	public void findsBackgroundColor() throws Exception {
		assertNotNull(bot.button("One").backgroundColor());
	}

	@Test
	public void findsForegroundColor() throws Exception {
		assertNotNull(bot.button("One").foregroundColor());
	}

	@Before
	public void setUp() throws Exception {
		bot.tabItem("Button").activate();
		bot.radio("SWT.PUSH").click();
	}

}
