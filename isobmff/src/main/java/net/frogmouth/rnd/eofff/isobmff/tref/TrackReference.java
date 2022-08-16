package net.frogmouth.rnd.eofff.isobmff.tref;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

/**
 * Identifiers for different kinds of track references.
 *
 * <p>This includes those track reference kinds identified in ISO/IEC 14496-12:2015(E). There are other reference kinds in other documents, potentially including {@code adrc} and {@code auxl} in later versions of 14496. {@code auxl can be found in HEIF - ISO/IEC 23008-12:2017 Section 7.5.3.
 */
public class TrackReference extends FourCC {

    /**
     * Additional audio track reference.
     *
     * <p>This is {@code adda}, indicating additional audio tracks for this base track.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section 12.2.6.
     */
    public static final TrackReference ADDA = new TrackReference("adda");

    /**
     * Content describes track reference.
     *
     * <p>This is {@code cdsc}, indicating this track describes the referenced track.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section 8.3.3.
     */
    public static final TrackReference CDSC = new TrackReference("cdsc");

    /**
     * Font track reference.
     *
     * <p>This is {@code font}, indicating this track uses fonts carried/defined in the referenced
     * track.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section 8.3.3.
     */
    public static final TrackReference FONT = new TrackReference("font");

    /**
     * Hint dependency track reference.
     *
     * <p>This is {@code hind}, indicating this track depends on the referenced hint track, i.e., it
     * should only be used if the referenced hint track is used.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section 8.3.3.
     */
    public static final TrackReference HIND = new TrackReference("hind");

    /**
     * Hint track reference.
     *
     * <p>This is {@code hint}, indicating the referenced track(s) contain the original media for
     * this hint track.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section 8.3.3.
     */
    public static final TrackReference HINT = new TrackReference("hint");

    /**
     * Subtitle track reference.
     *
     * <p>This is {@code subt}, indicating this track contains subtitle, timed text or overlay
     * graphical information for the referenced track or any track in the alternate group to which
     * the track belongs, if any.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section 8.3.3.
     */
    public static final TrackReference SUBT = new TrackReference("subt");

    /**
     * Video auxiliary depth track reference.
     *
     * <p>This is {@code vdep}, indicating this track contains auxiliary depth video information for
     * the referenced video track.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section 8.3.3.
     */
    public static final TrackReference VDEP = new TrackReference("vdep");

    /**
     * Video parallax track reference.
     *
     * <p>This is {@code vplx}, indicating this track contains auxiliary parallax video information
     * for the referenced video track.
     *
     * <p>See ISO/IEC 14496-12:2015(E) Section 8.3.3.
     */
    public static final TrackReference VPLX = new TrackReference("vplx");

    public TrackReference(int code) {
        super(code);
    }

    public TrackReference(String string) {
        super(string);
    }
}
