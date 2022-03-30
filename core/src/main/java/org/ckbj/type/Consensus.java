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

    public Consensus setId(String id) {
        this.id = id;
        return this;
    }

    public Consensus setGenesisHash(byte[] genesisHash) {
        this.genesisHash = genesisHash;
        return this;
    }

    public Consensus setDaoTypeHash(byte[] daoTypeHash) {
        this.daoTypeHash = daoTypeHash;
        return this;
    }

    public Consensus setSecp256k1Blake160SighashAllTypeHash(byte[] secp256k1Blake160SighashAllTypeHash) {
        this.secp256k1Blake160SighashAllTypeHash = secp256k1Blake160SighashAllTypeHash;
        return this;
    }

    public Consensus setSecp256k1Blake160MultisigAllTypeHash(byte[] secp256k1Blake160MultisigAllTypeHash) {
        this.secp256k1Blake160MultisigAllTypeHash = secp256k1Blake160MultisigAllTypeHash;
        return this;
    }

    public Consensus setInitialPrimaryEpochReward(BigInteger initialPrimaryEpochReward) {
        this.initialPrimaryEpochReward = initialPrimaryEpochReward;
        return this;
    }

    public Consensus setSecondaryEpochReward(BigInteger secondaryEpochReward) {
        this.secondaryEpochReward = secondaryEpochReward;
        return this;
    }

    public Consensus setMaxUnclesNum(int maxUnclesNum) {
        this.maxUnclesNum = maxUnclesNum;
        return this;
    }

    public Consensus setOrphanRateTarget(Ratio orphanRateTarget) {
        this.orphanRateTarget = orphanRateTarget;
        return this;
    }

    public Consensus setEpochDurationTarget(long epochDurationTarget) {
        this.epochDurationTarget = epochDurationTarget;
        return this;
    }

    public Consensus setTxProposalWindow(ProposalWindow txProposalWindow) {
        this.txProposalWindow = txProposalWindow;
        return this;
    }

    public Consensus setProposerRewardRatio(Ratio proposerRewardRatio) {
        this.proposerRewardRatio = proposerRewardRatio;
        return this;
    }

    public Consensus setCellbaseMaturity(EpochFraction cellbaseMaturity) {
        this.cellbaseMaturity = cellbaseMaturity;
        return this;
    }

    public Consensus setMedianTimeBlockCount(int medianTimeBlockCount) {
        this.medianTimeBlockCount = medianTimeBlockCount;
        return this;
    }

    public Consensus setMaxBlockCycles(long maxBlockCycles) {
        this.maxBlockCycles = maxBlockCycles;
        return this;
    }

    public Consensus setMaxBlockBytes(int maxBlockBytes) {
        this.maxBlockBytes = maxBlockBytes;
        return this;
    }

    public Consensus setBlockVersion(int blockVersion) {
        this.blockVersion = blockVersion;
        return this;
    }

    public Consensus setTxVersion(int txVersion) {
        this.txVersion = txVersion;
        return this;
    }

    public Consensus setTypeIdCodeHash(byte[] typeIdCodeHash) {
        this.typeIdCodeHash = typeIdCodeHash;
        return this;
    }

    public Consensus setMaxBlockProposalsLimit(int maxBlockProposalsLimit) {
        this.maxBlockProposalsLimit = maxBlockProposalsLimit;
        return this;
    }

    public Consensus setPrimaryEpochRewardHalvingInterval(int primaryEpochRewardHalvingInterval) {
        this.primaryEpochRewardHalvingInterval = primaryEpochRewardHalvingInterval;
        return this;
    }

    public Consensus setPermanentDifficultyInDummy(boolean permanentDifficultyInDummy) {
        this.permanentDifficultyInDummy = permanentDifficultyInDummy;
        return this;
    }

    public Consensus setHardforkFeatures(List<HardForkFeature> hardforkFeatures) {
        this.hardforkFeatures = hardforkFeatures;
        return this;
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
