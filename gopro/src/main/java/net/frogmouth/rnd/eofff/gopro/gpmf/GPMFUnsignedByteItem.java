package net.frogmouth.rnd.eofff.gopro.gpmf;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class GPMFUnsignedByteItem extends GPMFItem {

    private int value;

    public GPMFUnsignedByteItem(FourCC fourCC) {
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
        value = context.readUnsignedInt8();
        context.skipBytes(Integer.BYTES - Byte.BYTES);
    }

    @Override
    void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBase(writer);
        writer.writeUnsignedInt8(value);
        writer.writeUnsignedInt8(0); // padding
        writer.writeUnsignedInt8(0); // padding
        writer.writeUnsignedInt8(0); // padding
    }

    @Override
    protected int getType() {
        return 66; // 'B
    }

    @Override
    protected int getSampleSize() {
        return Byte.BYTES;
    }

    @Override
    protected int getRepeat() {
        return 1;
    }
}
