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
        SampleToChunkBox box = new SampleToChunkBox(new FourCC("stsc"));
        box.setVersion(version);
        box.setFlags(flags);
        for (SampleToChunkEntry entry : entries) {
            box.addEntry(entry);
        }
        return box;
    }
}
