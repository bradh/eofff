package net.frogmouth.rnd.eofff.isobmff.mehd;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Movie Extends Header Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.8.2
 */
public class MovieExtendsHeaderBox extends FullBox {

    public static final FourCC MEHD_ATOM = new FourCC("mehd");

    private long fragmentDuration;

    public MovieExtendsHeaderBox() {
        super(MEHD_ATOM);
    }

    public long getFragmentDuration() {
        return fragmentDuration;
    }

    public void setFragmentDuration(long fragmentDuration) {
        this.fragmentDuration = fragmentDuration;
    }

    @Override
    public long getBodySize() {
        long size = 0;
        if (getVersion() == 1) {
            size += Long.BYTES;
        } else {
            size += Integer.BYTES;
        }
        return size;
    }

    @Override
    public String getFullName() {
        return "MovieExtendsHeaderBox";
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        if (getVersion() == 1) {
            stream.writeLong(this.fragmentDuration);
        } else {
            stream.writeUnsignedInt32(this.fragmentDuration);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("fragment_duration=");
        sb.append(fragmentDuration);
        return sb.toString();
    }
}
