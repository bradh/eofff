package net.frogmouth.rnd.eofff.isobmff;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/** @author bradh */
public class OutputStreamWriter {

    private final OutputStream outputStream;

    public OutputStreamWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(byte[] bytes) throws IOException {
        outputStream.write(bytes);
    }

    // public void write(int i) throws IOException {
    //     outputStream.write(i);
    // }
    public void writeByte(int i) throws IOException {
        outputStream.write(i);
    }

    public void writeNullTerminatedString(String name) throws IOException {
        write(name.getBytes(StandardCharsets.US_ASCII));
        writeByte(0); // NULL terminator
    }

    public void writeShort(short i) throws IOException {
        outputStream.write(shortToBytes(i));
    }

    private byte[] shortToBytes(short i) {
        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES);
        buffer.putShort(i);
        buffer.rewind();
        return buffer.array();
    }

    public void writeInt(int i) throws IOException {
        outputStream.write(intToBytes(i));
    }

    private byte[] intToBytes(int i) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(i);
        buffer.rewind();
        return buffer.array();
    }

    public void writeLong(long i) throws IOException {
        outputStream.write(longToBytes(i));
    }

    private byte[] longToBytes(long i) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(i);
        buffer.rewind();
        return buffer.array();
    }

    public void writeFourCC(FourCC fourCC) throws IOException {
        outputStream.write(fourCC.toBytes());
    }

    public void writeUnsignedInt32(int i) throws IOException {
        outputStream.write(intToBytes(i));
    }

    public void writeUnsignedInt32(long i) throws IOException {
        // TODO: check range
        outputStream.write(intToBytes((int) i));
    }
}
