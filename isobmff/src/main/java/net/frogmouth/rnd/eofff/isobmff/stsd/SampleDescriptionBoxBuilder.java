package net.frogmouth.rnd.eofff.isobmff.stsd;

import java.util.ArrayList;
import java.util.List;
import net.frogmouth.rnd.eofff.isobmff.sampleentry.SampleEntry;

public class SampleDescriptionBoxBuilder {

    private int version;
    private int flags;
    private final List<SampleEntry> sampleEntries = new ArrayList<>();

    public SampleDescriptionBoxBuilder() {}

    public SampleDescriptionBoxBuilder withVersion(int version) {
        this.version = version;
        return this;
    }

    public SampleDescriptionBoxBuilder withFlags(int flags) {
        this.flags = flags;
        return this;
    }

    public SampleDescriptionBoxBuilder withNestedBox(SampleEntry box) {
        this.sampleEntries.add(box);
        return this;
    }

    public SampleDescriptionBox build() {
        SampleDescriptionBox box = new SampleDescriptionBox();
        box.setVersion(version);
        box.setFlags(flags);
        box.addSampleEntries(sampleEntries);
        return box;
    }
}
