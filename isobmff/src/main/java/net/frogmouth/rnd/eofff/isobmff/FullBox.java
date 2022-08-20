package net.frogmouth.rnd.eofff.isobmff;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.trgr.TrackGroupBox;

public class FullBox extends BaseBox {
    private static final long BYTES_IN_FULL_BOX_HEADER = Integer.BYTES + FourCC.BYTES + 1 + 3;
    private static final long BYTES_IN_LARGE_FULL_BOX_HEADER =
            Integer.BYTES + FourCC.BYTES + Long.BYTES + 1 + 3;

    private int version;
    private int flags;

    public FullBox(FourCC name) {
        super(name);
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

    private boolean needLargeSize(long bodySize) {
        return 0xFFFFFFFFL < bodySize + BYTES_IN_FULL_BOX_HEADER;
    }

    @Override
    protected void writeBoxHeader(OutputStreamWriter stream) throws IOException {
        long bodySize = getBodySize();
        if (needLargeSize(bodySize)) {
            long boxSize = BYTES_IN_LARGE_FULL_BOX_HEADER + bodySize;
            stream.writeUnsignedInt32(TrackGroupBox.LARGE_SIZE_FLAG);
            stream.writeFourCC(this.getFourCC());
            stream.writeLong(boxSize);
        } else {
            long boxSize = BYTES_IN_FULL_BOX_HEADER + bodySize;
            stream.writeUnsignedInt32(boxSize);
            stream.writeFourCC(this.getFourCC());
        }
        stream.write(getVersionAndFlagsAsBytes());
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
