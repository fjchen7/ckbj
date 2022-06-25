package io.github.fjchen7.ckbj.chain;

import io.github.fjchen7.ckbj.type.Cell;
import io.github.fjchen7.ckbj.type.Script;
import io.github.fjchen7.ckbj.type.Transaction;

import java.util.ArrayList;
import java.util.List;

public interface LockScriptFulfillment {

    boolean match(Script script);

    void fulfill(Transaction transaction, int... inputGroup);

    default boolean fulfill(Transaction transaction, List<Cell> inputsDetail) {
        List<Integer> inputGroupList = new ArrayList<>();
        for (int i = 0; i < inputsDetail.size(); i++) {
            if (match(inputsDetail.get(i).getLock())) {
                inputGroupList.add(i);
            }
        }
        if (inputGroupList.size() == 0) {
            return false;
        } else {
            int[] inputGroup = new int[inputGroupList.size()];
            for (int i = 0; i < inputGroup.length; i++) {
                inputGroup[i] = inputGroupList.get(i);
            }
            fulfill(transaction, inputGroup);
            return true;
        }
    }
}