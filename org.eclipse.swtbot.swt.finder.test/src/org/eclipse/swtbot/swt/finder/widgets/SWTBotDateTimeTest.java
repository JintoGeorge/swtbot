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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.hamcrest.core.AnyOf;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotDateTimeTest extends AbstractControlExampleTest {

	private SWTBotDateTime	dateTime;

	@Test
	public void findsDateTime() throws Exception {
		assertEquals(DateTime.class, dateTime.widget.getClass());
	}

	@Test
	public void setsAndGetsDateOnDateTime() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(0);
		calendar.set(2012, 10, 20, 0, 0, 0);
		Date expected = calendar.getTime();
		assertFalse(expected.equals(dateTime.getDate()));
		dateTime.setDate(expected);
		Date actual = dateTime.getDate();
		assertEquals(expected, actual);
	}

	@Test
	public void shouldHandleInvalidDates() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(0);
		calendar.set(2011, 1, 3, 0, 0, 0);
		Date expected = calendar.getTime();
		assertFalse(expected.equals(dateTime.getDate()));
		dateTime.setDate(expected);

		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(0);
		calendar.set(2010, 11, 31, 0, 0, 0);
		dateTime.setDate(calendar.getTime());
		assertEquals(calendar.getTime(), dateTime.getDate());
	}

	@Test
	public void bug347824() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(0);
		calendar.set(2011, Calendar.MAY, 31, 0, 0, 0);
		Date expected = calendar.getTime();
		assertFalse(expected.equals(dateTime.getDate()));
		dateTime.setDate(expected);
		assertEquals(expected, dateTime.getDate());

		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(0);
		calendar.set(2010, Calendar.SEPTEMBER, 28, 0, 0, 0);
		dateTime.setDate(calendar.getTime());
		assertEquals(calendar.getTime(), dateTime.getDate());
	}

	@Test
	public void sendsNotification() throws Exception {
		bot.checkBox("Listen").click();
		SWTBotDateTime dateTime = bot.dateTimeInGroup("DateTime");
		dateTime.setDate(new Date());
		// FIXME https://bugs.eclipse.org/bugs/show_bug.cgi?id=206715
		// FIXED > 071107 for all platforms.
		String expectedLinux = "Selection [13]: SelectionEvent{DateTime";
		String expectedWindows = "Selection [13]: SelectionEvent{DateTime {DateTime";
		String text = bot.textInGroup("Listeners").getText();
		assertThat(text, AnyOf.anyOf(containsString(expectedLinux), containsString(expectedWindows)));
		assertThat(text, containsString(" data=null item=null detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(0, dateTime.widget) +" text=null doit=true}"));
	}

	@Before
	public void prepareExample() throws Exception {
		bot.tabItem("DateTime").activate();
		dateTime = bot.dateTimeInGroup("DateTime");
	}
}
