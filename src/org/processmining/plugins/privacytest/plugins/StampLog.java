package org.processmining.plugins.privacytest.plugins;

import java.util.Iterator;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.deckfour.xes.util.XsDateTimeConversion;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;

public class StampLog {
	@Plugin(name = "Stamp log", returnLabels = { "Stamped log" }, returnTypes = {
			XLog.class }, parameterLabels = {
					"Log" }, userAccessible = true, help = "Stamp log with time numbers")
	@UITopiaVariant(affiliation = "", author = "J.J.H. de Kroon", email = "j.j.h.d.kroon@student.tue.nl")
	public XLog log2log(UIPluginContext context, XLog log) {
		XsDateTimeConversion conversion = new XsDateTimeConversion();
		for(Iterator<XTrace> it = log.iterator(); it.hasNext();) {
            XTrace trace = it.next();
            for(Iterator<XEvent> it2 = trace.iterator(); it2.hasNext();) {
                XEvent event = it2.next();
                XAttributeMap nrs = event.getAttributes();
                long time = conversion.parseXsDateTime(nrs.get("time:timestamp").toString()).getTime();

                nrs.put("time:pos", new XAttributeLiteralImpl("time:pos", "" + time));
                nrs.put("time:neg", new XAttributeLiteralImpl("time:neg", "-" + time));
                nrs.remove("time:timestamp");
            }
        }
		
		return log;
	}
}
