package net.frogmouth.rnd.eofff.isobmff.stsz;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class SampleSizeBox extends FullBox {

    private long sampleSize;
    private List<Long> entries = new ArrayList<>();

    public SampleSizeBox(long size, FourCC name) {
        super(size, name);
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

    public List<Long> getEntries() {
        return new ArrayList<>(entries);
    }

    public void addEntry(Long entry) {
        this.entries.add(entry);
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
        }
        for (Long item : getEntries()) {
            sb.append("\n");
            sb.append("\t\t\t\t\t  sample_size=");
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
