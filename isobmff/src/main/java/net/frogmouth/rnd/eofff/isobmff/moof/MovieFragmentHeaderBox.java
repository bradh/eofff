package net.frogmouth.rnd.eofff.isobmff.moof;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class MovieFragmentHeaderBox extends FullBox {
    private long sequenceNumber;

    public MovieFragmentHeaderBox(FourCC name) {
        super(name);
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
