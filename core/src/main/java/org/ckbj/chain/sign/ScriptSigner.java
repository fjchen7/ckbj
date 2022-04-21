package org.ckbj.chain.sign;

import org.ckbj.type.Cell;
import org.ckbj.type.Script;
import org.ckbj.type.Transaction;

import java.util.List;

public interface ScriptSigner {
    boolean match(Script script);

    boolean sign(Transaction transaction, List<Cell> inputsDetail);
}
