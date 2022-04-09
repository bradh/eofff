package net.frogmouth.rnd.eofff.isobmff.stco;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class ChunkOffsetBox extends FullBox {

    private final List<Long> entries = new ArrayList<>();

    public ChunkOffsetBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "ChunkOffsetBox";
    }

    public List<Long> getEntries() {
        return new ArrayList<>(entries);
    }

    public void addEntry(Long entry) {
        this.entries.add(entry);
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
