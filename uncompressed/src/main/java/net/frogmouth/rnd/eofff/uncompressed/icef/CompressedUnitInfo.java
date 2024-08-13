package net.frogmouth.rnd.eofff.uncompressed.icef;

public record CompressedUnitInfo(long unitOffset, long unitSize) {

    public String toString() {
        return "unit_offset=" + unitOffset + ", unit_size=" + unitSize;
    }
}
