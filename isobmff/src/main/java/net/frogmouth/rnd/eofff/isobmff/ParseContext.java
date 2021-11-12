package net.frogmouth.rnd.eofff.isobmff;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import jdk.incubator.foreign.MemoryAccess;
import jdk.incubator.foreign.MemorySegment;

public class ParseContext {
    private final MemorySegment memorySegment;
    private long cursor;

    public ParseContext(MemorySegment memorySegment) {
        this.memorySegment = memorySegment;
        this.cursor = 0;
    }

    public boolean hasRemaining() {
        return cursor < memorySegment.byteSize();
    }

    public int getInteger() {
        int i = MemoryAccess.getIntAtOffset(memorySegment, cursor, ByteOrder.BIG_ENDIAN);
        cursor += Integer.BYTES;
        return i;
    }

    public long getUnsignedInteger() {
        long i =
                MemoryAccess.getIntAtOffset(memorySegment, cursor, ByteOrder.BIG_ENDIAN)
                        & 0x00FFFFFFFFl;
        cursor += Integer.BYTES;
        return i;
    }

    public FourCC readFourCC() {
        int i = getInteger();
        return new FourCC(i);
    }

    public void skipBytes(long l) {
        System.out.println("skipping " + l);
        cursor += l;
    }

    public long getCursorPosition() {
        return cursor;
    }

    public byte getByte() {
        byte b = MemoryAccess.getByteAtOffset(memorySegment, cursor);
        cursor += Byte.BYTES;
        return b;
    }

    public void getBytes(byte[] flags) {
        for (int i = 0; i < flags.length; i++) {
            flags[i] = getByte();
        }
    }

    public List<Box> parseNestedBoxes(long limit) {
        List<Box> nestedBoxes = new ArrayList<>();
        while (cursor < limit) {
            nestedBoxes.add(parseBox());
        }
        return nestedBoxes;
    }

    private Box parseBox() {
        long offset = cursor;
        long boxSize = getUnsignedInteger();
        FourCC boxName = readFourCC();
        BoxParser parser = BoxFactoryManager.getParser(boxName);
        Box box = parser.parse(this, offset, boxSize, boxName.toString());
        return box;
    }
}
