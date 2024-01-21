package net.frogmouth.rnd.eofff.gopro.gpmf;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class GPMFUnsignedLongMultiItem extends GPMFItem {

    private final List<Long> values = new ArrayList<>();
    private final int sampleSize;
    private final int repeat;

    public GPMFUnsignedLongMultiItem(FourCC fourCC, int sampleSize, int repeat) {
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
            int numSamples = sampleSize / Long.BYTES;
            for (int n = 0; n < numSamples; n++) {
                long i = context.readUnsignedInt64();
                values.add(i);
            }
        }
    }
}
