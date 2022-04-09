package net.frogmouth.rnd.eofff.isobmff.ctts;

public record CompositionOffsetBoxEntry(long sampleCount, long sampleOffset) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sample_count=");
        sb.append(sampleCount);
        sb.append(", sample_offset=");
        sb.append(sampleOffset);
        return sb.toString();
    }
}
