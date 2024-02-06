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
 * <p>See ISO/IEC 14496-12:2022 Section 8.7.4
 */
public class SampleToChunkBox extends FullBox {
    public static final FourCC STSC_ATOM = new FourCC("stsc");

    private final List<SampleToChunkEntry> entries = new ArrayList<>();

    public SampleToChunkBox() {
        super(STSC_ATOM);
    }

    @Override
    public String getFullName() {
        return "SampleToChunkBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
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
        this.writeBoxHeader(stream);
        stream.writeInt(entries.size());
        for (SampleToChunkEntry entry : entries) {
            entry.writeTo(stream);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        for (SampleToChunkEntry item : getEntries()) {
            sb.append("\n");
            this.addIndent(nestingLevel + 1, sb);
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
