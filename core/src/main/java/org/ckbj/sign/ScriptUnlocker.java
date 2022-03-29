package org.ckbj.sign;

import org.ckbj.protocol.type.DetailedTransaction;

public interface ScriptUnlocker {
    void unlock(DetailedTransaction transaction, ScriptGroup scriptGroup);
}
