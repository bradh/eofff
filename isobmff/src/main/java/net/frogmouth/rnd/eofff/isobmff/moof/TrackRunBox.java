package net.frogmouth.rnd.eofff.isobmff.moof;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class TrackRunBox extends FullBox {
    private long sampleCount;
    private int dataOffset;
    private long firstSampleFlags;
    private final List<TrackRunSample> samples = new ArrayList<>();

    public TrackRunBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "Track Run Box";
    }

    public long getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(long sampleCount) {
        this.sampleCount = sampleCount;
    }

    public int getDataOffset() {
        return dataOffset;
    }

    public void setDataOffset(int dataOffset) {
        this.dataOffset = dataOffset;
    }

    public long getFirstSampleFlags() {
        return firstSampleFlags;
    }

    public void setFirstSampleFlags(long firstSampleFlags) {
        this.firstSampleFlags = firstSampleFlags;
    }

    public void addSample(TrackRunSample sample) {
        samples.add(sample);
    }

    public List<TrackRunSample> getSamples() {
        return new ArrayList<>(samples);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': sample_count=");
        sb.append(getSampleCount());
        sb.append(", data_offset=");
        sb.append(getDataOffset());
        sb.append(", first_sample_flags=");
        sb.append(getFirstSampleFlags());
        for (TrackRunSample sample : getSamples()) {
            sb.append("\n");
            sb.append("\t\t\t");
            sb.append(sample.toString());
        }
        return sb.toString();
    }
}
