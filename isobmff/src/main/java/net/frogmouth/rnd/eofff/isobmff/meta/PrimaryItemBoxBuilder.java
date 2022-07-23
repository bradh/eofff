package net.frogmouth.rnd.eofff.isobmff.meta;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class PrimaryItemBoxBuilder {

    private int version;
    private int flags;
    private long itemId;

    public PrimaryItemBoxBuilder() {}

    public PrimaryItemBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public PrimaryItemBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public PrimaryItemBoxBuilder withItemId(long itemId) {
        this.itemId = itemId;
        return this;
    }

    public PitmBox build() {
        int size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        if ((version == 1) || (itemId >= (1 << 16))) {
            size += Integer.BYTES;
        } else {
            size += Short.BYTES;
        }
        PitmBox box = new PitmBox(size, new FourCC("pitm"));
        box.setVersion(version);
        box.setFlags(flags);
        box.setItemID(itemId);
        return box;
    }
}
