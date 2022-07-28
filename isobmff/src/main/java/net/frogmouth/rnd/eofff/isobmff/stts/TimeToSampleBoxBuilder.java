package net.frogmouth.rnd.eofff.isobmff.stts;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class TimeToSampleBoxBuilder {

    private final List<TimeToSampleEntry> entries = new ArrayList<>();

    public TimeToSampleBoxBuilder() {}

    public TimeToSampleBoxBuilder withReference(TimeToSampleEntry entry) {
        this.entries.add(entry);
        return this;
    }

    public TimeToSampleBox build() {
        int size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        size += Integer.BYTES;
        for (TimeToSampleEntry entry : entries) {
            size += entry.getSize();
        }
        TimeToSampleBox box = new TimeToSampleBox(size, new FourCC("stts"));
        for (TimeToSampleEntry entry : entries) {
            box.addEntry(entry);
        }
        return box;
    }
}
