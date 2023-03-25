package net.frogmouth.rnd.eofff.isobmff.iref;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class SingleItemReferenceBox extends BaseBox {

    private static final long BYTES_IN_BOX_HEADER = Integer.BYTES + FourCC.BYTES;

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

    public long getSize(int version) {
        long size = 0;
        size += BYTES_IN_BOX_HEADER;
        if (version == 0) {
            size += Short.BYTES;
            size += Short.BYTES;
            size += (this.references.size() * Short.BYTES);
        } else {
            size += Integer.BYTES;
            size += Short.BYTES;
            size += (this.references.size() * Integer.BYTES);
        }
        return size;
    }

    void writeTo(OutputStreamWriter stream, int version) throws IOException {
        long boxSize = getSize(version);
        stream.writeUnsignedInt32(boxSize);
        stream.writeFourCC(this.getFourCC());
        if (version == 0) {
            stream.writeUnsignedInt16(fromItemId);
            stream.writeUnsignedInt16(references.size());
            for (long ref : references) {
                stream.writeUnsignedInt16(ref);
            }
        } else {
            stream.writeUnsignedInt32(fromItemId);
            stream.writeUnsignedInt32(references.size());
            for (long ref : references) {
                stream.writeUnsignedInt32(ref);
            }
        }
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
