package org.ckbj.type;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import org.ckbj.crypto.Blake2b;
import org.ckbj.utils.Hex;

import java.lang.reflect.Type;
import java.math.BigInteger;

@JsonAdapter(DetailedCell.TypeAdapter.class)
public class DetailedCell {
    private Cell cell = new Cell();
    private byte[] data;
    private Status status = Status.UNKNOWN;

    public Cell getCell() {
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

    public DetailedCell setCell(Cell cell) {
        this.cell = cell;
        return this;
    }

    public DetailedCell setCapacity(BigInteger capacity) {
        this.cell.setCapacity(capacity);
        return this;
    }

    public DetailedCell setType(Script type) {
        this.cell.setType(type);
        return this;
    }

    public DetailedCell setLock(Script lock) {
        this.cell.setLock(lock);
        return this;
    }

    public DetailedCell setData(byte[] data) {
        this.data = data;
        return this;
    }

    public DetailedCell setData(String data) {
        return setData(Hex.decode(data));
    }

    public DetailedCell setStatus(Status status) {
        this.status = status;
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

    protected static class TypeAdapter implements JsonDeserializer<DetailedCell> {
        @Override
        public DetailedCell deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            DetailedCell detailedCell = new DetailedCell();
            JsonObject obj = json.getAsJsonObject();

            detailedCell.cell = context.deserialize(obj.getAsJsonObject("cell").get("output"), Cell.class);
            detailedCell.data = context.deserialize(obj.getAsJsonObject("cell").getAsJsonObject("data").get("content"), byte[].class);
            detailedCell.status = context.deserialize(obj.get("status"), Status.class);
            return detailedCell;
        }
    }
}
