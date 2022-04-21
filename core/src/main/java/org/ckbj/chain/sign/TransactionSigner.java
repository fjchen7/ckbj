package org.ckbj.chain.sign;

import org.ckbj.type.Cell;
import org.ckbj.type.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransactionSigner {
    private final Set<ScriptSigner> ScriptSigners = new HashSet<>();

    public TransactionSigner register(ScriptSigner signer) {
        ScriptSigners.add(signer);
        return this;
    }

    public void sign(Transaction transaction, List<Cell> inputsDetail) {
        Set<ScriptSigner> signers = new HashSet<>();
        for (ScriptSigner signer: ScriptSigners) {
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
        for (ScriptSigner signer: signers) {
            signer.sign(transaction, inputsDetail);
        }
    }
}
