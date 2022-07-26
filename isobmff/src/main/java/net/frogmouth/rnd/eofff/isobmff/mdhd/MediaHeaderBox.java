package net.frogmouth.rnd.eofff.isobmff.mdhd;

import static net.frogmouth.rnd.eofff.isobmff.BaseBox.intToBytes;
import static net.frogmouth.rnd.eofff.isobmff.BaseBox.shortToBytes;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

/**
 * Media Header Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.4.2.
 */
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
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        stream.write(getVersionAndFlagsAsBytes());
        if (getVersion() == 1) {
            stream.write(longToBytes(this.creationTime));
            stream.write(longToBytes(this.modificationTime));
            stream.write(intToBytes((int) this.timescale));
            stream.write(longToBytes(this.duration));
        } else {
            stream.write(intToBytes((int) this.creationTime));
            stream.write(intToBytes((int) this.modificationTime));
            stream.write(intToBytes((int) this.timescale));
            stream.write(intToBytes((int) this.duration));
        }
        byte[] languageBytes = language.getBytes(StandardCharsets.US_ASCII);
        short packedLanguage = (short) (languageBytes[2] - 0x60);
        packedLanguage |= (short) (languageBytes[1] - 0x60) << 5;
        packedLanguage |= (short) (languageBytes[0] - 0x60) << 10;
        stream.write(shortToBytes(packedLanguage));
        stream.write(shortToBytes((short) 0));
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
