package net.frogmouth.rnd.eofff.isobmff.stsc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Sample to Chunk Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.7.4
 */
public class SampleToChunkBox extends FullBox {

    private final List<SampleToChunkEntry> entries = new ArrayList<>();

    public SampleToChunkBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "SampleToChunkBox";
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        size += Integer.BYTES;
        for (SampleToChunkEntry entry : entries) {
            size += entry.getSize();
        }
        return size;
    }

    public List<SampleToChunkEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    public void addEntry(SampleToChunkEntry entry) {
        this.entries.add(entry);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        stream.write(getVersionAndFlagsAsBytes());
        stream.writeInt(entries.size());
        for (SampleToChunkEntry entry : entries) {
            entry.writeTo(stream);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("':");
        for (SampleToChunkEntry item : getEntries()) {
            sb.append("\n");
            sb.append("\t\t\t\t\t  ");
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
