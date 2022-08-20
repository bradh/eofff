package net.frogmouth.rnd.eofff.isobmff.mdia;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBoxBuilder;

public class MediaBoxBuilder extends AbstractContainerBoxBuilder<MediaBox> {

    public MediaBoxBuilder() {}

    @Override
    public MediaBox build() {
        MediaBox box = new MediaBox();
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
