package org.ckbj.sign;

import org.ckbj.protocol.type.*;
import org.ckbj.sign.scriptUnlocker.Secp256k1Blake160ScriptUnlocker;

import java.util.List;
import java.util.Map;

public class TransactionUnlocker {
    private Map<Script, ScriptUnlocker> scriptSignerMap;

    public TransactionUnlocker register(Script script, ScriptUnlocker scriptUnlocker) {
        scriptSignerMap.put(script, scriptUnlocker);
        return this;
    }

    public TransactionUnlocker registerSecp256k1Blake160Address(String address, String privateKey) {
        register(Script.fromAddress(address), new Secp256k1Blake160ScriptUnlocker(privateKey));
        return this;
    }

    private List<ScriptGroup> groupScript(DetailedTransaction transaction) {
        return null;
    }

    public void unlock(DetailedTransaction transaction) {
        List<ScriptGroup> scriptGroups = groupScript(transaction);
        for (ScriptGroup group : scriptGroups) {
            Script script = group.getScript();
            ScriptUnlocker scriptUnlocker = scriptSignerMap.get(script);
            if (scriptUnlocker != null) {
                scriptUnlocker.unlock(transaction, group);
            } else {
                throw new RuntimeException("Cannot find ScriptUnlocker for script " + script);
            }
        }
    }
}
