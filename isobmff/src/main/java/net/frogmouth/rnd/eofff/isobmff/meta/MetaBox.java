package net.frogmouth.rnd.eofff.isobmff.meta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;
import net.frogmouth.rnd.eofff.isobmff.FullBox;
import net.frogmouth.rnd.eofff.isobmff.OutputStreamWriter;

public class MetaBox extends FullBox {
    public static final FourCC META_ATOM = new FourCC("meta");

    private final List<Box> nestedBoxes = new ArrayList<>();

    public MetaBox() {
        super(META_ATOM);
    }

    @Override
    public String getFullName() {
        return "MetaBox";
    }

    @Override
    public long getSize() {
        long size = Integer.BYTES + FourCC.BYTES + 1 + 3;
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

    @Override
    public void writeTo(OutputStreamWriter stream) throws IOException {
        this.writeBoxHeader(stream);
        for (Box box : nestedBoxes) {
            box.writeTo(stream);
        }
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
