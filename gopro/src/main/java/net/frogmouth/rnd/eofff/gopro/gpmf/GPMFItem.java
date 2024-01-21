package net.frogmouth.rnd.eofff.gopro.gpmf;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

abstract class GPMFItem {
    private final FourCC fourCC;

    public GPMFItem(FourCC fourCC) {
        this.fourCC = fourCC;
    }

    public FourCC getFourCC() {
        return fourCC;
    }

    abstract void parse(ParseContext context);

    abstract String toString(int nestingLevel);

    protected StringBuilder getStringBuilder(int nestingLevel) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nestingLevel; i++) {
            sb.append("    ");
        }
        sb.append(this.getFourCC());
        sb.append(": ");
        return sb;
    }
}
