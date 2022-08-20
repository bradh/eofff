package net.frogmouth.rnd.eofff.isobmff.ctts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Composition Time to Sample Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.6.1.3.
 */
public class CompositionOffsetBox extends FullBox {
    public static final FourCC CTTS_ATOM = new FourCC("ctts");

    private final List<CompositionOffsetBoxEntry> entries = new ArrayList<>();

    public CompositionOffsetBox() {
        super(CTTS_ATOM);
    }

    @Override
    public String getFullName() {
        return "CompositionOffsetBox";
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        size += Integer.BYTES;
        for (CompositionOffsetBoxEntry entry : entries) {
            size += entry.getSize(getVersion());
        }
        return size;
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES;
        for (CompositionOffsetBoxEntry entry : entries) {
            size += entry.getSize(getVersion());
        }
        return size;
    }

    public List<CompositionOffsetBoxEntry> getEntries() {
        return new ArrayList<>(this.entries);
    }

    public void addEntry(CompositionOffsetBoxEntry item) {
        this.entries.add(item);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeInt(entries.size());
        for (CompositionOffsetBoxEntry entry : entries) {
            entry.writeTo(stream, getVersion());
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("' (version=");
        sb.append(getVersion());
        sb.append("): item_count=");
        sb.append(getEntries().size());
        for (CompositionOffsetBoxEntry item : getEntries()) {
            sb.append("\n");
            sb.append("\t\t\t\t\t  ");
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
