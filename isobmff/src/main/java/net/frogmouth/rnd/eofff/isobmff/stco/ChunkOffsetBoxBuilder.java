package net.frogmouth.rnd.eofff.isobmff.stco;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

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
        int size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        size += Integer.BYTES;
        size += (offsets.size() * Integer.BYTES);
        ChunkOffsetBox box = new ChunkOffsetBox(size, new FourCC("stco"));
        box.setVersion(version);
        box.setFlags(flags);
        for (Long offset : offsets) {
            box.addEntry(offset);
        }
        return box;
    }
}
