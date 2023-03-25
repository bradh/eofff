package net.frogmouth.rnd.eofff.imagefileformat.items.rgan;

import static net.frogmouth.rnd.eofff.imagefileformat.items.rgan.RegionItem.MAX_UNSIGNED_16_BITS;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class Ellipse implements Region {
    private final int x;
    private final int y;
    private final long radiusX;
    private final long radiusY;

    public Ellipse(int x, int y, long radiusX, long radiusY) {
        this.x = x;
        this.y = y;
        if (radiusX < 0) {
            throw new IllegalArgumentException("X radius is unsigned, it can't be negative");
        }
        this.radiusX = radiusX;
        if (radiusY < 0) {
            throw new IllegalArgumentException("Y radius is unsigned, it can't be negative");
        }
        this.radiusY = radiusY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public long getRadiusX() {
        return radiusX;
    }

    public long getRadiusY() {
        return radiusY;
    }

    @Override
    public GeometryType getGeometryType() {
        return GeometryType.ELLIPSE;
    }

    @Override
    public boolean needsLongFormat() {
        return (x > Short.MAX_VALUE)
                || (x < Short.MIN_VALUE)
                || (y > Short.MAX_VALUE)
                || (y < Short.MIN_VALUE)
                || (radiusX > MAX_UNSIGNED_16_BITS)
                || (radiusY > MAX_UNSIGNED_16_BITS);
    }

    @Override
    public void writeTo(OutputStreamWriter stream, boolean useLongForm) throws IOException {
        if (useLongForm) {
            stream.writeInt(x);
            stream.writeInt(y);
            stream.writeUnsignedInt32(radiusX);
            stream.writeUnsignedInt32(radiusY);
        } else {
            stream.writeShort((short) x);
            stream.writeShort((short) y);
            stream.writeUnsignedInt16(radiusX);
            stream.writeUnsignedInt16(radiusY);
        }
    }
}
