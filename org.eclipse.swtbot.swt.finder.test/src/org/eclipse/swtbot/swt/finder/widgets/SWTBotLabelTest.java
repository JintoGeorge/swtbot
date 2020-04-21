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

import org.eclipse.swt.SWT;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotLabelTest extends AbstractControlExampleTest {

	@Test
	public void findsLabel() throws Exception {
		bot.label("One");
	}

	@Test
	public void findsAlignment() throws Exception {
		assertEquals(SWT.LEFT, bot.label("One").alignment());
		bot.radio("Center").click();
		assertEquals(SWT.CENTER, bot.label("One").alignment());
		bot.radio("Right").click();
		assertEquals(SWT.RIGHT, bot.label("One").alignment());
	}

	@Test
	public void getsImage() throws Exception {
		assertNotNull(bot.label("").image());
	}

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("Label").activate();
		bot.checkBox("Horizontal Fill").select();
	}

}
