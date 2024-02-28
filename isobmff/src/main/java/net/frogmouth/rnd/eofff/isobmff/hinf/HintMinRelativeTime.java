package net.frogmouth.rnd.eofff.isobmff.hinf;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Hint statistics box - smallest relative transmission time, in milliseconds (tmin).
 *
 * <p>See ISO/IEC 14496-12:2022 Section 9.1.5
 */
public class HintMinRelativeTime extends BaseBox {
    public static final FourCC TMIN_ATOM = new FourCC("tmin");
    private long time;

    public HintMinRelativeTime() {
        super(TMIN_ATOM);
    }

    @Override
    public String getFullName() {
        return "MinRelativeTime";
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
