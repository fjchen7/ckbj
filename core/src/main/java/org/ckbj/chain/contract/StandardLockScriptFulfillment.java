package org.ckbj.chain.contract;

import org.ckbj.chain.Contract;
import org.ckbj.chain.Network;
import org.ckbj.molecule.Serializer;
import org.ckbj.type.Script;
import org.ckbj.type.Transaction;
import org.ckbj.type.WitnessArgs;

public abstract class StandardLockScriptFulfillment implements LockScriptFulfillment {
    protected abstract boolean doMatch(byte[] scriptArgs);

    public abstract void fulfill(Transaction transaction, int... inputGroup);

    public abstract Contract.Type getContractType();

    @Override
    public boolean match(Script script) {
        if (Network.TESTNET.getContractType(script) == getContractType()
                || Network.MAINNET.getContractType(script) == getContractType()) {
            return doMatch(script.getArgs());
        } else {
            return false;
        }
    }

    public static byte[] setWitnessArgsLock(byte[] originalWitness, byte[] lockPlaceholder) {
        WitnessArgs witnessArgs;
        if (originalWitness == null || originalWitness.length == 0) {
            witnessArgs = WitnessArgs
                    .builder()
                    .build();
        } else {
            witnessArgs = Serializer.deserializeWitnessArgs(originalWitness);
        }
        witnessArgs.setLock(lockPlaceholder);
        return Serializer.serialize(witnessArgs);
    }
}
