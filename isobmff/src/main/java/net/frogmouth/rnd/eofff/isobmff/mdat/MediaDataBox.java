package net.frogmouth.rnd.eofff.isobmff.mdat;

import java.io.IOException;
import java.util.Arrays;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Media Data Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.1.1
 */
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
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("length=");
        sb.append(data.length);
        return sb.toString();
    }

    public byte[] getDataAt(long offset, long size) {
        int relativeOffsetStart = (int) (offset - initialOffset);
        int relativeOffsetEnd = (int) (relativeOffsetStart + size);
        return Arrays.copyOfRange(data, relativeOffsetStart, relativeOffsetEnd);
    }

    public long appendData(byte[] bytes) {
        long insertionPoint = this.initialOffset + this.data.length;
        byte[] result = new byte[this.data.length + bytes.length];
        System.arraycopy(this.data, 0, result, 0, this.data.length);
        System.arraycopy(bytes, 0, result, this.data.length, bytes.length);
        this.data = result;
        return insertionPoint;
    }
}
