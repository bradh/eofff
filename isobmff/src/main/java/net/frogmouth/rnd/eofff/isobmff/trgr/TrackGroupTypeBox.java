package net.frogmouth.rnd.eofff.isobmff.trgr;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Track Group Type Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.3.4.
 */
public record TrackGroupTypeBox(TrackGroupType groupType, long groupID, int version, int flags) {

    public TrackGroupTypeBox(TrackGroupType groupType, long groupID) {
        this(groupType, groupID, 0, 0);
    }

    public long getSize() {
        return Integer.BYTES + FourCC.BYTES + 1 + 3 + Integer.BYTES;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("group_type=");
        sb.append(groupType.toString());
        sb.append(", track_group_id=");
        sb.append(groupID);
        return sb.toString();
    }

    void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) getSize());
        stream.writeFourCC(groupType);
        stream.writeInt(0); // version and flags
        stream.writeInt((int) groupID);
    }
}
