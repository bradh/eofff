package net.frogmouth.rnd.eofff.isobmff.moov;

import java.io.IOException;
import java.io.OutputStream;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * Movie Header Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.2.2.
 */
public class MovieHeaderBox extends FullBox {
    private long creationTime;
    private long modificationTime;
    private long timescale;
    private long duration;
    private double rate;
    private double volume;
    private int[] matrix;
    private long nextTrackId;

    public MovieHeaderBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "MovieHeaderBox";
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
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        stream.write(getVersionAndFlagsAsBytes());
        if (getVersion() == 1) {
            stream.write(longToBytes(creationTime));
            stream.write(longToBytes(modificationTime));
            stream.write(intToBytes((int) timescale));
            stream.write(longToBytes(duration));
        } else {
            stream.write(intToBytes((int) creationTime));
            stream.write(intToBytes((int) modificationTime));
            stream.write(intToBytes((int) timescale));
            stream.write(intToBytes((int) duration));
        }
        stream.write(intToBytes((int) (rate * Math.pow(2, 16))));
        stream.write(shortToBytes((short) (volume * Math.pow(2, 8))));
        stream.write(shortToBytes((short) 0));
        stream.write(intToBytes(0));
        stream.write(intToBytes(0));
        for (int i : matrix) {
            stream.write(intToBytes(i));
        }
        for (int i = 0; i < 6; i++) {
            stream.write(intToBytes(0));
        }
        stream.write(intToBytes((int) nextTrackId));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': TODO");
        return sb.toString();
    }
}
