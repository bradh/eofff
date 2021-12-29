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
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(" '");
        sb.append(getFourCC());
        sb.append("':   (Container)");
        for (Box nestedBox : getNestedBoxes()) {
            sb.append("\n");
            sb.append("\t");
            sb.append(nestedBox.toString());
        }
        return sb.toString();
    }
}
