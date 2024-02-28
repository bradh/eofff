package net.frogmouth.rnd.eofff.isobmff.hinf;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Hint statistics box - total packets sent (nump).
 *
 * <p>See ISO/IEC 14496-12:2022 Section 9.1.5
 */
public class HintTotalPacketsSent64 extends BaseBox {
    public static final FourCC NUMP_ATOM = new FourCC("nump");
    private long packetsSent;

    public HintTotalPacketsSent64() {
        super(NUMP_ATOM);
    }

    @Override
    public String getFullName() {
        return "TotalPacketsSent";
    }

    public long getPacketsSent() {
        return packetsSent;
    }

    public void setPacketsSent(long packetsSent) {
        this.packetsSent = packetsSent;
    }

    @Override
    public long getBodySize() {
        return Long.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeLong(packetsSent);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("packetssent=");
        sb.append(packetsSent);
        return sb.toString();
    }
}
