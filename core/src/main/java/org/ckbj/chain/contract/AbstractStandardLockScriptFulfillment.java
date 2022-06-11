package org.ckbj.chain.contract;

import org.ckbj.chain.Contract;
import org.ckbj.chain.LockScriptFulfillment;
import org.ckbj.chain.Network;
import org.ckbj.type.Script;
import org.ckbj.type.Transaction;

public abstract class AbstractStandardLockScriptFulfillment implements LockScriptFulfillment {
    protected abstract boolean doMatch(byte[] scriptArgs);

    public abstract void fulfill(Transaction transaction, int... inputGroup);

    public abstract Contract.Name getContractName();

    @Override
    public boolean match(Script script) {
        if (Network.TESTNET.getContractName(script) == getContractName()
                || Network.MAINNET.getContractName(script) == getContractName()) {
            return doMatch(script.getArgs());
        } else {
            return false;
        }
    }
}
