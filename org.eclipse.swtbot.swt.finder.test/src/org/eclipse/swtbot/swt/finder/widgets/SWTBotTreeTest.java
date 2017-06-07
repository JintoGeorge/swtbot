/*******************************************************************************
 * Copyright (c) 2008, 2017 Ketan Padegaonkar and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Ketan Padegaonkar - initial API and implementation
 *     Cédric Chabanois - http://swtbot.org/bugzilla/show_bug.cgi?id=10
 *     Mickael Istria - Updated to support SWT 4.2
 *     Kristine Jetzke - Bug 420121
 *     Aparna Argade - Bug 508710
 *******************************************************************************/
package org.eclipse.swtbot.swt.finder.widgets;

import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertSameWidget;
import static org.eclipse.swtbot.swt.finder.SWTBotTestCase.assertTextContains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.exceptions.AssertionFailedException;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.test.AbstractControlExampleTest;
import org.eclipse.swtbot.swt.finder.utils.TableCollection;
import org.eclipse.swtbot.swt.finder.utils.TableRow;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ketan Padegaonkar &lt;KetanPadegaonkar [at] gmail [dot] com&gt;
 * @version $Id$
 */
public class SWTBotTreeTest extends AbstractControlExampleTest {

	private SWTBot		bot;
	private SWTBotTree	tree;

	@Test
	public void findsTable() throws Exception {
		assertEquals(Tree.class, tree.widget.getClass());
	}

	@Test
	public void getsRowCount() throws Exception {
		assertEquals(4, tree.rowCount());
		assertTrue(tree.hasItems());
	}

	@Test
	public void getsColumnCount() throws Exception {
		assertEquals(0, tree.columnCount());
	}

	@Test
	public void getsColumnCountOnMultiColumnTree() throws Exception {
		bot.checkBox("Multiple Columns").select();
		tree = bot.treeInGroup("Tree");
		assertEquals(4, tree.columnCount());
	}

	@Test
	public void getsAllColumnHeadings() throws Exception {
		bot.checkBox("Multiple Columns").select();
		tree = bot.treeInGroup("Tree");
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("Name");
		arrayList.add("Type");
		arrayList.add("Size");
		arrayList.add("Modified");

		assertEquals(arrayList, tree.columns());
	}

	@Test
	public void getsColumnTextBasedOnRowColumnNumbers() throws Exception {
		bot.checkBox("Multiple Columns").select();
		tree = bot.treeInGroup("Tree");

		assertEquals("2556", tree.cell(1, 2));
		assertEquals("Node 2", tree.cell(1, 0));
		assertEquals("today", tree.cell(3, 3));
	}

	@Test
	public void getsTextCellBasedOnRowNumberColumnText() throws Exception {
		bot.checkBox("Multiple Columns").select();
		tree = bot.treeInGroup("Tree");

		assertEquals("2556", tree.cell(1, "Size"));
		assertEquals("Node 2", tree.cell(1, "Name"));
		assertEquals("today", tree.cell(3, "Modified"));
	}

	@Test
	public void getsSelectionCount() throws Exception {
		assertEquals(0, tree.selection().columnCount());
		assertEquals(0, tree.selection().rowCount());
		assertEquals(0, tree.selectionCount());
	}

	@Test
	public void setsSelection() throws Exception {
		tree.select(3);
		assertEquals(1, tree.selectionCount());
	}

	@Test
	public void setsMultipleSelection() throws Exception {
		bot.radio("SWT.MULTI").click();
		tree = bot.treeInGroup("Tree");
		tree.select(new String[] { "Node 2", "Node 4" });
		assertEquals(2, tree.selectionCount());
		TableCollection selection = tree.selection();
		assertEquals("Node 2", selection.get(0, 0));
		assertEquals("Node 4", selection.get(1, 0));
	}

	@Test(expected=WidgetNotFoundException.class)
	public void setsMultipleSelectionUnknownItem() throws Exception {
		bot.radio("SWT.MULTI").click();
		tree = bot.treeInGroup("Tree");
		tree.select(new String[] { "Node 2", "x", "Node 4" });

	}

	@Test
	public void unselectsSelection() throws Exception {
		bot.radio("SWT.MULTI").click();
		bot.checkBox("Listen").select();
		bot.button("Clear").click();
		tree = bot.treeInGroup("Tree");
		tree.select(new int[] { 1 });
		SWTBotText text = bot.textInGroup("Listeners");
		bot.button("Clear").click();
		tree.unselect();
		assertEquals(0, tree.selectionCount());
		//event.item should contain the item getting unselected, stateMask should reflect CTRL key
		assertEventMatches(text, "MouseDown [3]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x40000 x=0 y=0 count=1}");
		assertEventMatches(text, "Selection [13]: SelectionEvent{Tree {} time=0 data=null item=TreeItem {Node 2} detail=0 x=0 y=0 width=0 height=0 stateMask=0xc0000 text=null doit=true}");
		assertEventMatches(text, "MouseUp [4]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0xc0000 x=0 y=0 count=1}");
	}

