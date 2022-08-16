package net.frogmouth.rnd.eofff.isobmff.stsd;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.Box;
import net.frogmouth.rnd.eofff.isobmff.FourCC;

public class SampleDescriptionBoxBuilder {

    private int version;
    private int flags;
    private final List<Box> nestedBoxes = new ArrayList<>();

    public SampleDescriptionBoxBuilder() {}

    public SampleDescriptionBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public SampleDescriptionBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public SampleDescriptionBoxBuilder withNestedBox(Box box) {
        this.nestedBoxes.add(box);
        return this;
    }

    public SampleDescriptionBox build() {
        SampleDescriptionBox box = new SampleDescriptionBox(new FourCC("stsd"));
        box.setVersion(version);
        box.setFlags(flags);
        box.addNestedBoxes(nestedBoxes);
        return box;
    }
}
