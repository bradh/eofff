package net.frogmouth.rnd.eofff.isobmff;

import java.nio.ByteBuffer;

public abstract class BoxParser extends AbstractParser {

    public abstract String getFourCC();

    public abstract Box parse(
            ByteBuffer byteBuffer, long initialOffset, long boxSize, String boxName);
}
