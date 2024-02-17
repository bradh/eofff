package net.frogmouth.rnd.eofff.isobmff.trgr;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Identifiers for different kinds of track groups.
 *
 * <p>This includes those track group kinds identified in ISO/IEC 14496-12:2022(E). There are other
 * reference kinds in other documents.
 */
public class TrackGroupType extends FourCC {

    /**
     * Multi source presentation group.
     *
     * <p>This is {@code msrc}, indicating this track is part of a multi-source presentation.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section 8.3.4.3.
     */
    public static final TrackGroupType MSRC = new TrackGroupType("msrc");

    /**
     * Stereo group.
     *
     * <p>This is {@code ster}, indicating this track is either the left or right view of a stereo
     * pair suitable for playback on a stereoscopic display.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section 8.3.4.4.
     */
    // TODO: this isn't right - needs to be split off.
    public static final TrackGroupType STER = new TrackGroupType("ster");

    public TrackGroupType(int code) {
        super(code);
    }

    public TrackGroupType(String string) {
        super(string);
    }
}
