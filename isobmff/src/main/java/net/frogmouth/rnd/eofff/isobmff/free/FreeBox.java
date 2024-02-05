package net.frogmouth.rnd.eofff.isobmff.free;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Free Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.1.2
 */
public class FreeBox extends FreeSpaceBox {

    public static final FourCC FREE_ATOM = new FourCC("free");

    public FreeBox() {
        super(FREE_ATOM);
    }
}
