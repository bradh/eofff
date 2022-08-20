package net.frogmouth.rnd.eofff.isobmff.trak;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBoxBuilder;

public class TrackBoxBuilder extends AbstractContainerBoxBuilder<TrackBox> {

    public TrackBoxBuilder() {}

    @Override
    public TrackBox build() {
        TrackBox box = new TrackBox();
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
