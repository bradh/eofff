package net.frogmouth.rnd.eofff.isobmff;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.frogmouth.rnd.eofff.isobmff.ftyp.Brand;
import net.frogmouth.rnd.eofff.isobmff.tref.TrackReference;
import net.frogmouth.rnd.eofff.isobmff.trgr.TrackGroupType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParseContext {
    private static final Logger LOG = LoggerFactory.getLogger(ParseContext.class);
    private final MemorySegment memorySegment;
    private long cursor;

    static final ValueLayout.OfByte BYTE_BIG_ENDIAN =
            ValueLayout.JAVA_BYTE.withByteAlignment(1).withOrder(ByteOrder.BIG_ENDIAN);
    static final ValueLayout.OfShort SHORT_BIG_ENDIAN =
            ValueLayout.JAVA_SHORT.withByteAlignment(1).withOrder(ByteOrder.BIG_ENDIAN);
    static final ValueLayout.OfInt INT_BIG_ENDIAN =
            ValueLayout.JAVA_INT.withByteAlignment(1).withOrder(ByteOrder.BIG_ENDIAN);
    static final ValueLayout.OfLong LONG_BIG_ENDIAN =
            ValueLayout.JAVA_LONG.withByteAlignment(1).withOrder(ByteOrder.BIG_ENDIAN);
    static final ValueLayout.OfFloat FLOAT_BIG_ENDIAN =
            ValueLayout.JAVA_FLOAT.withByteAlignment(1).withOrder(ByteOrder.BIG_ENDIAN);

    public ParseContext(MemorySegment memorySegment) {
        this.memorySegment = memorySegment;
        this.cursor = 0;
    }

    public boolean hasRemaining() {
        return cursor < memorySegment.byteSize();
    }

    public long getRemainingSizeFrom(long offset) {
        return memorySegment.byteSize() - offset;
    }

    public boolean hasRemainingUntil(long pos) {
        return cursor < pos;
    }

    public int readInt16() {
        int i = memorySegment.get(SHORT_BIG_ENDIAN, cursor);
        cursor += Short.BYTES;
        return i;
    }

    public int readInt32() {
        int i = memorySegment.get(INT_BIG_ENDIAN, cursor);
        cursor += Integer.BYTES;
        return i;
    }

    public long readInt64() {
        long i = memorySegment.get(LONG_BIG_ENDIAN, cursor);
        cursor += Long.BYTES;
        return i;
    }

    public int readUnsignedInt8() {
        int i = memorySegment.get(BYTE_BIG_ENDIAN, cursor) & 0x00FF;
        cursor += Byte.BYTES;
        return i;
    }

    public int readUnsignedInt16() {
        int i = memorySegment.get(SHORT_BIG_ENDIAN, cursor) & 0x00FFFF;
        cursor += Short.BYTES;
        return i;
    }

    public long readUnsignedInt32() {
        long i = memorySegment.get(INT_BIG_ENDIAN, cursor) & 0x00FFFFFFFFl;
        cursor += Integer.BYTES;
        return i;
    }

    // Read, but do not advance the cursor
    public long peekUnsignedInt32() {
        long i = memorySegment.get(INT_BIG_ENDIAN, cursor) & 0x00FFFFFFFFl;
        return i;
    }

    public long readUnsignedInt64() {
        long i = memorySegment.get(LONG_BIG_ENDIAN, cursor);
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

    public UUID readUUID() {
        long highBits = this.readUnsignedInt64();
        long lowBits = this.readUnsignedInt64();
        return new UUID(highBits, lowBits);
    }

    public float readDouble32() {
        float f = memorySegment.get(FLOAT_BIG_ENDIAN, cursor);
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
        byte b = memorySegment.get(BYTE_BIG_ENDIAN, cursor);
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
        return slice.toArray(ValueLayout.JAVA_BYTE);
    }

    public ISO639Language readPackedLanguageCode() {
        return ISO639Language.readPackedLanguageCode(this);
    }

    // See ISO/IEC 14496-1 Section 8.3.3.
    // We could parameterise the maximum number of bytes if needed.
    public int readLengthVariable() {
        final int MAX_BYTES = 4;
        int result = 0;
        for (int i = 0; i < MAX_BYTES; i++) {
            int b = this.readUnsignedInt8();
            result = (result << 7) + (b & 0x7F);
            if ((b & 0x80) != 0x80) {
                break;
            }
        }
        return result;
    }
}
