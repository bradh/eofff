package net.frogmouth.rnd.eofff.isobmff.minf;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBoxBuilder;

public class MediaInformationBoxBuilder extends AbstractContainerBoxBuilder<MediaInformationBox> {

    public MediaInformationBoxBuilder() {}

    @Override
    public MediaInformationBox build() {
        MediaInformationBox box = new MediaInformationBox();
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
