package net.frogmouth.rnd.eofff.isobmff.trgr;

import java.io.IOException;
import java.io.OutputStream;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public record TrackGroupTypeBox(FourCC groupType, long groupID) {

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

    void writeTo(OutputStream stream) throws IOException {
        stream.write(BaseBox.intToBytes((int) getSize()));
        stream.write(groupType.toBytes());
        stream.write(BaseBox.intToBytes(0)); // version and flags
        stream.write(BaseBox.intToBytes((int) groupID));
    }
}
