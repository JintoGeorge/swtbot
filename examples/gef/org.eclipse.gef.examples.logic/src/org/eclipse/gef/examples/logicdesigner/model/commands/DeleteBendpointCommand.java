/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.logicdesigner.model.commands;

import org.eclipse.draw2d.Bendpoint;

public class DeleteBendpointCommand 
	extends BendpointCommand 
{

private Bendpoint bendpoint;

public void execute() {
	bendpoint = (Bendpoint)getWire().getBendpoints().get(getIndex());
	getWire().removeBendpoint(getIndex());
	super.execute();
}

public void undo() {
	super.undo();
	getWire().insertBendpoint(getIndex(), bendpoint);
}

}


