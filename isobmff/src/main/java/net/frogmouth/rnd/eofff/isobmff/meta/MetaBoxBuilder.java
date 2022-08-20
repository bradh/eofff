package net.frogmouth.rnd.eofff.isobmff.meta;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;

public class MetaBoxBuilder {

    private int version;
    private int flags;
    private final List<Box> nestedBoxes = new ArrayList<>();

    public MetaBoxBuilder() {}

    public MetaBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public MetaBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public MetaBoxBuilder withNestedBox(Box box) {
        this.nestedBoxes.add(box);
        return this;
    }

    public MetaBox build() {
        MetaBox box = new MetaBox();
        box.setVersion(version);
        box.setFlags(flags);
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
