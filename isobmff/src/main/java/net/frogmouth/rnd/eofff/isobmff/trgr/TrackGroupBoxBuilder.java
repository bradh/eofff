package net.frogmouth.rnd.eofff.isobmff.trgr;

import java.util.ArrayList;
import java.util.List;

public class TrackGroupBoxBuilder {

    private final List<TrackGroupTypeBox> groups = new ArrayList<>();

    public TrackGroupBoxBuilder() {}

    public TrackGroupBoxBuilder withGroup(TrackGroupTypeBox group) {
        this.groups.add(group);
        return this;
    }

    public TrackGroupBox build() {
        TrackGroupBox box = new TrackGroupBox();
        for (TrackGroupTypeBox group : groups) {
            box.addEntry(group);
        }
        return box;
    }
}
