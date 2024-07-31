package net.frogmouth.rnd.eofff.uncompressed_experiments.geo;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public record TiePoint(int i, int j, double x, double y) {

    public static final int BYTES = (2 * Integer.BYTES) + (2 * Double.BYTES);

    public void writeTo(OutputStreamWriter writer) throws IOException {
        writer.writeUnsignedInt32(i);
        writer.writeUnsignedInt32(j);
        writer.writeDouble64(x);
        writer.writeDouble64(y);
    }
}
