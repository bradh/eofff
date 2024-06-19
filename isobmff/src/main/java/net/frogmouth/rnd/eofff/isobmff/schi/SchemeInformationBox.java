package net.frogmouth.rnd.eofff.isobmff.minf;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Media Information Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.4.4.
 */
public class MediaInformationBox extends AbstractContainerBox {

    public static final FourCC MINF_ATOM = new FourCC("minf");

    public MediaInformationBox() {
        super(MINF_ATOM);
    }

    @Override
    public String getFullName() {
        return "MediaInformationBox";
    }
}
