package org.ckbj.type;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.math.BigInteger;

@JsonAdapter(DetailedCell.TypeAdapter.class)
public class DetailedCell {
    private Cell cell;
    private byte[] data;
    private Status status;

    private DetailedCell() {
    }

    public static Builder builder() {
        return new Builder();
    }

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

    public byte[] dataHash() {
        // TODO:
//        Blake2b blake2b = new Blake2b();
//        blake2b.update(data);
//        return blake2b.doFinalBytes();
        return null;
    }

    public BigInteger getOccupiedCapacity() {
        // TODO:
        return null;
//        BigInteger byteSize = Utils.ckbToShannon(8);
//        if (data != null) {
//            byteSize = byteSize.add(Utils.ckbToShannon(data.length));
//        }
//        if (cell.getLock() != null) {
//            byteSize = byteSize.add(cell.getLock().occupiedCapacity());
//        }
//        if (cell.getType() != null) {
//            byteSize = byteSize.add(cell.getType().occupiedCapacity());
//        }
//        return byteSize;
    }

    public static final class Builder {
        private Cell.Builder cellBuilder;
        private byte[] data;
        private Status status;

        private Builder() {
            cellBuilder = Cell.builder();
        }

        public Builder setCell(Cell cell) {
            cellBuilder = Cell.builder(cell);
            return this;
        }

        public Builder setCapacityInBytes(int capacityInBytes) {
            cellBuilder.setCapacityInBytes(capacityInBytes);
            return this;
        }

        public Builder setCapacity(BigInteger capacity) {
            cellBuilder.setCapacity(capacity);
            return this;
        }

        public Builder setType(Script type) {
            cellBuilder.setType(type);
            return this;
        }

        public Builder setLock(Script lock) {
            cellBuilder.setLock(lock);
            return this;
        }

        public Builder setData(byte[] data) {
            this.data = data;
            return this;
        }

        public Builder setStatus(Status status) {
            this.status = status;
            return this;
        }

        public DetailedCell build() {
            DetailedCell detailedCell = new DetailedCell();
            detailedCell.data = this.data;
            detailedCell.cell = this.cellBuilder.build();
            detailedCell.status = this.status;
            return detailedCell;
        }
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
