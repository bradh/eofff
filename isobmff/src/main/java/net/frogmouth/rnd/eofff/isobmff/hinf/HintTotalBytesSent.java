package net.frogmouth.rnd.eofff.isobmff.hinf;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Hint statistics box - total bytes sent (totl).
 *
 * <p>See ISO/IEC 14496-12:2022 Section 9.1.5
 */
public class HintTotalBytesSent extends BaseBox {
    public static final FourCC TOTL_ATOM = new FourCC("totl");
    private long bytesSent;

    public HintTotalBytesSent() {
        super(TOTL_ATOM);
    }

    @Override
    public String getFullName() {
        return "TotalBytesSent";
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
