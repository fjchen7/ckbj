package io.github.fjchen7.ckbj.chain.contract;

import io.github.fjchen7.ckbj.chain.LockScriptFulfillment;
import io.github.fjchen7.ckbj.type.Script;
import io.github.fjchen7.ckbj.type.Transaction;
import io.github.fjchen7.ckbj.chain.Contract;
import io.github.fjchen7.ckbj.chain.Network;

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
