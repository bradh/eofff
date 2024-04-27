package net.frogmouth.rnd.eofff.isobmff.trun;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Track Run Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.8.8.
 */
public class TrackRunBox extends FullBox {
    public static final FourCC TRUN_ATOM = new FourCC("trun");
    static final int DATA_OFFSET_PRESENT_FLAG = 0x000001;
    static final int FIRST_SAMPLE_FLAGS_PRESENT_FLAG = 0x000004;
    static final int SAMPLE_DURATION_PRESENT_FLAG = 0x000100;
    static final int SAMPLE_SIZE_PRESENT_FLAG = 0x000200;
    static final int SAMPLE_FLAGS_PRESENT_FLAG = 0x000400;
    static final int SAMPLE_COMPOSITION_TIME_OFFSETS_PRESENT_FLAG = 0x000800;

    private long sampleCount;
    private int dataOffset;
    private long firstSampleFlags;
    private final List<TrackRunSample> samples = new ArrayList<>();

    public TrackRunBox() {
        super(TRUN_ATOM);
    }

    @Override
    public String getFullName() {
        return "TrackRunBox";
    }

    public long getSampleCount() {
        return samples.size();
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
    public long getBodySize() {
        int flags = buildFlags();
        this.setFlags(flags);
        long size = 0;
        size += Integer.BYTES; // sample_count
        if ((flags & DATA_OFFSET_PRESENT_FLAG) == DATA_OFFSET_PRESENT_FLAG) {
            size += Integer.BYTES;
        }
        if ((flags & FIRST_SAMPLE_FLAGS_PRESENT_FLAG) == FIRST_SAMPLE_FLAGS_PRESENT_FLAG) {
            size += Integer.BYTES;
        }
        long bytesPerSample = 0;
        if ((flags & SAMPLE_DURATION_PRESENT_FLAG) == SAMPLE_DURATION_PRESENT_FLAG) {
            bytesPerSample += Integer.BYTES;
        }
        if ((flags & SAMPLE_SIZE_PRESENT_FLAG) == SAMPLE_SIZE_PRESENT_FLAG) {
            bytesPerSample += Integer.BYTES;
        }
        if ((flags & SAMPLE_FLAGS_PRESENT_FLAG) == SAMPLE_FLAGS_PRESENT_FLAG) {
            bytesPerSample += Integer.BYTES;
        }
        if ((flags & SAMPLE_COMPOSITION_TIME_OFFSETS_PRESENT_FLAG)
                == SAMPLE_COMPOSITION_TIME_OFFSETS_PRESENT_FLAG) {
            bytesPerSample += Integer.BYTES;
        }
        size += (getSampleCount() * bytesPerSample);
        return size;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(getSampleCount());
        if ((getFlags() & DATA_OFFSET_PRESENT_FLAG) == DATA_OFFSET_PRESENT_FLAG) {
            stream.writeInt(dataOffset);
        }
        if ((getFlags() & FIRST_SAMPLE_FLAGS_PRESENT_FLAG) == FIRST_SAMPLE_FLAGS_PRESENT_FLAG) {
            stream.writeUnsignedInt32(this.firstSampleFlags);
        }
        for (TrackRunSample trackRunSample : this.samples) {
            if ((getFlags() & SAMPLE_DURATION_PRESENT_FLAG) == SAMPLE_DURATION_PRESENT_FLAG) {
                stream.writeUnsignedInt32(trackRunSample.sampleDuration());
            }
            if ((getFlags() & SAMPLE_SIZE_PRESENT_FLAG) == SAMPLE_SIZE_PRESENT_FLAG) {
                stream.writeUnsignedInt32(trackRunSample.sampleSize());
            }
            if ((getFlags() & SAMPLE_FLAGS_PRESENT_FLAG) == SAMPLE_FLAGS_PRESENT_FLAG) {
                stream.writeUnsignedInt32(trackRunSample.sampleFlags());
            }
            if ((getFlags() & SAMPLE_COMPOSITION_TIME_OFFSETS_PRESENT_FLAG)
                    == SAMPLE_COMPOSITION_TIME_OFFSETS_PRESENT_FLAG) {
                // TODO: handle the version 1 case
                stream.writeUnsignedInt32(trackRunSample.sampleCompositionTimeOffset());
            }
        }
    }

    protected int buildFlags() {
        int flags = this.getFlags();
        if (dataOffset != 0) {
            flags |= DATA_OFFSET_PRESENT_FLAG;
        }
        if (firstSampleFlags != 0) {
            flags |= FIRST_SAMPLE_FLAGS_PRESENT_FLAG;
        }
        for (TrackRunSample trackRunSample : this.samples) {
            if (trackRunSample.sampleDuration() != 0) {
                flags |= SAMPLE_DURATION_PRESENT_FLAG;
            }
            if (trackRunSample.sampleSize() != 0) {
                flags |= SAMPLE_SIZE_PRESENT_FLAG;
            }
            if (trackRunSample.sampleFlags() != 0) {
                flags |= SAMPLE_FLAGS_PRESENT_FLAG;
            }
            if (trackRunSample.sampleCompositionTimeOffset() != 0) {
                flags |= SAMPLE_COMPOSITION_TIME_OFFSETS_PRESENT_FLAG;
            }
        }
        return flags;
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("flags=");
        sb.append(String.format("0x%06x", this.getFlags()));
        sb.append(", sample_count=");
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
