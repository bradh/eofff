package net.frogmouth.rnd.eofff.yuv.converters;

public enum SourceFormat {
    TwoYUV("2yuv", Interleaving.Packed, 1, 3, 0, 2),
    YUV2("yuv2", Interleaving.Packed, 0, 2, 1, 3),
    VYUY("vyuy", Interleaving.Packed, 1, 3, 2, 0),
    YVYU("yvyu", Interleaving.Packed, 0, 2, 3, 1);

    private final String format;
    private final Interleaving interleave;
    private final int y0Offset;
    private final int y1Offset;
    private final int cbOffset;
    private final int crOffset;

    private SourceFormat(
            String format,
            Interleaving interleaving,
            int y0Offset,
            int y1Offset,
            int cbOffset,
            int crOffset) {
        this.format = format;
        this.interleave = interleaving;
        this.y0Offset = y0Offset;
        this.y1Offset = y1Offset;
        this.cbOffset = cbOffset;
        this.crOffset = crOffset;
    }

    public String getFormat() {
        return format;
    }

    public Interleaving getInterleave() {
        return interleave;
    }

    public int getY0Offset() {
        return y0Offset;
    }

    public int getY1Offset() {
        return y1Offset;
    }

    public int getCbOffset() {
        return cbOffset;
    }

    public int getCrOffset() {
        return crOffset;
    }
}
