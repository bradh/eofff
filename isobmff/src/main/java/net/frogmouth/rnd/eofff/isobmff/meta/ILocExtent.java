package net.frogmouth.rnd.eofff.isobmff.meta;

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
}
