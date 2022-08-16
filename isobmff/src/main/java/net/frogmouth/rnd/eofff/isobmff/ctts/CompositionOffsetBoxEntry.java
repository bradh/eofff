package net.frogmouth.rnd.eofff.isobmff.ctts;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public record CompositionOffsetBoxEntry(long sampleCount, long sampleOffset) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sample_count=");
        sb.append(sampleCount);
        sb.append(", sample_offset=");
        sb.append(sampleOffset);
        return sb.toString();
    }

    public long getSize(int version) {
        if (version == 0) {
            return 2 * Integer.BYTES;
        } else {
            return 2 * Integer.BYTES;
        }
    }

    void writeTo(OutputStreamWriter stream, int version) throws IOException {
        if (version == 0) {
            stream.writeInt((int) sampleCount);
            stream.writeInt((int) sampleOffset);
        } else if (version == 1) {
            stream.writeInt((int) sampleCount);
            stream.writeInt((int) sampleOffset);
        }
    }
}
