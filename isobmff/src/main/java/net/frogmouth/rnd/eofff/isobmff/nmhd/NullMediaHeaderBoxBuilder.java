package net.frogmouth.rnd.eofff.isobmff.nmhd;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class NullMediaHeaderBoxBuilder {

    private int version;
    private int flags;

    public NullMediaHeaderBoxBuilder() {}

    public NullMediaHeaderBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public NullMediaHeaderBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public NullMediaHeaderBox build() {
        int size = Integer.BYTES + FourCC.BYTES + 1 + 3;

        NullMediaHeaderBox box = new NullMediaHeaderBox(size, new FourCC("nmhd"));
        box.setVersion(version);
        box.setFlags(flags);
        return box;
    }
}
