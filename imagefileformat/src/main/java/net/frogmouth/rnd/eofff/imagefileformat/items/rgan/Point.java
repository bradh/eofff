package net.frogmouth.rnd.eofff.imagefileformat.items.rgan;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class Point implements Region {
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }

    @Override
    public GeometryType getGeometryType() {
        return GeometryType.POINT;
    }

    @Override
    public boolean needsLongFormat() {
        return (x > Short.MAX_VALUE)
                || (x < Short.MIN_VALUE)
                || (y > Short.MAX_VALUE)
                || (y < Short.MIN_VALUE);
    }

    @Override
    public void writeTo(OutputStreamWriter stream, boolean useLongForm) throws IOException {
        if (useLongForm) {
            stream.writeInt(x);
            stream.writeInt(y);
        } else {
            stream.writeShort((short) x);
            stream.writeShort((short) y);
        }
    }
}
