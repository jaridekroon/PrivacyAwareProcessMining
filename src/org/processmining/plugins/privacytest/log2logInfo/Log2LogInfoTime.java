package org.processmining.plugins.privacytest.log2logInfo;

import java.util.List;
import java.util.Map;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.model.XEvent;
import org.processmining.plugins.InductiveMiner.MultiSet;
import org.processmining.plugins.InductiveMiner.dfgOnly.Dfg;
import org.processmining.plugins.InductiveMiner.dfgOnly.DfgImpl;
import org.processmining.plugins.InductiveMiner.dfgOnly.log2logInfo.IMLog2IMLogInfo;
import org.processmining.plugins.InductiveMiner.mining.IMLogInfo;
import org.processmining.plugins.InductiveMiner.mining.logs.IMLog;
import org.processmining.plugins.InductiveMiner.mining.logs.IMTrace;

import gnu.trove.map.hash.THashMap;
import gnu.trove.map.hash.TObjectIntHashMap;

public class Log2LogInfoTime implements IMLog2IMLogInfo {
	public IMLogInfo createLogInfo(IMLog log) {
		return log2logInfoTime(log);
	}

	public static IMLogInfo log2logInfoTime(IMLog log) {
		//initialise, read the log
		Dfg dfg = new DfgImpl();
		MultiSet<XEventClass> activities = new MultiSet<XEventClass>();
		TObjectIntHashMap<XEventClass> minimumSelfDistances = new TObjectIntHashMap<>();
		THashMap<XEventClass, MultiSet<XEventClass>> minimumSelfDistancesBetween = new THashMap<XEventClass, MultiSet<XEventClass>>();
		long numberOfEvents = 0;
		long numberOfActivityInstances = 0;

		XEventClass fromEventClass;
		XEventClass toEventClass;
		
		XEvent fromEvent;
		XEvent toEvent;

		//walk trough the log
		Map<XEventClass, Integer> eventSeenAt;
		List<XEventClass> readTrace;
		
		for (IMTrace trace : log) {
			toEventClass = null;
			fromEventClass = null;
			
			toEvent = null;
			fromEvent = null;
			
			for (XEvent e : trace) {
				XEventClass ec = log.classify(trace,e);
				dfg.addActivity(ec);
				
				fromEventClass = toEventClass;
				toEventClass = ec;
				
				fromEvent = toEvent;
				toEvent = e;
				
				if (fromEventClass != null) {
					long lasttime = Long.parseLong(fromEvent.getAttributes().get("time:neg").toString());
	                long nexttime = Long.parseLong(toEvent.getAttributes().get("time:pos").toString());
					dfg.addDirectlyFollowsEdge(fromEventClass, toEventClass, lasttime + nexttime);
				}
			}
		}

		return new IMLogInfo(dfg, activities, minimumSelfDistancesBetween, minimumSelfDistances, numberOfEvents,
				numberOfActivityInstances, log.size());
	}

	public boolean useLifeCycle() {
		return false;
	}
}
