package net.frogmouth.rnd.eofff.isobmff.nmhd;

import java.io.IOException;
import java.io.OutputStream;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * Video Media Header Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.4.5.2.
 */
public class NullMediaHeaderBox extends FullBox {

    public NullMediaHeaderBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "NullMediaHeaderBox";
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
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
