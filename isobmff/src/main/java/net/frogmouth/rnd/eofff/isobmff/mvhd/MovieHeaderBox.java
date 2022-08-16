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
    private long creationTime;
    private long modificationTime;
    private long timescale;
    private long duration;
    private double rate;
    private double volume;
    private int[] matrix;
    private long nextTrackId;

    public MovieHeaderBox(FourCC name) {
        super(name);
    }

    @Override
    public String getFullName() {
        return "MovieHeaderBox";
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES;
        if (getVersion() == 1) {
            size += (3 * Long.BYTES + Integer.BYTES);
        } else {
            size += (4 * Long.BYTES);
        }
        size += Integer.BYTES;
        size += Short.BYTES;
        size += Short.BYTES;
        size += Integer.BYTES;
        size += Integer.BYTES;

        for (int i : matrix) {
            size += Integer.BYTES;
        }
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
        stream.writeInt((int) this.getSize());
        stream.writeFourCC(getFourCC());
        stream.write(getVersionAndFlagsAsBytes());
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
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': TODO");
        return sb.toString();
    }
}