	@Test
	public void setEmptySelection() throws Exception {
		bot.radio("SWT.MULTI").click();
		tree = bot.treeInGroup("Tree");
		tree.select(new int[] { 1, 2 });
		assertEquals(2, tree.selectionCount());
		tree.select(new int[] {});
		assertEquals(0, tree.selectionCount());

		tree.select(new String[] { "Node 2", "Node 4" });
		assertEquals(2, tree.selectionCount());
		tree.select(new String[] {});
		assertEquals(0, tree.selectionCount());
	}

	@Test
	public void getsSingleSelection() throws Exception {
		bot.checkBox("Multiple Columns").select();
		tree = bot.treeInGroup("Tree");
		tree.select(2);
		TableCollection selection = tree.selection();
		assertEquals(1, selection.rowCount());
		assertEquals(4, selection.columnCount());
		assertEquals(new TableCollection().add(new TableRow(new String[] { "Node 3", "images", "91571", "yesterday" })), selection);
	}

	@Test
	public void selectsMultipleOnSingleSelectTree() throws Exception {
		try {
			tree.select(1, 2, 3);
			fail("Expecting an exception");
		} catch (Exception e) {
			assertEquals("Tree does not support SWT.MULTI, cannot make multiple selections.", e.getMessage());
		}
		try {
			tree.select("Node 2", "Node 4");
			fail("Expecting an exception");
		} catch (Exception e) {
			assertEquals("Tree does not support SWT.MULTI, cannot make multiple selections.", e.getMessage());
		}
	}

	@Test
	public void findsAnyTree() throws Exception {
		assertSameWidget(tree.widget, bot.tree().widget);
	}

	@Test
	public void getsMultipleSingleSelection() throws Exception {
		bot.radio("SWT.MULTI").click();
		bot.checkBox("Multiple Columns").select();
		bot.checkBox("Listen").select();
		bot.button("Clear").click();
		tree = bot.treeInGroup("Tree");
		tree.select(new int[] { 1, 2 });

		TableCollection selection = tree.selection();
		assertEquals("Node 2", selection.get(0, 0));
		assertEquals("databases", selection.get(0, 1));
		assertEquals("2556", selection.get(0, 2));
		assertEquals("tomorrow", selection.get(0, 3));

		assertEquals(new TableRow(new String[] { "Node 3", "images", "91571", "yesterday" }), selection.get(1));

		SWTBotText text = bot.textInGroup("Listeners");

		// first selection: event.item should contain the first selected item,
		// stateMask should not reflect CTRL key
		assertEventMatches(text, "MouseDown [3]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x0 x=0 y=0 count=1}");
		assertEventMatches(text, "Selection [13]: SelectionEvent{Tree {} time=0 data=null item=TreeItem {Node 2} detail=0 x=0 y=0 width=0 height=0 stateMask=0x80000 text=null doit=true}");
		assertEventMatches(text, "MouseUp [4]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x80000 x=0 y=0 count=1}");

		// subsequent selection: event.item should contain the second selected item,
		// stateMask should reflect CTRL key
		assertEventMatches(text, "MouseDown [3]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x40000 x=0 y=0 count=1}");
		assertEventMatches(text, "Selection [13]: SelectionEvent{Tree {} time=0 data=null item=TreeItem {Node 3} detail=0 x=0 y=0 width=0 height=0 stateMask=0xc0000 text=null doit=true}");
		assertEventMatches(text, "MouseUp [4]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0xc0000 x=0 y=0 count=1}");
	}

