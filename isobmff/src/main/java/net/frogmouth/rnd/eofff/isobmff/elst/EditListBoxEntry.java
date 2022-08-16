package net.frogmouth.rnd.eofff.isobmff.elst;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public record EditListBoxEntry(
        long segmentDuration, long mediaTime, int mediaRateInteger, int mediaRateFraction) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("segment_duration=");
        sb.append(segmentDuration);
        sb.append(", media_time=");
        sb.append(mediaTime);
        sb.append(", media_rate_integer=");
        sb.append(mediaRateInteger);
        sb.append(", media_rate_fraction=");
        sb.append(mediaRateFraction);
        return sb.toString();
    }

    public long getSize(int version) {
        long size = 0;
        if (version == 1) {
            size += (2 * Long.BYTES);
        } else {
            size += (2 * Integer.BYTES);
        }
        size += (2 * Short.BYTES);
        return size;
    }

    void writeTo(OutputStreamWriter stream, int version) throws IOException {
        if (version == 1) {
            stream.writeLong(segmentDuration);
            stream.writeLong(mediaTime);
        } else {
            stream.writeInt((int) segmentDuration);
            stream.writeInt((int) mediaTime);
        }
        stream.writeShort((short) mediaRateInteger);
        stream.writeShort((short) mediaRateFraction);
    }
}
