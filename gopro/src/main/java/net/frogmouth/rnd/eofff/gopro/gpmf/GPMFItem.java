package net.frogmouth.rnd.eofff.gopro.gpmf;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

abstract class GPMFItem {
    private final FourCC fourCC;

    public GPMFItem(FourCC fourCC) {
        this.fourCC = fourCC;
    }

    public FourCC getFourCC() {
        return fourCC;
    }

    abstract void parse(ParseContext context);

    abstract String toString(int nestingLevel);

    protected StringBuilder getStringBuilder(int nestingLevel) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nestingLevel; i++) {
            sb.append("    ");
        }
        sb.append(this.getFourCC());
        sb.append(": ");
        return sb;
    }

    abstract void writeTo(OutputStreamWriter writer) throws IOException;

    protected void writeBase(OutputStreamWriter writer) throws IOException {
        writer.writeFourCC(fourCC);
        writer.writeByte(getType());
        writer.writeByte(getSampleSize());
        writer.writeUnsignedInt16(getRepeat());
    }

    protected abstract int getType();

    protected abstract int getSampleSize();

    protected abstract int getRepeat();
}
