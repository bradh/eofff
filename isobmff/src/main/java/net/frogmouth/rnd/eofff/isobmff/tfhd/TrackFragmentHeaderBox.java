package net.frogmouth.rnd.eofff.isobmff.tfhd;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Track Fragment Header Box.
 *
 * <p>Note that correct handling of this box is dependent on specific flags.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.8.7.
 */
public class TrackFragmentHeaderBox extends FullBox {

    public static final FourCC TFHD_ATOM = new FourCC("tfhd");
    static final int DEFAULT_SAMPLE_FLAGS_PRESENT_FLAG = 32;
    static final int SAMPLE_DESCRIPTION_INDEX_PRESENT_FLAG = 2;
    static final int DEFAULT_SAMPLE_SIZE_PRESENT_FLAG = 16;
    static final int DEFAULT_SAMPLE_DURATION_PRESENT_FLAG = 8;
    static final int BASE_DATA_OFFSET_PRESENT_FLAG = 1;

    private long trackID;
    private long baseDataOffset;
    private long sampleDescriptionIndex;
    private long defaultSampleDuration;
    private long defaultSampleSize;
    private long defaultSampleFlags;

    public TrackFragmentHeaderBox() {
        super(TFHD_ATOM);
    }

    @Override
    public String getFullName() {
        return "Track Fragment Header Box";
    }

    public long getTrackID() {
        return trackID;
    }

    public void setTrackID(long trackID) {
        this.trackID = trackID;
    }

    public long getBaseDataOffset() {
        return baseDataOffset;
    }

    public void setBaseDataOffset(long baseDataOffset) {
        this.baseDataOffset = baseDataOffset;
    }

    public long getSampleDescriptionIndex() {
        return sampleDescriptionIndex;
    }

    public void setSampleDescriptionIndex(long sampleDescriptionIndex) {
        this.sampleDescriptionIndex = sampleDescriptionIndex;
    }

    public long getDefaultSampleDuration() {
        return defaultSampleDuration;
    }

    public void setDefaultSampleDuration(long defaultSampleDuration) {
        this.defaultSampleDuration = defaultSampleDuration;
    }

    public long getDefaultSampleSize() {
        return defaultSampleSize;
    }

    public void setDefaultSampleSize(long defaultSampleSize) {
        this.defaultSampleSize = defaultSampleSize;
    }

    public long getDefaultSampleFlags() {
        return defaultSampleFlags;
    }

    public void setDefaultSampleFlags(long defaultSampleFlags) {
        this.defaultSampleFlags = defaultSampleFlags;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(trackID);
        if (isFlagSet(BASE_DATA_OFFSET_PRESENT_FLAG)) {
            stream.writeLong(baseDataOffset);
        }
        if (isFlagSet(SAMPLE_DESCRIPTION_INDEX_PRESENT_FLAG)) {
            stream.writeUnsignedInt32(sampleDescriptionIndex);
        }
        if (isFlagSet(DEFAULT_SAMPLE_DURATION_PRESENT_FLAG)) {
            stream.writeUnsignedInt32(defaultSampleDuration);
        }
        if (isFlagSet(DEFAULT_SAMPLE_SIZE_PRESENT_FLAG)) {
            stream.writeUnsignedInt32(defaultSampleSize);
        }
        if (isFlagSet(DEFAULT_SAMPLE_FLAGS_PRESENT_FLAG)) {
            stream.writeUnsignedInt32(defaultSampleFlags);
        }
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES;
        if (isFlagSet(BASE_DATA_OFFSET_PRESENT_FLAG)) {
            size += Long.BYTES;
        }
        if (isFlagSet(SAMPLE_DESCRIPTION_INDEX_PRESENT_FLAG)) {
            size += Integer.BYTES;
        }
        if (isFlagSet(DEFAULT_SAMPLE_DURATION_PRESENT_FLAG)) {
            size += Integer.BYTES;
        }
        if (isFlagSet(DEFAULT_SAMPLE_SIZE_PRESENT_FLAG)) {
            size += Integer.BYTES;
        }
        if (isFlagSet(DEFAULT_SAMPLE_FLAGS_PRESENT_FLAG)) {
            size += Integer.BYTES;
        }
        return size;
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("track_ID=");
        sb.append(getTrackID());
        sb.append(", base_data_offset=");
        sb.append(getBaseDataOffset());
        sb.append(", sample_description_index=");
        sb.append(getSampleDescriptionIndex());
        sb.append(", default_sample_duration=");
        sb.append(getDefaultSampleDuration());
        sb.append(", default_sample_size=");
        sb.append(getDefaultSampleSize());
        sb.append(", default_sample_flags=");
        sb.append(String.format("0x%x", getDefaultSampleFlags()));
        return sb.toString();
    }
}
