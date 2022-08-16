package net.frogmouth.rnd.eofff.isobmff.dinf;

import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBoxBuilder;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.moov.*;

public class DataInformationBoxBuilder extends AbstractContainerBoxBuilder<DataInformationBox> {

    public DataInformationBoxBuilder() {}

    @Override
    public DataInformationBox build() {
        int size = getBoxSize();
        DataInformationBox box = new DataInformationBox(new FourCC("dinf"));
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
