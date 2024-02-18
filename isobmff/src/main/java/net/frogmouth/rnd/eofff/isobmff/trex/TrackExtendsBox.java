package net.frogmouth.rnd.eofff.isobmff.trex;

import java.io.IOException;
import java.util.HexFormat;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Track Extends Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.8.3
 */
public class TrackExtendsBox extends FullBox {

    private long trackID;
    private long defaultSampleDescriptionIndex;
    private long defaultSampleDuration;
    private long defaultSampleSize;
    // TODO: this should probably be a flags structure or class, shared with TrackRunBox
    private long defaultSampleFlags;

    public static final FourCC TREX_ATOM = new FourCC("trex");

    public TrackExtendsBox() {
        super(TREX_ATOM);
    }

    public long getTrackID() {
        return trackID;
    }

    public void setTrackID(long trackID) {
        this.trackID = trackID;
    }

    public long getDefaultSampleDescriptionIndex() {
        return defaultSampleDescriptionIndex;
    }

    public void setDefaultSampleDescriptionIndex(long defaultSampleDescriptionIndex) {
        this.defaultSampleDescriptionIndex = defaultSampleDescriptionIndex;
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
    public String getFullName() {
        return "TrackExtendsBox";
    }

    @Override
    public long getBodySize() {
        return (5 * Integer.BYTES);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeUnsignedInt32(trackID);
        stream.writeUnsignedInt32(defaultSampleDescriptionIndex);
        stream.writeUnsignedInt32(defaultSampleDuration);
        stream.writeUnsignedInt32(defaultSampleSize);
        stream.writeUnsignedInt32(defaultSampleFlags);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("track_ID=");
        sb.append(trackID);
        sb.append(", default_sample_description_index=");
        sb.append(defaultSampleDescriptionIndex);
        sb.append(", default_sample_duration=");
        sb.append(defaultSampleDuration);
        sb.append(", default_sample_size=");
        sb.append(defaultSampleSize);
        sb.append(", default_sample_flags=");
        sb.append(HexFormat.of().withPrefix("0x").toHexDigits(defaultSampleFlags));
        return sb.toString();
    }
}
