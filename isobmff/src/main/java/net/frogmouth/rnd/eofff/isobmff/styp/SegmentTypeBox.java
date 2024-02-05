package net.frogmouth.rnd.eofff.isobmff.styp;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ftyp.GeneralTypeBox;

/**
 * Segment Type Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.16.2.
 */
public class SegmentTypeBox extends GeneralTypeBox {
    public static final FourCC STYP_ATOM = new FourCC("styp");

    public SegmentTypeBox() {
        super(STYP_ATOM);
    }

    @Override
    public String getFullName() {
        return "Segment Type Box";
    }
}
