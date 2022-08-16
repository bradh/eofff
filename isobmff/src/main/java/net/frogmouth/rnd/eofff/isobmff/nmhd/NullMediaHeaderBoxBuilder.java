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
        NullMediaHeaderBox box = new NullMediaHeaderBox(new FourCC("nmhd"));
        box.setVersion(version);
        box.setFlags(flags);
        return box;
    }
}
