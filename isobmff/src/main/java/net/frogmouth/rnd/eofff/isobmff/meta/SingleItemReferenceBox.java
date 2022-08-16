package net.frogmouth.rnd.eofff.isobmff.meta;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class SingleItemReferenceBox extends BaseBox {
    private long fromItemId;
    private final List<Long> references = new ArrayList<>();

    public SingleItemReferenceBox(FourCC name) {
        super(name);
    }

    public long getFromItemId() {
        return fromItemId;
    }

    public void setFromItemId(long fromItemId) {
        this.fromItemId = fromItemId;
    }

    public List<Long> getReferences() {
        return new ArrayList<>(references);
    }

    public void addReference(long reference) {
        this.references.add(reference);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" Reference: ");
        sb.append(this.getFourCC().toString());
        sb.append(", from item_id=");
        sb.append(this.getFromItemId());
        sb.append(" to ");
        for (int i = 0; i < references.size() - 1; i++) {
            sb.append(references.get(i));
            sb.append(", ");
        }
        sb.append(references.get(references.size() - 1));
        return sb.toString();
    }
}
