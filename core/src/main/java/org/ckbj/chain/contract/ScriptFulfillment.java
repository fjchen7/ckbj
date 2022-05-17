package org.ckbj.chain.contract;

import org.ckbj.type.Cell;
import org.ckbj.type.Script;
import org.ckbj.type.Transaction;

import java.util.List;

public interface ScriptFulfillment {
    boolean match(Script script);

    boolean fulfill(Transaction transaction, List<Cell> inputsDetail);

    void fulfill(Transaction transaction, int... inputGroup);
}
