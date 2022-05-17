package org.ckbj.chain;

import org.ckbj.chain.contract.ScriptFulfillment;
import org.ckbj.type.Cell;
import org.ckbj.type.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransactionFulfillment {
    private final Set<ScriptFulfillment> scriptFulfillments = new HashSet<>();

    public TransactionFulfillment register(ScriptFulfillment scriptFulfillment) {
        scriptFulfillments.add(scriptFulfillment);
        return this;
    }

    public void fulfill(Transaction transaction, List<Cell> inputsDetail) {
        Set<ScriptFulfillment> signers = new HashSet<>();
        for (ScriptFulfillment signer: scriptFulfillments) {
            for (Cell cell: inputsDetail) {
                if (signer.match(cell.getLock()) || signer.match(cell.getType())) {
                    signers.add(signer);
                    break;
                }
            }
            for (Cell cell: transaction.getOutputs()) {
                if (signer.match(cell.getType())) {
                    signers.add(signer);
                }
            }
        }
        for (ScriptFulfillment signer: signers) {
            signer.fulfill(transaction, inputsDetail);
        }
    }
}
