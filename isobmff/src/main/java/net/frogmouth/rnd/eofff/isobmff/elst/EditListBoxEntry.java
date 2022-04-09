package net.frogmouth.rnd.eofff.isobmff.elst;

public record EditListBoxEntry(
        long segmentDuration, long mediaTime, int mediaRateInteger, int mediaRateFraction) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("segment_duration=");
        sb.append(segmentDuration);
        sb.append(", media_time=");
        sb.append(mediaTime);
        sb.append(", media_rate_integer=");
        sb.append(mediaRateInteger);
        sb.append(", media_rate_fraction=");
        sb.append(mediaRateFraction);
        return sb.toString();
    }
}
