package net.frogmouth.rnd.eofff.isobmff.co64;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Chunk Offset Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.7.5
 */
public class ChunkLargeOffsetBox extends FullBox {
    public static final FourCC CO64_ATOM = new FourCC("co64");

    private final List<Long> entries = new ArrayList<>();

    public ChunkLargeOffsetBox() {
        super(CO64_ATOM);
    }

    @Override
    public String getFullName() {
        return "ChunkLargeOffsetBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES;
        size += (entries.size() * Long.BYTES);
        return size;
    }

    public List<Long> getEntries() {
        return new ArrayList<>(entries);
    }

    public void addEntry(Long entry) {
        this.entries.add(entry);
    }

    public void shiftChunks(long shift) {
        for (int i = 0; i < entries.size(); i++) {
            long entry = entries.get(i);
            entry += shift;
            entries.set(i, entry);
        }
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(entries.size());
        for (long entry : entries) {
            stream.writeLong((int) entry);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        for (Long entry : entries) {
            sb.append("\n");
            this.addIndent(nestingLevel + 1, sb);
            sb.append("chunk_offset=");
            sb.append(entry.toString());
        }
        return sb.toString();
    }
}
