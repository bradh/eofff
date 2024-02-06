package net.frogmouth.rnd.eofff.isobmff.ttyp;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ftyp.*;

/**
 * Track Type Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.3.5
 */
public class TrackTypeBox extends GeneralTypeBox {

    public static final FourCC TTYP_ATOM = new FourCC("ttyp");

    public TrackTypeBox() {
        super(TTYP_ATOM);
    }

    @Override
    public String getFullName() {
        return "TrackTypeBox";
    }
}
