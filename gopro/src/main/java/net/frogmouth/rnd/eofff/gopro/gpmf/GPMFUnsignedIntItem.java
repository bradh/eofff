package net.frogmouth.rnd.eofff.gopro.gpmf;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class GPMFUnsignedIntItem extends GPMFItem {

    private long value;

    public GPMFUnsignedIntItem(FourCC fourCC) {
        super(fourCC);
    }

    public long getValue() {
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
        value = context.readUnsignedInt32();
    }

    @Override
    void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBase(writer);
        writer.writeUnsignedInt32(value);
    }

    @Override
    protected int getType() {
        return 76; // 'L'
    }

    @Override
    protected int getSampleSize() {
        return Integer.BYTES;
    }

    @Override
    protected int getRepeat() {
        return 1;
    }
}
