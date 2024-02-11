package net.frogmouth.rnd.eofff.gopro.gpmf;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class GPMFFloatItem extends GPMFItem {

    private float value;

    public GPMFFloatItem(FourCC fourCC) {
        super(fourCC);
    }

    public float getValue() {
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
        value = context.readDouble32();
    }

    @Override
    void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBase(writer);
        writer.writeDouble32(value);
    }

    @Override
    protected int getType() {
        return 102; // 'f'
    }

    @Override
    protected int getSampleSize() {
        return Float.BYTES;
    }

    @Override
    protected int getRepeat() {
        return 1;
    }
}
