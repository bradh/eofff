package net.frogmouth.rnd.eofff.isobmff.sidx;

public record SegmentIndexReference(
        int referenceType,
        int referencedSize,
        long subSegmentDuration,
        boolean startsWithSAP,
        int sapType,
        int sapDeltaTime) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("reference_type=");
        sb.append(referenceType());
        sb.append(", referenced_size=");
        sb.append(referencedSize());
        sb.append(", subsegment_duration=");
        sb.append(subSegmentDuration());
        sb.append(", starts_with_SAP=");
        sb.append(startsWithSAP());
        sb.append(", SAP_type=");
        sb.append(sapType());
        sb.append(", SAP_delta_time=");
        sb.append(sapDeltaTime());
        return sb.toString();
    }
}
