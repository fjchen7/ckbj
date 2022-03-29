package org.ckbj.sign.scriptUnlocker;

import org.ckbj.protocol.type.DetailedTransaction;
import org.ckbj.sign.ScriptGroup;
import org.ckbj.sign.ScriptUnlocker;

public class Secp256k1Blake160ScriptUnlocker implements ScriptUnlocker {
    private String privateKey;

    public Secp256k1Blake160ScriptUnlocker(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public void unlock(DetailedTransaction transaction, ScriptGroup scriptGroup) {

    }
}
