package net.frogmouth.rnd.eofff.isobmff.nmhd;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Video Media Header Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.4.5.2.
 */
public class NullMediaHeaderBox extends FullBox {

    public NullMediaHeaderBox(FourCC name) {
        super(name);
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
    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        stream.write(getVersionAndFlagsAsBytes());
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
