package org.processmining.plugins.privacytest.plugins;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.plugins.InductiveMiner.mining.MiningParameters;
import org.processmining.plugins.InductiveMiner.mining.logs.IMLog;
import org.processmining.plugins.InductiveMiner.mining.logs.IMLogImpl;
import org.processmining.plugins.InductiveMiner.mining.logs.XLifeCycleClassifier;
import org.processmining.plugins.privacytest.log2logInfo.BigIntegerMap;
import org.processmining.plugins.privacytest.log2logInfo.EncLog2EncLogInfoTime;
import org.processmining.plugins.privacytest.plugins.dialogs.EncXLog2DfgTimeDialog;

import de.henku.jpaillier.PublicKey;

public class EncLog2DfgTime {
	@Plugin(name = "Convert encrypted log to time sum dfg",
			returnLabels = { "Dfg time sum", "BigInteger map" },
			returnTypes = {String.class, BigIntegerMap.class },
			parameterLabels = {"XLog", "publicKey" }, 
			userAccessible = true,
			help = "Convert encrypted log to time sum dfg.")
	@UITopiaVariant(affiliation = "", author = "J.J.H. de Kroon", email = "j.j.h.d.kroon@student.tue.nl")
	public Object[] log2Dfg(UIPluginContext context, XLog log, PublicKey publicKey) {
		context.getFutureResult(0)
				.setLabel("Directly follows graph of " + XConceptExtension.instance().extractName(log));
		EncXLog2DfgTimeDialog dialog = new EncXLog2DfgTimeDialog(log, publicKey);
		InteractionResult result = context.showWizard("Convert encrypted log to time sum dfg", true, true, dialog);
		if (result != InteractionResult.FINISHED) {
			return null;
		}

		XLifeCycleClassifier lifeCycleClassifier = MiningParameters.getDefaultLifeCycleClassifier();
		IMLog imlog = new IMLogImpl(log, dialog.getClassifier(), lifeCycleClassifier);
		
		return new EncLog2EncLogInfoTime().encryptedResult(imlog, publicKey);
	}
}
