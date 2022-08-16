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
        TimeToSampleBox box = new TimeToSampleBox(new FourCC("stts"));
        for (TimeToSampleEntry entry : entries) {
            box.addEntry(entry);
        }
        return box;
    }
}
