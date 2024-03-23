package net.frogmouth.rnd.eofff.mpeg4.mp4v;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntry;

/**
 * MP4 Visual Sample Entry.
 *
 * <p>See ISO/IEC 14496-14 Section 6.7.2.
 */
public class MP4VisualSampleEntry extends VisualSampleEntry {

    public static FourCC MP4V_ATOM = new FourCC("mp4v");

    public MP4VisualSampleEntry() {
        super(MP4V_ATOM);
    }

    @Override
    public String getFullName() {
        return "MP4VisualSampleEntry";
    }
}
