package org.processmining.plugins.privacytest.plugins;

import java.math.BigInteger;
import java.util.HashMap;

import org.deckfour.xes.classification.XEventClass;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.plugins.InductiveMiner.dfgOnly.Dfg;
import org.processmining.plugins.InductiveMiner.dfgOnly.DfgImpl;
import org.processmining.plugins.privacytest.log2logInfo.BigIntegerMap;
import org.processmining.plugins.privacytest.log2logInfo.Pair;

import de.henku.jpaillier.KeyPair;

public class DecryptDfg {
	
	@Plugin(name = "Decrypt dfg", 
			returnLabels = { "Decrypted dfg time sum" }, 
			returnTypes = {Dfg.class },
			parameterLabels = {"BigInteger map", "keyPair"},
			userAccessible = true,
			help = "Decrypt time sum dfg.")
	@UITopiaVariant(affiliation = "", author = "J.J.H. de Kroon", email = "j.j.h.d.kroon@student.tue.nl")
	public static Dfg decrypt(UIPluginContext context, BigIntegerMap map, KeyPair keyPair) {
		Dfg dfg = new DfgImpl();
		for(HashMap.Entry<Pair<XEventClass, XEventClass>,BigInteger> e : map.entrySet()) {
			Pair<XEventClass, XEventClass> pair = e.getKey();
			dfg.addActivity(pair.getLeft());
			dfg.addActivity(pair.getRight());
			dfg.addDirectlyFollowsEdge(pair.getLeft(), pair.getRight(), keyPair.decrypt(e.getValue().mod(keyPair.getPublicKey().getnSquared())).longValue());
		}
		
		return dfg;
	}
}
