package net.frogmouth.rnd.eofff.isobmff.stco;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Chunk Offset Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.7.5
 */
public class ChunkOffsetBox extends FullBox {

    private final List<Long> entries = new ArrayList<>();

    public ChunkOffsetBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "ChunkOffsetBox";
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        size += Integer.BYTES;
        size += (entries.size() * Integer.BYTES);
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
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        stream.write(getVersionAndFlagsAsBytes());
        stream.writeInt(entries.size());
        for (long entry : entries) {
            stream.writeInt((int) entry);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("':");
        for (Long item : getEntries()) {
            sb.append("\n");
            sb.append("\t\t\t\t\t  chunk_offset=");
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
