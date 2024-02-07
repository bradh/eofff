package net.frogmouth.rnd.eofff.isobmff.cslg;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * Composition Time to Sample Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.6.1.4.
 */
public class CompositionToDecodeBox extends FullBox {

    public static final FourCC CSLG_ATOM = new FourCC("cslg");

    private long compositionToDTSShift;
    private long leastDecodeToDisplayDelta;
    private long greatestDecodeToDisplayDelta;
    private long compositionStartTime;
    private long compositionEndTime;

    public CompositionToDecodeBox() {
        super(CSLG_ATOM);
    }

    @Override
    public String getFullName() {
        return "CompositionToDecodeBox";
    }

    public long getCompositionToDTSShift() {
        return compositionToDTSShift;
    }

    public void setCompositionToDTSShift(long compositionToDTSShift) {
        this.compositionToDTSShift = compositionToDTSShift;
    }

    public long getLeastDecodeToDisplayDelta() {
        return leastDecodeToDisplayDelta;
    }

    public void setLeastDecodeToDisplayDelta(long leastDecodeToDisplayDelta) {
        this.leastDecodeToDisplayDelta = leastDecodeToDisplayDelta;
    }

    public long getGreatestDecodeToDisplayDelta() {
        return greatestDecodeToDisplayDelta;
    }

    public void setGreatestDecodeToDisplayDelta(long greatestDecodeToDisplayDelta) {
        this.greatestDecodeToDisplayDelta = greatestDecodeToDisplayDelta;
    }

    public long getCompositionStartTime() {
        return compositionStartTime;
    }

    public void setCompositionStartTime(long compositionStartTime) {
        this.compositionStartTime = compositionStartTime;
    }

    public long getCompositionEndTime() {
        return compositionEndTime;
    }

    public void setCompositionEndTime(long compositionEndTIme) {
        this.compositionEndTime = compositionEndTIme;
    }

    // TODO: write

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("(version=");
        sb.append(getVersion());
        sb.append("): compositionToDTSShift=");
        sb.append(getCompositionToDTSShift());
        sb.append(", leastDecodeToDisplayDelta=");
        sb.append(getLeastDecodeToDisplayDelta());
        sb.append(", greatestDecodeToDisplayDelta=");
        sb.append(getGreatestDecodeToDisplayDelta());
        sb.append(", compositionStartTime=");
        sb.append(this.getCompositionStartTime());
        sb.append(", compositionEndTime=");
        sb.append(this.getCompositionEndTime());
        return sb.toString();
    }
}
