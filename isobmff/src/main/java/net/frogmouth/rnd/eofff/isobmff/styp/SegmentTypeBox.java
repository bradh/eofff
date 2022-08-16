package net.frogmouth.rnd.eofff.isobmff.styp;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.ftyp.FileTypeLikeBox;

/**
 * Segment Type Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.16.2.
 */
public class SegmentTypeBox extends FileTypeLikeBox {

    public SegmentTypeBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "Segment Type Box";
    }
}
