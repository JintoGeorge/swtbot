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
 *     Jan Koehnlein - [bug 416994] filter disposed shells
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.waits;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Shell;
import org.hamcrest.Matcher;

/**
 * Condiion that waits for a shell with the specified text to appear.
 *
 * @see Conditions
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 * @since 2.0
 */
class WaitForShell extends WaitForObjectCondition<Shell> {

	WaitForShell(Matcher<Shell> matcher) {
		super(matcher);
	}

	@Override
	public String getFailureMessage() {
		return "Could not find shell matching: " + matcher; //$NON-NLS-1$
	}

	@Override
	protected List<Shell> findMatches() {
		Shell[] shells = findShells();
		ArrayList<Shell> matchingShells = new ArrayList<Shell>();
		for (Shell shell : shells) {
			if (!shell.isDisposed() && matcher.matches(shell)) {
				matchingShells.add(shell);
			}
		}
		return matchingShells;
	}

	/**
	 * Subclasses may override to find other shells.
	 */
	Shell[] findShells() {
		return bot.getFinder().getShells();
	}

}
