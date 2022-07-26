package net.frogmouth.rnd.eofff.isobmff.moov;

import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class SampleTableBoxBuilder extends AbstractContainerBoxBuilder<SampleTableBox> {

    public SampleTableBoxBuilder() {}

    @Override
    public SampleTableBox build() {
        int size = getBoxSize();
        SampleTableBox box = new SampleTableBox(size, new FourCC("stbl"));
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
