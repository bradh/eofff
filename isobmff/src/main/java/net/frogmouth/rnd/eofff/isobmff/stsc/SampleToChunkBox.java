package net.frogmouth.rnd.eofff.isobmff.stsc;

import static net.frogmouth.rnd.eofff.isobmff.BaseBox.intToBytes;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * Sample to Chunk Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.7.4
 */
public class SampleToChunkBox extends FullBox {

    private final List<SampleToChunkEntry> entries = new ArrayList<>();

    public SampleToChunkBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "SampleToChunkBox";
    }

    public List<SampleToChunkEntry> getEntries() {
        return new ArrayList<>(entries);
    }

    public void addEntry(SampleToChunkEntry entry) {
        this.entries.add(entry);
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        stream.write(getVersionAndFlagsAsBytes());
        stream.write(intToBytes(entries.size()));
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
