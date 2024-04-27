package net.frogmouth.rnd.eofff.isobmff.trun;

public record TrackRunSample(
        long sampleDuration, long sampleSize, long sampleFlags, long sampleCompositionTimeOffset) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sample_duration=");
        sb.append(sampleDuration);
        sb.append(", sample_size=");
        sb.append(sampleSize);
        sb.append(", sample_flags=");
        sb.append(String.format("0x%x", sampleFlags));
        sb.append(", sample_composition_time_offset=");
        sb.append(sampleCompositionTimeOffset);
        return sb.toString();
    }
}
