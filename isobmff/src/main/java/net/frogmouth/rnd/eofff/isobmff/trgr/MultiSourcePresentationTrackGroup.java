package net.frogmouth.rnd.eofff.isobmff.trgr;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Multi source presentation group.
 *
 * <p>This is {@code msrc}, indicating this track is part of a multi-source presentation.
 *
 * <p>See ISO/IEC 14496-12:2022(E) Section 8.3.4.3.
 */
public class MultiSourcePresentationTrackGroup extends BaseTrackGroupTypeBox {

    public static final FourCC MSRC = new FourCC("msrc");

    public MultiSourcePresentationTrackGroup() {
        super(MSRC);
    }

    @Override
    public String getFullName() {
        return "MultiSourcePresentationTrackGroup";
    }
}
