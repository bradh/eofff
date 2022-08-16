package net.frogmouth.rnd.eofff.isobmff.mdhd;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class MediaHeaderBoxBuilder {

    private int version;
    private int flags;
    private String language = "und";
    private long duration;
    private long timescale;
    private long creationTime;
    private long modificationTime;

    public MediaHeaderBoxBuilder() {}

    public MediaHeaderBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public MediaHeaderBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public MediaHeaderBoxBuilder withLanguage(String lang) {
        this.language = lang;
        return this;
    }

    public MediaHeaderBoxBuilder withDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public MediaHeaderBoxBuilder withTimeScale(long timescale) {
        this.timescale = timescale;
        return this;
    }

    public MediaHeaderBoxBuilder withCreationTime(long time) {
        this.creationTime = time;
        return this;
    }

    public MediaHeaderBoxBuilder withModificationTime(long time) {
        this.modificationTime = time;
        return this;
    }

    public MediaHeaderBox build() {

        MediaHeaderBox box = new MediaHeaderBox(new FourCC("mdhd"));
        box.setVersion(version);
        box.setFlags(flags);
        box.setCreationTime(creationTime);
        box.setModificationTime(modificationTime);
        box.setTimescale(timescale);
        box.setDuration(duration);
        box.setLanguage(language);
        return box;
    }
}
