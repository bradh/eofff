package net.frogmouth.rnd.eofff.isobmff;

public class FullBox extends BaseBox {

    private int version;
    private int flags;

    public FullBox(long size, FourCC name) {
        super(size, name);
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public boolean isFlagSet(int bitmask) {
        return ((this.flags & bitmask) == bitmask);
    }
}
