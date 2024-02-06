package net.frogmouth.rnd.eofff.isobmff.stz2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Compact Sample Size Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.7.3.3.
 */
public class CompactSampleSizeBox extends FullBox {

    public static final FourCC STZ2_ATOM = new FourCC("stz2");

    private int field_size;
    private final List<Integer> entries = new ArrayList<>();

    public CompactSampleSizeBox() {
        super(STZ2_ATOM);
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += 3; // reserved
        size += 1; // field_size
        size += Integer.BYTES; // sample_count
        size += (entries.size() * field_size / Byte.SIZE);
        return size;
    }

    @Override
    public String getFullName() {
        return "CompactSampleSizeBox";
    }

    public int getFieldSize() {
        return field_size;
    }

    public void setFieldSize(int size) {
        this.field_size = size;
    }

    public void addEntry(Integer entry) {
        this.entries.add(entry);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        for (int i = 0; i < 3; i++) {
            stream.writeByte(0); // reserved
        }
        stream.writeByte(field_size);
        stream.writeUnsignedInt32(entries.size());
        for (int i = 0; i < entries.size(); i++) {
            if (field_size == 16) {
                stream.writeUnsignedInt16(entries.get(i));
            } else if (field_size == 8) {
                stream.writeByte(entries.get(i));
            } else if (field_size == 4) {
                int v = (entries.get(i) << 4) | entries.get(i + 1);
                stream.writeByte(v);
                i++;
            } else {
                throw new UnsupportedOperationException("invalid field size in stz2");
            }
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("field_size: ");
        sb.append(field_size);
        sb.append(", sample_count: ");
        sb.append(entries.size());
        sb.append(", samples:");
        for (Integer entry : entries) {
            sb.append("\n");
            this.addIndent(nestingLevel + 1, sb);
            sb.append("entry_size: ");
            sb.append(entry);
        }
        return sb.toString();
    }
}
