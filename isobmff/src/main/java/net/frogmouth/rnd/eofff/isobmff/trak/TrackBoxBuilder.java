package net.frogmouth.rnd.eofff.isobmff.trak;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class TrackBoxBuilder extends AbstractContainerBoxBuilder<TrackBox> {

    public TrackBoxBuilder() {}

    @Override
    public TrackBox build() {
        int size = getBoxSize();
        TrackBox box = new TrackBox(new FourCC("trak"));
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
