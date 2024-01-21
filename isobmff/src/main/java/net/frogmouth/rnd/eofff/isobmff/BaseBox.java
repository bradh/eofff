package net.frogmouth.rnd.eofff.isobmff;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.trgr.TrackGroupBox;

public class BaseBox implements Box {

    private static final long BYTES_IN_BOX_HEADER = Integer.BYTES + FourCC.BYTES;
    private static final long BYTES_IN_LARGE_BOX_HEADER = Integer.BYTES + FourCC.BYTES + Long.BYTES;
    protected static final int LARGE_SIZE_FLAG = 1;

    private static final String INDENT = "    ";
    // private long size;
    private FourCC boxName;

    public BaseBox(FourCC name) {
        setBoxName(name);
    }

    @Override
    public long getSize() {
        long bodySize = getBodySize();
        if (needLargeSize(bodySize)) {
            return bodySize + BYTES_IN_LARGE_BOX_HEADER;
        } else {
            return bodySize + BYTES_IN_BOX_HEADER;
        }
    }

    @Override
    public long getBodySize() {
        throw new UnsupportedOperationException(
                "can't get size of unimplemented box : " + boxName.toString());
    }

    @Override
    public FourCC getFourCC() {
        return boxName;
    }

    public final void setBoxName(FourCC name) {
        this.boxName = name;
    }

    @Override
    public String getFullName() {
        return "Unimplemented Box";
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = getBaseStringBuilder(nestingLevel);
        sb.append("TODO");
        return sb.toString();
    }

    protected StringBuilder getBaseStringBuilder(int nestingLevel) {
        StringBuilder sb = new StringBuilder();
        addIndent(nestingLevel, sb);
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': ");
        return sb;
    }

    protected void addIndent(int nestingLevel, StringBuilder sb) {
        for (int i = 0; i < nestingLevel; i++) {
            sb.append(INDENT);
        }
    }

    @Override
    public void writeTo(OutputStreamWriter writer) throws IOException {
        System.out.println("need writeTo() implementation for " + boxName.toString());
    }

    private boolean needLargeSize(long bodySize) {
        return 0xFFFFFFFFL < bodySize + BYTES_IN_BOX_HEADER;
    }

    protected void writeBoxHeader(OutputStreamWriter stream) throws IOException {
        long bodySize = getBodySize();
        if (needLargeSize(bodySize)) {
            long boxSize = BYTES_IN_LARGE_BOX_HEADER + bodySize;
            stream.writeUnsignedInt32(TrackGroupBox.LARGE_SIZE_FLAG);
            stream.writeFourCC(this.getFourCC());
            stream.writeLong(boxSize);
        } else {
            long boxSize = BYTES_IN_BOX_HEADER + bodySize;
            stream.writeUnsignedInt32(boxSize);
            stream.writeFourCC(this.getFourCC());
        }
    }
}
