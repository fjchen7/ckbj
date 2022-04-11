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

    public void setProof(MerkleProof proof) {
        this.proof = proof;
    }

    public byte[] getWitnessesRoot() {
        return witnessesRoot;
    }

    public void setWitnessesRoot(byte[] witnessesRoot) {
        this.witnessesRoot = witnessesRoot;
    }

    public byte[] getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(byte[] blockHash) {
        this.blockHash = blockHash;
    }

    public static class MerkleProof {
        private List<Integer> indices;
        private List<byte[]> lemmas;

        public MerkleProof(List<Integer> indices, List<byte[]> lemmas) {
            this.indices = indices;
            this.lemmas = lemmas;
        }

        public int getProofIndex(int i) {
            return indices.get(i);
        }

        public List<Integer> getIndices() {
            return indices;
        }

        public byte[] getProofLemma(int i) {
            return lemmas.get(i);
        }

        public List<byte[]> getLemmas() {
            return lemmas;
        }
    }
}
