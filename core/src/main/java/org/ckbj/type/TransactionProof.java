package org.ckbj.type;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionProof {
    private MerkleProof proof;
    private byte[] witnessesRoot;
    private byte[] blockHash;

    public MerkleProof getProof() {
        return proof;
    }

    public byte[] getWitnessesRoot() {
        return witnessesRoot;
    }

    public byte[] getBlockHash() {
        return blockHash;
    }

    public TransactionProof setProof(MerkleProof proof) {
        this.proof = proof;
        return this;
    }

    public TransactionProof setWitnessesRoot(byte[] witnessesRoot) {
        this.witnessesRoot = witnessesRoot;
        return this;
    }

    public TransactionProof setBlockHash(byte[] blockHash) {
        this.blockHash = blockHash;
        return this;
    }

    public static class MerkleProof {
        @SerializedName("indices")
        private List<Integer> indexes;
        private List<byte[]> lemmas;

        public MerkleProof(List<Integer> indexes, List<byte[]> lemmas) {
            this.indexes = indexes;
            this.lemmas = lemmas;
        }

        public int getProofIndex(int i) {
            return indexes.get(i);
        }

        public List<Integer> getIndexes() {
            return indexes;
        }

        public byte[] getProofLemma(int i) {
            return lemmas.get(i);
        }

        public List<byte[]> getLemmas() {
            return lemmas;
        }
    }
}
