package net.frogmouth.rnd.eofff.isobmff.mdat;

import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class MediaDataBox extends BaseBox {

    private long dataOffset;
    private long dataLength;

    public MediaDataBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "MediaDataBox";
    }

    public long getDataOffset() {
        return dataOffset;
    }

    public void setDataOffset(long offset) {
        dataOffset = offset;
    }

    public long getDataLength() {
        return dataLength;
    }

    public void setDataLength(long dataLength) {
        this.dataLength = dataLength;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(getFullName())
                .append(" '")
                .append(getFourCC())
                .append("': offset=")
                .append(getDataOffset())
                .append(", length=")
                .append(getDataLength())
                .toString();
    }
}
