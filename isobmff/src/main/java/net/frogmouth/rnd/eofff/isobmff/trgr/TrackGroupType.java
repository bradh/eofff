package net.frogmouth.rnd.eofff.isobmff.trgr;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Identifiers for different kinds of track groups.
 *
 * <p>This includes those track group kinds identified in ISO/IEC 14496-12:2015(E). There are other
 * reference kinds in other documents, potentially including {@code ster} in later versions of
 * 14496.
 */
public class TrackGroupType extends FourCC {

    /**
     * Multi source presentation group.
     *
     * <p>This is {@code msrc}, indicating this track is part of a multi-source presentation.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section 8.3.4.3.
     */
    public static final TrackGroupType MSRC = new TrackGroupType("msrc");

    public TrackGroupType(int code) {
        super(code);
    }

    public TrackGroupType(String string) {
        super(string);
    }
}
