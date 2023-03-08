package net.frogmouth.rnd.eofff.isobmff.mdat;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class MediaDataBox extends BaseBox {

    public static final FourCC MDAT_ATOM = new FourCC("mdat");
    private long initialOffset;
    private byte[] data;

    public MediaDataBox() {
        super(MDAT_ATOM);
    }

    @Override
    public String getFullName() {
        return "MediaDataBox";
    }

    @Override
    public long getBodySize() {
        return data.length;
    }

    public long getInitialOffset() {
        return initialOffset;
    }

    public void setInitialOffset(long initialOffset) {
        this.initialOffset = initialOffset;
    }

    public byte[] getData() {
        return data.clone();
    }

    public void setData(byte[] data) {
        this.data = data.clone();
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
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
