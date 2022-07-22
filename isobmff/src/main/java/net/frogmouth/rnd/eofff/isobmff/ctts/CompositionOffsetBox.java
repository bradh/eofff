package net.frogmouth.rnd.eofff.isobmff.ctts;

import static net.frogmouth.rnd.eofff.isobmff.BaseBox.intToBytes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * Composition Time to Sample Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.6.1.3.
 */
public class CompositionOffsetBox extends FullBox {
    private final List<CompositionOffsetBoxEntry> entries = new ArrayList<>();

    public CompositionOffsetBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "CompositionOffsetBox";
    }

    public List<CompositionOffsetBoxEntry> getEntries() {
        return new ArrayList<>(this.entries);
    }

    public void addEntry(CompositionOffsetBoxEntry item) {
        this.entries.add(item);
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        stream.write(getVersionAndFlagsAsBytes());
        stream.write(intToBytes(entries.size()));
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
