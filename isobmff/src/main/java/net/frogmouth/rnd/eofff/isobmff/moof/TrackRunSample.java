package net.frogmouth.rnd.eofff.isobmff.moof;

public class TrackRunSample {
    private long sampleDuration;
    private long sampleSize;
    private long sampleFlags;
    private long sampleCompositionTimeOffset;

    public long getSampleDuration() {
        return sampleDuration;
    }

    public void setSampleDuration(long sampleDuration) {
        this.sampleDuration = sampleDuration;
    }

    public long getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(long sampleSize) {
        this.sampleSize = sampleSize;
    }

    public long getSampleFlags() {
        return sampleFlags;
    }

    public void setSampleFlags(long sampleFlags) {
        this.sampleFlags = sampleFlags;
    }

    public long getSampleCompositionTimeOffset() {
        return sampleCompositionTimeOffset;
    }

    public void setSampleCompositionTimeOffset(long sampleCompositionTimeOffset) {
        this.sampleCompositionTimeOffset = sampleCompositionTimeOffset;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sample_duration=");
        sb.append(getSampleDuration());
        sb.append(", sample_size=");
        sb.append(getSampleSize());
        sb.append(", sample_flags=");
        sb.append(String.format("0x%x", getSampleFlags()));
        sb.append(", sample_composition_time_offset=");
        sb.append(getSampleCompositionTimeOffset());
        return sb.toString();
    }
}
