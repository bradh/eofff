package net.frogmouth.rnd.eofff.isobmff.hinf;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Hint statistics box - bytes sent, not including RTP headers (tpay).
 *
 * <p>See ISO/IEC 14496-12:2022 Section 9.1.5
 */
public class HintBytesSent extends BaseBox {
    public static final FourCC TPAY_ATOM = new FourCC("tpay");
    private long bytesSent;

    public HintBytesSent() {
        super(TPAY_ATOM);
    }

    @Override
    public String getFullName() {
        return "PayloadBytesSent";
    }

    public long getBytesSent() {
        return bytesSent;
    }

    public void setBytesSent(long bytesSent) {
        this.bytesSent = bytesSent;
    }

    @Override
    public long getBodySize() {
        return Integer.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(bytesSent);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("bytessent=");
        sb.append(bytesSent);
        return sb.toString();
    }
}
