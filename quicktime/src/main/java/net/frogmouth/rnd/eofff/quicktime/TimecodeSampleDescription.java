package net.frogmouth.rnd.eofff.quicktime;

import java.io.IOException;
import java.util.HexFormat;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.BaseSampleEntry;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;

public class TimecodeSampleDescription extends BaseSampleEntry implements SampleEntry {

    private long timecodeFlags;
    private long timeScale;
    private long frameDuration;
    private int numberOfFrames;

    public static final FourCC TMCD_ATOM = new FourCC("tmcd");

    public TimecodeSampleDescription() {
        super(TMCD_ATOM);
    }

    public long getTimecodeFlags() {
        return timecodeFlags;
    }

    public void setTimecodeFlags(long timecodeFlags) {
        this.timecodeFlags = timecodeFlags;
    }

    public long getTimeScale() {
        return timeScale;
    }

    public void setTimeScale(long timeScale) {
        this.timeScale = timeScale;
    }

    public long getFrameDuration() {
        return frameDuration;
    }

    public void setFrameDuration(long frameDuration) {
        this.frameDuration = frameDuration;
    }

    public int getNumberOfFrames() {
        return numberOfFrames;
    }

    public void setNumberOfFrames(int numberOfFrames) {
        this.numberOfFrames = numberOfFrames;
    }

    @Override
    public String getFullName() {
        return "Timecode sample description";
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        writeBaseSampleEntryContent(stream);
        stream.writeUnsignedInt32(0);
        stream.writeUnsignedInt32(this.timecodeFlags);
        stream.writeUnsignedInt32(this.timeScale);
        stream.writeUnsignedInt32(this.frameDuration);
        stream.writeUnsignedInt8(this.numberOfFrames);
        stream.writeUnsignedInt8(0);
        for (Box box : nestedBoxes) {
            box.writeTo(stream);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("data_reference_index: ");
        sb.append(getDataReferenceIndex());
        sb.append(", timecode flags: ");
        sb.append(HexFormat.of().withPrefix("0x").toHexDigits((int) timecodeFlags));
        sb.append(", time scale: ");
        sb.append(timeScale);
        sb.append(", frame duration: ");
        sb.append(frameDuration);
        sb.append(", fps: ");
        sb.append(numberOfFrames);
        for (Box nestedBox : nestedBoxes) {
            sb.append("\n");
            sb.append(nestedBox.toString(nestingLevel + 1));
        }
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long size = getBaseBodySize();
        size += Integer.BYTES;
        size += Integer.BYTES; // timecode flags
        size += Integer.BYTES; // timescale
        size += Integer.BYTES; // frame duration
        size += Byte.BYTES; // number of frames
        size += Byte.BYTES; // reserved
        for (Box box : nestedBoxes) {
            size += box.getSize();
        }
        return size;
    }
}
