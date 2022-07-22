package net.frogmouth.rnd.eofff.isobmff.mdat;

import java.io.IOException;
import java.io.OutputStream;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class MediaDataBox extends BaseBox {

    // private long dataOffset;
    // private long dataLength;
    private byte[] data;

    public MediaDataBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "MediaDataBox";
    }

    /*
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
    */

    public byte[] getData() {
        return data.clone();
    }

    public void setData(byte[] data) {
        this.data = data.clone();
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        stream.write(data);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(getFullName())
                .append(" '")
                .append(getFourCC())
                .append(", length=")
                .append(data.length)
                .toString();
    }
}
