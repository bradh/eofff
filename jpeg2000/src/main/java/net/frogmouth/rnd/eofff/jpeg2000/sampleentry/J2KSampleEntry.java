package net.frogmouth.rnd.eofff.jpeg2000.sampleentry;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntry;

/**
 * JPEG 2000 Sample Entry.
 *
 * <p>See ISO/IEC 15444-16:2021 Section 7.3.1
 */
public class J2KSampleEntry extends VisualSampleEntry {

    public static final FourCC J2KI_ATOM = new FourCC("j2ki");

    public J2KSampleEntry() {
        super(J2KI_ATOM);
        setCompressorName("JPEG 2000");
    }

    @Override
    public String getFullName() {
        return "J2KSampleEntry";
    }
}
