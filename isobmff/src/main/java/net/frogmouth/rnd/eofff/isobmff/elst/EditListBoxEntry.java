package net.frogmouth.rnd.eofff.isobmff.elst;

import java.io.IOException;
import java.io.OutputStream;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;

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

    void writeTo(OutputStream stream, int version) throws IOException {
        if (version == 1) {
            stream.write(BaseBox.longToBytes(segmentDuration));
            stream.write(BaseBox.longToBytes(mediaTime));
        } else {
            stream.write(BaseBox.intToBytes((int) segmentDuration));
            stream.write(BaseBox.intToBytes((int) mediaTime));
        }
        stream.write(BaseBox.shortToBytes((short) mediaRateInteger));
        stream.write(BaseBox.shortToBytes((short) mediaRateFraction));
    }
}
