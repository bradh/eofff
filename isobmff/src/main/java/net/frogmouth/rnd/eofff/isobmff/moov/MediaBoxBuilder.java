package net.frogmouth.rnd.eofff.isobmff.moov;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class MediaBoxBuilder extends AbstractContainerBoxBuilder<MediaBox> {

    public MediaBoxBuilder() {}

    @Override
    public MediaBox build() {
        int size = getBoxSize();
        MediaBox box = new MediaBox(size, new FourCC("mdia"));
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
