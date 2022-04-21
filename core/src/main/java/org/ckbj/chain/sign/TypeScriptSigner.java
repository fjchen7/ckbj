package org.ckbj.chain.sign;

import org.ckbj.type.Cell;
import org.ckbj.type.Transaction;

import java.util.ArrayList;
import java.util.List;

public abstract class TypeScriptSigner implements ScriptSigner {
    @Override
    public boolean sign(Transaction transaction, List<Cell> inputsDetail) {
        List<Integer> inputGroup = new ArrayList<>();
        List<Integer> outputGroup = new ArrayList<>();
        for (int i = 0; i < inputsDetail.size(); i++) {
            if (match(inputsDetail.get(i).getType())) {
                inputGroup.add(i);
            }
        }
        for (int i = 0; i < transaction.getOutputs().size(); i++) {
            if (match(transaction.getOutputs().get(i).getType())) {
                outputGroup.add(i);
            }
        }
        return doSign(transaction, inputsDetail, inputGroup, outputGroup);
    }

    public abstract boolean doSign(Transaction transaction, List<Cell> inputsDetail,
                                   List<Integer> inputGroup, List<Integer> outputGroup);
}
