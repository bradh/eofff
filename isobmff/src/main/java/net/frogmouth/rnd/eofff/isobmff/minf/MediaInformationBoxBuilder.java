package net.frogmouth.rnd.eofff.isobmff.minf;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class MediaInformationBoxBuilder extends AbstractContainerBoxBuilder<MediaInformationBox> {

    public MediaInformationBoxBuilder() {}

    @Override
    public MediaInformationBox build() {
        int size = getBoxSize();
        MediaInformationBox box = new MediaInformationBox(new FourCC("minf"));
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
