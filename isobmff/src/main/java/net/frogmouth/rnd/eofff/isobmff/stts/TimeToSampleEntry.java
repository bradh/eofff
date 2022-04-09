package net.frogmouth.rnd.eofff.isobmff.stts;

public record TimeToSampleEntry(long sampleCount, long sampleDelta) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sample_count=");
        sb.append(sampleCount);
        sb.append(", sample_delta=");
        sb.append(sampleDelta);
        return sb.toString();
    }
}
