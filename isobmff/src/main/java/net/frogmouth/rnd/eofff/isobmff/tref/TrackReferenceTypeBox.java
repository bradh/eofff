package net.frogmouth.rnd.eofff.isobmff.tref;

import java.util.Arrays;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public record TrackReferenceTypeBox(FourCC referenceType, long[] trackIDs) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("reference_type=");
        sb.append(referenceType.toString());
        sb.append(", track_IDs=");
        sb.append(Arrays.toString(trackIDs));
        return sb.toString();
    }
}
