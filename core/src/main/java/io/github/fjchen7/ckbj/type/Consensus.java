package io.github.fjchen7.ckbj.type;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getGenesisHash() {
        return genesisHash;
    }

    public void setGenesisHash(byte[] genesisHash) {
        this.genesisHash = genesisHash;
    }

    public byte[] getDaoTypeHash() {
        return daoTypeHash;
    }

    public void setDaoTypeHash(byte[] daoTypeHash) {
        this.daoTypeHash = daoTypeHash;
    }

    public byte[] getSecp256k1Blake160SighashAllTypeHash() {
        return secp256k1Blake160SighashAllTypeHash;
    }

    public void setSecp256k1Blake160SighashAllTypeHash(byte[] secp256k1Blake160SighashAllTypeHash) {
        this.secp256k1Blake160SighashAllTypeHash = secp256k1Blake160SighashAllTypeHash;
    }

    public byte[] getSecp256k1Blake160MultisigAllTypeHash() {
        return secp256k1Blake160MultisigAllTypeHash;
    }

    public void setSecp256k1Blake160MultisigAllTypeHash(byte[] secp256k1Blake160MultisigAllTypeHash) {
        this.secp256k1Blake160MultisigAllTypeHash = secp256k1Blake160MultisigAllTypeHash;
    }

    public BigInteger getInitialPrimaryEpochReward() {
        return initialPrimaryEpochReward;
    }

    public void setInitialPrimaryEpochReward(BigInteger initialPrimaryEpochReward) {
        this.initialPrimaryEpochReward = initialPrimaryEpochReward;
    }

    public BigInteger getSecondaryEpochReward() {
        return secondaryEpochReward;
    }

    public void setSecondaryEpochReward(BigInteger secondaryEpochReward) {
        this.secondaryEpochReward = secondaryEpochReward;
    }

    public int getMaxUnclesNum() {
        return maxUnclesNum;
    }

    public void setMaxUnclesNum(int maxUnclesNum) {
        this.maxUnclesNum = maxUnclesNum;
    }

    public Ratio getOrphanRateTarget() {
        return orphanRateTarget;
    }

    public void setOrphanRateTarget(Ratio orphanRateTarget) {
        this.orphanRateTarget = orphanRateTarget;
    }

    public long getEpochDurationTarget() {
        return epochDurationTarget;
    }

    public void setEpochDurationTarget(long epochDurationTarget) {
        this.epochDurationTarget = epochDurationTarget;
    }

    public ProposalWindow getTxProposalWindow() {
        return txProposalWindow;
    }

    public void setTxProposalWindow(ProposalWindow txProposalWindow) {
        this.txProposalWindow = txProposalWindow;
    }

    public Ratio getProposerRewardRatio() {
        return proposerRewardRatio;
    }

    public void setProposerRewardRatio(Ratio proposerRewardRatio) {
        this.proposerRewardRatio = proposerRewardRatio;
    }

    public EpochFraction getCellbaseMaturity() {
        return cellbaseMaturity;
    }

    public void setCellbaseMaturity(EpochFraction cellbaseMaturity) {
        this.cellbaseMaturity = cellbaseMaturity;
    }

    public int getMedianTimeBlockCount() {
        return medianTimeBlockCount;
    }

    public void setMedianTimeBlockCount(int medianTimeBlockCount) {
        this.medianTimeBlockCount = medianTimeBlockCount;
    }

    public long getMaxBlockCycles() {
        return maxBlockCycles;
    }

    public void setMaxBlockCycles(long maxBlockCycles) {
        this.maxBlockCycles = maxBlockCycles;
    }

    public int getMaxBlockBytes() {
        return maxBlockBytes;
    }

    public void setMaxBlockBytes(int maxBlockBytes) {
        this.maxBlockBytes = maxBlockBytes;
    }

    public int getBlockVersion() {
        return blockVersion;
    }

    public void setBlockVersion(int blockVersion) {
        this.blockVersion = blockVersion;
    }

    public int getTxVersion() {
        return txVersion;
    }

    public void setTxVersion(int txVersion) {
        this.txVersion = txVersion;
    }

    public byte[] getTypeIdCodeHash() {
        return typeIdCodeHash;
    }

    public void setTypeIdCodeHash(byte[] typeIdCodeHash) {
        this.typeIdCodeHash = typeIdCodeHash;
    }

    public int getMaxBlockProposalsLimit() {
        return maxBlockProposalsLimit;
    }

    public void setMaxBlockProposalsLimit(int maxBlockProposalsLimit) {
        this.maxBlockProposalsLimit = maxBlockProposalsLimit;
    }

    public int getPrimaryEpochRewardHalvingInterval() {
        return primaryEpochRewardHalvingInterval;
    }

    public void setPrimaryEpochRewardHalvingInterval(int primaryEpochRewardHalvingInterval) {
        this.primaryEpochRewardHalvingInterval = primaryEpochRewardHalvingInterval;
    }

    public boolean isPermanentDifficultyInDummy() {
        return permanentDifficultyInDummy;
    }

    public void setPermanentDifficultyInDummy(boolean permanentDifficultyInDummy) {
        this.permanentDifficultyInDummy = permanentDifficultyInDummy;
    }

    public List<HardForkFeature> getHardforkFeatures() {
        return hardforkFeatures;
    }

    public void setHardforkFeatures(List<HardForkFeature> hardforkFeatures) {
        this.hardforkFeatures = hardforkFeatures;
    }

    public static class Ratio {
        private int denom;
        private int numer;

        public int getDenom() {
            return denom;
        }

        public void setDenom(int denom) {
            this.denom = denom;
        }

        public int getNumer() {
            return numer;
        }

        public void setNumer(int numer) {
            this.numer = numer;
        }
    }

    public static class ProposalWindow {
        private int closest;
        private int farthest;

        public int getClosest() {
            return closest;
        }

        public void setClosest(int closest) {
            this.closest = closest;
        }

        public int getFarthest() {
            return farthest;
        }

        public void setFarthest(int farthest) {
            this.farthest = farthest;
        }
    }

    public static class HardForkFeature {
        private String rfc;
        private Integer epochNumber;

        public String getRfc() {
            return rfc;
        }

        public void setRfc(String rfc) {
            this.rfc = rfc;
        }

        public Integer getEpochNumber() {
            return epochNumber;
        }

        public void setEpochNumber(Integer epochNumber) {
            this.epochNumber = epochNumber;
        }
    }
}
