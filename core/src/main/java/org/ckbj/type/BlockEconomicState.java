package org.ckbj.type;

import java.math.BigInteger;

public class BlockEconomicState {
    private BlockIssuance issuance;
    private MinerReward minerReward;
    private BigInteger txsFee;
    private byte[] finalizedAt;

    public BlockIssuance getIssuance() {
        return issuance;
    }

    public MinerReward getMinerReward() {
        return minerReward;
    }

    public BigInteger getTxsFee() {
        return txsFee;
    }

    public byte[] getFinalizedAt() {
        return finalizedAt;
    }

    public BlockEconomicState setIssuance(BlockIssuance issuance) {
        this.issuance = issuance;
        return this;
    }

    public BlockEconomicState setMinerReward(MinerReward minerReward) {
        this.minerReward = minerReward;
        return this;
    }

    public BlockEconomicState setTxsFee(BigInteger txsFee) {
        this.txsFee = txsFee;
        return this;
    }

    public BlockEconomicState setFinalizedAt(byte[] finalizedAt) {
        this.finalizedAt = finalizedAt;
        return this;
    }

    public static class BlockIssuance {
        public BigInteger primary;
        public BigInteger secondary;

        public BigInteger getPrimary() {
            return primary;
        }

        public BigInteger getSecondary() {
            return secondary;
        }
    }

    public static class MinerReward {
        public BigInteger committed;
        public BigInteger primary;
        public BigInteger proposal;
        public BigInteger secondary;

        public BigInteger getCommitted() {
            return committed;
        }

        public BigInteger getPrimary() {
            return primary;
        }

        public BigInteger getProposal() {
            return proposal;
        }

        public BigInteger getSecondary() {
            return secondary;
        }
    }
}
