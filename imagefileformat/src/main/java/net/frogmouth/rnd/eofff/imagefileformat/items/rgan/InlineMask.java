package net.frogmouth.rnd.eofff.imagefileformat.items.rgan;

import static java.util.zip.Deflater.BEST_COMPRESSION;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class InlineMask implements Region {
    private final int x;
    private final int y;
    private final long width;
    private final long height;
    private final byte[] data;
    private boolean deflate = false;

    public InlineMask(int x, int y, long width, long height, byte[] data) {
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
        this.data = data;
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

    public byte[] getData() {
        return data;
    }

    @Override
    public GeometryType getGeometryType() {
        return GeometryType.INLINE_MASK;
    }

    @Override
    public boolean needsLongFormat() {
        return (x > Short.MAX_VALUE)
                || (x < Short.MIN_VALUE)
                || (y > Short.MAX_VALUE)
                || (y < Short.MIN_VALUE)
                || (width > RegionItem.MAX_UNSIGNED_16_BITS)
                || (height > RegionItem.MAX_UNSIGNED_16_BITS);
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
        if (deflate) {
            Deflater deflater = new Deflater(BEST_COMPRESSION, false);
            deflater.setInput(data);
            deflater.finish();
            byte[] deflateOutputBuffer = new byte[32 * 1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while (!deflater.finished()) {
                int outputSize = deflater.deflate(deflateOutputBuffer);
                baos.write(deflateOutputBuffer, 0, outputSize);
            }
            stream.writeByte(0x01);
            stream.writeUnsignedInt32(baos.size());
            stream.write(baos.toByteArray());
        } else {
            stream.writeByte(0x00);
            stream.write(data);
        }
    }
}
