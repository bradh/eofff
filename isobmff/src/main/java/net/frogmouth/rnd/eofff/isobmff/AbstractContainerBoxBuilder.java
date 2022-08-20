package net.frogmouth.rnd.eofff.isobmff;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainerBoxBuilder<T extends AbstractContainerBox> {

    protected final List<Box> nestedBoxes = new ArrayList<>();

    public AbstractContainerBoxBuilder() {}

    public AbstractContainerBoxBuilder<T> withNestedBox(Box box) {
        this.nestedBoxes.add(box);
        return this;
    }

    protected int getBoxSize() {
        // TODO: this needs to handle the largebox option.
        int size = Integer.BYTES + FourCC.BYTES;
        for (Box box : nestedBoxes) {
            size += box.getSize();
        }
        return size;
    }

    public abstract T build();
}
