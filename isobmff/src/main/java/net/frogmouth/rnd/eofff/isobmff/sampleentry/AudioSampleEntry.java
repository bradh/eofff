package net.frogmouth.rnd.eofff.isobmff.sampleentry;

import java.io.IOException;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Audio Sample Entry.
 *
 * <p>See ISO/IEC 14496-12:2022 Section 12.2.3.
 */
public abstract class AudioSampleEntry extends BaseSampleEntry implements SampleEntry {

    private int channelCount;
    private int sampleSize = 16;
    private long sampleRate;

    public AudioSampleEntry(FourCC format) {
        super(format);
    }

    public int getChannelCount() {
        return channelCount;
    }

    public void setChannelCount(int channelCount) {
        this.channelCount = channelCount;
    }

    public int getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    public long getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(long sampleRate) {
        this.sampleRate = sampleRate;
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        writeBaseSampleEntryContent(stream);
        for (int i = 0; i < 2; i++) {
            stream.writeUnsignedInt32(0); // reserved
        }
        stream.writeUnsignedInt16(channelCount);
        stream.writeUnsignedInt16(sampleSize);
        stream.writeUnsignedInt16(0); // pre_defined
        stream.writeUnsignedInt16(0); // reserved
        stream.writeUnsignedInt32(sampleRate);
        for (Box box : nestedBoxes) {
            box.writeTo(stream);
        }
    }

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("data_reference_index: ");
        sb.append(getDataReferenceIndex());
        sb.append(", channelcount: ");
        sb.append(channelCount);
        sb.append(", samplesize: ");
        sb.append(sampleSize);
        sb.append(", samplerate: ");
        sb.append((float) sampleRate / (1 << 16));
        for (Box nestedBox : nestedBoxes) {
            sb.append("\n");
            sb.append(nestedBox.toString(nestingLevel + 1));
        }
        return sb.toString();
    }

    @Override
    public long getBodySize() {
        long size = getBaseBodySize();
        size += (2 * Integer.BYTES); // reserved
        size += Short.BYTES; // channelcount
        size += Short.BYTES; // samplesize
        size += Short.BYTES; // pre_defined
        size += Short.BYTES; // reserved
        size += Integer.BYTES; // samplerate
        // TODO:
        for (Box box : nestedBoxes) {
            size += box.getSize();
        }
        return size;
    }
}
