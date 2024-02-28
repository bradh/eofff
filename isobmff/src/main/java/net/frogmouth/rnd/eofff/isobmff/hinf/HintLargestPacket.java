package net.frogmouth.rnd.eofff.isobmff.hinf;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Hint statistics box - largest packet sent, including RTP header, in bytes (pmax).
 *
 * <p>See ISO/IEC 14496-12:2022 Section 9.1.5
 */
public class HintLargestPacket extends BaseBox {
    public static final FourCC PMAX_ATOM = new FourCC("pmax");
    private long bytes;

    public HintLargestPacket() {
        super(PMAX_ATOM);
    }

    @Override
    public String getFullName() {
        return "LargestPacket";
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    @Override
    public long getBodySize() {
        return Integer.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(bytes);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("bytes=");
        sb.append(bytes);
        return sb.toString();
    }
}
