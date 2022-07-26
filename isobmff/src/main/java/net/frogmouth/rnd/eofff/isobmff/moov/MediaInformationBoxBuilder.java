package net.frogmouth.rnd.eofff.isobmff.moov;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class MediaInformationBoxBuilder extends AbstractContainerBoxBuilder<MediaInformationBox> {

    public MediaInformationBoxBuilder() {}

    @Override
    public MediaInformationBox build() {
        int size = getBoxSize();
        MediaInformationBox box = new MediaInformationBox(size, new FourCC("minf"));
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
