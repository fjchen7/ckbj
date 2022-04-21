package org.ckbj.chain.sign;

import org.ckbj.type.Cell;
import org.ckbj.type.Script;
import org.ckbj.type.Transaction;

import java.util.ArrayList;
import java.util.List;

public abstract class LockScriptSigner implements ScriptSigner {
    @Override
    public boolean sign(Transaction transaction, List<Cell> inputsDetail) {
        List<Integer> inputGroup = new ArrayList<>();
        for (int i = 0; i < inputsDetail.size(); i++) {
            if (match(inputsDetail.get(i).getLock())) {
                inputGroup.add(i);
            }
        }
        return doSign(transaction, inputsDetail, inputGroup);
    }

    public abstract boolean match(Script script);

    protected abstract boolean doSign(Transaction transaction, List<Cell> inputsDetail, List<Integer> inputGroup);
}
