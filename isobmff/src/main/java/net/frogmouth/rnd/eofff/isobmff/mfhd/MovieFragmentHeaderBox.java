package net.frogmouth.rnd.eofff.isobmff.mfhd;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class MovieFragmentHeaderBox extends FullBox {
    public static final FourCC MFHD_ATOM = new FourCC("mfhd");
    private long sequenceNumber;

    public MovieFragmentHeaderBox() {
        super(MFHD_ATOM);
    }

    @Override
    public String getFullName() {
        return "Movie Fragment Header Box";
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    // TODO: write
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': sequence_number=");
        sb.append(getSequenceNumber());
        return sb.toString();
    }
}
