package net.frogmouth.rnd.eofff.gopro.gpmf;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
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
}
