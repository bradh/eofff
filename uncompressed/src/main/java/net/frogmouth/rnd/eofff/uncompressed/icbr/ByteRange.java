package net.frogmouth.rnd.eofff.uncompressed.icbr;

public record ByteRange(long rangeOffset, long rangeSize) {

    public String toString() {
        return "range_offset=" + rangeOffset + ", range_size=" + rangeSize;
    }
}
