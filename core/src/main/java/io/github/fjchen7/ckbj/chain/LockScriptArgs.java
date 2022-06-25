package io.github.fjchen7.ckbj.chain;

import io.github.fjchen7.ckbj.chain.address.Address;
import io.github.fjchen7.ckbj.type.Script;

public interface LockScriptArgs extends ScriptArgs {

    default Address toAddress(Network network) {
        Script script = toScript(network);
        return new Address(script, network);
    }

    byte[] getWitnessPlaceholder(byte[] originalWitness);
}
