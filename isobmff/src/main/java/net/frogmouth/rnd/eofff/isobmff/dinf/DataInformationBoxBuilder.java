package net.frogmouth.rnd.eofff.isobmff.dinf;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBoxBuilder;

public class DataInformationBoxBuilder extends AbstractContainerBoxBuilder<DataInformationBox> {

    public DataInformationBoxBuilder() {}

    @Override
    public DataInformationBox build() {
        DataInformationBox box = new DataInformationBox();
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
