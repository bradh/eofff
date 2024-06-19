package net.frogmouth.rnd.eofff.isobmff.enca;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.AudioSampleEntry;

/**
 * Encrypted Audio Sample Entry.
 *
 * <p>See ISO/IEC 14496-12 Section 8.12.1.
 */
public class EncryptedAudioSampleEntry extends AudioSampleEntry {

    public static FourCC ENCA_ATOM = new FourCC("enca");

    public EncryptedAudioSampleEntry() {
        super(ENCA_ATOM);
    }

    @Override
    public String getFullName() {
        return "EncryptedAudioSampleEntry";
    }
}
