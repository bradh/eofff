package net.frogmouth.rnd.eofff.isobmff.stbl;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBoxBuilder;

public class SampleTableBoxBuilder extends AbstractContainerBoxBuilder<SampleTableBox> {

    public SampleTableBoxBuilder() {}

    @Override
    public SampleTableBox build() {
        SampleTableBox box = new SampleTableBox();
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
