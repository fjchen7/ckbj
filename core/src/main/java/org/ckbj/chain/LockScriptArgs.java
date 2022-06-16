package org.ckbj.chain;

import org.ckbj.chain.address.Address;
import org.ckbj.type.Script;

public interface LockScriptArgs extends ScriptArgs {

    default Address toAddress(Network network) {
        Script script = toScript(network);
        return new Address(script, network);
    }

    byte[] getWitnessPlaceholder(byte[] originalWitness);
}
