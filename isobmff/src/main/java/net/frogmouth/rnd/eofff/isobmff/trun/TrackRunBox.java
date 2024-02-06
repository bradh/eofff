package net.frogmouth.rnd.eofff.isobmff.trun;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * Track Run Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.8.8.
 */
public class TrackRunBox extends FullBox {
    public static final FourCC TRUN_ATOM = new FourCC("trun");

    private long sampleCount;
    private int dataOffset;
    private long firstSampleFlags;
    private final List<TrackRunSample> samples = new ArrayList<>();

    public TrackRunBox() {
        super(TRUN_ATOM);
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

    // TODO: write

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("sample_count=");
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
