package org.ckbj.type;

import java.math.BigInteger;
import java.util.List;

public class Consensus {
    private String id;
    private byte[] genesisHash;
    private byte[] daoTypeHash;
    private byte[] secp256k1Blake160SighashAllTypeHash;
    private byte[] secp256k1Blake160MultisigAllTypeHash;
    private BigInteger initialPrimaryEpochReward;
    private BigInteger secondaryEpochReward;
    private int maxUnclesNum;
    private Ratio orphanRateTarget;
    private long epochDurationTarget;
    private ProposalWindow txProposalWindow;
    private Ratio proposerRewardRatio;
    private EpochFraction cellbaseMaturity;
    private int medianTimeBlockCount;
    private long maxBlockCycles;
    private int maxBlockBytes;
    private int blockVersion;
    private int txVersion;
    private byte[] typeIdCodeHash;
    private int maxBlockProposalsLimit;
    private int primaryEpochRewardHalvingInterval;
    private boolean permanentDifficultyInDummy;
    private List<HardForkFeature> hardforkFeatures;

    private Consensus() {
    }

    public String getId() {
        return id;
    }

    public byte[] getGenesisHash() {
        return genesisHash;
    }

    public byte[] getDaoTypeHash() {
        return daoTypeHash;
    }

    public byte[] getSecp256k1Blake160SighashAllTypeHash() {
        return secp256k1Blake160SighashAllTypeHash;
    }

    public byte[] getSecp256k1Blake160MultisigAllTypeHash() {
        return secp256k1Blake160MultisigAllTypeHash;
    }

    public BigInteger getInitialPrimaryEpochReward() {
        return initialPrimaryEpochReward;
    }

    public BigInteger getSecondaryEpochReward() {
        return secondaryEpochReward;
    }

    public int getMaxUnclesNum() {
        return maxUnclesNum;
    }

    public Ratio getOrphanRateTarget() {
        return orphanRateTarget;
    }

    public long getEpochDurationTarget() {
        return epochDurationTarget;
    }

    public ProposalWindow getTxProposalWindow() {
        return txProposalWindow;
    }

    public Ratio getProposerRewardRatio() {
        return proposerRewardRatio;
    }

    public EpochFraction getCellbaseMaturity() {
        return cellbaseMaturity;
    }

    public int getMedianTimeBlockCount() {
        return medianTimeBlockCount;
    }

    public long getMaxBlockCycles() {
        return maxBlockCycles;
    }

    public int getMaxBlockBytes() {
        return maxBlockBytes;
    }

    public int getBlockVersion() {
        return blockVersion;
    }

    public int getTxVersion() {
        return txVersion;
    }

    public byte[] getTypeIdCodeHash() {
        return typeIdCodeHash;
    }

    public int getMaxBlockProposalsLimit() {
        return maxBlockProposalsLimit;
    }

    public int getPrimaryEpochRewardHalvingInterval() {
        return primaryEpochRewardHalvingInterval;
    }

    public boolean isPermanentDifficultyInDummy() {
        return permanentDifficultyInDummy;
    }

    public HardForkFeature getHardforkFeatures(int i) {
        return hardforkFeatures.get(i);
    }

    public List<HardForkFeature> getHardforkFeatures() {
        return hardforkFeatures;
    }

    public static class Ratio {
        private int denom;
        private int numer;

        public int getDenom() {
            return denom;
        }

        public int getNumer() {
            return numer;
        }
    }

    public static class ProposalWindow {
        private int closest;
        private int farthest;

        public int getClosest() {
            return closest;
        }

        public int getFarthest() {
            return farthest;
        }
    }

    public static class HardForkFeature {
        private String rfc;
        private Integer epochNumber;

        public String getRfc() {
            return rfc;
        }

        public Integer getEpochNumber() {
            return epochNumber;
        }
    }
}
