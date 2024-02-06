package net.frogmouth.rnd.eofff.isobmff.traf;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Track Fragment Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.8.6.
 */
public class TrackFragmentBox extends AbstractContainerBox {
    public static final FourCC TRAF_ATOM = new FourCC("traf");

    public TrackFragmentBox() {
        super(TRAF_ATOM);
    }

    @Override
    public String getFullName() {
        return "TrackFragmentBox";
    }
}
