package net.frogmouth.rnd.eofff.isobmff.tref;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public record TrackReferenceTypeBox(FourCC referenceType, long[] trackIDs) {

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

    void writeTo(OutputStream stream) throws IOException {
        stream.write(BaseBox.intToBytes((int) getSize()));
        stream.write(referenceType.toBytes());
        for (long id : trackIDs) {
            stream.write(BaseBox.intToBytes((int) id));
        }
    }
}
