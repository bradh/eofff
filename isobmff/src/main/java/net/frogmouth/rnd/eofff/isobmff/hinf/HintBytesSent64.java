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
public class HintBytesSent64 extends BaseBox {
    public static final FourCC TPYL_ATOM = new FourCC("tpyl");
    private long bytesSent;

    public HintBytesSent64() {
        super(TPYL_ATOM);
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
        return Long.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeLong(bytesSent);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("bytessent=");
        sb.append(bytesSent);
        return sb.toString();
    }
}
