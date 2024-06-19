package net.frogmouth.rnd.eofff.mpeg4.mp4a;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.AudioSampleEntry;

/**
 * MP4 Audio Sample Entry.
 *
 * <p>See ISO/IEC 14496-14 Section 6.7.2.
 */
public class MP4AudioSampleEntry extends AudioSampleEntry {

    public static FourCC MP4A_ATOM = new FourCC("mp4a");

    public MP4AudioSampleEntry() {
        super(MP4A_ATOM);
    }

    @Override
    public String getFullName() {
        return "MP4AudioSampleEntry";
    }
}
