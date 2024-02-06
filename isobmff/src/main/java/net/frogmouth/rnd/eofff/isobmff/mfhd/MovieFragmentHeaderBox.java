package net.frogmouth.rnd.eofff.isobmff.mfhd;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Movie Fragment Header Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.8.5.
 */
public class MovieFragmentHeaderBox extends FullBox {
    public static final FourCC MFHD_ATOM = new FourCC("mfhd");
    private long sequenceNumber;

    public MovieFragmentHeaderBox() {
        super(MFHD_ATOM);
    }

    @Override
    public String getFullName() {
        return "MovieFragmentHeaderBox";
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @Override
    public long getBodySize() {
        return Integer.BYTES;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(sequenceNumber);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("': sequence_number=");
        sb.append(getSequenceNumber());
        return sb.toString();
    }
}
