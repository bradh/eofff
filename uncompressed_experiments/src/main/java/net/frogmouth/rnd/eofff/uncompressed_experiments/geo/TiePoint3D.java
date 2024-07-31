package net.frogmouth.rnd.eofff.uncompressed_experiments.geo;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public record TiePoint3D(int i, int j, double x, double y, double z) {

    public static final int BYTES = (2 * Integer.BYTES) + (3 * Double.BYTES);

    public void writeTo(OutputStreamWriter writer) throws IOException {
        writer.writeUnsignedInt32(i);
        writer.writeUnsignedInt32(j);
        writer.writeDouble64(x);
        writer.writeDouble64(y);
        writer.writeDouble64(z);
    }
}
