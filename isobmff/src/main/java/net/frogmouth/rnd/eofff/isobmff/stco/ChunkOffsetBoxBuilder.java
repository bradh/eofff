package net.frogmouth.rnd.eofff.isobmff.stco;

import java.util.ArrayList;
import java.util.List;

public class ChunkOffsetBoxBuilder {

    private int version;
    private int flags;
    private final List<Long> offsets = new ArrayList<>();

    public ChunkOffsetBoxBuilder() {}

    public ChunkOffsetBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public ChunkOffsetBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public ChunkOffsetBoxBuilder addOffset(long offset) {
        offsets.add(offset);
        return this;
    }

    public ChunkOffsetBox build() {
        ChunkOffsetBox box = new ChunkOffsetBox();
        box.setVersion(version);
        box.setFlags(flags);
        for (Long offset : offsets) {
            box.addEntry(offset);
        }
        return box;
    }
}
