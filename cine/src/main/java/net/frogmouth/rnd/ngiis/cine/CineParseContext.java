package net.frogmouth.rnd.ngiis.cine;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CineParseContext {
    private static final Logger LOG = LoggerFactory.getLogger(CineParseContext.class);
    private final MemorySegment memorySegment;
    private long cursor;

    static final ValueLayout.OfByte BYTE_LITTLE_ENDIAN =
            ValueLayout.JAVA_BYTE.withByteAlignment(1).withOrder(ByteOrder.LITTLE_ENDIAN);
    static final ValueLayout.OfShort SHORT_LITTLE_ENDIAN =
            ValueLayout.JAVA_SHORT.withByteAlignment(1).withOrder(ByteOrder.LITTLE_ENDIAN);
    static final ValueLayout.OfInt INT_LITTLE_ENDIAN =
            ValueLayout.JAVA_INT.withByteAlignment(1).withOrder(ByteOrder.LITTLE_ENDIAN);
    static final ValueLayout.OfLong LONG_LITTLE_ENDIAN =
            ValueLayout.JAVA_LONG.withByteAlignment(1).withOrder(ByteOrder.LITTLE_ENDIAN);
    static final ValueLayout.OfFloat FLOAT_LITTLE_ENDIAN =
            ValueLayout.JAVA_FLOAT.withByteAlignment(1).withOrder(ByteOrder.LITTLE_ENDIAN);
    private long CHUNK_TYPE_BYTE_LEN = 4;

    public CineParseContext(MemorySegment memorySegment) {
        this.memorySegment = memorySegment;
        this.cursor = 0;
    }

    public long getCursorPosition() {
        return cursor;
    }

    public boolean hasRemaining() {
        return cursor < memorySegment.byteSize();
    }

    public int readUnsignedInt8() {
        int i = memorySegment.get(BYTE_LITTLE_ENDIAN, cursor) & 0x00FF;
        cursor += Byte.BYTES;
        return i;
    }

    public int readUnsignedInt16() {
        int i = memorySegment.get(SHORT_LITTLE_ENDIAN, cursor) & 0x00FFFF;
        cursor += Short.BYTES;
        return i;
    }

    public int readInt16() {
        int i = memorySegment.get(SHORT_LITTLE_ENDIAN, cursor);
        cursor += Short.BYTES;
        return i;
    }

    public int readInt32() {
        int i = memorySegment.get(INT_LITTLE_ENDIAN, cursor);
        cursor += Integer.BYTES;
        return i;
    }

    public float readFloat32() {
        float f = memorySegment.get(FLOAT_LITTLE_ENDIAN, cursor);
        cursor += Float.BYTES;
        return f;
    }

    public long readUnsignedInt32() {
        long i = memorySegment.get(INT_LITTLE_ENDIAN, cursor) & 0x00FFFFFFFFl;
        cursor += Integer.BYTES;
        return i;
    }

    public long readInt64() {
        long i = memorySegment.get(LONG_LITTLE_ENDIAN, cursor);
        cursor += Long.BYTES;
        return i;
    }

    public void skipBytes(long l) {
        cursor += l;
    }

    public byte[] getBytes(long numBytes) {
        MemorySegment slice = this.memorySegment.asSlice(this.cursor, numBytes);
        cursor += numBytes;
        return slice.toArray(ValueLayout.JAVA_BYTE);
    }

    public Time64 readTIME64() {
        long fractions = readUnsignedInt32();
        long seconds = readUnsignedInt32();
        return new Time64(fractions, seconds);
    }

    void skipToOffset(long offset) {
        this.cursor = offset;
    }

    String readString(int maxLength) {
        long initialCursorPosition = this.cursor;
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
        this.cursor = initialCursorPosition + maxLength;
        return new String(bytes, 0, len, StandardCharsets.UTF_8);
    }

    public byte readByte() {
        byte b = memorySegment.get(BYTE_LITTLE_ENDIAN, cursor);
        cursor += Byte.BYTES;
        return b;
    }

    int readBool32() {
        return readInt32();
    }

    RECT readRECT() {
        int left = this.readInt32();
        int top = this.readInt32();
        int right = this.readInt32();
        int bottom = this.readInt32();
        return new RECT(left, top, right, bottom);
    }

    WBGAIN readWBGAIN() {
        float r = readFloat32();
        float b = readFloat32();
        return new WBGAIN(r, b);
    }

    IMFILTER readIMFILTER() {
        int dim = readInt32();
        int shifts = readInt32();
        int bias = readInt32();
        int coef[] = new int[25];
        for (int i = 0; i < coef.length; i++) {
            coef[i] = readInt32();
        }
        return new IMFILTER(dim, shifts, bias, coef);
    }

    TC readTC() {
        // TODO: implement
        int byte0 = readUnsignedInt8();
        int framesU = (byte0 & 0xF0) >> 4;
        int framesT = (byte0 & 0b00001100) >> 2;
        int dropFrameFlag = (byte0 & 0b00000010) >> 1;
        int colorFrameFlag = (byte0 & 0x01);
        int byte1 = readUnsignedInt8();
        int secondsU = (byte1 & 0xF0) >> 4;
        int secondsT = (byte1 & 0b00001110) >> 1;
        int flag1 = (byte1 & 0x01);
        int byte2 = readUnsignedInt8();
        int minutesU = (byte2 & 0xF0) >> 4;
        int minutesT = (byte2 & 0b00001110) >> 1;
        int flag2 = (byte2 & 0x01);
        int byte3 = readUnsignedInt8();
        int hoursU = (byte3 & 0xF0) >> 4;
        int hoursT = (byte3 & 0b00001100) >> 2;
        int flag3 = (byte3 & 0b00000010) >> 1;
        int flag4 = (byte3 & 0x01);
        this.skipBytes(4);
        return new TC();
    }

    CineImage readCineImage() {
        long annotationSize = readUnsignedInt32();
        System.out.println("annotation size: " + annotationSize);
        assert (annotationSize == 8);
        skipBytes(annotationSize - 8);
        long imageSize = readUnsignedInt32();
        System.out.println("image size: " + imageSize);
        CineImage image = new CineImage();
        MemorySegment slice = this.memorySegment.asSlice(this.cursor, imageSize);
        byte[] data = slice.toArray(BYTE_LITTLE_ENDIAN);
        image.setPixelData(data);
        return image;
    }
}
