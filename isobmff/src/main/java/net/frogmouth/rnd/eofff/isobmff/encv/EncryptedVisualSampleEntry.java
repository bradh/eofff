package net.frogmouth.rnd.eofff.isobmff.encv;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.VisualSampleEntry;

/**
 * Encrypted Visual Sample Entry.
 *
 * <p>See ISO/IEC 14496-12 Section 8.12.
 */
public class EncryptedVisualSampleEntry extends VisualSampleEntry {

    public static FourCC ENCV_ATOM = new FourCC("encv");

    public EncryptedVisualSampleEntry() {
        super(ENCV_ATOM);
    }

    @Override
    public String getFullName() {
        return "EncryptedVisualSampleEntry";
    }
}
