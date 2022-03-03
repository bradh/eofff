package net.frogmouth.rnd.eofff.isobmff.sidx;

public class SegmentIndexReference {
    private int referenceType;
    private int referencedSize;
    private long subSegmentDuration;
    private boolean startsWithSAP;
    private int sapType;
    private int sapDeltaTime;

    public int getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(int referenceType) {
        this.referenceType = referenceType;
    }

    public int getReferencedSize() {
        return referencedSize;
    }

    public void setReferencedSize(int referencedSize) {
        this.referencedSize = referencedSize;
    }

    public long getSubSegmentDuration() {
        return subSegmentDuration;
    }

    public void setSubSegmentDuration(long subSegmentDuration) {
        this.subSegmentDuration = subSegmentDuration;
    }

    public boolean isStartsWithSAP() {
        return startsWithSAP;
    }

    public void setStartsWithSAP(boolean startsWithSAP) {
        this.startsWithSAP = startsWithSAP;
    }

    public int getSapType() {
        return sapType;
    }

    public void setSapType(int sapType) {
        this.sapType = sapType;
    }

    public int getSapDeltaTime() {
        return sapDeltaTime;
    }

    public void setSapDeltaTime(int sapDeltaTime) {
        this.sapDeltaTime = sapDeltaTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("reference_type=");
        sb.append(getReferenceType());
        sb.append(", referenced_size=");
        sb.append(getReferencedSize());
        sb.append(", subsegment_duration=");
        sb.append(getSubSegmentDuration());
        sb.append(", starts_with_SAP=");
        sb.append(isStartsWithSAP());
        sb.append(", SAP_type=");
        sb.append(getSapType());
        sb.append(", SAP_delta_time=");
        sb.append(getSapDeltaTime());
        return sb.toString();
    }
}
