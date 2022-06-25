package io.github.fjchen7.ckbj.type;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import io.github.fjchen7.ckbj.utils.Hex;

import java.lang.reflect.Type;
import java.util.List;

@JsonAdapter(OnChainTransaction.TypeAdapter.class)
public class OnChainTransaction {
    private Transaction transaction;
    private Status status = Status.UNKNOWN;
    private String statusReason;
    private byte[] blockHash;

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public byte[] getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        setBlockHash(Hex.toByteArray(blockHash));
    }

    public void setBlockHash(byte[] blockHash) {
        this.blockHash = blockHash;
    }

    public int getVersion() {
        return transaction.getVersion();
    }

    public void setVersion(int version) {
        transaction.setVersion(version);
    }

    public List<CellDep> getCellDeps() {
        return transaction.getCellDeps();
    }

    public void setCellDeps(List<CellDep> cellDeps) {
        transaction.setCellDeps(cellDeps);
    }

    public List<byte[]> getHeaderDeps() {
        return transaction.getHeaderDeps();
    }

    public void setHeaderDeps(List<byte[]> headerDeps) {
        transaction.setHeaderDeps(headerDeps);
    }

    public List<CellInput> getInputs() {
        return transaction.getInputs();
    }

    public void setInputs(List<CellInput> inputs) {
        transaction.setInputs(inputs);
    }

    public List<Cell> getOutputs() {
        return transaction.getOutputs();
    }

    public void setOutputs(List<Cell> outputs) {
        transaction.setOutputs(outputs);
    }

    public List<byte[]> getOutputsData() {
        return transaction.getOutputsData();
    }

    public List<byte[]> getWitnesses() {
        return transaction.getWitnesses();
    }

    public void setWitnesses(List<byte[]> witnesses) {
        transaction.setWitnesses(witnesses);
    }

    public byte[] hash() {
        return transaction.hash();
    }

    public enum Status {
        @SerializedName("pending")
        PENDING,
        @SerializedName("proposed")
        PROPOSED,
        @SerializedName("committed")
        COMMITTED,
        @SerializedName("unknown")
        UNKNOWN,
        @SerializedName("reject")
        REJECTED
    }

    protected static class TypeAdapter implements JsonDeserializer<OnChainTransaction> {
        @Override
        public OnChainTransaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            JsonObject objTxStatus = obj.getAsJsonObject("tx_status");
            OnChainTransaction tx = new OnChainTransaction();
            tx.transaction = context.deserialize(obj.getAsJsonObject("transaction"), Transaction.class);
            tx.status = context.deserialize(objTxStatus.get("status"), Status.class);
            tx.statusReason = context.deserialize(objTxStatus.get("reason"), String.class);
            tx.blockHash = context.deserialize(objTxStatus.get("block_hash"), byte[].class);
            return tx;
        }
    }
}