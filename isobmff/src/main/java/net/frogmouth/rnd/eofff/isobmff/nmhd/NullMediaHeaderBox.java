package net.frogmouth.rnd.eofff.isobmff.nmhd;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Null Media Header Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.4.5.2.
 */
public class NullMediaHeaderBox extends FullBox {

    public static final FourCC NMHD_ATOM = new FourCC("nmhd");

    public NullMediaHeaderBox() {
        super(NMHD_ATOM);
    }

    @Override
    public String getFullName() {
        return "NullMediaHeaderBox";
    }

    @Override
    public long getSize() {
        return Integer.BYTES + FourCC.BYTES + 1 + 3;
    }

    @Override
    public long getBodySize() {
        return 0;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        return sb.toString();
    }
}
