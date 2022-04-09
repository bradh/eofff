package net.frogmouth.rnd.eofff.isobmff.stsc;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

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
