package net.frogmouth.rnd.eofff.isobmff.stts;

import java.io.IOException;
import java.io.OutputStream;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;

public record TimeToSampleEntry(long sampleCount, long sampleDelta) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sample_count=");
        sb.append(sampleCount);
        sb.append(", sample_delta=");
        sb.append(sampleDelta);
        return sb.toString();
    }

    public int getSize() {
        return Integer.BYTES + Integer.BYTES;
    }

    void writeTo(OutputStream stream) throws IOException {
        stream.write(BaseBox.intToBytes((int) sampleCount));
        stream.write(BaseBox.intToBytes((int) sampleDelta));
    }
}
