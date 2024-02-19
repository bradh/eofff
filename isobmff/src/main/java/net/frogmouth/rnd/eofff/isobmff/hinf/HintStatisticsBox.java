package net.frogmouth.rnd.eofff.isobmff.hinf;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Hint Statistics Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 9.1.5.
 */
public class HintStatisticsBox extends AbstractContainerBox {

    public static final FourCC HINF_ATOM = new FourCC("hinf");

    public HintStatisticsBox() {
        super(HINF_ATOM);
    }

    @Override
    public String getFullName() {
        return "HintStatisticsBox";
    }
}
