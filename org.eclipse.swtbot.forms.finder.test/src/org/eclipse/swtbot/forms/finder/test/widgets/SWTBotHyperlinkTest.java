/*******************************************************************************
 * Copyright (c) 2010 Chris Aniszczyk and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Chris Aniszczyk <caniszczyk@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.forms.finder.test.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotAssert.assertMatchesRegex;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withRegex;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.swtbot.forms.finder.widgets.SWTBotHyperlink;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class SWTBotHyperlinkTest extends AbstractSWTBotFormsTest {

	@Test
	public void findHyperlink() throws Exception {
		String text = "This is an example of a form that is much longer and will need to wrap.";
		SWTBotHyperlink link = bot.hyperlink(text);
		assertNotNull(link);
		assertEquals(text, link.getText());
		assertEquals(Hyperlink.class, link.widget.getClass());
	}

	@Test
	public void canFindHyperlinkWithRegex() throws Exception {
		String text = "example of a form .* is much longer";
		SWTBotHyperlink link = new SWTBotHyperlink((Hyperlink) bot.widget(withRegex(text)));
		assertNotNull(link);
		assertMatchesRegex(text, link.getText());
		assertEquals(Hyperlink.class, link.widget.getClass());
	}

}
