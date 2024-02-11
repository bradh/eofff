package net.frogmouth.rnd.eofff.gopro.gpmf;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class GPMFSignedByteItem extends GPMFItem {

    private byte value;

    public GPMFSignedByteItem(FourCC fourCC) {
        super(fourCC);
    }

    public byte getValue() {
        return value;
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = getStringBuilder(nestingLevel);
        sb.append((int) value);
        return sb.toString();
    }

    @Override
    void parse(ParseContext context) {
        value = context.readByte();
        context.skipBytes(Integer.BYTES - Byte.BYTES);
    }

    @Override
    void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBase(writer);
        writer.writeByte(value);
        writer.writeByte(0); // padding
        writer.writeByte(0); // padding
        writer.writeByte(0); // padding
    }

    @Override
    protected int getType() {
        return 98; // 'b'
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
