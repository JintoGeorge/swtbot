/*******************************************************************************
 * Copyright (c) 2008,2011,2013 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Marcel Hoetter - added ToolItemResolver
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.resolvers;

import org.eclipse.swt.widgets.Widget;

/**
 * Finds a resolver that can resolve the parent and children of a widget.
 *
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class Resolvable implements IResolvable {

	/** The resolver */
	protected Resolver	resolver;

	/**
	 * Create a resolvable instance, with some default resolvers.
	 */
	public Resolvable() {
		this(new Resolver());
		resolver.addResolver(new ExpandBarResolver());
		resolver.addResolver(new ExpandItemResolver());
		resolver.addResolver(new CTabFolderResolver());
		resolver.addResolver(new TabFolderResolver());
		resolver.addResolver(new CTabItemResolver());
		resolver.addResolver(new TabItemResolver());
		resolver.addResolver(new ToolbarResolver());
		resolver.addResolver(new CompositeResolver());
		resolver.addResolver(new NullResolver());
		resolver.addResolver(new ToolItemResolver());
	}

	/**
	 * Creates a resolvable using the given resolvable item. It is recommended that the default construction be used.
	 *
	 * @param resolver the resolver
	 */
	public Resolvable(Resolver resolver) {
		this.resolver = resolver;
	}

	/**
	 * Returns {@code true} if any of the resolvers can resolve the widget.
	 *
	 * @see org.eclipse.swtbot.swt.finder.resolvers.IResolvable#canResolve(org.eclipse.swt.widgets.Widget)
	 * @param w The widget to resolve.
	 * @return <code>true</code> if any of the resolvers can resolve the widget. Otherwise <code>false</code>.
	 */
	@Override
	public boolean canResolve(Widget w) {
		Class<?>[] resolvableClasses = getResolvableClasses();
		for (Class<?> clazz : resolvableClasses) {
			if (w.getClass().equals(clazz))
				return true;
		}

		return false;
	}

	/**
	 * Gets the complete list of widget types that this object can resolve.
	 *
	 * @return the types that this resolver can resolve
	 */
	@Override
	public Class<?>[] getResolvableClasses() {
		return resolver.getResolvableClasses();
	}

	/**
	 * Gets the resolver.
	 *
	 * @return the resolver.
	 */
	public Resolver getResolver() {
		return resolver;
	}

}
