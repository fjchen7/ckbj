package org.ckbj.chain;

import org.ckbj.type.Script;

public interface ScriptArgs {
    byte[] getArgs();

    Contract.Name getContractName();

    default Script toScript(Network network) {
        Contract.Name contractName = getContractName();
        byte[] args = getArgs();
        return network.getContract(contractName).createScript(args);
    }
}
