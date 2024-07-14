package net.frogmouth.rnd.ngiis.png;

import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.nio.ByteOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PngParseContext {
    private static final Logger LOG = LoggerFactory.getLogger(PngParseContext.class);
    private final MemorySegment memorySegment;
    private long cursor;

    static final ValueLayout.OfByte BYTE_BIG_ENDIAN =
            ValueLayout.JAVA_BYTE.withByteAlignment(1).withOrder(ByteOrder.BIG_ENDIAN);
    static final ValueLayout.OfShort SHORT_BIG_ENDIAN =
            ValueLayout.JAVA_SHORT.withByteAlignment(1).withOrder(ByteOrder.BIG_ENDIAN);
    static final ValueLayout.OfInt INT_BIG_ENDIAN =
            ValueLayout.JAVA_INT.withByteAlignment(1).withOrder(ByteOrder.BIG_ENDIAN);
    private long CHUNK_TYPE_BYTE_LEN = 4;

    public PngParseContext(MemorySegment memorySegment) {
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
        int i = memorySegment.get(BYTE_BIG_ENDIAN, cursor) & 0x00FF;
        cursor += Byte.BYTES;
        return i;
    }

    public long readUnsignedInt32() {
        long i = memorySegment.get(INT_BIG_ENDIAN, cursor) & 0x00FFFFFFFFl;
        cursor += Integer.BYTES;
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

    PngChunkType readChunkType() {
        byte[] chunkBytes = getBytes(CHUNK_TYPE_BYTE_LEN);
        PngChunkType chunkType = PngChunkType.lookup(chunkBytes);
        return chunkType;
    }
}
