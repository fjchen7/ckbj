package org.ckbj.type;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.math.BigInteger;

@JsonAdapter(OnChainCell.TypeAdapter.class)
public class OnChainCell {
    private Cell cell = new Cell();
    private Status status = Status.UNKNOWN;
    private OutPoint outPoint;

    public Cell toCell() {
        return new Cell(cell);
    }

    public BigInteger getCapacity() {
        return cell.getCapacity();
    }

    public OnChainCell setCapacity(BigInteger capacity) {
        this.setCapacity(capacity);
        return this;
    }

    public Script getType() {
        return cell.getType();
    }

    public OnChainCell setType(Script type) {
        this.setType(type);
        return this;
    }

    public Script getLock() {
        return cell.getLock();
    }

    public OnChainCell setLock(Script lock) {
        this.setLock(lock);
        return this;
    }

    public byte[] getData() {
        return cell.getData();
    }

    public OnChainCell setData(byte[] data) {
        this.cell.setData(data);
        return this;
    }

    public OnChainCell setData(String data) {
        this.cell.setData(data);
        return this;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public OutPoint getOutPoint() {
        return outPoint;
    }

    public OnChainCell setOutPoint(OutPoint outPoint) {
        this.outPoint = outPoint;
        return this;
    }

    public byte[] dataHash() {
        return this.cell.dataHash();
    }

    public enum Status {
        @SerializedName("live")
        LIVE,
        @SerializedName("dead")
        DEAD,
        @SerializedName("unknown")
        UNKNOWN
    }

    protected static class TypeAdapter implements JsonDeserializer<OnChainCell> {
        @Override
        public OnChainCell deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            OnChainCell onChainCell = new OnChainCell();
            JsonObject obj = json.getAsJsonObject();

            onChainCell.cell = context.deserialize(obj.getAsJsonObject("cell").get("output"), Cell.class);
            byte[] data = context.deserialize(obj.getAsJsonObject("cell").getAsJsonObject("data").get("content"), byte[].class);
            onChainCell.setData(data);
            onChainCell.status = context.deserialize(obj.get("status"), Status.class);
            return onChainCell;
        }
    }
}
