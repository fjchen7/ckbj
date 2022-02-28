package org.nervos.sign;

import org.nervos.protocol.type.DetailedTransaction;

public interface ScriptUnlocker {
    void unlock(DetailedTransaction transaction, ScriptGroup scriptGroup);
}
