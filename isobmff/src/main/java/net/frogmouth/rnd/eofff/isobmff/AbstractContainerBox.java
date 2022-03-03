package net.frogmouth.rnd.eofff.isobmff;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainerBox extends BaseBox {

    protected final List<Box> nestedBoxes = new ArrayList<>();

    public AbstractContainerBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public abstract String getFullName();

    public List<Box> getNestedBoxes() {
        return new ArrayList<>(nestedBoxes);
    }

    public void addNestedBoxes(List<Box> boxes) {
        nestedBoxes.addAll(boxes);
    }

    @Override
    public String toString() {
        return toString(1);
    }

    public String toString(int nestingLevel) {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("':   (Container)");
        for (Box nestedBox : getNestedBoxes()) {
            sb.append("\n");
            for (int i = 0; i < nestingLevel; i++) {
                sb.append("\t");
            }
            if (nestedBox instanceof AbstractContainerBox) {
                AbstractContainerBox abstractContainerBox = (AbstractContainerBox) nestedBox;
                sb.append(abstractContainerBox.toString(nestingLevel + 1));
            } else {
                sb.append(nestedBox.toString());
            }
        }
        return sb.toString();
    }
}
