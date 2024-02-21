package net.frogmouth.rnd.eofff.gopro;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * GoPro Metadata box.
 *
 * <p>This box structure is assumed, not actually defined.
 */
public class GoProMetaDataBox extends FullBox {
    public static final FourCC GPMD_ATOM = new FourCC("gpmd");

    public GoProMetaDataBox() {
        super(GPMD_ATOM);
    }

    @Override
    public String getFullName() {
        return "GoPro Metadata";
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
