package net.frogmouth.rnd.eofff.gopro.gpmf;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class GPMFUnhandledItem extends GPMFItem {

    private final int sampleSize;
    private final int repeat;

    public GPMFUnhandledItem(FourCC fourCC, int sampleSize, int repeat) {
        super(fourCC);
        this.sampleSize = sampleSize;
        this.repeat = repeat;
    }

    @Override
    void parse(ParseContext parseContext) {
        int bodyBytes = sampleSize * repeat;
        System.out.println("skipping: " + bodyBytes);
        parseContext.skipBytes(bodyBytes);
        int residual = bodyBytes % Integer.BYTES;
        if (residual != 0) {
            parseContext.skipBytes(Integer.BYTES - residual);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = getStringBuilder(nestingLevel);
        sb.append("--[UNHANDLED]--");
        return sb.toString();
    }
}
