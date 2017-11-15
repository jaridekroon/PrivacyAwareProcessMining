package org.processmining.plugins.privacytest.plugins;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.plugins.InductiveMiner.dfgOnly.Dfg;
import org.processmining.plugins.InductiveMiner.mining.MiningParameters;
import org.processmining.plugins.InductiveMiner.mining.logs.IMLogImpl;
import org.processmining.plugins.InductiveMiner.mining.logs.XLifeCycleClassifier;
import org.processmining.plugins.privacytest.plugins.dialogs.XLog2DfgTimeDialog;

public class XLog2DfgTime {

	@Plugin(name = "Convert log to time sum dfg", returnLabels = { "Dfg time sum" }, returnTypes = {
			Dfg.class }, parameterLabels = {
					"Log" }, userAccessible = true, help = "Convert a log into a dfg with time sum as values.")
	@UITopiaVariant(affiliation = "", author = "J.J.H. de Kroon", email = "j.j.h.d.kroon@student.tue.nl")
	public Dfg log2Dfg(UIPluginContext context, XLog log) {
		context.getFutureResult(0)
				.setLabel("Directly follows graph of " + XConceptExtension.instance().extractName(log));
		XLog2DfgTimeDialog dialog = new XLog2DfgTimeDialog(log);
		InteractionResult result = context.showWizard("Convert log to time sum dfg", true, true, dialog);
		if (result != InteractionResult.FINISHED) {
			return null;
		}

		XLifeCycleClassifier lifeCycleClassifier = MiningParameters.getDefaultLifeCycleClassifier();
		return dialog.getIMLog2IMLogInfo()
				.createLogInfo(new IMLogImpl(log, dialog.getClassifier(), lifeCycleClassifier)).getDfg();
	}
}
