/*******************************************************************************
 * Copyright (c) 2014 SWTBot Committers and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Matt Biggs - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.e4.finder.waits;

import java.util.List;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swtbot.e4.finder.widgets.WorkbenchContentsFinder;
import org.eclipse.swtbot.swt.finder.waits.WaitForObjectCondition;
import org.hamcrest.Matcher;

/**
 * Waits until a view part that matches the specified matcher appears.
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @author Ralf Ebert www.ralfebert.de (bug 271630)
 * @version $Id$
 */
public class WaitForPart extends WaitForObjectCondition<MPart> {

	private final IEclipseContext context;

	/**
	 * Creates a condition that waits until the matcher is true.
	 *
	 * @param matcher the matcher
	 */
	WaitForPart(final IEclipseContext context, final Matcher<MPart> matcher) {
		super(matcher);
		this.context = context;
	}

	@Override
	public String getFailureMessage() {
		return "Could not find view matching: " + matcher;
	}

	@Override
	protected List<MPart> findMatches() {
		return new WorkbenchContentsFinder(context).findParts(matcher);
	}

}

