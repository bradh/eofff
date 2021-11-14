package net.frogmouth.rnd.eofff.isobmff.meta;

import java.util.ArrayList;
import java.util.List;

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
