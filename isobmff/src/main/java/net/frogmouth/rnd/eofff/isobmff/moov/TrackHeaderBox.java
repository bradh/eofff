package net.frogmouth.rnd.eofff.isobmff.moov;

import java.io.IOException;
import java.io.OutputStream;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * Track Header Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.3.2.
 */
public class TrackHeaderBox extends FullBox {
    private long creationTime;
    private long modificationTime;
    private long trackId;
    private long duration;
    private int layer;
    private int alternateGroup;
    private int volume;
    private int[] matrix;
    private long width;
    private long height;

    public TrackHeaderBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "TrackHeaderBox";
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

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        stream.write(getVersionAndFlagsAsBytes());
        if (getVersion() == 1) {
            stream.write(longToBytes(creationTime));
            stream.write(longToBytes(modificationTime));
            stream.write(intToBytes((int) trackId));
            stream.write(intToBytes(0));
            stream.write(longToBytes(duration));
        } else {
            stream.write(intToBytes((int) creationTime));
            stream.write(intToBytes((int) modificationTime));
            stream.write(intToBytes((int) trackId));
            stream.write(intToBytes(0));
            stream.write(intToBytes((int) duration));
        }
        stream.write(intToBytes(0));
        stream.write(intToBytes(0));
        stream.write(shortToBytes((short) layer));
        stream.write(shortToBytes((short) alternateGroup));
        stream.write(shortToBytes((short) volume));
        stream.write(shortToBytes((short) 0));
        for (int i : matrix) {
            stream.write(intToBytes((int) i));
        }
        stream.write(intToBytes((int) width));
        stream.write(intToBytes((int) height));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': creation_time=");
        sb.append(getCreationTime());
        sb.append(", modification_time=");
        sb.append(getModificationTime());
        sb.append(", track_ID=");
        sb.append(getTrackId());
        sb.append(", duration=");
        sb.append(getDuration());
        sb.append(", TODO");
        return sb.toString();
    }
}
