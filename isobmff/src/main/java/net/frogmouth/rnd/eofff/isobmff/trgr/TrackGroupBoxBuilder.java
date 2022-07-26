package net.frogmouth.rnd.eofff.isobmff.trgr;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class TrackGroupBoxBuilder {

    private final List<TrackGroupTypeBox> groups = new ArrayList<>();

    public TrackGroupBoxBuilder() {}

    public TrackGroupBoxBuilder withGroup(TrackGroupTypeBox group) {
        this.groups.add(group);
        return this;
    }

    public TrackGroupBox build() {
        int size = Integer.BYTES + FourCC.BYTES;
        for (TrackGroupTypeBox group : groups) {
            size += group.getSize();
        }
        TrackGroupBox box = new TrackGroupBox(size, new FourCC("trgr"));
        for (TrackGroupTypeBox group : groups) {
            box.addEntry(group);
        }
        return box;
    }
}
