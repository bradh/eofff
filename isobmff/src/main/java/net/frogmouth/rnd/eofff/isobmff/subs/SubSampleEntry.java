package net.frogmouth.rnd.eofff.isobmff.subs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class SubSampleEntry {

    private long sampleDelta;
    private final List<SubSample> subsamples = new ArrayList<>();

    public long getSampleDelta() {
        return sampleDelta;
    }

    public void setSampleDelta(long sampleDelta) {
        this.sampleDelta = sampleDelta;
    }

    public List<SubSample> getSubsamples() {
        return subsamples;
    }

    public void addSubSample(SubSample subSample) {
        this.subsamples.add(subSample);
    }

    public long getSize(int version) {
        long size = 0;
        size += Integer.BYTES;
        size += Short.BYTES;
        if (version == 1) {
            size += (subsamples.size() * SubSample.VERSION1_BYTES);
        } else {
            size += (subsamples.size() * SubSample.VERSION0_BYTES);
        }
        return size;
    }

    void writeTo(StringBuilder sb, int nestingLevel) {
        sb.append("sample_delta=");
        sb.append(sampleDelta);
        sb.append(", subsample_count=");
        sb.append(subsamples.size());
        for (SubSample subsample : subsamples) {
            sb.append("\n");
            BaseBox.addIndent(nestingLevel + 1, sb);
            subsample.writeTo(sb);
        }
    }

    void writeTo(OutputStreamWriter stream, int version) throws IOException {
        stream.writeUnsignedInt32(sampleDelta);
        stream.writeUnsignedInt16(subsamples.size());
        for (SubSample subsample : subsamples) {
            subsample.writeTo(stream, version);
        }
    }
}
