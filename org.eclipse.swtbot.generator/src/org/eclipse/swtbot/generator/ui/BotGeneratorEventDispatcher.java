/*******************************************************************************
 * Copyright (c) 2012 Red Hat Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Mickael Istria (Red Hat) - initial API and implementation
 *    Rastislav Wagner (Red Hat) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swtbot.generator.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swtbot.generator.framework.GenerationComplexRule;
import org.eclipse.swtbot.generator.framework.GenerationRule;
import org.eclipse.swtbot.generator.framework.GenerationSimpleRule;
import org.eclipse.swtbot.generator.framework.Generator;
import org.eclipse.swtbot.generator.framework.WidgetUtils;

public class BotGeneratorEventDispatcher implements Listener{

	public static final Event FLUSH_GENERATION_RULES = new Event();

	public static interface CodeGenerationListener {
		public void handleCodeGenerated(GenerationRule code);
	}

	private Generator generator;
	private List<CodeGenerationListener> listeners = new ArrayList<CodeGenerationListener>();
	private List<Shell> ignoredShells;
	private boolean recording = false;

	private List<GenerationSimpleRule> simpleRules = new ArrayList<GenerationSimpleRule>();
	private GenerationComplexRule longestMatchedComplex;
	private List<GenerationComplexRule> activeComplexRules = new ArrayList<GenerationComplexRule>();

	public void setGenerator(Generator generator) {
		this.generator = generator;
	}

	public void addListener(CodeGenerationListener listener) {
		this.listeners.add(listener);
	}

	public void ignoreShells(List<Shell> shells) {
		this.ignoredShells = shells;
	}

	public boolean isRecording() {
		return this.recording;
	}

	public void setRecording(boolean recording){
		this.recording = recording;
	}

	public Generator getCurrentGenerator() {
		return this.generator;
	}

	@Override
	public void handleEvent(Event event) {
		if (!this.recording) {
			return;
		}
		if(checkIgnoredShells(event)){
			return;
		}
		if (!(event.widget instanceof Shell) && event.widget instanceof Control
				&& !(((Control) event.widget).isFocusControl()
						&& ((Control) event.widget).isVisible()
							&& ((Control) event.widget).isEnabled())) {
			return;
		}
		processRules(event, false);
	}

	private boolean checkIgnoredShells(Event event){
		Shell shell = null;
		if (event.widget instanceof Control) {
			shell = WidgetUtils.getShell((Control) event.widget);
		} else if(event.widget instanceof Item){
			if(event.widget instanceof MenuItem){
				shell = WidgetUtils.getShell(((MenuItem) event.widget).getParent().getParent());
			} else if(event.widget instanceof TabItem){
				shell = WidgetUtils.getShell(((TabItem) event.widget).getParent());
			} else if(event.widget instanceof ToolItem){
				shell = WidgetUtils.getShell(((ToolItem) event.widget).getParent());
			}
		}
		if(shell!= null){
			if (shell.getParent() instanceof Shell) {
				shell = (Shell)shell.getParent();
			}
			for(Shell ignoredShell: ignoredShells){
				if (ignoredShell != null && ignoredShell.equals(shell)){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Process all recorded events and generates code, ignoring all future events
	 * to compute generated code.
	 */
	public void flushGenerationRules() {
		processRules(null, true);
	}

	/**
	 *
	 * @param event the event to process (can be null)
	 * @param forceGeneration to force consumption of rules and generation.
	 */
	private void processRules(Event event, boolean forceGeneration) {
		if (event != null) {
			for (GenerationSimpleRule rule : generator.createSimpleRules()) {
				if (rule.appliesTo(event)) {
					rule.initializeForEvent(event);
					simpleRules.add(rule); //store simple rule for further processing
				}
			}
		}

		if (!this.simpleRules.isEmpty()) {
			if (this.activeComplexRules.isEmpty()) {
				this.activeComplexRules.addAll(this.generator.createComplexRules());
			}
			filterComplexRulesAndUpdateLongest();
			if (this.activeComplexRules.isEmpty() || forceGeneration) {
				if (this.longestMatchedComplex != null) {
					// generate code
					dispatchCodeGenerated(longestMatchedComplex);
					// Remove matched contained rules
					this.simpleRules.removeAll(this.longestMatchedComplex.getInitializationRules());
					// Remove current complex rull
					this.longestMatchedComplex = null;
					// continue on remaining simple rules
					processRules(null, forceGeneration);
				} else {
					// generate code for first simple rule
					dispatchCodeGenerated(this.simpleRules.get(0));
					// consume first simple rule
					this.simpleRules.remove(0);
					// continue on remaining simple rules
					processRules(null, forceGeneration);
				}
			}
		}
	}

	private void filterComplexRulesAndUpdateLongest() {
		for(int i=0; i < this.simpleRules.size(); i++){
			Set<GenerationComplexRule> notMatchingRules= new HashSet<GenerationComplexRule>();
			for(GenerationComplexRule complexRule : this.activeComplexRules){
				if(!complexRule.appliesToPartially(simpleRules.get(i), i)){
					notMatchingRules.add(complexRule);
				} else {
					List<GenerationSimpleRule> modifSimpleRules = new ArrayList<GenerationSimpleRule>();
					modifSimpleRules.addAll(simpleRules);
					GenerationComplexRule matchedComplex = null;
					while(matchedComplex == null && !modifSimpleRules.isEmpty()){
						if(complexRule.appliesTo(modifSimpleRules)){
							matchedComplex=complexRule;
							matchedComplex.initializeForRules(modifSimpleRules);
						} else {
							modifSimpleRules.remove(modifSimpleRules.size()-1);
						}
					}
					if(matchedComplex != null){
						if (longestMatchedComplex == null || matchedComplex.getInitializationRules().size() > longestMatchedComplex.getInitializationRules().size()){
							longestMatchedComplex = matchedComplex;
						}
					}
				}
			}
			this.activeComplexRules.removeAll(notMatchingRules);
		}
	}

	private void dispatchCodeGenerated(GenerationRule code) {
		if (code != null && code.getActions() != null) {
			for (CodeGenerationListener listener : this.listeners) {
				listener.handleCodeGenerated(code);
			}
		}
	}

}