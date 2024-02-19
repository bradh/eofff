package net.frogmouth.rnd.eofff.isobmff.hnti;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Movie or Track Hint Information Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 9.1.4.
 */
public class HintInformation extends AbstractContainerBox {

    public static final FourCC HNTI_ATOM = new FourCC("hnti");

    public HintInformation() {
        super(HNTI_ATOM);
    }

    @Override
    public String getFullName() {
        return "HintInformation";
    }
}
