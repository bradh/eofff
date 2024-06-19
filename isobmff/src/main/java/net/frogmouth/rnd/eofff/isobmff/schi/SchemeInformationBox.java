package net.frogmouth.rnd.eofff.isobmff.schi;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Scheme Information Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.12.7.
 */
public class SchemeInformationBox extends AbstractContainerBox {

    public static final FourCC SCHI_ATOM = new FourCC("schi");

    public SchemeInformationBox() {
        super(SCHI_ATOM);
    }

    @Override
    public String getFullName() {
        return "SchemeInformationBox";
    }
}
