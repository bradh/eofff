package net.frogmouth.rnd.eofff.isobmff.mvhd;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Movie Header Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.2.2.
 */
public class MovieHeaderBox extends FullBox {

    public static final FourCC MVHD_ATOM = new FourCC("mvhd");

    private long creationTime;
    private long modificationTime;
    private long timescale;
    private long duration;
    private double rate;
    private double volume;
    private int[] matrix;
    private long nextTrackId;

    public MovieHeaderBox() {
        super(MVHD_ATOM);
    }

    @Override
    public String getFullName() {
        return "MovieHeaderBox";
    }

    @Override
    public long getBodySize() {
        long size = 0;
        if (getVersion() == 1) {
            size += (3 * Long.BYTES + Integer.BYTES);
        } else {
            size += (4 * Integer.BYTES);
        }
        size += Integer.BYTES;
        size += Short.BYTES;
        size += Short.BYTES;
        size += Integer.BYTES;
        size += Integer.BYTES;
        size += (Integer.BYTES * matrix.length);
        for (int i = 0; i < 6; i++) {
            size += Integer.BYTES;
        }
        size += Integer.BYTES;
        return size;
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

    public long getTimescale() {
        return timescale;
    }

    public void setTimescale(long timescale) {
        this.timescale = timescale;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public int[] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[] matrix) {
        this.matrix = matrix;
    }

    public long getNextTrackId() {
        return nextTrackId;
    }

    public void setNextTrackId(long nextTrackId) {
        this.nextTrackId = nextTrackId;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        writeBodyTo(stream);
    }

    private void writeBodyTo(OutputStreamWriter stream) throws IOException {
        if (getVersion() == 1) {
            stream.writeLong(creationTime);
            stream.writeLong(modificationTime);
            stream.writeInt((int) timescale);
            stream.writeLong(duration);
        } else {
            stream.writeInt((int) creationTime);
            stream.writeInt((int) modificationTime);
            stream.writeInt((int) timescale);
            stream.writeInt((int) duration);
        }
        stream.writeInt((int) (rate * Math.pow(2, 16)));
        stream.writeShort((short) (volume * Math.pow(2, 8)));
        stream.writeShort((short) 0);
        stream.writeInt(0);
        stream.writeInt(0);
        for (int i : matrix) {
            stream.writeInt(i);
        }
        for (int i = 0; i < 6; i++) {
            stream.writeInt(0);
        }
        stream.writeInt((int) nextTrackId);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = getBaseStringBuilder(nestingLevel);
        sb.append("': creation_time=");
        sb.append(creationTime);
        sb.append(", modification_time=");
        sb.append(modificationTime);
        sb.append(", timescale=");
        sb.append(timescale);
        sb.append(", duration=");
        sb.append(duration);
        sb.append(", rate=");
        sb.append(rate);
        sb.append(", volume=");
        sb.append(volume);
        sb.append(", next_track_id=");
        sb.append(nextTrackId);
        return sb.toString();
    }
}
