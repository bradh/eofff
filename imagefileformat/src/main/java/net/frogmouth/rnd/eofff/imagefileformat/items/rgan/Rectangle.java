package net.frogmouth.rnd.eofff.imagefileformat.items.rgan;

import static net.frogmouth.rnd.eofff.imagefileformat.items.rgan.RegionItem.MAX_UNSIGNED_16_BITS;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class Rectangle implements Region {
    private final int x;
    private final int y;
    private final long width;
    private final long height;

    public Rectangle(int x, int y, long width, long height) {
        this.x = x;
        this.y = y;
        if (width < 0) {
            throw new IllegalArgumentException("width is unsigned, it can't be negative");
        }
        this.width = width;
        if (height < 0) {
            throw new IllegalArgumentException("height is unsigned, it can't be negative");
        }
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public long getWidth() {
        return width;
    }

    public long getHeight() {
        return height;
    }

    @Override
    public GeometryType getGeometryType() {
        return GeometryType.RECTANGLE;
    }

    @Override
    public boolean needsLongFormat() {
        return (x > Short.MAX_VALUE)
                || (x < Short.MIN_VALUE)
                || (y > Short.MAX_VALUE)
                || (y < Short.MIN_VALUE)
                || (width > MAX_UNSIGNED_16_BITS)
                || (height > MAX_UNSIGNED_16_BITS);
    }

    @Override
    public void writeTo(OutputStreamWriter stream, boolean useLongForm) throws IOException {
        if (useLongForm) {
            stream.writeInt(x);
            stream.writeInt(y);
            stream.writeUnsignedInt32(width);
            stream.writeUnsignedInt32(height);
        } else {
            stream.writeShort((short) x);
            stream.writeShort((short) y);
            stream.writeUnsignedInt16(width);
            stream.writeUnsignedInt16(height);
        }
    }
}
