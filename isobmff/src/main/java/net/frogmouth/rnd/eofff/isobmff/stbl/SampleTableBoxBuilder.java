package net.frogmouth.rnd.eofff.isobmff.stbl;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class SampleTableBoxBuilder extends AbstractContainerBoxBuilder<SampleTableBox> {

    public SampleTableBoxBuilder() {}

    @Override
    public SampleTableBox build() {
        int size = getBoxSize();
        SampleTableBox box = new SampleTableBox(new FourCC("stbl"));
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
