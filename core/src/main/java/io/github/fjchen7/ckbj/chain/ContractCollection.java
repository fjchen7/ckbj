package io.github.fjchen7.ckbj.chain;

import io.github.fjchen7.ckbj.type.Script;

/**
 * The contract collection of the chain.
 */
public interface ContractCollection {

    Contract getContract(Contract.Name contractName);

    default Contract getContract(Script script) {
        return getContract(getContractName(script));
    }

    /**
     * get contract type used by script
     *
     * @param script
     * @return return contract type used by script, or null if not found
     */
    Contract.Name getContractName(Script script);
}
