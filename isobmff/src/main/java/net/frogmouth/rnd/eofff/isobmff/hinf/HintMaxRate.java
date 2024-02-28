package net.frogmouth.rnd.eofff.isobmff.hinf;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Hint statistics box - maximum rate, bytes per period in milliseconds (maxr).
 *
 * <p>See ISO/IEC 14496-12:2022 Section 9.1.5
 */
public class HintMaxRate extends BaseBox {
    public static final FourCC MAXR_ATOM = new FourCC("maxr");
    private long bytes;
    private long period;

    public HintMaxRate() {
        super(MAXR_ATOM);
    }

    @Override
    public String getFullName() {
        return "MaxRate";
    }

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    @Override
    public long getBodySize() {
        return Integer.BYTES + Integer.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(period);
        stream.writeUnsignedInt32(bytes);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("period=");
        sb.append(period);
        sb.append(", bytes=");
        sb.append(bytes);
        return sb.toString();
    }
}
