package net.frogmouth.rnd.eofff.isobmff.stsc;

public record SampleToChunkEntry(
        long firstChunk, long samplesPerChunk, long sampleDescriptionIndex) {

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("first_chunk=");
        sb.append(firstChunk);
        sb.append(", samples_per_chunk=");
        sb.append(samplesPerChunk);
        sb.append(", sample_description_index=");
        sb.append(sampleDescriptionIndex);
        return sb.toString();
    }
}
