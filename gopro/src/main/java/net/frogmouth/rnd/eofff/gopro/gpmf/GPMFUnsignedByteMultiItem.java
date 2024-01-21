package net.frogmouth.rnd.eofff.gopro.gpmf;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class GPMFUnsignedByteMultiItem extends GPMFItem {

    private final List<Integer> values = new ArrayList<>();
    private final int sampleSize;
    private final int repeat;

    public GPMFUnsignedByteMultiItem(FourCC fourCC, int sampleSize, int repeat) {
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
            int numSamples = sampleSize / Byte.BYTES;
            for (int n = 0; n < numSamples; n++) {
                int i = context.readUnsignedInt8();
                values.add(i);
            }
        }
        int residual = (sampleSize * repeat) % Integer.BYTES;
        if (residual != 0) {
            context.skipBytes(Integer.BYTES - residual);
        }
    }
}
