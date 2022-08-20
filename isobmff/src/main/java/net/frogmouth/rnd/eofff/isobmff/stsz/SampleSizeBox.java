package net.frogmouth.rnd.eofff.isobmff.stsz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

/**
 * Sample Size Box.
 *
 * <p>See ISO/IEC 14496-12:2015 Section 8.7.3.
 */
public class SampleSizeBox extends FullBox {

    public static final FourCC STSZ_ATOM = new FourCC("stsz");

    private long sampleSize;
    private long sampleCount;
    private final List<Long> entries = new ArrayList<>();

    public SampleSizeBox() {
        super(STSZ_ATOM);
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        size += Integer.BYTES;
        size += Integer.BYTES;
        return size;
    }

    @Override
    public long getBodySize() {
        long size = 0;
        size += Integer.BYTES;
        size += Integer.BYTES;
        return size;
    }

    @Override
    public String getFullName() {
        return "SampleSizeBox";
    }

    public long getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(long sampleSize) {
        this.sampleSize = sampleSize;
    }

    public long getSampleCount() {
        return sampleCount;
    }

    public void setSampleCount(long sampleCount) {
        this.sampleCount = sampleCount;
    }

    public List<Long> getEntries() {
        return new ArrayList<>(entries);
    }

    public void addEntry(Long entry) {
        this.entries.add(entry);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        stream.writeInt((int) this.sampleSize);
        if (sampleSize == 0) {
            stream.writeInt(this.entries.size());
            for (long entry : entries) {
                stream.writeInt((int) entry);
            }
        } else {
            stream.writeInt((int) this.sampleCount);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': sample_size=");
        sb.append(getSampleSize());
        if (getSampleSize() == 0) {
            sb.append(" (variable - see entries below)");
        } else {
            sb.append(" (constant for all entries)");
            sb.append(", sample count: ");
            sb.append(this.getSampleCount());
        }
        for (Long item : getEntries()) {
            sb.append("\n");
            sb.append("\t\t\t\t\t  sample_size=");
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
