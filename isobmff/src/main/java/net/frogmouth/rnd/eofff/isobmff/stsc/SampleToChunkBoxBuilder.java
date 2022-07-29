package net.frogmouth.rnd.eofff.isobmff.stsc;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class SampleToChunkBoxBuilder {

    private int version;
    private int flags;
    private List<SampleToChunkEntry> entries = new ArrayList<>();

    public SampleToChunkBoxBuilder() {}

    public SampleToChunkBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public SampleToChunkBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public SampleToChunkBoxBuilder addEntry(SampleToChunkEntry enty) {
        this.entries.add(enty);
        return this;
    }

    public SampleToChunkBox build() {
        int size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        size += Integer.BYTES;
        for (SampleToChunkEntry entry : entries) {
            size += entry.getSize();
        }
        SampleToChunkBox box = new SampleToChunkBox(size, new FourCC("stsc"));
        box.setVersion(version);
        box.setFlags(flags);
        for (SampleToChunkEntry entry : entries) {
            box.addEntry(entry);
        }
        return box;
    }
}
