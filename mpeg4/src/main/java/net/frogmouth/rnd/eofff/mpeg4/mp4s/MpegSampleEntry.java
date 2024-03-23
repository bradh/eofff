package net.frogmouth.rnd.eofff.mpeg4.mp4s;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.BaseSampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;

/**
 * MP4 Generic Sample Entry.
 *
 * <p>See ISO/IEC 14496-14 Section 6.7.2.
 */
public class MpegSampleEntry extends BaseSampleEntry implements SampleEntry {

    public static FourCC MP4S_ATOM = new FourCC("mp4s");

    public MpegSampleEntry() {
        super(MP4S_ATOM);
    }

    @Override
    public String getFullName() {
        return "MpegSampleEntry";
    }
}
