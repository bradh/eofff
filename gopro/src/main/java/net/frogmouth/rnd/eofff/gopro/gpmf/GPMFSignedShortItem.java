package net.frogmouth.rnd.eofff.gopro.gpmf;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class GPMFSignedShortItem extends GPMFItem {

    private int value;

    public GPMFSignedShortItem(FourCC fourCC) {
        super(fourCC);
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = getStringBuilder(nestingLevel);
        sb.append(value);
        return sb.toString();
    }

    @Override
    void parse(ParseContext context) {
        value = context.readInt16();
        context.skipBytes(Integer.BYTES - Short.BYTES);
    }

    @Override
    void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBase(writer);
        writer.writeShort((short) value);
        writer.writeUnsignedInt16(0); // padding
    }

    @Override
    protected int getType() {
        return 115; // 's'
    }

    @Override
    protected int getSampleSize() {
        return Short.BYTES;
    }

    @Override
    protected int getRepeat() {
        return 1;
    }
}
