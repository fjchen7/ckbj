package org.nervos.sign.scriptUnlocker;

import org.nervos.protocol.type.DetailedTransaction;
import org.nervos.sign.ScriptGroup;
import org.nervos.sign.ScriptUnlocker;

import java.util.List;

public class Secp256k1Blake160ScriptUnlocker implements ScriptUnlocker {
    private String privateKey;

    public Secp256k1Blake160ScriptUnlocker(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public void unlock(DetailedTransaction transaction, ScriptGroup scriptGroup) {

    }
}
