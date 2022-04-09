package net.frogmouth.rnd.eofff.isobmff.mdhd;

import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class MediaHeaderBox extends FullBox {
    private long creationTime;
    private long modificationTime;
    private long timescale;
    private long duration;
    private String language;

    public MediaHeaderBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "MediaHeaderBox";
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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
        sb.append(", timescale=");
        sb.append(getTimescale());
        sb.append(", duration=");
        sb.append(getDuration());
        sb.append(", language=");
        sb.append(getLanguage());
        return sb.toString();
    }
}
