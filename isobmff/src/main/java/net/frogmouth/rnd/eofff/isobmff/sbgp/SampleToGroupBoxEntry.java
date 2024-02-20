package net.frogmouth.rnd.eofff.isobmff.sbgp;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public record SampleToGroupBoxEntry(long sampleCount, long groupDescriptionIndex) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sample_count=");
        sb.append(sampleCount);
        sb.append(", group_description_index=");
        sb.append(groupDescriptionIndex);
        return sb.toString();
    }

    void writeTo(OutputStreamWriter writer) throws IOException {
        writer.writeUnsignedInt32(sampleCount);
        writer.writeUnsignedInt32(groupDescriptionIndex);
    }
}
