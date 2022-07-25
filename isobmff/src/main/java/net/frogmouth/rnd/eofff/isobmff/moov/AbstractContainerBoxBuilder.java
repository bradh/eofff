package net.frogmouth.rnd.eofff.isobmff.moov;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.AbstractContainerBox;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public abstract class AbstractContainerBoxBuilder<T extends AbstractContainerBox> {

    protected final List<Box> nestedBoxes = new ArrayList<>();

    public AbstractContainerBoxBuilder() {}

    public AbstractContainerBoxBuilder<T> withNestedBox(Box box) {
        this.nestedBoxes.add(box);
        return this;
    }

    protected int getBoxSize() {
        int size = Integer.BYTES + FourCC.BYTES;
        for (Box box : nestedBoxes) {
            size += box.getSize();
        }
        return size;
    }

    public abstract T build();
}
