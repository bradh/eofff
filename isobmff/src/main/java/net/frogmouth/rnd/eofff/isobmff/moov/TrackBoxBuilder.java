package net.frogmouth.rnd.eofff.isobmff.moov;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class TrackBoxBuilder extends AbstractContainerBoxBuilder<TrackBox> {

    public TrackBoxBuilder() {}

    @Override
    public TrackBox build() {
        int size = getBoxSize();
        TrackBox box = new TrackBox(size, new FourCC("trak"));
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
