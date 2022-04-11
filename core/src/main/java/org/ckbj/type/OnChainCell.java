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

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell) {
        this.cell = cell;
    }

    public BigInteger getCapacity() {
        return cell.getCapacity();
    }

    public void setCapacity(BigInteger capacity) {
        this.setCapacity(capacity);
    }

    public Script getType() {
        return cell.getType();
    }

    public void setType(Script type) {
        this.setType(type);
    }

    public Script getLock() {
        return cell.getLock();
    }

    public void setLock(Script lock) {
        this.setLock(lock);
    }

    public byte[] getData() {
        return cell.getData();
    }

    public void setData(byte[] data) {
        this.cell.setData(data);
    }

    public void setData(String data) {
        this.cell.setData(data);
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

    public void setOutPoint(OutPoint outPoint) {
        this.outPoint = outPoint;
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
