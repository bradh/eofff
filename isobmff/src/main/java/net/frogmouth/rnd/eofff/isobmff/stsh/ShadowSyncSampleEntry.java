package net.frogmouth.rnd.eofff.isobmff.stsh;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public record ShadowSyncSampleEntry(long shadowedSampleNumber, long syncSampleNumber) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("shadowed_sample_number=");
        sb.append(shadowedSampleNumber);
        sb.append(", sync_sample_number=");
        sb.append(syncSampleNumber);
        return sb.toString();
    }

    void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeUnsignedInt32(shadowedSampleNumber);
        stream.writeUnsignedInt32(syncSampleNumber);
    }

    public int getSize() {
        return 2 * Integer.BYTES;
    }
}
