package org.ckbj.type;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import org.ckbj.crypto.Blake2b;
import org.ckbj.utils.Hex;

import java.lang.reflect.Type;
import java.math.BigInteger;

@JsonAdapter(OnChainCell.TypeAdapter.class)
public class OnChainCell {
    private Cell cell = new Cell();
    private byte[] data;
    private Status status = Status.UNKNOWN;
    private OutPoint outPoint;

    public Cell toCell() {
        return cell;
    }

    public BigInteger getCapacity() {
        return cell.getCapacity();
    }

    public Script getType() {
        return cell.getType();
    }

    public Script getLock() {
        return cell.getLock();
    }

    public byte[] getData() {
        return data;
    }

    public Status getStatus() {
        return status;
    }

    public OutPoint getOutPoint() {
        return outPoint;
    }

    public OnChainCell setCell(Cell cell) {
        this.cell = cell;
        return this;
    }

    public OnChainCell setCapacity(BigInteger capacity) {
        this.cell.setCapacity(capacity);
        return this;
    }

    public OnChainCell setType(Script type) {
        this.cell.setType(type);
        return this;
    }

    public OnChainCell setLock(Script lock) {
        this.cell.setLock(lock);
        return this;
    }

    public OnChainCell setData(byte[] data) {
        this.data = data;
        return this;
    }

    public OnChainCell setData(String data) {
        return setData(Hex.toByteArray(data));
    }

    public OnChainCell setStatus(Status status) {
        this.status = status;
        return this;
    }

    public OnChainCell setOutPoint(OutPoint outPoint) {
        this.outPoint = outPoint;
        return this;
    }

    public byte[] dataHash() {
        return Blake2b.digest256(data);
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
            onChainCell.data = context.deserialize(obj.getAsJsonObject("cell").getAsJsonObject("data").get("content"), byte[].class);
            onChainCell.status = context.deserialize(obj.get("status"), Status.class);
            return onChainCell;
        }
    }
}
