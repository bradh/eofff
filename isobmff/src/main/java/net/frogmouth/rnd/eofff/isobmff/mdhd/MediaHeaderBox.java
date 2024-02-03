package net.frogmouth.rnd.eofff.isobmff.mdhd;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.ISO639Language;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Media Header Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.4.2.
 */
public class MediaHeaderBox extends FullBox {

    public static final FourCC MDHD_ATOM = new FourCC("mdhd");

    private long creationTime;
    private long modificationTime;
    private long timescale;
    private long duration;
    private ISO639Language language;

    public MediaHeaderBox() {
        super(MDHD_ATOM);
    }

    @Override
    public long getBodySize() {
        long size = 0;
        if (getVersion() == 1) {
            size += 28;
        } else {
            size += 16;
        }
        size += ISO639Language.BYTES;
        size += 2; // predefined
        return size;
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

    public double getDurationMilliseconds() {
        return 1000.0 * duration / timescale;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public ISO639Language getLanguage() {
        return language;
    }

    public void setLanguage(ISO639Language language) {
        this.language = language;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        if (getVersion() == 1) {
            stream.writeLong(this.creationTime);
            stream.writeLong(this.modificationTime);
            stream.writeInt((int) this.timescale);
            stream.writeLong(this.duration);
        } else {
            stream.writeInt((int) this.creationTime);
            stream.writeInt((int) this.modificationTime);
            stream.writeInt((int) this.timescale);
            stream.writeInt((int) this.duration);
        }
        stream.writePackedLanguageCode(language);
        stream.writeShort((short) 0);
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("creation_time=");
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
