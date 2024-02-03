package net.frogmouth.rnd.eofff.gopro.gpmf;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
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
}
