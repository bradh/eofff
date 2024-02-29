package net.frogmouth.rnd.eofff.isobmff.mfro;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Movie Fragment Random Access Offset Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.8.11.
 */
public class MovieFragmentRandomAccessOffsetBox extends FullBox {
    public static final FourCC MFRO_ATOM = new FourCC("mfro");

    private long parentSize;

    public MovieFragmentRandomAccessOffsetBox() {
        super(MFRO_ATOM);
    }

    @Override
    public String getFullName() {
        return "MovieFragmentRandomAccessOffsetBox";
    }

    public long getParentSize() {
        return parentSize;
    }

    public void setParentSize(long parentSize) {
        this.parentSize = parentSize;
    }

    @Override
    public long getBodySize() {
        return Integer.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(parentSize);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("parent_size=");
        sb.append(parentSize);
        return sb.toString();
    }
}
