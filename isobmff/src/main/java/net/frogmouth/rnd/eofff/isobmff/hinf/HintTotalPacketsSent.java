package net.frogmouth.rnd.eofff.isobmff.hinf;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Hint statistics box - total packets sent (npck).
 *
 * <p>See ISO/IEC 14496-12:2022 Section 9.1.5
 */
public class HintTotalPacketsSent extends BaseBox {
    public static final FourCC NPCK_ATOM = new FourCC("npck");
    private long packetsSent;

    public HintTotalPacketsSent() {
        super(NPCK_ATOM);
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
        return Integer.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(packetsSent);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("packetssent=");
        sb.append(packetsSent);
        return sb.toString();
    }
}
