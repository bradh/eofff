package net.frogmouth.rnd.eofff.isobmff.tref;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Identifiers for different kinds of track references.
 *
 * <p>This includes those track reference kinds identified in ISO/IEC 14496-12:2022(E).
 *
 * <p>There are other reference kinds in other documents.
 */
public class TrackReference extends FourCC {

    /**
     * Additional audio track reference.
     *
     * <p>This is {@code adda}, indicating additional audio tracks for this base track.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section 12.2.6.
     */
    public static final TrackReference ADDA = new TrackReference("adda");

    /**
     * Auxiliary media for the indicated track.
     *
     * <p>This is {@code auxl}.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section 8.3.3.
     */
    public static final TrackReference AUXL = new TrackReference("auxl");

    /**
     * Content describes track reference.
     *
     * <p>This is {@code cdsc}, indicating this track describes the referenced track.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section 8.3.3.
     */
    public static final TrackReference CDSC = new TrackReference("cdsc");

    /**
     * Content describes collectively track reference.
     *
     * <p>This is {@code cdtg}, indicating this track describes the referenced tracks and track
     * groups collectively. This is only valid in timed metadata tracks.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section 8.3.3.
     */
    public static final TrackReference CDTG = new TrackReference("cdtg");

    /**
     * Font track reference.
     *
     * <p>This is {@code font}, indicating this track uses fonts carried/defined in the referenced
     * track.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section 8.3.3.
     */
    public static final TrackReference FONT = new TrackReference("font");

    /**
     * Hint dependency track reference.
     *
     * <p>This is {@code hind}, indicating this track depends on the referenced hint track, i.e., it
     * should only be used if the referenced hint track is used.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section 8.3.3.
     */
    public static final TrackReference HIND = new TrackReference("hind");

    /**
     * Hint track reference.
     *
     * <p>This is {@code hint}, indicating the referenced track(s) contain the original media for
     * this hint track.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section 8.3.3.
     */
    public static final TrackReference HINT = new TrackReference("hint");

    /**
     * Shadow sync track reference.
     *
     * <p>This is {@code shsc}, indicating this track is a shadow sync for the main track..
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section 8.3.3 and 8.6.3.
     */
    public static final TrackReference SHSC = new TrackReference("shsc");

    /**
     * Subtitle track reference.
     *
     * <p>This is {@code subt}, indicating this track contains subtitle, timed text or overlay
     * graphical information for the referenced track or any track in the alternate group to which
     * the track belongs, if any.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section 8.3.3.
     */
    public static final TrackReference SUBT = new TrackReference("subt");

    /**
     * Thumbnail track reference.
     *
     * <p>This is {@code thmb}, indicating this track contains thumbnail images for the referenced
     * track.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section 8.3.3.
     */
    public static final TrackReference THMB = new TrackReference("thmb");

    /**
     * Video auxiliary depth track reference.
     *
     * <p>This is {@code vdep}, indicating this track contains auxiliary depth video information for
     * the referenced video track.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section 8.3.3.
     */
    public static final TrackReference VDEP = new TrackReference("vdep");

    /**
     * Video parallax track reference.
     *
     * <p>This is {@code vplx}, indicating this track contains auxiliary parallax video information
     * for the referenced video track.
     *
     * <p>See ISO/IEC 14496-12:2022(E) Section 8.3.3.
     */
    public static final TrackReference VPLX = new TrackReference("vplx");

    public TrackReference(int code) {
        super(code);
    }

    public TrackReference(String string) {
        super(string);
    }
}
