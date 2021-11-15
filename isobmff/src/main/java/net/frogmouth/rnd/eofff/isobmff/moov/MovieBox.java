package net.frogmouth.rnd.eofff.isobmff.moov;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.BaseBox;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

// TODO: turn this into a ContainerBox
public class MovieBox extends BaseBox {
    private final List<Box> nestedBoxes = new ArrayList<>();

    public MovieBox(long size, FourCC name) {
        super(size, name);
    }

    @Override
    public String getFullName() {
        return "MovieBox";
    }

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
