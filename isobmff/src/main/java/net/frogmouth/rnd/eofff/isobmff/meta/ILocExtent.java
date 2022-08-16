package net.frogmouth.rnd.eofff.isobmff.meta;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class ILocExtent {
    private long extentIndex;
    private long extentOffset;
    private long extentLength;

    public long getExtentIndex() {
        return extentIndex;
    }

    public void setExtentIndex(long extentIndex) {
        this.extentIndex = extentIndex;
    }

    public long getExtentOffset() {
        return extentOffset;
    }

    public void setExtentOffset(long extentOffset) {
        this.extentOffset = extentOffset;
    }

    public long getExtentLength() {
        return extentLength;
    }

    public void setExtentLength(long extentLength) {
        this.extentLength = extentLength;
    }

    public void writeTo(OutputStreamWriter stream, int version) throws IOException {
        // TODO: needs more logic to handle variable extentIndex size
        if ((version == 1) || (version == 2)) {
            stream.writeInt((int) extentIndex);
        }
        stream.writeInt((int) extentOffset);
        stream.writeInt((int) extentLength);
    }

    public int getSize(int version) {
        int size = 0;
        // TODO: needs more logic to handle variable extentIndex size
        if ((version == 1) || (version == 2)) {
            size += Integer.BYTES;
        }
        size += (2 * Integer.BYTES);
        return size;
    }
}
