package net.frogmouth.rnd.eofff.uncompressed.cpat;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public record PatternDefinitionEntry(int component_index, float component_gain) {
    public static int BYTES = Integer.BYTES + Float.BYTES;

    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeUnsignedInt32(component_index);
        stream.writeDouble32(component_gain);
    }

    public void appendToStringBuilder(StringBuilder sb) {
        sb.append(", component_index=");
        sb.append(component_index);
        sb.append(", component_gain=");
        sb.append(component_gain);
    }
}
