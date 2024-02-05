package net.frogmouth.rnd.eofff.isobmff.free;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Skip Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.1.2
 */
public class SkipBox extends FreeSpaceBox {

    public static final FourCC SKIP_ATOM = new FourCC("skip");

    public SkipBox() {
        super(SKIP_ATOM);
    }
}
