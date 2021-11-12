package net.frogmouth.rnd.eofff.isobmff;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public abstract class BoxParser {

    public abstract String getFourCC();

    public abstract Box parse(
            ByteBuffer byteBuffer, long initialOffset, long boxSize, String boxName);

    protected String readFourCC(ByteBuffer byteBuffer) {
        byte[] dst = new byte[4];
        byteBuffer.get(dst);
        return new String(dst, StandardCharsets.US_ASCII);
    }

    protected int readInteger(ByteBuffer byteBuffer) {
        return byteBuffer.getInt();
    }
}
