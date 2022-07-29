package net.frogmouth.rnd.eofff.isobmff.stsc;

import java.io.IOException;
import java.io.OutputStream;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;

public record SampleToChunkEntry(
        long firstChunk, long samplesPerChunk, long sampleDescriptionIndex) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("first_chunk=");
        sb.append(firstChunk);
        sb.append(", samples_per_chunk=");
        sb.append(samplesPerChunk);
        sb.append(", sample_description_index=");
        sb.append(sampleDescriptionIndex);
        return sb.toString();
    }

    void writeTo(OutputStream stream) throws IOException {
        stream.write(BaseBox.intToBytes((int) firstChunk));
        stream.write(BaseBox.intToBytes((int) samplesPerChunk));
        stream.write(BaseBox.intToBytes((int) sampleDescriptionIndex));
    }

    public int getSize() {
        return 3 * Integer.BYTES;
    }
}
