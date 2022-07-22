package net.frogmouth.rnd.eofff.isobmff;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class BaseBox implements Box {

    private long size;
    private FourCC boxName;

    public BaseBox(long size, FourCC name) {
        setBoxName(name);
        setSize(size);
    }

    @Override
    public long getSize() {
        return size;
    }

    public void adjustSize(long adjustment) {
        size += adjustment;
    }

    @Override
    public byte[] getSizeAsBytes() {
        // TODO: handle largebox case
        return intToBytes((int) size);
    }

    public final void setSize(long size) {
        this.size = size;
    }

    @Override
    public FourCC getFourCC() {
        return boxName;
    }

    public final void setBoxName(FourCC name) {
        this.boxName = name;
    }

    @Override
    public String getFullName() {
        return "Unimplemented Box";
    }

    @Override
    public String toString() {
        return getFullName() + " '" + getFourCC() + "'";
    }

    @Override
    public void writeTo(OutputStream writer) throws IOException {
        System.out.println("need writeTo() implementation for " + boxName.toString());
    }

    public static byte[] intToBytes(int i) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(i);
        buffer.rewind();
        return buffer.array();
    }

    public static byte[] shortToBytes(short i) {
        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
        buffer.putShort(i);
        buffer.rewind();
        return buffer.array();
    }

    public static byte[] longToBytes(long i) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(i);
        buffer.rewind();
        return buffer.array();
    }
}
