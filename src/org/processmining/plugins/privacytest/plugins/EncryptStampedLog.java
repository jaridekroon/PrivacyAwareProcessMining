package org.processmining.plugins.privacytest.plugins;

import java.math.BigInteger;
import java.util.Iterator;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;

import de.henku.jpaillier.KeyPair;
import de.henku.jpaillier.KeyPairBuilder;
import de.henku.jpaillier.PublicKey;

public class EncryptStampedLog {
	@Plugin(name = "Encrypt stamped log", returnLabels = { "Encrypted log", "keyPair", "Public key" }, returnTypes = {
			XLog.class, KeyPair.class, PublicKey.class}, parameterLabels = {
					"Log" }, userAccessible = true, help = "Encrypt log timestamps using JPaillier encryption")
	@UITopiaVariant(affiliation = "", author = "J.J.H. de Kroon", email = "j.j.h.d.kroon@student.tue.nl")
	public Object[] log2log(UIPluginContext context, XLog log) {
		XLog copy = (XLog)log.clone();
		KeyPair keyPair;
        PublicKey publicKey;
        KeyPairBuilder keyGen = new KeyPairBuilder();
        keyPair = keyGen.generateKeyPair();
        publicKey = keyPair.getPublicKey();

        for(Iterator<XTrace> it = copy.iterator(); it.hasNext();) {
            XTrace trace = it.next();
            for(Iterator<XEvent> it2 = trace.iterator(); it2.hasNext();) {
                XEvent event = it2.next();
                try {
                    XAttributeMap nrs = event.getAttributes();
                    BigInteger pos = new BigInteger(nrs.get("time:pos").toString());
                    BigInteger neg = new BigInteger(nrs.get("time:neg").toString());
                    BigInteger encryptedPos = publicKey.encrypt(pos);
                    BigInteger encryptedNeg = publicKey.encrypt(neg);
                    nrs.put("time:pos", new XAttributeLiteralImpl("time:pos", encryptedPos.toString()));
                    nrs.put("time:neg", new XAttributeLiteralImpl("time:neg", encryptedNeg.toString()));
                    event.setAttributes(nrs);
                }
                catch (Exception e) {
                    System.out.println(e);
                }
            }
        }
        Object[] result = new Object[3];
        result[0] = copy;
        result[1] = keyPair;
        result[2] = keyPair.getPublicKey();
		
		return result;
	}
}
