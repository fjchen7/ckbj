package org.ckbj.chain;

import org.ckbj.type.Cell;
import org.ckbj.type.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransactionFulfillment {
    private final Set<LockScriptFulfillment> lockScriptFulfillmentSet = new HashSet<>();

    public TransactionFulfillment register(LockScriptFulfillment lockScriptFulfillment) {
        this.lockScriptFulfillmentSet.add(lockScriptFulfillment);
        return this;
    }

    public void fulfill(Transaction transaction, List<Cell> inputsDetail) {
        Set<LockScriptFulfillment> matchedFulfillmentSet = new HashSet<>();
        for (LockScriptFulfillment fulfillment: lockScriptFulfillmentSet) {
            for (Cell cell: transaction.getOutputs()) {
                if (fulfillment.match(cell.getLock())) {
                    matchedFulfillmentSet.add(fulfillment);
                    break;
                }
            }
        }
        for (LockScriptFulfillment fulfillment: matchedFulfillmentSet) {
            fulfillment.fulfill(transaction, inputsDetail);
        }
    }
}
