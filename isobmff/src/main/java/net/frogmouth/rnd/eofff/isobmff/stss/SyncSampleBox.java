package net.frogmouth.rnd.eofff.isobmff.stss;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class SyncSampleBox extends FullBox {
    private final List<Long> entries = new ArrayList<>();

    public SyncSampleBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "SyncSampleBox";
    }

    public List<Long> getEntries() {
        return new ArrayList<>(this.entries);
    }

    public void addEntry(Long item) {
        this.entries.add(item);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("': item_count=");
        sb.append(getEntries().size());
        for (Long item : getEntries()) {
            sb.append("\n");
            sb.append("\t\t\t\t\t  sample_number=");
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
