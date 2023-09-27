package net.frogmouth.rnd.eofff.isobmff;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import jdk.incubator.foreign.MemoryAccess;
import jdk.incubator.foreign.MemorySegment;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.tref.TrackReference;
import net.frogmouth.rnd.eofff.isobmff.trgr.TrackGroupType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParseContext {
    private static final Logger LOG = LoggerFactory.getLogger(ParseContext.class);
    private final MemorySegment memorySegment;
    private long cursor;

    public ParseContext(MemorySegment memorySegment) {
        this.memorySegment = memorySegment;
        this.cursor = 0;
    }

    public boolean hasRemaining() {
        return cursor < memorySegment.byteSize();
    }

    public boolean hasRemainingUntil(long pos) {
        return cursor < pos;
    }

    public int readInt16() {
        int i = MemoryAccess.getShortAtOffset(memorySegment, cursor, ByteOrder.BIG_ENDIAN);
        cursor += Short.BYTES;
        return i;
    }

    public int readInt32() {
        int i = MemoryAccess.getIntAtOffset(memorySegment, cursor, ByteOrder.BIG_ENDIAN);
        cursor += Integer.BYTES;
        return i;
    }

    public long readInt64() {
        long i = MemoryAccess.getLongAtOffset(memorySegment, cursor, ByteOrder.BIG_ENDIAN);
        cursor += Long.BYTES;
        return i;
    }

    public int readUnsignedInt8() {
        int i = MemoryAccess.getByteAtOffset(memorySegment, cursor) & 0x00FF;
        cursor += Byte.BYTES;
        return i;
    }

    public int readUnsignedInt16() {
        int i =
                MemoryAccess.getShortAtOffset(memorySegment, cursor, ByteOrder.BIG_ENDIAN)
                        & 0x00FFFF;
        cursor += Short.BYTES;
        return i;
    }

    public long readUnsignedInt32() {
        long i =
                MemoryAccess.getIntAtOffset(memorySegment, cursor, ByteOrder.BIG_ENDIAN)
                        & 0x00FFFFFFFFl;
        cursor += Integer.BYTES;
        return i;
    }

    public long readUnsignedInt64() {
        long i = MemoryAccess.getLongAtOffset(memorySegment, cursor, ByteOrder.BIG_ENDIAN);
        cursor += Long.BYTES;
        return i;
    }

    public long readUnsignedInt(int numBits) {
        switch (numBits) {
            case Integer.SIZE:
                return readUnsignedInt32();
            case Long.SIZE:
                return readUnsignedInt64();
            case 0:
                return 0;
            default:
                break;
        }
        throw new IllegalArgumentException(
                String.format("Only reading of 32 and 64 bits is supported, not %d", numBits));
    }

    public float readDouble32() {
        float f = MemoryAccess.getFloatAtOffset(memorySegment, cursor, ByteOrder.BIG_ENDIAN);
        cursor += Float.BYTES;
        return f;
    }

    public Brand readBrand() {
        int i = readInt32();
        return new Brand(i);
    }

    public TrackReference readTrackReference() {
        int i = readInt32();
        return new TrackReference(i);
    }

    public TrackGroupType readTrackGroupType() {
        int i = readInt32();
        return new TrackGroupType(i);
    }

    public FourCC readFourCC() {
        int i = readInt32();
        return new FourCC(i);
    }

    public long getCursorPosition() {
        return cursor;
    }

    public void setCursorPosition(long l) {
        if (cursor != l) {
            LOG.warn("Adjusting cursor by {} which might mean missing parsing", l - cursor);
        }
        cursor = l;
    }

    public void skipBytes(long l) {
        LOG.debug("skipping {}", l);
        cursor += l;
    }

    public byte readByte() {
        byte b = MemoryAccess.getByteAtOffset(memorySegment, cursor);
        cursor += Byte.BYTES;
        return b;
    }

    public void readBytes(byte[] targetBytes) {
        for (int i = 0; i < targetBytes.length; i++) {
            targetBytes[i] = readByte();
        }
    }

    public List<Box> parseNestedBoxes(long limit) {
        List<Box> nestedBoxes = new ArrayList<>();
        while (cursor < limit) {
            nestedBoxes.add(parseBox());
        }
        return nestedBoxes;
    }

    public Box parseBox() {
        long offset = cursor;
        long boxSize = readUnsignedInt32();
        FourCC boxName = readFourCC();
        BoxParser parser = BoxFactoryManager.getParser(boxName);
        Box box = parser.parse(this, offset, boxSize, boxName);
        setCursorPosition(offset + boxSize);
        return box;
    }

    public String readNullDelimitedString(long maxLength) {
        if ((maxLength < 0) || (maxLength > Integer.MAX_VALUE)) {
            throw new IllegalArgumentException(
                    "There is no way this code can produce a string that long.");
        }
        int maxLen = (int) maxLength;
        byte[] bytes = new byte[maxLen];
        int len = 0;
        while (len < maxLength) {
            bytes[len] = readByte();
            if (bytes[len] == 0x00) {
                break;
            }
            len += 1;
        }
        return new String(bytes, 0, len);
    }

    public ByteBuffer getByteBuffer(long offset, long length) {
        MemorySegment slice = this.memorySegment.asSlice(offset, length);
        ByteBuffer byteBuffer = slice.asByteBuffer();
        return byteBuffer;
    }

    public byte[] getBytes(long numBytes) {
        MemorySegment slice = this.memorySegment.asSlice(this.cursor, numBytes);
        cursor += numBytes;
        return slice.toByteArray();
    }
}