	@Test
	public void expandsNode() throws Exception {
		bot.checkBox("Multiple Columns").select();
		bot.checkBox("Listen").select();
		Widget notifications = bot.textInGroup("Listeners").widget;
		tree = bot.treeInGroup("Tree");
		tree.expandNode("Node 2");
		assertEquals(6, tree.visibleRowCount());
		assertTextContains("Expand [17]: TreeEvent{Tree {} time=", notifications);
		assertTextContains("data=null item=TreeItem {Node 2} detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(0, tree.widget) + " text=null doit=true}",
				notifications);
	}

	@Test
	public void expandsNodeToMax() throws Exception {
		bot.checkBox("Multiple Columns").select();
		bot.checkBox("Listen").select();
		tree = bot.treeInGroup("Tree");
		tree.expandNode("Node 2", true);
		assertEquals(7, tree.visibleRowCount());
		assertTextContains("data=null item=TreeItem {Node 2} detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(0, tree.widget) + " text=null doit=true}", bot
				.textInGroup("Listeners").widget);
		assertTextContains("data=null item=TreeItem {Node 2.2} detail=0 x=0 y=0 width=0 height=0 stateMask=" + toStateMask(0, tree.widget) + " text=null doit=true}", bot
				.textInGroup("Listeners").widget);
	}

	@Test
	public void getsAllTreeItems() throws Exception {
		SWTBotTreeItem[] items = tree.getAllItems();
		for (int i = 0; i < items.length; i++)
			assertEquals("Node " + (i + 1), items[i].getText());
	}

	@Test
	public void clicksOnANodeInAColumn() throws Exception {
		bot.checkBox("Multiple Columns").select();
		bot.checkBox("Listen").select();
		final SWTBotTreeItem node = bot.tree().expandNode("Node 2", true);
		bot.button("Clear").click();
		Rectangle columnBounds = getColumnBounds(node, 2);
		int targetX = columnBounds.x +(columnBounds.width/2);
		int targetY = columnBounds.y + (columnBounds.height/2);
		node.click(2);
		SWTBotText listener = bot.textInGroup("Listeners");
		assertTextContains("MouseDown [3]: MouseEvent{Tree {}", listener);
		assertTextContains("Selection [13]: SelectionEvent{Tree {}", listener);
		assertTextContains("item=TreeItem {Node 2}", listener);
		assertTextContains("x=" + targetX +" y=" + targetY, listener);
		assertTextContains("MouseUp [4]: MouseEvent{Tree {}", listener);
	}

	private Rectangle getColumnBounds(final SWTBotTreeItem node, final int columnIndex) {
		return UIThreadRunnable.syncExec(new Result<Rectangle>() {
			@Override
			public Rectangle run() {
				return node.widget.getBounds(2);
			}
		});
	}

	@Test
	public void clicksOnANode() throws Exception {
		bot.checkBox("Listen").select();
		SWTBotTreeItem node = bot.tree().expandNode("Node 3").expandNode("Node 3.1");
		bot.button("Clear").click();
		node.click();
		SWTBotText listener = bot.textInGroup("Listeners");
		assertEventMatches(listener, "MouseDown [3]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x0 x=0 y=0 count=1}");
		assertEventMatches(listener, "Selection [13]: SelectionEvent{Tree {} time=0 data=null item=TreeItem {Node 3.1} detail=0 x=0 y=0 width=0 height=0 stateMask=0x0 text=null doit=true}");
		assertEventMatches(listener, "MouseUp [4]: MouseEvent{Tree {} time=0 data=null button=1 stateMask=0x80000 x=0 y=0 count=1}");
	}


	@Test
	public void doubleClicksOnANode() throws Exception {
		bot.checkBox("Listen").select();
		SWTBotTreeItem node = bot.tree().expandNode("Node 3").expandNode("Node 3.1");
		bot.button("Clear").click();
		node.doubleClick();
		SWTBotText listener = bot.textInGroup("Listeners");

		assertTextContains("MouseDown [3]: MouseEvent{Tree {}", listener);
		assertTextContains("Selection [13]: SelectionEvent{Tree {}", listener);
		assertTextContains("item=TreeItem {Node 3.1}", listener);
		assertTextContains("MouseUp [4]: MouseEvent{Tree {}", listener);

		assertTextContains("MouseDoubleClick [8]: MouseEvent{Tree {} ", listener);
	}

	@Test
	public void expandANodePath() throws Exception {
		SWTBotTree tree = bot.treeInGroup("Tree");

		tree.expandNode("Node 2", "Node 2.2", "Node 2.2.1");
		assertEquals(7, tree.visibleRowCount());
	}

	@Test(expected=AssertionFailedException.class)
	public void expandEmptyPath() throws Exception {
		bot.tree().expandNode();
	}

	@Before
	public void setUp() throws Exception {
		bot = new SWTBot();
		bot.tabItem("Tree").activate();
		bot.checkBox("Listen").deselect();
		bot.button("Clear").click();
		bot.checkBox("Horizontal Fill").select();
		bot.checkBox("Vertical Fill").select();
		bot.checkBox("Header Visible").select();
		bot.checkBox("Multiple Columns").deselect();
		tree = bot.treeInGroup("Tree");

	}
}
