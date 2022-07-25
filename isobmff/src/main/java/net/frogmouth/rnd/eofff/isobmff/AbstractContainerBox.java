package net.frogmouth.rnd.eofff.isobmff;

import java.io.IOException;
import java.io.OutputStream;
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

    public void appendNestedBox(Box box) {
        long sizeOfAddedBox = box.getSize();
        nestedBoxes.add(box);
        adjustSize(sizeOfAddedBox);
    }

    public void removeNestedBox(Box box) {
        long sizeOfRemovedBox = box.getSize();
        nestedBoxes.remove(box);
        adjustSize(-1 * sizeOfRemovedBox);
    }

    @Override
    public String toString() {
        return toString(1);
    }

    @Override
    public void writeTo(OutputStream stream) throws IOException {
        stream.write(this.getSizeAsBytes());
        stream.write(getFourCC().toBytes());
        for (Box box : nestedBoxes) {
            /*
            System.out.println(
                    "writing nested box: "
                            + box.getFourCC().toString()
                            + " ["
                            + box.getFullName()
                            + "]");
            */
            box.writeTo(stream);
        }
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
            if (nestedBox instanceof AbstractContainerBox abstractContainerBox) {
                sb.append(abstractContainerBox.toString(nestingLevel + 1));
            } else {
                sb.append(nestedBox.toString());
            }
        }
        return sb.toString();
    }
}
