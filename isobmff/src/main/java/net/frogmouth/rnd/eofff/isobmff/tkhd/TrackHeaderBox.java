package net.frogmouth.rnd.eofff.isobmff.tkhd;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Track Header Box.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 8.3.2.
 */
public class TrackHeaderBox extends FullBox {

    public static final FourCC TKHD_ATOM = new FourCC("tkhd");
    private static final double SCALE_FACTOR_16_16 = 1.0 / Math.pow(2.0, 16);

    private long creationTime;
    private long modificationTime;
    private long trackId = 1;
    private long duration;
    private int layer = 0;
    private int alternateGroup = 0;
    private int volume = 0;
    private int[] matrix = {65536, 0, 0, 0, 65536, 0, 0, 0, 1073741824};
    private long width;
    private long height;

    public TrackHeaderBox() {
        super(TKHD_ATOM);
    }

    @Override
    public long getBodySize() {
        long size = 0;
        if (getVersion() == 1) {
            size += Long.BYTES;
            size += Long.BYTES;
            size += Integer.BYTES;
            size += Integer.BYTES;
            size += Long.BYTES;
        } else {
            size += Integer.BYTES;
            size += Integer.BYTES;
            size += Integer.BYTES;
            size += Integer.BYTES;
            size += Integer.BYTES;
        }
        size += 2 * Integer.BYTES;
        size += Short.BYTES;
        size += Short.BYTES;
        size += Short.BYTES;
        size += Short.BYTES;
        size += 9 * Integer.BYTES;
        size += Integer.BYTES;
        size += Integer.BYTES;
        return size;
    }

    @Override
    public String getFullName() {
        return "TrackHeaderBox";
    }

    public boolean isEnabled() {
        return ((getFlags() & 0x000001) == 0x000001);
    }

    public boolean isTrackInMovie() {
        return ((getFlags() & 0x000002) == 0x000002);
    }

    public boolean isTrackInPreview() {
        return ((getFlags() & 0x000004) == 0x000004);
    }

    public boolean isTrackSizeAspectRatio() {
        return ((getFlags() & 0x000008) == 0x000008);
    }

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public long getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(long modificationTime) {
        this.modificationTime = modificationTime;
    }

    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getAlternateGroup() {
        return alternateGroup;
    }

    public void setAlternateGroup(int alternateGroup) {
        this.alternateGroup = alternateGroup;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int[] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[] matrix) {
        this.matrix = matrix;
    }

    public long getRawWidth() {
        return width;
    }

    public double getWidth() {
        return width * SCALE_FACTOR_16_16;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getRawHeight() {
        return height;
    }

    public double getHeight() {
        return height * SCALE_FACTOR_16_16;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        if (getVersion() == 1) {
            stream.writeLong(creationTime);
            stream.writeLong(modificationTime);
            stream.writeInt((int) trackId);
            stream.writeInt(0);
            stream.writeLong(duration);
        } else {
            stream.writeInt((int) creationTime);
            stream.writeInt((int) modificationTime);
            stream.writeInt((int) trackId);
            stream.writeInt(0);
            stream.writeInt((int) duration);
        }
        stream.writeInt(0);
        stream.writeInt(0);
        stream.writeShort((short) layer);
        stream.writeShort((short) alternateGroup);
        stream.writeShort((short) volume);
        stream.writeShort((short) 0);
        for (int i : matrix) {
            stream.writeInt((int) i);
        }
        stream.writeInt((int) width << 16);
        stream.writeInt((int) height << 16);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        addTimingToStringBuilder(creationTime, modificationTime, sb);
        sb.append(", track_ID=");
        sb.append(getTrackId());
        sb.append(", duration=");
        sb.append(getDuration());
        sb.append(", layer=");
        sb.append(layer);
        sb.append(", alternate_group=");
        sb.append(alternateGroup);
        sb.append(", volume=");
        sb.append(volume);
        sb.append(", width=");
        sb.append(width);
        sb.append(", height=");
        sb.append(height);
        return sb.toString();
    }
}
