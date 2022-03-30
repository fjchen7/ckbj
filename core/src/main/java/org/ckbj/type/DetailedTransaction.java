package org.ckbj.type;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.List;

@JsonAdapter(DetailedTransaction.TypeAdapter.class)
public class DetailedTransaction {
    private Transaction transaction;
    private Status status;
    private String statusReason;
    private byte[] blockHash;

    private DetailedTransaction() {
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Status getStatus() {
        return status;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public byte[] getBlockHash() {
        return blockHash;
    }

    public int getVersion() {
        return transaction.getVersion();
    }

    public List<CellDep> getCellDeps() {
        return transaction.getCellDeps();
    }

    public List<byte[]> getHeaderDeps() {
        return transaction.getHeaderDeps();
    }

    public List<CellInput> getInputs() {
        return transaction.getInputs();
    }

    public List<Cell> getOutputs() {
        return transaction.getOutputs();
    }

    public List<byte[]> getOutputsData() {
        return transaction.getOutputsData();
    }

    public List<byte[]> getWitnesses() {
        return transaction.getWitnesses();
    }

    public byte[] hash() {
        return transaction.hash();
    }

    public enum Status {
        @SerializedName("pending")
        PENDING,
        @SerializedName("proposed")
        PROPOSED,
        @SerializedName("proposed")
        COMMITTED,
        @SerializedName("unknown")
        UNKNOWN,
        @SerializedName("reject")
        REJECTED
    }

    protected static class TypeAdapter implements JsonDeserializer<DetailedTransaction> {
        @Override
        public DetailedTransaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            JsonObject objTxStatus = obj.getAsJsonObject("tx_status");
            DetailedTransaction tx = new DetailedTransaction();
            tx.transaction = context.deserialize(obj.getAsJsonObject("transaction"), Transaction.class);
            tx.status = Status.valueOf(objTxStatus.get("status").getAsString().toUpperCase());
            tx.statusReason = context.deserialize(objTxStatus.get("reason"), String.class);
            tx.blockHash = context.deserialize(objTxStatus.get("block_hash"), byte[].class);
            return tx;
        }
    }
}