package net.frogmouth.rnd.eofff.isobmff.stts;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;

public class TimeToSampleBox extends FullBox {
    private final List<TimeToSampleEntry> entries = new ArrayList<>();

    public TimeToSampleBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "TimeToSampleBox";
    }

    public List<TimeToSampleEntry> getEntries() {
        return new ArrayList<>(this.entries);
    }

    public void addEntry(TimeToSampleEntry item) {
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
        for (TimeToSampleEntry item : getEntries()) {
            sb.append("\n");
            sb.append("\t\t\t\t\t  ");
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
