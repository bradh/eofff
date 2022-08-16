package net.frogmouth.rnd.eofff.isobmff.mdia;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class MediaBoxBuilder extends AbstractContainerBoxBuilder<MediaBox> {

    public MediaBoxBuilder() {}

    @Override
    public MediaBox build() {
        int size = getBoxSize();
        MediaBox box = new MediaBox(new FourCC("mdia"));
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
