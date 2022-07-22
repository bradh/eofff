package net.frogmouth.rnd.eofff.isobmff.ctts;

import java.io.IOException;
import java.io.OutputStream;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;

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

    void writeTo(OutputStream stream, int version) throws IOException {
        if (version == 0) {
            stream.write(BaseBox.intToBytes((int) sampleCount));
            stream.write(BaseBox.intToBytes((int) sampleOffset));
        } else if (version == 1) {
            stream.write(BaseBox.intToBytes((int) sampleCount));
            stream.write(BaseBox.intToBytes((int) sampleOffset));
        }
    }
}
