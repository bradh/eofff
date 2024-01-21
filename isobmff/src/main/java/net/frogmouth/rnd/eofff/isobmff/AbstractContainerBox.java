package net.frogmouth.rnd.eofff.isobmff;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractContainerBox extends BaseBox {

    protected final List<Box> nestedBoxes = new ArrayList<>();

    public AbstractContainerBox(FourCC name) {
        super(name);
    }

    @Override
    public abstract String getFullName();

    // @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES;
        for (Box box : nestedBoxes) {
            size += box.getSize();
        }
        return size;
    }

    @Override
    public long getBodySize() {
        long size = 0;
        for (Box box : nestedBoxes) {
            size += box.getSize();
        }
        return size;
    }

    public List<Box> getNestedBoxes() {
        return new ArrayList<>(nestedBoxes);
    }

    public void addNestedBoxes(List<Box> boxes) {
        nestedBoxes.addAll(boxes);
    }

    public void appendNestedBox(Box box) {
        nestedBoxes.add(box);
    }

    public void removeNestedBox(Box box) {
        nestedBoxes.remove(box);
    }

    @Override
    public String toString() {
        return toString(0);
    }

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        stream.writeInt((int) this.getSize());
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

    @Override
    public String toString(int nestingLevel) {
        StringBuilder sb = this.getBaseStringBuilder(nestingLevel);
        sb.append("(Container)");
        for (Box nestedBox : getNestedBoxes()) {
            sb.append("\n");
            if (nestedBox == null) {
                sb.append("[NULL BOX]");
                continue;
            }
            if (nestedBox instanceof AbstractContainerBox abstractContainerBox) {
                sb.append(abstractContainerBox.toString(nestingLevel + 1));
            } else {
                sb.append(nestedBox.toString(nestingLevel + 1));
            }
        }
        return sb.toString();
    }
}
