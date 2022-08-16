package net.frogmouth.rnd.eofff.isobmff.pasp;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Pixel Aspect Ratio Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 12.x.x.x.
 */
public class PixelAspectRatioBox extends BaseBox {

    private long hSpacing;
    private long vSpacing;

    public PixelAspectRatioBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "PixelAspectRatioBox";
    }

    @Override
    public long getSize() {
        return Integer.BYTES + FourCC.BYTES + 2 * Integer.BYTES;
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
    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        stream.writeUnsignedInt32((int) hSpacing);
        stream.writeUnsignedInt32((int) vSpacing);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append(", hSpacing: ");
        sb.append(this.hSpacing);
        sb.append(", vSpacing: ");
        sb.append(this.vSpacing);
        return sb.toString();
    }
}
