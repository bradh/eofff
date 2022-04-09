package net.frogmouth.rnd.eofff.isobmff.sbgp;

public record SampleToGroupBoxEntry(long sampleCount, long groupDescriptionIndex) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("sample_count=");
        sb.append(sampleCount);
        sb.append(", group_description_index=");
        sb.append(groupDescriptionIndex);
        return sb.toString();
    }
}
