package net.frogmouth.rnd.eofff.gopro.gpmf;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public class GPMFFourCCItem extends GPMFItem {

    private FourCC value;

    public GPMFFourCCItem(FourCC fourCC) {
        super(fourCC);
    }

    public FourCC getValue() {
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
        value = context.readFourCC();
    }
}
