package net.frogmouth.rnd.eofff.isobmff;

public class FullBox extends BaseBox {
    
    private int version;
    private byte[] flags;
    
    public FullBox(long size, String name) {
        super(size, name);
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public byte[] getFlags() {
        return flags;
    }

    public void setFlags(byte[] flags) {
        this.flags = flags;
    }
    
}
