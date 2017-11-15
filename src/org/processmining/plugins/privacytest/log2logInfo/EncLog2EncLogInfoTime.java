package org.processmining.plugins.privacytest.log2logInfo;

import java.math.BigInteger;
import java.util.HashMap;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.model.XEvent;
import org.processmining.plugins.InductiveMiner.mining.logs.IMLog;
import org.processmining.plugins.InductiveMiner.mining.logs.IMTrace;

import de.henku.jpaillier.PublicKey;

public class EncLog2EncLogInfoTime {
	public Object[] encryptedResult(IMLog imlog, PublicKey publicKey) {
		XEventClass fromEventClass;
		XEventClass toEventClass;
		
		XEvent fromEvent;
		XEvent toEvent;
		BigIntegerMap bigmap = new BigIntegerMap();
		
		for (IMTrace trace : imlog) {
			toEventClass = null;
			fromEventClass = null;
			
			toEvent = null;
			fromEvent = null;
			
			for (XEvent e : trace) {
				XEventClass ec = imlog.classify(trace,e);
				
				fromEventClass = toEventClass;
				toEventClass = ec;
				
				fromEvent = toEvent;
				toEvent = e;
				
				if (fromEventClass != null) {
					BigInteger encryptedLast = new BigInteger(fromEvent.getAttributes().get("time:neg").toString());
	                BigInteger encryptedNext = new BigInteger(toEvent.getAttributes().get("time:pos").toString());
					Pair<XEventClass, XEventClass> key = new Pair<>(fromEventClass, toEventClass);
					if (bigmap.containsKey(key)) {
						bigmap.put(key, bigmap.get(key).multiply(encryptedNext.multiply(encryptedLast).mod(publicKey.getnSquared())));
					} 
					else {
						bigmap.put(key, encryptedNext.multiply(encryptedLast).mod(publicKey.getnSquared()));
					}
				}
			}
		}
		
		String input = "<html>";
		for(HashMap.Entry<Pair<XEventClass, XEventClass>,BigInteger> entry : bigmap.entrySet()) {
			String str = entry.getValue().toString().replaceAll(".{220}", "$0<br>");
            input = input + entry.getKey().getLeft().getId() + " to " + entry.getKey().getRight().getId() + ": " + str + "<br><br>";
        }
		input += "</html>";
		
		Object[] r = new Object[2];
		r[0] = input;
		r[1] = bigmap;
		
		return r;
	}
}
