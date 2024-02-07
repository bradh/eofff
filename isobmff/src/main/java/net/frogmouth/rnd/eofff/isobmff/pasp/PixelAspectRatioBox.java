package net.frogmouth.rnd.eofff.isobmff.pasp;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Pixel Aspect Ratio Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 12.1.4.
 */
public class PixelAspectRatioBox extends BaseBox {
    public static final FourCC PASP_ATOM = new FourCC("pasp");
    private long hSpacing;
    private long vSpacing;

    public PixelAspectRatioBox() {
        super(PASP_ATOM);
    }

    @Override
    public String getFullName() {
        return "PixelAspectRatioBox";
    }

    public long getHorizontalSpacing() {
        return hSpacing;
    }

    public void setHorizontalSpacing(long hSpacing) {
        this.hSpacing = hSpacing;
    }

    public long getVerticalSpacing() {
        return vSpacing;
    }

    public void setVerticalSpacing(long vSpacing) {
        this.vSpacing = vSpacing;
    }

    @Override
    public long getBodySize() {
        return 2 * Integer.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32((int) hSpacing);
        stream.writeUnsignedInt32((int) vSpacing);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("hSpacing: ");
        sb.append(this.hSpacing);
        sb.append(", vSpacing: ");
        sb.append(this.vSpacing);
        return sb.toString();
    }
}
