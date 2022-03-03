package net.frogmouth.rnd.eofff.isobmff.sidx;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * Segment Index Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.16.3.
 */
class SegmentIndexBox extends FullBox {

    private long referenceId;
    private long timescale;
    private long earliestPresentationTime;
    private long firstOffset;
    private final List<SegmentIndexReference> references = new ArrayList<>();

    public SegmentIndexBox(long size, FourCC name) {
        super(size, name);
    }

    public long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(long referenceId) {
        this.referenceId = referenceId;
    }

    public long getTimescale() {
        return timescale;
    }

    public void setTimescale(long timescale) {
        this.timescale = timescale;
    }

    public long getEarliestPresentationTime() {
        return earliestPresentationTime;
    }

    public void setEarliestPresentationTime(long earliestPresentationTime) {
        this.earliestPresentationTime = earliestPresentationTime;
    }

    public long getFirstOffset() {
        return firstOffset;
    }

    public void setFirstOffset(long firstOffset) {
        this.firstOffset = firstOffset;
    }

    public void addReference(SegmentIndexReference reference) {
        this.references.add(reference);
    }

    public List<SegmentIndexReference> getReferences() {
        return new ArrayList<>(this.references);
    }

    @Override
    public String getFullName() {
        return "Segment Index Box";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': reference_ID=");
        sb.append(getReferenceId());
        sb.append(", timescale=");
        sb.append(getTimescale());
        sb.append(", earliest_presentation_time=");
        sb.append(getEarliestPresentationTime());
        sb.append(", first_offset=");
        sb.append(getFirstOffset());
        for (SegmentIndexReference reference : getReferences()) {
            sb.append("\n");
            sb.append("\t\t\t");
            sb.append(reference.toString());
        }
        return sb.toString();
    }
}
