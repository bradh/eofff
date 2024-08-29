package net.frogmouth.rnd.eofff.ogc;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.ParseContext;

public record TiePoint3D(int i, int j, double x, double y, double z) {

    static TiePoint3D parseFrom(ParseContext parseContext) {
        long i = parseContext.readUnsignedInt32();
        long j = parseContext.readUnsignedInt32();
        double x = parseContext.readDouble64();
        double y = parseContext.readDouble64();
        double z = parseContext.readDouble64();
        return new TiePoint3D((int) i, (int) j, x, y, z);
    }

    public static final int BYTES = (2 * Integer.BYTES) + (3 * Double.BYTES);

    public void writeTo(OutputStreamWriter writer) throws IOException {
        writer.writeUnsignedInt32(i);
        writer.writeUnsignedInt32(j);
        writer.writeDouble64(x);
        writer.writeDouble64(y);
        writer.writeDouble64(z);
    }

    public void addToStringBuilder(StringBuilder sb) {
        sb.append("(");
        sb.append(i);
        sb.append(",");
        sb.append(j);
        sb.append(") -> (");
        sb.append(x);
        sb.append(",");
        sb.append(y);
        sb.append(",");
        sb.append(z);
        sb.append(")");
    }
}
