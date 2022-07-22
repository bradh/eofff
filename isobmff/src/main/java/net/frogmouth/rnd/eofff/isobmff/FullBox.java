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

    protected byte[] getVersionAndFlagsAsBytes() {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) version;
        bytes[1] = (byte) ((flags >> 16) & 0xFF);
        bytes[2] = (byte) ((flags >> 8) & 0xFF);
        bytes[3] = (byte) ((flags) & 0xFF);
        return bytes;
    }
}
