package net.frogmouth.rnd.eofff.isobmff.hinf;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Hint statistics box - largest relative transmission time, in milliseconds (tmax).
 *
 * <p>See ISO/IEC 14496-12:2022 Section 9.1.5
 */
public class HintMaxRelativeTime extends BaseBox {
    public static final FourCC TMAX_ATOM = new FourCC("tmax");
    private long time;

    public HintMaxRelativeTime() {
        super(TMAX_ATOM);
    }

    @Override
    public String getFullName() {
        return "MaxRelativeTime";
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public long getBodySize() {
        return Integer.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(time);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("time=");
        sb.append(time);
        return sb.toString();
    }
}
