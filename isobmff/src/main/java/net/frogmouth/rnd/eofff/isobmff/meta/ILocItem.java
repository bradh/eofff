package net.frogmouth.rnd.eofff.isobmff.meta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class ILocItem {
    private long itemId;
    private int constructionMethod;
    private int dataReferenceIndex;
    private long baseOffset;
    private final List<ILocExtent> extents = new ArrayList<>();

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public int getConstructionMethod() {
        return constructionMethod;
    }

    public void setConstructionMethod(int constructionMethod) {
        this.constructionMethod = constructionMethod;
    }

    public int getDataReferenceIndex() {
        return dataReferenceIndex;
    }

    public void setDataReferenceIndex(int dataReferenceIndex) {
        this.dataReferenceIndex = dataReferenceIndex;
    }

    public long getBaseOffset() {
        return baseOffset;
    }

    public void setBaseOffset(long baseOffset) {
        this.baseOffset = baseOffset;
    }

    public List<ILocExtent> getExtents() {
        return new ArrayList<>(extents);
    }

    public void addExtent(ILocExtent extent) {
        this.extents.add(extent);
    }

    void writeTo(OutputStreamWriter stream, final int version) throws IOException {
        if (version < 2) {
            stream.writeShort((short) itemId);
        } else {
            stream.writeInt((int) itemId);
        }
        if ((version == 1) || (version == 2)) {
            stream.writeShort((short) constructionMethod);
        }
        stream.writeShort((short) dataReferenceIndex);
        // TODO: handle variable base Offset size - this only handles the case where it can be 4,
        // not 0 or 8
        stream.writeInt((int) baseOffset);
        stream.writeShort((short) extents.size());
        for (ILocExtent extent : extents) {
            extent.writeTo(stream, version);
        }
    }

    public int getSize(final int version) {
        int size = 0;
        if (version < 2) {
            size += Short.BYTES;
        } else {
            size += Integer.BYTES;
        }
        if ((version == 1) || (version == 2)) {
            size += Short.BYTES;
        }
        size += Short.BYTES;
        // TODO: handle variable base Offset size - this only handles the case where it can be 4,
        // not 0 or 8
        size += Integer.BYTES; // base_offset
        size += Short.BYTES; // extent_count
        for (ILocExtent extent : extents) {
            size += extent.getSize(version);
        }
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("item_ID=");
        sb.append(getItemId());
        sb.append(", construction_method=");
        sb.append(getConstructionMethod());
        sb.append(", data_reference_index=");
        sb.append(getDataReferenceIndex());
        sb.append(", base_offset=");
        sb.append(getBaseOffset());
        sb.append(", extent_count=");
        sb.append(getExtents().size());
        for (int i = 0; i < getExtents().size(); i++) {
            ILocExtent extent = new ILocExtent();
            if (extent.getExtentIndex() > 0) {
                sb.append(", extent_index=");
                sb.append(extent.getExtentIndex());
            }
            sb.append(", extent_offset=");
            sb.append(extent.getExtentOffset());
            sb.append(", extent_length=");
            sb.append(extent.getExtentLength());
        }
        sb.append(";");
        return sb.toString();
    }
}
