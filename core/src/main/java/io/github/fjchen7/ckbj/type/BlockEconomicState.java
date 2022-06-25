package io.github.fjchen7.ckbj.type;

import java.math.BigInteger;

public class BlockEconomicState {
    private BlockIssuance issuance;
    private MinerReward minerReward;
    private BigInteger txsFee;
    private byte[] finalizedAt;

    public BlockIssuance getIssuance() {
        return issuance;
    }

    public void setIssuance(BlockIssuance issuance) {
        this.issuance = issuance;
    }

    public MinerReward getMinerReward() {
        return minerReward;
    }

    public void setMinerReward(MinerReward minerReward) {
        this.minerReward = minerReward;
    }

    public BigInteger getTxsFee() {
        return txsFee;
    }

    public void setTxsFee(BigInteger txsFee) {
        this.txsFee = txsFee;
    }

    public byte[] getFinalizedAt() {
        return finalizedAt;
    }

    public void setFinalizedAt(byte[] finalizedAt) {
        this.finalizedAt = finalizedAt;
    }

    public static class BlockIssuance {
        private BigInteger primary;
        private BigInteger secondary;

        public BigInteger getPrimary() {
            return primary;
        }

        public void setPrimary(BigInteger primary) {
            this.primary = primary;
        }

        public BigInteger getSecondary() {
            return secondary;
        }

        public void setSecondary(BigInteger secondary) {
            this.secondary = secondary;
        }
    }

    public static class MinerReward {
        private BigInteger committed;
        private BigInteger primary;
        private BigInteger proposal;
        private BigInteger secondary;

        public BigInteger getCommitted() {
            return committed;
        }

        public void setCommitted(BigInteger committed) {
            this.committed = committed;
        }

        public BigInteger getPrimary() {
            return primary;
        }

        public void setPrimary(BigInteger primary) {
            this.primary = primary;
        }

        public BigInteger getProposal() {
            return proposal;
        }

        public void setProposal(BigInteger proposal) {
            this.proposal = proposal;
        }

        public BigInteger getSecondary() {
            return secondary;
        }

        public void setSecondary(BigInteger secondary) {
            this.secondary = secondary;
        }
    }
}
