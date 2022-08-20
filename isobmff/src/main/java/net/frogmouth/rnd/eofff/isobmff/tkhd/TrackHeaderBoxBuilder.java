package net.frogmouth.rnd.eofff.isobmff.tkhd;

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
        TrackHeaderBox box = new TrackHeaderBox();
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
