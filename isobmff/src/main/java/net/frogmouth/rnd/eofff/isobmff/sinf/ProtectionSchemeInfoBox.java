package net.frogmouth.rnd.eofff.isobmff.sinf;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Protection Scheme Information Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.12.2.
 */
public class ProtectionSchemeInfoBox extends AbstractContainerBox {

    public static final FourCC SINF_ATOM = new FourCC("sinf");

    public ProtectionSchemeInfoBox() {
        super(SINF_ATOM);
    }

    @Override
    public String getFullName() {
        return "ProtectionSchemeInfoBox";
    }
}
