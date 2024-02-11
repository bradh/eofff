package net.frogmouth.rnd.eofff.gopro.gpmf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class GPMFSignedShortMultiItem extends GPMFItem {

    private final List<Integer> values = new ArrayList<>();
    private final int sampleSize;
    private final int repeat;

    public GPMFSignedShortMultiItem(FourCC fourCC, int sampleSize, int repeat) {
        super(fourCC);
        this.sampleSize = sampleSize;
        this.repeat = repeat;
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = getStringBuilder(nestingLevel);
        sb.append(values);
        return sb.toString();
    }

    @Override
    void parse(ParseContext context) {
        for (int r = 0; r < repeat; r++) {
            int numSamples = sampleSize / Short.BYTES;
            for (int n = 0; n < numSamples; n++) {
                int i = context.readInt16();
                values.add(i);
            }
        }
        int residual = (sampleSize * repeat) % Integer.BYTES;
        if (residual != 0) {
            context.skipBytes(Integer.BYTES - residual);
        }
    }

    @Override
    void writeTo(OutputStreamWriter writer) throws IOException {
        this.writeBase(writer);
        for (int value : values) {
            writer.writeShort((short) value);
        }
        int residual = (sampleSize * repeat) % Integer.BYTES;
        if (residual != 0) {
            for (int i = 0; i < (Integer.BYTES - residual); i++) {
                writer.writeByte(0); // padding;
            }
        }
    }

    @Override
    protected int getType() {
        return 115; // 's'
    }

    @Override
    protected int getSampleSize() {
        return sampleSize;
    }

    @Override
    protected int getRepeat() {
        return repeat;
    }
}
