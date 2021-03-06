package net.frogmouth.rnd.eofff.isobmff.moof;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class TrackFragmentHeaderBox extends FullBox {
    private long trackID;
    private long baseDataOffset;
    private long sampleDescriptionIndex;
    private long defaultSampleDuration;
    private long defaultSampleSize;
    private long defaultSampleFlags;

    public TrackFragmentHeaderBox(long size, FourCC name) {
        super(size, name);
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': track_ID=");
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
