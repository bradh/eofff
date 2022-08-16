package net.frogmouth.rnd.eofff.isobmff.tref;

import java.io.IOException;
import java.util.Arrays;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Track Reference Type Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.3.3.
 */
public record TrackReferenceTypeBox(TrackReference referenceType, long[] trackIDs) {

    public long getSize() {
        return Integer.BYTES + FourCC.BYTES + trackIDs.length * Integer.BYTES;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("reference_type=");
        sb.append(referenceType.toString());
        sb.append(", track_IDs=");
        sb.append(Arrays.toString(trackIDs));
        return sb.toString();
    }

    void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeUnsignedInt32((int) this.getSize());
        stream.writeFourCC(referenceType);
        for (long id : trackIDs) {
            stream.writeUnsignedInt32((int) id);
        }
    }
}
