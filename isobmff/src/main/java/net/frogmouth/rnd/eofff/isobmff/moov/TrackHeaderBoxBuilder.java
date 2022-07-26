package net.frogmouth.rnd.eofff.isobmff.moov;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class TrackHeaderBoxBuilder {

    private static final int[] DEFAULT_MATRIX =
            new int[] {0x00010000, 0, 0, 0x00010000, 0, 0, 0, 0, 0x40000000};
    private int version;
    private int flags;
    private long trackID;
    private long duration;

    public TrackHeaderBoxBuilder() {}

    public TrackHeaderBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public TrackHeaderBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public TrackHeaderBox build() {
        int size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        if (version == 1) {
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
        TrackHeaderBox box = new TrackHeaderBox(size, new FourCC("tkhd"));
        box.setVersion(version);
        box.setFlags(flags);
        box.setTrackId(trackID);
        box.setDuration(duration);
        box.setMatrix(DEFAULT_MATRIX);
        return box;
    }

    public TrackHeaderBoxBuilder withTrackID(int id) {
        this.trackID = id;
        return this;
    }

    public TrackHeaderBoxBuilder withDuration(long duration) {
        this.duration = duration;
        return this;
    }
}
