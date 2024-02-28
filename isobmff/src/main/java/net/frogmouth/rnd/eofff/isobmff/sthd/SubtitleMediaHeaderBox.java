package net.frogmouth.rnd.eofff.isobmff.sthd;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Subtitle Media Header Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 12.6.2.
 */
public class SubtitleMediaHeaderBox extends FullBox {
    public static final FourCC STHD_ATOM = new FourCC("sthd");

    public SubtitleMediaHeaderBox() {
        super(STHD_ATOM);
    }

    @Override
    public String getFullName() {
        return "SubtitleMediaHeaderBox";
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
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        return sb.toString();
    }
}
