package net.frogmouth.rnd.eofff.isobmff.meta;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class MetaBoxBuilder {

    private int version;
    private int flags;
    private List<Box> nestedBoxes = new ArrayList<>();

    public MetaBoxBuilder() {}

    public MetaBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public MetaBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public MetaBoxBuilder withNesteBox(Box box) {
        this.nestedBoxes.add(box);
        return this;
    }

    public MetaBox build() {
        int size = Integer.BYTES + FourCC.BYTES + 1 + 3;
        for (Box box : nestedBoxes) {
            size += box.getSize();
        }
        MetaBox box = new MetaBox(size, new FourCC("meta"));
        box.setVersion(version);
        box.setFlags(flags);
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
