package net.frogmouth.rnd.eofff.uncompressed.sbpm;

public record PixelCoordinate(long row, long column) {
    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }
}
