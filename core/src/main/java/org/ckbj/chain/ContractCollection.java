package org.ckbj.chain;

import org.ckbj.type.Script;

/**
 * The contract collection of the chain.
 */
public interface ContractCollection {

    Contract getContract(Contract.Type contractType);

    default Contract getContract(Script script) {
        return getContract(getContractType(script));
    }

    /**
     * get contract type used by script
     *
     * @param script
     * @return return contract type used by script, or null if not found
     */
    Contract.Type getContractType(Script script);
}
